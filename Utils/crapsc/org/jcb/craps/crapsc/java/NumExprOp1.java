
package org.jcb.craps.crapsc.java;
 
import java.io.*;
import java.util.*;
import java.text.*;
import org.jcb.craps.*;


public class NumExprOp1 extends NumExpr {
	
	private String op;
	private NumExpr arg1;
	
	public NumExprOp1(String op, NumExpr arg1) {
		this.op = op;
		this.arg1 = arg1;
	}
	
	public String toString() {
		//return ("(" + op + "," + arg1 + ")");
		if (op.equals("+")) return ("" + arg1); else return (op + arg1);
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		return arg1.isInstanciated(localSymbols, globalSymbols, cl);
	}

	public long getValue(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		long val1 = arg1.getValue(localSymbols, globalSymbols, cl);
		if (op.equals("+"))
			return val1;
		else if (op.equals("-"))
			return -val1;
		else
			return -1;
	}

}

