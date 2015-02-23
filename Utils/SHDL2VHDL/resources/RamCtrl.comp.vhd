component RamCtrl is
    port(
        rst        : in std_logic; -- active '1'
        clk        : in std_logic;
        address    : in std_logic_vector(22 downto 0);  -- read/write address (word addressed)
        read       : in std_logic;                      -- read mode
        readData   : out std_logic_vector(15 downto 0);
        readDone   : out std_logic;
        write      : in std_logic;                      -- write mode
        writeData  : in std_logic_vector(15 downto 0);  -- write value
        writeDone  : out std_logic;

        -- all pins
        MemDB      : inout std_logic_vector(15 downto 0); -- Memory data bus
        MemAdr     : out std_logic_vector(23 downto 1);   -- Memory Address bus
        RamCS      : out std_logic;  -- RAM CS
        FlashCS    : out std_logic;  -- Flash CS
        MemWR      : out std_logic;  -- memory write
        MemOE      : out std_logic;  -- memory read (Output Enable), also controls the MemDB direction
        RamUB      : out std_logic;  -- RAM Upper byte enable
        RamLB      : out std_logic;  -- RAM Lower byte enable
        RamCRE     : out std_logic;  -- Cfg Register enable
        RamAdv     : out std_logic;  -- RAM Address Valid pin
        RamClk     : out std_logic;  -- RAM Clock
        RamWait    : in std_logic;   -- RAM Wait pin
        FlashRp    : out std_logic;  -- Flash RP pin
        FlashStSts : in std_logic    -- Flash ST-STS pin
    );
end component;
