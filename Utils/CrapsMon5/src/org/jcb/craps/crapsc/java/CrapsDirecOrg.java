package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsDirecOrg extends CrapsInstrDirecSynth {

	NumExpr expr;

	public CrapsDirecOrg(NumExpr expr) {
		this.expr = expr;
	}

	public NumExpr getExpr() {
		return expr;
	}

	public String toString() {
		return (".ORG: expr=" + expr);
	}

	public String format() {
		return (".ORG:   " + expr);
	}

	// expression giving the number of bytes
	private NumExpr length0 = new NumExprInt(0);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return length0;
	}

	// no content for .org
	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		return -1;
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return expr.isInstanciated(localSymbols, globalSymbols, this);
	}

}

