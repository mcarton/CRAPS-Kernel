package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthJmp extends CrapsInstrDirecSynth {

	private String src_reg;

	public CrapsSynthJmp(String src_reg) {
		this.src_reg = src_reg;
	}

	public String toString() {
		return ("SYNTH_JMP: src_reg=" + src_reg);
	}

	public String format() {
		return ("jmp   " + src_reg);
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


	private long cacheWord = 4294967296L;
	private long getWord(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord != 4294967296L) return cacheWord;
		int nrs1 = Integer.parseInt(src_reg.substring(2));
		cacheWord = nrs1 * 16384; //2^14
		cacheWord += 56 * 524288; // 2^19, codeop=111000
		cacheWord += 2147483648L; //2^31
		return cacheWord;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		int msb = (int) (getWord(localSymbols, globalSymbols) / 65536);
		int lsb = (int) (getWord(localSymbols, globalSymbols) % 65536);
		switch (i) {
			case 0: return (msb / 256);
			case 1: return (msb % 256);
			case 2: return (lsb / 256);
			case 3: return (lsb % 256);
		}
		return -1;
	}

}

