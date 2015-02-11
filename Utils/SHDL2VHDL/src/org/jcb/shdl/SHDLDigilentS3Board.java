
package org.jcb.shdl;

import java.io.*;
import java.util.*;

import org.jcb.shdl.shdlc.java.*;

public class SHDLDigilentS3Board extends SHDLBoard {

	public SHDLDigilentS3Board() {
		super();
	}

	public String getBoardName() {
		return "S3-Board";
	}

	public String getBoardModuleName() {
		return "DigilentS3";
	}

	public String[] getBoardPrefixes() {
		return new String[] { "mclk", "btn", "sw", "ld", "an", "ssg", "hs", "vs",
			"red", "grn", "blu", "ps2d", "ps2c", "rxd", "txd", "rxda", "txda",
			"ce", "ub", "lb", "we", "oe", "addr", "m1io", "m2io" };
	}
	public String[] getBoardIOStatus() {
		return new String[] { "in", "in", "in", "out", "out", "out", "out", "out",
			"out", "out", "out", "inout", "out", "in", "out", "in", "out",
       			"out", "out", "out", "out", "out", "out", "inout", "inout" };
	}
	public int[] getBoardN1() {
		return new int[] { 0, 3, 7, 7, 3, 7, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0,
       			1, 1, 1, 0, 0, 17, 15, 15 };
	}
	public int[] getBoardN2() {
		return new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0,
			2, 2, 2, 0, 0, 0, 0, 0 };
	}
	// default values; only for 'out' signals
	public String[] getBoardDefaultValues() {
		return new String[] { "in", "in", "in", "00000000", "1111", "11111111", "0", "0",
			"0", "0", "0", "0", "1", "in", "1", "in", "1",
       			"11", "11", "11", "1", "1", "000000000000000000", "inout", "inout" };
	}
}

