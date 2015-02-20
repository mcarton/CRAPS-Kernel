----------------------------------------------------------------------------------
-- Company:
-- Engineer:
--
-- Create Date:    08:37:31 02/19/2015
-- Design Name:
-- Module Name:    MemInterface - Behavioral
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

entity MemInterface is
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
			  -- debug
			  StateMain: out Std_logic_vector(4 downto 0);
			  StateWriter:out Std_logic_vector(1 downto 0)
			  );
end MemInterface;

architecture Behavioral of MemInterface is
	 -- state machines state declaration

    constant IdleState:std_logic_vector(4 downto 0):= "00000";
    constant writeAddr1:std_logic_vector(4 downto 0):= "00001";
    constant writeAddr2:std_logic_vector(4 downto 0):= "00010";
    constant writeAddr3:std_logic_vector(4 downto 0):= "00011";
    constant writeAddr1_wait:std_logic_vector(4 downto 0):= "00100";
    constant writeAddr2_wait:std_logic_vector(4 downto 0):= "00101";
    constant writeAddr3_wait:std_logic_vector(4 downto 0):= "00110";
    constant ReadReg7_1:std_logic_vector(4 downto 0):= "00111";
    constant ReadReg7_1_wait:std_logic_vector(4 downto 0):= "01000";
    constant ReadReg7_2:std_logic_vector(4 downto 0):= "01001";
    constant ReadReg7_2_wait:std_logic_vector(4 downto 0):= "01010";
    constant ReadReg7_3:std_logic_vector(4 downto 0):= "01011";
    constant ReadReg7_3_wait:std_logic_vector(4 downto 0):= "01100";
    constant ReadReg7_4:std_logic_vector(4 downto 0):= "01101";
    constant ReadReg7_4_wait:std_logic_vector(4 downto 0):= "01110";
    constant WriteReg7_1:std_logic_vector(4 downto 0):= "01111";
    constant WriteReg7_1_wait:std_logic_vector(4 downto 0):= "10000";
    constant WriteReg7_2:std_logic_vector(4 downto 0):= "10001";
    constant WriteReg7_2_wait:std_logic_vector(4 downto 0):= "10010";
    constant WriteReg7_3:std_logic_vector(4 downto 0):= "10011";
    constant WriteReg7_3_wait:std_logic_vector(4 downto 0):= "10100";
    constant WriteReg7_4:std_logic_vector(4 downto 0):= "10101";
    constant WriteReg7_4_wait:std_logic_vector(4 downto 0):= "10110";
    constant Done:std_logic_vector(4 downto 0):= "10111";

    constant Writer_Idle:std_logic_vector(1 downto 0):= "00";
    constant Writer_Addr:std_logic_vector(1 downto 0):= "01";
    constant Writer_AddrDone:std_logic_vector(1 downto 0):= "11";
    constant Writer_Data:std_logic_vector(1 downto 0):= "10";

    -- state machines state signals
    signal CurState :std_logic_vector(4 downto 0):=IdleState;
    signal NextState:std_logic_vector(4 downto 0);

    signal Writer_curState:std_logic_vector(1 downto 0):=Writer_Idle;
    signal Writer_nextState:std_logic_vector(1 downto 0);

    -- communication between state machines:

    signal Writer_start:std_logic;
    signal Writer_done:std_logic;

    signal EppAddrBuf:std_logic_vector(7 downto 0);
    signal EppDataBuf:std_logic_vector(7 downto 0);

    -- buffers

    signal ReadDataBuf: std_logic_vector(31 downto 0) :=(others => '0');
begin

-- debug
StateMain <= CurState;
StateWriter <= Writer_CurState;

     process (clk)
      begin
         if clk = '1' and clk'Event then
            if Rst = '0' then
               CurState <= IdleState;
					Writer_curState <=Writer_Idle;
            else
               CurState <= NextState;
					Writer_curState <=Writer_NextState;
            end if;
         end if;
      end process;
		WaitMem <= '0' when CurState =  IdleState else
						'1';
		Dout <= ReadDataBuf;

		Writer_start <= '1' when CurState = WriteAddr1 or CurState = WriteAddr2 or
										CurState = WriteAddr3 or
										CurState = ReadReg7_1 or CurState = ReadReg7_2 or
										CurState = ReadReg7_3 or CurState = ReadReg7_4 or
										CurState = WriteReg7_1 or CurState = WriteReg7_2 or
										CurState = WriteReg7_3 or CurState = WriteReg7_4 else
							'0';

		ReadDataBuf(7 downto 0) <= EppDB when CurState = ReadReg7_1_wait else
												ReadDataBuf(7 downto 0);
		ReadDataBuf(15 downto 8) <= EppDB when CurState = ReadReg7_2_wait else
												ReadDataBuf(15 downto 8);
		ReadDataBuf(23 downto 16) <= EppDB when CurState = ReadReg7_3_wait else
												ReadDataBuf(23 downto 16);
		ReadDataBuf(31 downto 24) <= EppDB when CurState = ReadReg7_4_wait else
												ReadDataBuf(31 downto 24);

		EppAddrBuf(7 downto 3) <= "00000";

		EppAddrBuf(2 downto 0) <= "001" when CurState = WriteAddr1 or CurState = WriteAddr1_wait else
										  "010" when CurState = WriteAddr2 or CurState = WriteAddr2_wait else
										  "011" when CurState = WriteAddr3 or CurState = WriteAddr3_wait else
										  "111" when CurState = ReadReg7_1 or CurState = ReadReg7_1_wait or
														 CurState = ReadReg7_2 or CurState = ReadReg7_2_wait or
														 CurState = ReadReg7_3 or CurState = ReadReg7_3_wait or
														 CurState = ReadReg7_4 or CurState = ReadReg7_4_wait or
														 CurState = WriteReg7_1 or CurState = WriteReg7_1_wait or
														 CurState = WriteReg7_2 or CurState = WriteReg7_2_wait or
														 CurState = WriteReg7_3 or CurState = WriteReg7_3_wait or
														 CurState = WriteReg7_4 or CurState = WriteReg7_4_wait else
											"000";
		EppDataBuf <=  Abus(7 downto 0) when CurState = WriteAddr1 or CurState = WriteAddr1_wait else
							Abus(15 downto 8) when CurState = WriteAddr2 or CurState = WriteAddr2_wait else
							"0000" & Abus (19 downto 16) when CurState = WriteAddr3 or CurState = WriteAddr3_wait else
							Din(7 downto 0) when CurState = WriteReg7_1 or CurState = WriteReg7_1_wait else
							Din(15 downto 8) when CurState = WriteReg7_2 or CurState = WriteReg7_2_wait else
							Din(23 downto 16) when CurState = WriteReg7_3 or CurState = WriteReg7_3_wait else
							Din(31 downto 24) when CurState = WriteReg7_4 or CurState = WriteReg7_4_wait else
							"00000000";


		-- Main state machine
		process(CurState)
      begin
          case CurState is
				when IdleState =>
					if Writecmd = '1' or ReadCmd = '1' then
						NextState <= WriteAddr1;
					else
						NextState <= IdleState;
					end if;

				when writeAddr1 =>
					NextState <= WriteAddr1_wait;

				when writeAddr2 =>
					NextState <= WriteAddr1_wait;

				when writeAddr3 =>
					NextState <= WriteAddr1_wait;

				when writeAddr1_wait =>
					if Writer_done = '1' then
						NextState <= WriteAddr2;
					else
						NextState <= writeAddr1_wait;
					end if;

				when writeAddr2_wait =>
					if Writer_done = '1' then
						NextState <= WriteAddr3;
					else
						NextState <= writeAddr2_wait;
					end if;

				when writeAddr3_wait =>
					if Writer_done = '1' then
						if writecmd = '1' then
							NextState <= WriteReg7_1;
						else
							NextState <= ReadReg7_1;
						end if;
					else
						NextState <= writeAddr3_wait;
					end if;

				when ReadReg7_1 =>
					NextState <= ReadReg7_1_wait;

				when ReadReg7_1_wait =>
					if Writer_done = '1' then
						NextState <= ReadReg7_2;
					else
						NextState <= ReadReg7_1_wait;
					end if;

				when ReadReg7_2 =>
					NextState <= ReadReg7_2_wait;

				when ReadReg7_2_wait =>
					if Writer_done = '1' then
						NextState <= ReadReg7_2;
					else
						NextState <= ReadReg7_1_wait;
					end if;
				when ReadReg7_3 =>
					NextState <= ReadReg7_3_wait;

				when ReadReg7_3_wait =>
					if Writer_done = '1' then
						NextState <= ReadReg7_2;
					else
						NextState <= ReadReg7_1_wait;
					end if;

				when ReadReg7_4 =>
					NextState <= ReadReg7_4_wait;

				when ReadReg7_4_wait =>
					if Writer_done = '1' then
						NextState <= Done;
					else
						NextState <= ReadReg7_1_wait;
					end if;

				when WriteReg7_1 =>
					NextState <= WriteReg7_1_wait;

				when WriteReg7_1_wait =>
					if Writer_done = '1' then
						NextState <= WriteReg7_2;
					else
						NextState <= WriteReg7_1_wait;
					end if;

				when WriteReg7_2 =>
					NextState <= WriteReg7_2_wait;

				when WriteReg7_2_wait =>
					if Writer_done = '1' then
						NextState <= WriteReg7_3;
					else
						NextState <= WriteReg7_2_wait;
					end if;

				when WriteReg7_3 =>
					NextState <= WriteReg7_3_wait;

				when WriteReg7_3_wait =>
					if Writer_done = '1' then
						NextState <= WriteReg7_4;
					else
						NextState <= WriteReg7_3_wait;
					end if;

				when WriteReg7_4 =>
					NextState <= WriteReg7_4_wait;

				when WriteReg7_4_wait =>
					if Writer_done = '1' then
						NextState <= Done;
					else
						NextState <= WriteReg7_4_wait;
					end if;

				when Done =>
					NextState <= IdleState;
				when Others =>
					NextState <= IdleState;
			end case;
		end process;



		--Writer related signals

		Writer_done <= '1' when Writer_CurState = Writer_Idle else
							'0';
		EppDB <= EppAddrBuf when Writer_Curstate = Writer_Addr else
					EppDataBuf when Writer_Curstate = Writer_Data and writeCmd = '1' else
					"ZZZZZZZZ";
		EppAstb <= '0' when Writer_Curstate = Writer_Addr else
						'1';
		EppDstb <= '0' when Writer_Curstate = Writer_Data else
						'1';
		EppWrite <= '0' when Writer_Curstate = Writer_Addr or
									(Writer_Curstate = Writer_Data and
									  writeCmd = '1' )else
						'1';
		-- Writer State Machine (actually also reads)
		process(Writer_CurState)
      begin
          case Writer_CurState is
              when Writer_Idle =>
						if Writer_start = '1' then
							Writer_nextState <= Writer_Addr;
						else
							Writer_nextState <= Writer_Idle;
						end if;

				  when Writer_Addr =>
						if EppWait ='1' then
							Writer_nextState <= Writer_AddrDone;
						else
							Writer_nextState <= Writer_Addr;
						end if;
				  when Writer_AddrDone =>
						Writer_nextState <= Writer_Data;
				  when Writer_Data =>
						if EppWait ='1' then
							Writer_nextState <= Writer_Idle;
						else
							Writer_nextState <= Writer_Data;
						end if;
				 when others =>
						Writer_nextState <= Writer_Idle;

			end case;
      end process;

end Behavioral;
