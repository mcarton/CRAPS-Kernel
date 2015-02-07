package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


// inc, dec, neg, tst, clr

public class CrapsSynth1 extends CrapsInstrDirecSynth {

	private String codeop;
	private String reg;

	public CrapsSynth1(String codeop, String reg) {
		this.codeop = codeop;
		this.reg = reg;
	}

	public String toString() {
		return ("SYNTH1: codeop=" + codeop + ", reg=" + reg);
	}

	public String format() {
		return (codeop + "   " + reg);
	}

	// expression giving the number of ints
	private NumExpr two = new NumExprInt(2);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return two;
	}

	// true when all content ints are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return true;
	}

	private long cacheWord1 = 65536;
	private long getWord1(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord1 != 65536) return cacheWord1;
		int nrd = Integer.parseInt(reg.substring(2));
		if (codeop.equals("inc")) {
			// codeop addcc = 0
			cacheWord1 = 0 * 2048 + nrd * 256 + nrd * 32 + 17;
		} else if (codeop.equals("dec")) {
			// codeop subcc = 1
			cacheWord1 = 1 * 2048 + nrd * 256 + nrd * 32 + 17;
		} else if (codeop.equals("neg")) {
			// codeop subcc = 1
			cacheWord1 = 1 * 2048 + nrd * 256 + nrd;
		} else if (codeop.equals("tst")) {
			// codeop orcc = 3
			cacheWord1 = 3 * 2048 + nrd * 32;
		} else if (codeop.equals("clr")) {
			// codeop orcc = 3
			cacheWord1 = 3 * 2048 + nrd * 256 + 16;
		}
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

