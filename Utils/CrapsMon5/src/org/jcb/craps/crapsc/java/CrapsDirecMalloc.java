package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsDirecMalloc extends CrapsInstrDirecSynth {

	private NumExpr n;

	public CrapsDirecMalloc(NumExpr n) {
		this.n = n;
	}

	public String toString() {
		return (".MALLOC: n=" + n);
	}

	public String format() {
		return (".MALLOC  " + n);
	}

	// expression giving the number of bytes
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbol) {
		return n;
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbol) {
		return n.isInstanciated(localSymbols, globalSymbol, this);
	}

	// valid only when content is instanciated
	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbol) {
		return 0;
	}

}

