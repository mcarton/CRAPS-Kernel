
package org.jcb.shdl;

import java.io.*;
import java.util.*;

import org.jcb.shdl.shdlc.java.*;

public class SHDLDigilentNexysBoard extends SHDLBoard {

	public SHDLDigilentNexysBoard() {
		super() ;
	}

	public String getBoardName() {
		return "Nexys";
	}

	public String getBoardModuleName() {
		return "Nexys";
	}

	public String[] getBoardPrefixes() {
		return new String[] { "mclk", "btn", "sw", "ld", "an", "ssg", "rxd", "txd",
			"pdb", "astb", "dstb", "pwr", "pwait",
			"memdb", "memadr", "ramcs", "flashcs", "memwr", "memoe", "ramub",
			"ramlb", "ramcre", "ramadv", "ramclk", "ramwait", "flashrp", "flashststs",
			"red", "grn", "blue", "hs", "vs",
			"ja1", "ja2", "ja3", "ja4" ,"ja7", "ja8", "ja9", "ja10",
			"jb1", "jb2", "jb3", "jb4" ,"jb7", "jb8", "jb9", "jb10",
			"jc1", "jc2", "jc3", "jc4" ,"jc7", "jc8", "jc9", "jc10",
			"jd1", "jd2", "jd3", "jd4" ,"jd7", "jd8", "jd9", "jd10",
		};
	}
	public String[] getBoardIOStatus() {
		return new String[] { "in", "in", "in", "out", "out", "out", "in", "out",
			"inout", "in", "in", "in", "out",
			"inout", "out", "out", "out", "out", "out", "out",
			"out", "out", "out", "out", "in", "out", "in",
			"out", "out", "out", "out", "out",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
		};
	}
	public int[] getBoardN1() {
		return new int[] { 0, 3, 7, 7, 3, 7, 0, 0,
			7, 0, 0, 0, 0,
			15, 23, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			2, 2, 1, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
		};
	}
	public int[] getBoardN2() {
		return new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,
		};
	}
	// default values; only for 'out' signals
	public String[] getBoardDefaultValues() {
		return new String[] { "in", "in", "in", "00000000", "1111", "11111111", "in", "0",
			"inout", "in", "in", "in", "0", 
			"inout", "00000000000000000000000", "1", "1", "1", "1", "0",
			"0", "0", "0", "0", "in", "1", "in",
			"000", "000", "00", "0", "0",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
			"inout", "inout", "inout", "inout", "inout", "inout", "inout", "inout",
		 };
	}
}

