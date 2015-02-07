package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthClr extends CrapsInstrDirecSynth {

	private String dest_reg;

	public CrapsSynthClr(String dest_reg) {
		this.dest_reg = dest_reg;
	}

	public String toString() {
		return ("SYNTH_CLR: dest_reg=" + dest_reg);
	}

	public String format() {
		return ("clr   " + dest_reg);
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


	private long cacheMSB = 4294967296L;
	private long getMSB(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheMSB != 4294967296L) return cacheMSB;
		int nrd = Integer.parseInt(dest_reg.substring(2));
		cacheMSB = 18 * 524288; // 2^19, codeop=010010
		cacheMSB += nrd * 33554432; //2^25
		cacheMSB += 2147483648L; //2^31
		return cacheMSB;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		int msb = (int) (getMSB(localSymbols, globalSymbols) / 65536);
		switch (i) {
			case 0: return (msb / 256);
			case 1: return 0x90;
			case 2: return 0x00;
			case 3: return 0x00;
		}
		return -1;
	}

}

