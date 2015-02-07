package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthNegcc extends CrapsInstrDirecSynth {

	private String src_reg;

	public CrapsSynthNegcc(String src_reg) {
		this.src_reg = src_reg;
	}

	public String toString() {
		return ("SYNTH_NEGCC: src_reg=" + src_reg);
	}

	public String format() {
		return ("negcc " + src_reg);
	}


	// expression giving the number of bytes
	private NumExpr four = new NumExprInt(4);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return four;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return true;
	}


	private long cacheWord1 = 4294967296L;
	private long getWord1(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord1 != 4294967296L) return cacheWord1;
		int nrs1 = Integer.parseInt(src_reg.substring(2));
		cacheWord1 = nrs1;
		cacheWord1 += 20 * 524288; // 2^19, codeop=010100
		cacheWord1 += nrs1 * 33554432; //2^25;
		cacheWord1 += 2147483648L; //2^31
		return cacheWord1;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		long word = getWord1(localSymbols, globalSymbols);
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

