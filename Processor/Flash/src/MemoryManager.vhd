----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    12:27:20 02/19/2015 
-- Design Name: 
-- Module Name:    MemoryManager - Behavioral 
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

entity MemoryManager is
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
end MemoryManager;

architecture Behavioral of MemoryManager is

component NexysOnBoardMemCtrl is
  Port(
       clk  : in std_logic;       -- system clock (50MHz)
-- Epp interface signals
       HandShakeReqOut: out std_logic;    -- User Handshake Request
       ctlMsmStartIn: in std_logic;       -- Automatic process Start 
       ctlMsmDoneOut: out std_logic;      -- Automatic process Done 
       ctlMsmDwrIn: in std_logic;         -- Data Write pulse
       ctlEppRdCycleIn: in std_logic;     -- Indicates a READ Epp cycle
       EppRdDataOut: out std_logic_vector(7 downto 0);-- Data Input bus
       EppWrDataIn: in std_logic_vector(7 downto 0); -- Data Output bus
       regEppAdrIn: in std_logic_vector(7 downto 0); 
		                -- Epp Address Register content (bits 7:3 ignored)
       ComponentSelect : in std_logic;    
		              -- active HIGH, selects the current MemCtrl instance
-- Memory bus signals
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
    end component;
	 
	 	COMPONENT MemInterface is
    Port ( clk : in  STD_LOGIC;
			  Rst : in STD_LOGIC;
           ABus : in  STD_LOGIC_VECTOR (19 downto 0);
           Din : in  STD_LOGIC_VECTOR (31 downto 0);
           Dout : out  STD_LOGIC_VECTOR (31 downto 0);
           WriteCmd : in  STD_LOGIC;
           ReadCmd : in  STD_LOGIC;
           WaitMem : out  STD_LOGIC;
           EppAstb : out  STD_LOGIC;
           EppDstb : out  STD_LOGIC;
           EppWrite : out  STD_LOGIC;
           EppDb : inout  STD_LOGIC_VECTOR (7 downto 0);
           EppWait : in  STD_LOGIC;
			  			  StateMain: out Std_logic_vector(4 downto 0);
			  StateWriter:out Std_logic_vector(1 downto 0)
			  );
			  
end COMPONENT;
	
		COMPONENT EppCtrl is
    Port (

-- Epp-like bus signals
      clk    : in std_logic;        -- system clock (50MHz)
      EppAstb: in std_logic;        -- Address strobe
      EppDstb: in std_logic;        -- Data strobe
      EppWr  : in std_logic;        -- Port write signal
      EppRst : in std_logic;        -- Port reset signal
      EppDB  : inout std_logic_vector(7 downto 0); 	-- port data bus
      EppWait: out std_logic;       -- Port wait signal
-- User signals
      busEppOut: out std_logic_vector(7 downto 0); -- Data Output bus
      busEppIn: in std_logic_vector(7 downto 0);   -- Data Input bus
      ctlEppDwrOut: out std_logic;         -- Data Write pulse
      ctlEppRdCycleOut: inout std_logic;   -- Indicates a READ Epp cycle
      regEppAdrOut: inout std_logic_vector(7 downto 0) := "00000000"; 
                                         -- Epp Address Register content
      HandShakeReqIn: in std_logic;      -- User Handshake Request
      ctlEppStartOut: out std_logic;     -- Automatic process Start   
      ctlEppDoneIn: in std_logic         -- Automatic process Done 
         );
end component;
signal EppAstb:std_logic;
signal EppDstb:std_logic;
signal EppWrite:std_logic;
signal EppDb:std_logic_vector(7 downto 0);
signal EppWait:std_logic;

signal busEppOut:std_logic_vector(7 downto 0);
signal busEppIn:std_logic_vector(7 downto 0);
signal ctlEppDwrOut:std_logic;
signal ctlEppRdCycleOut:std_logic;
signal regEppAdrOut:std_logic_vector(7 downto 0);
signal HandShakeReqIn:std_logic;
signal ctlEppStartOut:std_logic;
signal ctlEppDoneIn:std_logic; 
begin

	Inst_MemInterface: MemInterface PORT MAP(
		clk => clk,
		Rst => Rst,
		ABus => ABus,
		Din => Din,
		Dout => Dout,
		WriteCmd => WriteCmd,
		ReadCmd => ReadCmd,
		WaitMem => WaitMem,
		EppAstb => EppAstb,
		EppDstb => EppDstb,
		EppWrite => EppWrite,
		EppDb => EppDb,
		EppWait => EppWait,
		StateMain => StateMain,
		StateWriter => StateWriter
		
	);
	
		Inst_EppCtrl: EppCtrl PORT MAP(
		clk => clk,
		EppAstb => EppAstb,
		EppDstb => EppDstb,
		EppWr => EppWrite,
		EppRst => Rst,
		EppDB => EppDb,
		EppWait => EppWait,
		busEppOut => busEppOut,
		busEppIn => busEppIn,
		ctlEppDwrOut => ctlEppDwrOut,
		ctlEppRdCycleOut => ctlEppRdCycleOut,
		regEppAdrOut => regEppAdrOut,
		HandShakeReqIn => HandShakeReqIn,
		ctlEppStartOut => ctlEppStartOut,
		ctlEppDoneIn => ctlEppDoneIn
	);
	
	Inst_NexysOnBoardMemCtrl: NexysOnBoardMemCtrl PORT MAP(
		clk => clk,
		HandShakeReqOut => HandShakeReqIn,
		ctlMsmStartIn => ctlEppStartOut,
		ctlMsmDoneOut => ctlEppDoneIn,
		ctlMsmDwrIn => ctlEppDwrOut,
		ctlEppRdCycleIn => ctlEppRdCycleOut,
		EppRdDataOut => busEppIn,
		EppWrDataIn => busEppOut,
		regEppAdrIn => regEppAdrOut,
		ComponentSelect => '1',
		MemDB => MemDB,
		MemAdr => MemAdr,
		FlashByte => FlashByte,
		RamCS => RamCS,
		FlashCS => FlashCS,
		MemWR => MemWR,
		MemOE => MemOE,
		RamUB => RamUB,
		RamLB => RamLB,
		RamCre => RamCre,
		RamAdv => RamAdv,
		RamClk => RamClk,
		RamWait => RamWait,
		FlashRp => FlashRp,
		FlashStSts => FlashStSts,
		MemCtrlEnabled => MemCtrlEnabled
	);

end Behavioral;

