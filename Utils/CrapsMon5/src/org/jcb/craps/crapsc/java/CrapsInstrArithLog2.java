package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


// notcc
public class CrapsInstrArithLog2 extends CrapsInstrArithLogMem {

	private String codeop;
	private String rs1;
	private String rd;

	public CrapsInstrArithLog2(String codeop, String rs1, String rd) {
		this.codeop = codeop;
		this.rs1 = rs1;
		this.rd = rd;
	}

	public String toString() {
		return ("INSTR2: codeop=" + codeop + ", rs1=" + rs1 + ", rd=" + rd);
	}

	public String format() {
		return (codeop + "    " + rs1 + ", " + rd);
	}

	private int cacheCodeop = -1;
	public int getCodeop() {
		if (cacheCodeop != -1) return cacheCodeop;
		if (codeop.equals("notcc")) cacheCodeop = 5;
		else if (codeop.equals("not")) cacheCodeop = 13;
		return cacheCodeop;
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return true;
	}


	private long cacheWord = 65538;
	private long getWord(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord != 65538) return cacheWord;

		cacheWord = 0;
		int nrs1 = Integer.parseInt(rs1.substring(2));
		cacheWord += nrs1 * 32;
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord += nrd * 256;
		cacheWord += getCodeop() * 2048;
		
		return cacheWord;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		long word = getWord(localSymbols, globalSymbols);
		int msb = (int) (word / 65536);
		int lsb = (int) (word % 65536);
		switch (i) {
			case 0: return (msb / 256);
			case 1: return (msb % 256);
			case 2: return (lsb / 256);
			case 3: return (lsb % 256);
		}
		return -1;
	}

}

