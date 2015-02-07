package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsInstrSetHi extends CrapsInstr {

	private NumExpr imm24;
	private String rd;

	public CrapsInstrSetHi(NumExpr imm24, String rd) {
		this.imm24 = imm24;
		this.rd = rd;
	}

	public String toString() {
		return ("SETHI, " + imm24 + ", " + rd);
	}

	public String format() {
		return ("sethi    " + imm24 + ", " + rd);
	}

	public int getCodeop() {
		return -1;
	}

	// expression giving the number of bytes
	private NumExpr four = new NumExprInt(4);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return four;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return imm24.isInstanciated(localSymbols, globalSymbols, this);
	}

	private long cacheImm24 = 2147483648L;
	private long getImm24(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheImm24 != 2147483648L) return cacheImm24;
		cacheImm24 = imm24.getValue(localSymbols, globalSymbols, this);
		return cacheImm24;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		long vimm24 = getImm24(localSymbols, globalSymbols);
		return ((vimm24 >= 0) && (vimm24 < 16777216L));
	}

	private long cacheWord = 4294967296L;
	private long getWord(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord != 4294967296L) return cacheWord;

		cacheWord = 0;
		long vimm24 = getImm24(localSymbols, globalSymbols);
		// 2-comp negative value
		if (vimm24 < 0) vimm24 += 4294967296L;
		cacheWord = vimm24;
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord += nrd * 16777216L; // 2^24 -> insertion au bit 24
		
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

