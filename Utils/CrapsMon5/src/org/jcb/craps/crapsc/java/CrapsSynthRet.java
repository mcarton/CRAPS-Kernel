package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthRet extends CrapsInstrDirecSynth {

	public CrapsSynthRet() {
	}

	public String toString() {
		return ("SYNTH_RET");
	}

	public String format() {
		return ("ret");
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

	// valid only when content is instanciated
	// ret = BC07 2001 = add %r28, 1, %r30
	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		switch (i) {
			case 0: return 0xBC;
			case 1: return 0x07;
			case 2: return 0x20;
			case 3: return 0x01;
		}
		return 0;
	}


}

