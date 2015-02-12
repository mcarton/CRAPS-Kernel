
package org.jcb.shdl.shdlc.java;

import java.util.*;
import java.util.regex.*;
import java.io.*;



public class SHDLPredefinedCommUSB extends SHDLPredefinedOccurence {

	public SHDLPredefinedCommUSB(SHDLModuleOccurence moduleOccurence, Pattern namePattern) {
		super(moduleOccurence, namePattern);
	}

	public boolean isInput(int index) {
		switch (index) {
			case 0: return true;
			case 2: return true;
			case 3: return true;
			case 4: return true;
			case 7: return true;
			default: return false;
		}
	}
	public boolean isOutput(int index) {
		switch (index) {
			case 5: return true;
			case 6: return true;
			default: return false;
		}
	}
	public boolean isInputOutput(int index) {
		switch (index) {
			case 1: return true;
			default: return false;
		}
	}
	public int getArity(int index) {
		switch (index) {
			case 1: return 8;
			case 6: return 128;
			case 7: return 128;
			default: return 1;
		}
	}

	public boolean check(boolean ok, SHDLModule module, PrintStream errorStream) {
		return true;
	}

	public String getVHDLComponentDeclaration() {
		StringBuffer sb = new StringBuffer();
		sb.append("    component " + getModuleOccurence().getName() + SHDLModule.newline);
		sb.append("        port (" + SHDLModule.newline);
		sb.append("            mclk     : in std_logic;" + SHDLModule.newline);
		sb.append("            pdb      : inout std_logic_vector(7 downto 0);" + SHDLModule.newline);
		sb.append("            astb     : in std_logic;" + SHDLModule.newline);
		sb.append("            dstb     : in std_logic;" + SHDLModule.newline);
		sb.append("            pwr      : in std_logic;" + SHDLModule.newline);
		sb.append("            pwait    : out std_logic;" + SHDLModule.newline);
		sb.append("            pc2board : out std_logic_vector(127 downto 0);" + SHDLModule.newline);
		sb.append("            board2pc : in std_logic_vector(127 downto 0)" + SHDLModule.newline);
		sb.append("        ) ;" + SHDLModule.newline);
		sb.append("    end component ;");
		return new String(sb);
	}

	public String getVHDLDefinition() {
		StringBuffer sb = new StringBuffer();
		sb.append("library ieee ;" + SHDLModule.newline);
		sb.append("use ieee.std_logic_1164.all ;" + SHDLModule.newline);
		sb.append("use ieee.std_logic_unsigned.all ;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("-- commUSB module" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("entity " + getModuleOccurence().getName() + " is" + SHDLModule.newline);
		sb.append("        port (" + SHDLModule.newline);
		sb.append("            mclk     : in std_logic;" + SHDLModule.newline);
		sb.append("            pdb      : inout std_logic_vector(7 downto 0);" + SHDLModule.newline);
		sb.append("            astb     : in std_logic;" + SHDLModule.newline);
		sb.append("            dstb     : in std_logic;" + SHDLModule.newline);
		sb.append("            pwr      : in std_logic;" + SHDLModule.newline);
		sb.append("            pwait    : out std_logic;" + SHDLModule.newline);
		sb.append("            pc2board : out std_logic_vector(127 downto 0);" + SHDLModule.newline);
		sb.append("            board2pc : in std_logic_vector(127 downto 0)" + SHDLModule.newline);
		sb.append("        ) ;" + SHDLModule.newline);
		sb.append("end " + getModuleOccurence().getName() + " ;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("architecture synthesis of " + getModuleOccurence().getName() + " is" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		
		sb.append("    constant	stEppReady   : std_logic_vector(7 downto 0) := \"0000\" & \"0000\";" + SHDLModule.newline);
		sb.append("    constant	stEppAwrA    : std_logic_vector(7 downto 0) := \"0001\" & \"0100\";" + SHDLModule.newline);
		sb.append("    constant	stEppAwrB    : std_logic_vector(7 downto 0) := \"0010\" & \"0001\";" + SHDLModule.newline);
		sb.append("    constant	stEppArdA    : std_logic_vector(7 downto 0) := \"0011\" & \"0010\";" + SHDLModule.newline);
		sb.append("    constant	stEppArdB    : std_logic_vector(7 downto 0) := \"0100\" & \"0011\";" + SHDLModule.newline);
		sb.append("    constant	stEppDwrA    : std_logic_vector(7 downto 0) := \"0101\" & \"1000\";" + SHDLModule.newline);
		sb.append("    constant	stEppDwrB    : std_logic_vector(7 downto 0) := \"0110\" & \"0001\";" + SHDLModule.newline);
		sb.append("    constant	stEppDrdA    : std_logic_vector(7 downto 0) := \"0111\" & \"0010\";" + SHDLModule.newline);
		sb.append("    constant	stEppDrdB    : std_logic_vector(7 downto 0) := \"1000\" & \"0011\";" + SHDLModule.newline);
		sb.append(SHDLModule.newline);

		sb.append("    signal	stEppCur     : std_logic_vector(7 downto 0) := stEppReady;" + SHDLModule.newline);
		sb.append("    signal	stEppNext    : std_logic_vector(7 downto 0);" + SHDLModule.newline);
		sb.append(SHDLModule.newline);

		sb.append("    attribute fsm_extract : string;" + SHDLModule.newline);
		sb.append("    attribute fsm_extract of stEppCur: signal is \"no\";" + SHDLModule.newline);
		sb.append("    attribute fsm_extract of stEppNext: signal is \"no\";" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		
		sb.append("    attribute fsm_encoding : string;" + SHDLModule.newline);
		sb.append("    attribute fsm_encoding of stEppCur: signal is \"user\";" + SHDLModule.newline);
		sb.append("    attribute fsm_encoding of stEppNext: signal is \"user\";" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		
		sb.append("    attribute signal_encoding : string;" + SHDLModule.newline);
		sb.append("    attribute signal_encoding of stEppCur: signal is \"user\";" + SHDLModule.newline);
		sb.append("    attribute signal_encoding of stEppNext: signal is \"user\";" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		
		sb.append("    signal	clkMain	     : std_logic;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		
		sb.append("    signal	ctlEppWait   : std_logic;" + SHDLModule.newline);
		sb.append("    signal	ctlEppAstb   : std_logic;" + SHDLModule.newline);
		sb.append("    signal	ctlEppDstb   : std_logic;" + SHDLModule.newline);
		sb.append("    signal	ctlEppDir    : std_logic;" + SHDLModule.newline);
		sb.append("    signal	ctlEppWr     : std_logic;" + SHDLModule.newline);
		sb.append("    signal	ctlEppAwr    : std_logic;" + SHDLModule.newline);
		sb.append("    signal	ctlEppDwr    : std_logic;" + SHDLModule.newline);
		sb.append("    signal	busEppOut    : std_logic_vector(7 downto 0);" + SHDLModule.newline);
		sb.append("    signal	busEppIn     : std_logic_vector(7 downto 0);" + SHDLModule.newline);
		sb.append("    signal	busEppData   : std_logic_vector(7 downto 0);" + SHDLModule.newline);
		
		sb.append("    signal	regEppAdr    : std_logic_vector(3 downto 0);" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		
		sb.append("------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append("-- Module Implementation" + SHDLModule.newline);
		sb.append("------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("begin" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("    ------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append("	-- Map basic status and control signals" + SHDLModule.newline);
		sb.append("    ------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	clkMain <= mclk;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	ctlEppAstb <= astb;" + SHDLModule.newline);
		sb.append("	ctlEppDstb <= dstb;" + SHDLModule.newline);
		sb.append("	ctlEppWr   <= pwr;" + SHDLModule.newline);
		sb.append("	pwait      <= ctlEppWait;	-- drive WAIT from state machine output" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	-- Data bus direction control. The internal input data bus always" + SHDLModule.newline);
		sb.append("	-- gets the port data bus. The port data bus drives the internal" + SHDLModule.newline);
		sb.append("	-- output data bus onto the pins when the interface says we are doing" + SHDLModule.newline);
		sb.append("	-- a read cycle and we are in one of the read cycles states in the" + SHDLModule.newline);
		sb.append("	-- state machine." + SHDLModule.newline);
		sb.append("	busEppIn <= pdb;" + SHDLModule.newline);
		sb.append("	pdb <= busEppOut when ctlEppWr = '1' and ctlEppDir = '1' else \"ZZZZZZZZ\";" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	-- Select either address or data onto the internal output data bus." + SHDLModule.newline);
		sb.append("	busEppOut <= \"0000\" & regEppAdr when ctlEppAstb = '0' else busEppData;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	-- Decode the address register and select the appropriate data register" + SHDLModule.newline);
		sb.append("	busEppData <=" + SHDLModule.newline);
		sb.append("		board2pc(7 downto 0) when regEppAdr = \"0000\" else" + SHDLModule.newline);
		sb.append("		board2pc(15 downto 8) when regEppAdr = \"0001\" else" + SHDLModule.newline);
		sb.append("		board2pc(23 downto 16) when regEppAdr = \"0010\" else" + SHDLModule.newline);
		sb.append("		board2pc(31 downto 24) when regEppAdr = \"0011\" else" + SHDLModule.newline);
		sb.append("		board2pc(39 downto 32) when regEppAdr = \"0100\" else" + SHDLModule.newline);
		sb.append("		board2pc(47 downto 40) when regEppAdr = \"0101\" else" + SHDLModule.newline);
		sb.append("		board2pc(55 downto 48) when regEppAdr = \"0110\" else" + SHDLModule.newline);
		sb.append("		board2pc(63 downto 56) when regEppAdr = \"0111\" else" + SHDLModule.newline);
		sb.append("		board2pc(71 downto 64) when regEppAdr = \"1000\" else" + SHDLModule.newline);
		sb.append("		board2pc(79 downto 72) when regEppAdr = \"1001\" else" + SHDLModule.newline);
		sb.append("		board2pc(87 downto 80) when regEppAdr = \"1010\" else" + SHDLModule.newline);
		sb.append("		board2pc(95 downto 88) when regEppAdr = \"1011\" else" + SHDLModule.newline);
		sb.append("		board2pc(103 downto 96) when regEppAdr = \"1100\" else" + SHDLModule.newline);
		sb.append("		board2pc(111 downto 104) when regEppAdr = \"1101\" else" + SHDLModule.newline);
		sb.append("		board2pc(119 downto 112) when regEppAdr = \"1110\" else" + SHDLModule.newline);
		sb.append("		board2pc(127 downto 120) when regEppAdr = \"1111\" ;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append("	-- EPP Interface Control State Machine" + SHDLModule.newline);
		sb.append("	------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	-- Map control signals from the current state" + SHDLModule.newline);
		sb.append("	ctlEppWait <= stEppCur(0);" + SHDLModule.newline);
		sb.append("	ctlEppDir  <= stEppCur(1);" + SHDLModule.newline);
		sb.append("	ctlEppAwr  <= stEppCur(2);" + SHDLModule.newline);
		sb.append("	ctlEppDwr  <= stEppCur(3);" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	-- This process moves the state machine to the next state" + SHDLModule.newline);
		sb.append("	-- on each clock cycle" + SHDLModule.newline);
		sb.append("	process (clkMain)" + SHDLModule.newline);
		sb.append("		begin" + SHDLModule.newline);
		sb.append("			if clkMain = '1' and clkMain'Event then" + SHDLModule.newline);
		sb.append("				stEppCur <= stEppNext;" + SHDLModule.newline);
		sb.append("			end if;" + SHDLModule.newline);
		sb.append("		end process;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	-- This process determines the next state machine state based" + SHDLModule.newline);
		sb.append("	-- on the current state and the state machine inputs." + SHDLModule.newline);
		sb.append("	process (stEppCur, stEppNext, ctlEppAstb, ctlEppDstb, ctlEppWr)" + SHDLModule.newline);
		sb.append("		begin" + SHDLModule.newline);
		sb.append("			case stEppCur is" + SHDLModule.newline);
		sb.append("				-- Idle state waiting for the beginning of an EPP cycle" + SHDLModule.newline);
		sb.append("				when stEppReady =>" + SHDLModule.newline);
		sb.append("					if ctlEppAstb = '0' then" + SHDLModule.newline);
		sb.append("						-- Address read or write cycle" + SHDLModule.newline);
		sb.append("						if ctlEppWr = '0' then" + SHDLModule.newline);
		sb.append("							stEppNext <= stEppAwrA;" + SHDLModule.newline);
		sb.append("						else" + SHDLModule.newline);
		sb.append("							stEppNext <= stEppArdA;" + SHDLModule.newline);
		sb.append("						end if;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("					elsif ctlEppDstb = '0' then" + SHDLModule.newline);
		sb.append("						-- Data read or write cycle" + SHDLModule.newline);
		sb.append("						if ctlEppWr = '0' then" + SHDLModule.newline);
		sb.append("							stEppNext <= stEppDwrA;" + SHDLModule.newline);
		sb.append("						else" + SHDLModule.newline);
		sb.append("							stEppNext <= stEppDrdA;" + SHDLModule.newline);
		sb.append("						end if;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("					else" + SHDLModule.newline);
		sb.append("						-- Remain in ready state" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppReady;" + SHDLModule.newline);
		sb.append("					end if;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				-- Write address register" + SHDLModule.newline);
		sb.append("				when stEppAwrA =>" + SHDLModule.newline);
		sb.append("					stEppNext <= stEppAwrB;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				when stEppAwrB =>" + SHDLModule.newline);
		sb.append("					if ctlEppAstb = '0' then" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppAwrB;" + SHDLModule.newline);
		sb.append("					else" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppReady;" + SHDLModule.newline);
		sb.append("					end if;		" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				-- Read address register" + SHDLModule.newline);
		sb.append("				when stEppArdA =>" + SHDLModule.newline);
		sb.append("					stEppNext <= stEppArdB;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				when stEppArdB =>" + SHDLModule.newline);
		sb.append("					if ctlEppAstb = '0' then" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppArdB;" + SHDLModule.newline);
		sb.append("					else" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppReady;" + SHDLModule.newline);
		sb.append("					end if;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				-- Write data register" + SHDLModule.newline);
		sb.append("				when stEppDwrA =>" + SHDLModule.newline);
		sb.append("					stEppNext <= stEppDwrB;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				when stEppDwrB =>" + SHDLModule.newline);
		sb.append("					if ctlEppDstb = '0' then" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppDwrB;" + SHDLModule.newline);
		sb.append("					else" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppReady;" + SHDLModule.newline);
		sb.append("					end if;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				-- Read data register" + SHDLModule.newline);
		sb.append("				when stEppDrdA =>" + SHDLModule.newline);
		sb.append("					stEppNext <= stEppDrdB;" + SHDLModule.newline);
		sb.append("								" + SHDLModule.newline);		
		sb.append("				when stEppDrdB =>" + SHDLModule.newline);
		sb.append("					if ctlEppDstb = '0' then" + SHDLModule.newline);
		sb.append("						stEppNext <= stEppDrdB;" + SHDLModule.newline);
		sb.append("					else" + SHDLModule.newline);
		sb.append("				  		stEppNext <= stEppReady;" + SHDLModule.newline);
		sb.append("					end if;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("				-- Some unknown state		" + SHDLModule.newline);		
		sb.append("				when others =>" + SHDLModule.newline);
		sb.append("					stEppNext <= stEppReady;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("			end case;" + SHDLModule.newline);
		sb.append("		end process;" + SHDLModule.newline);
		sb.append("		" + SHDLModule.newline);
		sb.append("    ------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append("	-- EPP Address register" + SHDLModule.newline);
		sb.append("    ------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("	process (clkMain, ctlEppAwr)" + SHDLModule.newline);
		sb.append("		begin" + SHDLModule.newline);
		sb.append("			if clkMain = '1' and clkMain'Event then" + SHDLModule.newline);
		sb.append("				if ctlEppAwr = '1' then" + SHDLModule.newline);
		sb.append("					regEppAdr <= busEppIn(3 downto 0);" + SHDLModule.newline);
		sb.append("				end if;" + SHDLModule.newline);
		sb.append("			end if;" + SHDLModule.newline);
		sb.append("		end process;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("    ------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append("	-- EPP Data registers" + SHDLModule.newline);
		sb.append("    ------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append(" 	-- The following processes implement the interface registers." + SHDLModule.newline);
		sb.append("	-- The ctlEppDwr signal is an output from the state machine that says" + SHDLModule.newline);
		sb.append("	-- we are in a 'write data register' state. This is combined with the" + SHDLModule.newline);
		sb.append("	-- address in the address register to determine which register to write." + SHDLModule.newline);

		for (int i = 0; i < 16; i++) {
			int n1 = i*8;
			int n2 = (i+1)*8-1;
			sb.append(SHDLModule.newline);
			sb.append("	process (clkMain, regEppAdr, ctlEppDwr, busEppIn)" + SHDLModule.newline);
			sb.append("		begin" + SHDLModule.newline);
			sb.append("			if clkMain = '1' and clkMain'Event then" + SHDLModule.newline);
			sb.append("				if ctlEppDwr = '1' and regEppAdr = \"" + formatBinary(i, 4) + "\" then" + SHDLModule.newline);
			sb.append("					pc2board(" + n2 + " downto " + n1 + ") <= busEppIn(7 downto 0);" + SHDLModule.newline);
			sb.append("				end if;" + SHDLModule.newline);
			sb.append("			end if;" + SHDLModule.newline);
			sb.append("		end process;" + SHDLModule.newline);
		}
		sb.append(SHDLModule.newline);
		sb.append("----------------------------------------------------------------------------" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("end synthesis ;" + SHDLModule.newline);
		return new String(sb);
	}
	
	private final String z6 = "000000";
	
	private String formatBinary(int n, int len) {
		String s = Integer.toString(n, 2);
		return z6.substring(0, 4 - s.length()) + s;
	}

}

