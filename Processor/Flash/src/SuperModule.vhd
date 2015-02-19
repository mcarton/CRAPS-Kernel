----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    06:34:58 02/17/2015 
-- Design Name: 
-- Module Name:    SuperModule - Behavioral 
-- Project Name: 
-- Target Devices: 
-- Tool versions: 
-- Description: 
--
-- Dependencies: 
--
-- Revision: 
-- Revision 0.01 - File Created
-- Additional Comments: 
--
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity SuperModule is
Port(
clk    : in std_logic; 
buttons: in std_logic_vector(3 downto 0);
leds:out std_logic_vector(7 downto 0);
switchs:in std_logic_vector(7 downto 0);

 MemDB: inout std_logic_vector(15 downto 0);-- Memory data bus
       MemAdr: out std_logic_vector(23 downto 1); -- Memory Address bus
		 FlashByte: out std_logic; -- Byte enable('0') or word enable('1')
       RamCS: out std_logic;     -- RAM CS
       FlashCS: out std_logic;   -- Flash CS
       MemWR: out std_logic;     -- memory write 
       MemOE: out std_logic;     -- memory read (Output Enable), 
		                           -- also controls the MemDB direction
       RamUB: out std_logic;     -- RAM Upper byte enable 
       RamLB: out std_logic;     -- RAM Lower byte enable 
       RamCre: out std_logic;    -- Cfg Register enable 
       RamAdv: out std_logic;    -- RAM Address Valid pin
       RamClk: out std_logic;    -- RAM Clock 
       RamWait: in std_logic;    -- RAM Wait pin 
       FlashRp: out std_logic;   -- Flash RP pin 
       FlashStSts: in std_logic; -- Flash ST-STS pin 
		  
       MemCtrlEnabled: out std_logic -- MemCtrl takes bus control 

);
end SuperModule;

architecture Behavioral of SuperModule is
component MemoryManager is
    Port ( clk : in  STD_LOGIC;
		 Rst : in STD_LOGIC;
       ABus : in  STD_LOGIC_VECTOR (19 downto 0);
       Din : in  STD_LOGIC_VECTOR (31 downto 0);
       Dout : out  STD_LOGIC_VECTOR (31 downto 0);
       WriteCmd : in  STD_LOGIC;
       ReadCmd : in  STD_LOGIC;
       WaitMem : out  STD_LOGIC;
	 
       MemDB: inout std_logic_vector(15 downto 0);-- Memory data bus
       MemAdr: out std_logic_vector(23 downto 1); -- Memory Address bus
		 FlashByte: out std_logic; -- Byte enable('0') or word enable('1')
       RamCS: out std_logic;     -- RAM CS
       FlashCS: out std_logic;   -- Flash CS
       MemWR: out std_logic;     -- memory write 
       MemOE: out std_logic;     -- memory read (Output Enable), 
		                           -- also controls the MemDB direction
       RamUB: out std_logic;     -- RAM Upper byte enable 
       RamLB: out std_logic;     -- RAM Lower byte enable 
       RamCre: out std_logic;    -- Cfg Register enable 
       RamAdv: out std_logic;    -- RAM Address Valid pin
       RamClk: out std_logic;    -- RAM Clock 
       RamWait: in std_logic;    -- RAM Wait pin 
       FlashRp: out std_logic;   -- Flash RP pin 
       FlashStSts: in std_logic; -- Flash ST-STS pin 
		  
       MemCtrlEnabled: out std_logic; -- MemCtrl takes bus control 
		 
		 	  StateMain: out Std_logic_vector(4 downto 0);
			  StateWriter:out Std_logic_vector(1 downto 0)
	 );
end component;


signal ledBuff: std_logic_vector( 7 downto 0);
constant IdleState : std_logic_vector(2 downto 0):="000";
constant readState : std_logic_vector(2 downto 0):="001";
constant readWaitState:std_logic_vector(2 downto 0):="010";
constant readDoneState:std_logic_vector(2 downto 0):="011";
constant writeState : std_logic_vector(2 downto 0):="100";
constant writeWaitState:std_logic_vector(2 downto 0):="101";
signal CurState:std_logic_vector(2 downto 0);
signal NextState:std_logic_vector(2 downto 0);

signal writecmdbuff : std_logic:='0';
signal readcmdbuff : std_logic :='0';
signal readmemory : std_logic;
signal writememory: std_logic;
signal rst : std_logic;
-- DBus signals:

signal dbus_main:std_logic_vector(31 downto 0);
signal dbus_writeBuffer:std_logic_vector(31 downto 0);
signal dbus_memoryOutput:std_logic_vector(31 downto 0);

signal mem_wait: std_logic;
signal statebuf: std_logic_vector(3 downto 0);
begin

dbus_writeBuffer(31 downto 24) <="10101010";
dbus_writeBuffer(23 downto 16) <="10101010";
dbus_writeBuffer(15 downto 8) <="10101010";
dbus_writeBuffer(7 downto 0) <= switchs;
leds <= ledBuff;


ledBuff(0) <= dbus_main(0) when curState= readDoneState else
								ledBuff(0);

writecmdbuff <= '1' when curState = IdleState and buttons(3)='1' else
					 '0' when curstate /= IdleState else
					 writecmdbuff;
readcmdbuff <= '1' when curState = IdleState and buttons(2) ='1' else
					 '0' when curstate /= IdleState else
					 readcmdbuff;
					 
writememory <= '1' when curState = writeState or curState = writeWaitState else
					'0';
readmemory <= '1' when curState = readState or curState = writeWaitState else
					'0';
dbus_main <= dbus_writeBuffer when curState = WriteState else
				 dbus_memoryOutput when curState = ReadDoneState else
				 "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";
					
					
rst <= '1' when buttons(0)='0' else '0';					 
process (clk)
      begin
         if clk = '1' and clk'Event then
            if rst = '0' then 
               CurState <= IdleState;
            else
               CurState <= NextState;
            end if;
         end if;
      end process;
process(CurState)
      begin
          case CurState is
              when IdleState =>
						if writecmdbuff = '1' then 
							NextState <=writeState;
						elsif readcmdbuff ='1' then
							NextState <= ReadState;
						else
							NextState <=IdleState;
						end if;
              when ReadState =>
						if mem_wait = '1' then
							NextState <= ReadWaitState;
						 else
							NextState <=CurState;
						 end if;
				  when ReadWaitState =>
						if mem_wait ='0' then
							NextState <= ReadDoneState;
						else
							NextState <= ReadWaitState;
						end if;
				 when ReadDoneState=>
						  NextState <=IdleState;
				 when WriteState =>
						if mem_wait = '1' then
							NextState <=WriteWaitState;
						else
							NextState <=WriteState;
						end if;
				when WriteWaitState =>
						if mem_wait = '0' then	
							NextState <=IdleState;
						else
							NextState <= WriteWaitState;
						end if;
				when others =>
						NextState <= IdleState;
			 end case;
       end process;



	Inst_MemoryManager: MemoryManager PORT MAP(
		clk => clk,
		Rst => rst,
		ABus => "01010101010101010101",
		Din => Dbus_main,
		Dout => dbus_memoryOutput,
		WriteCmd => writeMemory,
		ReadCmd => readMemory,
		WaitMem => mem_Wait,
		
		MemDB => MemDb,
		MemAdr => memAdr ,
		FlashByte => flashbyte,
		RamCS => ramcs,
		FlashCS =>flashcs ,
		MemWR => memwr,
		MemOE => MemOE,
		RamUB => RamUB,
		RamLB => RAmLB,
		RamCre => RamCre,
		RamAdv => RamAdv,
		RamClk => RamClk,
		RamWait => RamWait,
		FlashRp => FlashRp,
		FlashStSts => FlashStSts,
		MemCtrlEnabled =>MemCtrlEnabled ,
		
		StateMain => ledbuff(7 downto 3),
		StateWriter => ledbuff(2 downto 1)
	);

	end Behavioral;

