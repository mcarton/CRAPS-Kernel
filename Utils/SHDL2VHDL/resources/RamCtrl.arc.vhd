library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;


entity RamCtrl is
    Port(
        rst: in std_logic; -- active '1'
        clk: in std_logic;
        address: in std_logic_vector(22 downto 0); -- read/write address (word addressed)
        read: in std_logic; -- read mode
        readData: out std_logic_vector(15 downto 0);
        readDone: out std_logic;
        write: in std_logic; -- write mode
        writeData: in std_logic_vector(15 downto 0); -- write value
        writeDone: out std_logic;

        -- all pins
        MemDB: inout std_logic_vector(15 downto 0); -- Memory data bus
        MemAdr: out std_logic_vector(23 downto 1); -- Memory Address bus
        RamCS: out std_logic;     -- RAM CS
        FlashCS: out std_logic;   -- Flash CS
        MemWR: out std_logic;     -- memory write
        MemOE: out std_logic;     -- memory read (Output Enable), also controls the MemDB direction
        RamUB: out std_logic;     -- RAM Upper byte enable
        RamLB: out std_logic;     -- RAM Lower byte enable
        RamCRE: out std_logic;    -- Cfg Register enable
        RamAdv: out std_logic;    -- RAM Address Valid pin
        RamClk: out std_logic;    -- RAM Clock
        RamWait: in std_logic;    -- RAM Wait pin
        FlashRp: out std_logic;   -- Flash RP pin
        FlashStSts: in std_logic  -- Flash ST-STS pin
       );
end RamCtrl;


architecture synthesis of RamCtrl is
    -- State machine
    constant stIdle:      std_logic_vector(3 downto 0) := "0000"; -- all off
    constant stRead1:     std_logic_vector(3 downto 0) := "0001"; -- enable ram
    constant stRead2:     std_logic_vector(3 downto 0) := "0101"; -- read
    constant stRead3:     std_logic_vector(3 downto 0) := "0111"; -- stop reading
    constant stReadDone:  std_logic_vector(3 downto 0) := "1111"; -- all off
    constant stWrite1:    std_logic_vector(3 downto 0) := "1011"; -- enable ram
    constant stWrite2:    std_logic_vector(3 downto 0) := "1001"; -- write data
    constant stWrite3:    std_logic_vector(3 downto 0) := "1101"; -- pause
    constant stWriteDone: std_logic_vector(3 downto 0) := "1110"; -- all off

    signal state : std_logic_vector(3 downto 0) := stIdle;

    -- Counter used to generate delays
    signal DelayCnt : std_logic_vector(4 downto 0);

    signal regReadData : std_logic_vector(15 downto 0) := (others => '0');
begin
    readData <= regReadData;

    MemDB <= writeData when state = stWrite2 else "ZZZZZZZZ" & "ZZZZZZZZ";
    MemAdr <= address(22 downto 0);
    RamCS <= '0' when (state = stRead1 or state = stRead2 or state = stRead3 or
                       state = stWrite1 or state = stWrite2 or state = stWrite3);
    FlashCS <= '1'; -- disable Flash
    MemWR <= '0' when state = stWrite2 else '1';
    MemOE <= '0' when state = stRead2 else '1';
    RamUB <= '0';
    RamLB <= '0';

    -- Memory control signals not yet used
    RamClk <= '0';
    RamCRE <= '0';
    RamAdv <= '0';
    FlashRp <= not rst;

    -- Signal
    readDone <= '1' when state = stReadDone else '0';
    writeDone <= '1' when state = stWriteDone else '0';

    ------------------------------------------------------------------------
    -- State Machine
    ------------------------------------------------------------------------

    process (clk, rst)
    begin
        if clk'event and clk = '1' then
            if rst = '1' then
                state <= stIdle;
            else
                case state is
                    when stIdle =>
                        if read = '1' then
                            state <= stRead1;
                        elsif write = '1' then
                            state <= stWrite1;
                        else
                            state <= state;
                        end if;

                    when stRead1 => state <= stRead2;
                    when stRead2 =>
                       if DelayCnt = "--000" then
                         state <= stRead3;
                       else
                         state <= state;
                       end if;
                    when stRead3 => state <= stReadDone;

                    when stWrite1 => state <= stWrite2;
                    when stWrite2 =>
                        if DelayCnt = "--000" then
                            state <= stWrite3;
                        else
                            state <= state;
                        end if;
                    when stWrite3 => state <= stWriteDone;

                    when stReadDone =>
                        if DelayCnt = "--011" then
                            state <= stIdle;
                        else
                            state <= state;
                        end if;
                    when stWriteDone =>
                        if DelayCnt = "--011" then
                            state <= stIdle;
                        else
                            state <= state;
                        end if;
                    when others => state <= stIdle;
                end case;
            end if;
        end if;
    end process;

    -- Memory read
    process (clk)
    begin
        if clk'event and clk = '1' then
            if state = stRead2 then
                regReadData <= MemDB;
            end if;
        end if;
    end process;

    ------------------------------------------------------------------------
    -- Delay Counter
    ------------------------------------------------------------------------

    process (clk)
    begin
        if clk'event and clk = '1' then
            if state = stIdle then
                DelayCnt <= "00000";
            else
                DelayCnt <= DelayCnt + 1;
            end if;
        end if;
    end process;

end synthesis;
