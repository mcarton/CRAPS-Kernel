package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthSet extends CrapsInstrDirecSynth {

	private NumExpr imm32;
	private String rd;

	public CrapsSynthSet(NumExpr imm32, String rd) {
		this.imm32 = imm32;
		this.rd = rd;
	}

	public String toString() {
		return ("SYNTH_SET: imm32=" + imm32 + ", rd=" + rd);
	}

	public String format() {
		return ("set    " + imm32 + ", " + rd);
	}


	// number of bytes
	private NumExpr eight = new NumExprInt(8);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return eight;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return imm32.isInstanciated(localSymbols, globalSymbols, this);
	}

	private long cacheImm32 = 2147483648L;
	private long getImm32(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheImm32 != 2147483648L) return cacheImm32;
		cacheImm32 = imm32.getValue(localSymbols, globalSymbols, this);
		return cacheImm32;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		long imm32 = getImm32(localSymbols, globalSymbols);
		if ((imm32 < -2147483648L /*-2^31*/) || (imm32 > 4294967295L/*2^32-1*/)) return false;
		return true;
	}

	private long cacheWord1 = 4294967296L;
	private long getWord1(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord1 != 4294967296L) return cacheWord1;
		long imm32 = getImm32(localSymbols, globalSymbols);
		if (imm32 < 0) imm32 += 4294967296L;
		cacheWord1 = imm32 / 256; // 32-8=24 bits de poids forts
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord1 += nrd * 16777216L; // 2^24 -> insertion au bit 24
		return cacheWord1;
	}

	private long cacheWord2 = 4294967296L;
	private long getWord2(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord2 != 4294967296L) return cacheWord2;
		long imm32 = getImm32(localSymbols, globalSymbols);
		if (imm32 < 0) imm32 += 4294967296L;
		cacheWord2 = imm32 % 256; // 8 bits de poids faibles
		cacheWord2 += 8192; // 2^13 -> bit 13 = 1
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord2 += nrd * 16384; // 2^14
		cacheWord2 +=  18 * 524288; // 2^19, codeop=18 (orcc)
		cacheWord2 += nrd * 33554432L; // 2^25 -> insertion au bit 25
		cacheWord2 += 2147483648L; // 2^31 -> bit 31 = 1
		return cacheWord2;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		long word1 = getWord1(localSymbols, globalSymbols);
		long word2 = getWord2(localSymbols, globalSymbols);
		int msb1 = (int) (word1 / 65536);
		int lsb1 = (int) (word1 % 65536);
		int msb2 = (int) (word2 / 65536);
		int lsb2 = (int) (word2 % 65536);
		switch (i) {
			case 0: return (msb1 / 256);
			case 1: return (msb1 % 256);
			case 2: return (lsb1 / 256);
			case 3: return (lsb1 % 256);
			case 4: return (msb2 / 256);
			case 5: return (msb2 % 256);
			case 6: return (lsb2 / 256);
			case 7: return (lsb2 % 256);
		}
		return -1;
	}

}

