package org.jcb.craps.crapsc.java;

import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthInt extends CrapsInstrDirecSynth {

	public CrapsSynthInt() {
	}

	public String toString() {
		return "SYNTH_INT";
	}

	public String format() {
		return "int";
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
		switch(i) {
			case 0: return 96;
			case 1: return 128;
			default: return 0;
		}
	}
}

