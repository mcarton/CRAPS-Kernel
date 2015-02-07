package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthPush extends CrapsInstrDirecSynth {

	private String rs1;

	public CrapsSynthPush(String rs1) {
		this.rs1 = rs1;
	}

	public String toString() {
		return ("SYNTH_PUSH: rs1=" + rs1);
	}

	public String format() {
		return ("push   " + rs1);
	}


	// number of bytes
	private NumExpr eight = new NumExprInt(8);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return eight;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return true;
	}

	// sub	%r29, 1, %r29
	private long cacheWord1 = 4294967296L;
	private long getWord1(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord1 != 4294967296L) return cacheWord1;
		cacheWord1 = 1; // simm13 = 1
		cacheWord1 += 8192; // 2^13
		cacheWord1 += 29 * 16384; // 2^14
		cacheWord1 += 4 * 524288; // 2^19, codeop=100
		cacheWord1 += 29 * 33554432; // 2^25 -> insertion au bit 25
		cacheWord1 += 2147483648L; // 2 * 2^30 -> op = 10
		return cacheWord1;
	}

	// st	%rd, [%r29]
	private long cacheWord2 = 4294967296L;
	private long getWord2(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord2 != 4294967296L) return cacheWord2;
		int nrs1 = Integer.parseInt(rs1.substring(2));
		cacheWord2 = 29 * 16384; // 2^14
		cacheWord2 += 4 * 524288; // 2^19, codeop=100
		cacheWord2 += nrs1 * 33554432L; // 2^25 -> insertion au bit 25
		cacheWord2 += 3 * 1073741824L; // 3 * 2^30, op=11
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

