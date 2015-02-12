------------------------------------------------------------------------
-- Author:  Dan Pederson
--          Copyright 2004 Digilent, Inc.
------------------------------------------------------------------------
-- Description: This file defines a UART which tranfers data from
--              serial form to parallel form and vice versa.
------------------------------------------------------------------------
-- Revision History:
--  07/15/04 (Created) DanP
--  02/25/08 (Created) ClaudiaG: made use of the baudDivide constant
--                               in the Clock Dividing Processes
------------------------------------------------------------------------

component Rs232RefComp is
    port (
        TXD     : out   std_logic                      := '1';
        RXD     : in    std_logic;
        CLK     : in    std_logic;                             --Master Clock = 50MHz
        DBIN    : in    std_logic_vector (7 downto 0);         --Data Bus in
        DBOUT   : out   std_logic_vector (7 downto 0);         --Data Bus out
        RDA     : inout std_logic;                             --Read Data Available
        TBE     : inout std_logic                      := '1'; --Transfer Bus Empty
        RD      : in    std_logic;                             --Read Strobe
        WR      : in    std_logic;                             --Write Strobe
        PE      : out   std_logic;                             --Parity Error Flag
        FE      : out   std_logic;                             --Frame Error Flag
        OE      : out   std_logic;                             --Overwrite Error Flag
        RST     : in    std_logic                      := '0'  --Master Reset
    );
end component;
