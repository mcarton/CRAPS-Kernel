package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthNop extends CrapsInstrDirecSynth {

	public CrapsSynthNop() {
	}

	public String toString() {
		return ("SYNTH_NOP");
	}

	public String format() {
		return ("nop");
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

	private long getWord1(ObjModule localSymbols, ObjModule globalSymbols) {
		return 4 * 4194304; // 2^22, codeop=100
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

