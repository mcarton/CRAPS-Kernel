package org.jcb.craps.crapsc.java;

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

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		return 0; // sethi 0, %r0
	}
}

