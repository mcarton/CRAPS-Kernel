package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsDirecGlobal extends CrapsInstrDirecSynth {

	String symbol;

	public CrapsDirecGlobal(String symbol) {
		this.symbol = symbol;
	}

	public String toString() {
		return (".GLOBAL: sym=" + symbol);
	}

	public String format() {
		return (".GLOBAL: " + symbol);
	}

	// expression giving the number of bytes
	private NumExpr length0 = new NumExprInt(0);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return length0;
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return localSymbols.isDefined(symbol);
	}

	// no content for .global
	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		return -1;
	}

	public String getSymbol() {
		return symbol;
	}

}

