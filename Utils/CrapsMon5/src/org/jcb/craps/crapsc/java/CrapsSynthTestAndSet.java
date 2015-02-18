package org.jcb.craps.crapsc.java;

import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthTestAndSet extends CrapsInstrDirecSynth {
	private String reg;

	public CrapsSynthTestAndSet(String reg) {
		this.reg = reg;
	}

	public String toString() {
		return ("SYNTH_TESTANDSET: reg=" + reg);
	}

	public String format() {
		return "ts   " + reg;
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

		cacheWord = 0x60000000;
		cacheWord += Integer.parseInt(reg.substring(2));
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

