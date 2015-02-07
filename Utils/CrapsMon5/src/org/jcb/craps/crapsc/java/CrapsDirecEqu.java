package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;


public class CrapsDirecEqu extends CrapsInstrDirecSynth {

	public NumExpr expr;

	public CrapsDirecEqu(NumExpr expr) {
		this.expr = expr;
	}

	public String toString() {
		return (".EQU: expr=" + expr);
	}

	public String format() {
		return (".EQU:   " + expr);
	}

	// expression giving the number of bytes
	private NumExpr length0 = new NumExprInt(0);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return length0;
	}

	// no content for .equ
	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		return -1;
	}

	// specific to this directive
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return expr.isInstanciated(localSymbols, globalSymbols, this);
	}

}

