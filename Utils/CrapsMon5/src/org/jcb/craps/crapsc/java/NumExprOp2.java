
package org.jcb.craps.crapsc.java;
 
import java.io.*;
import java.util.*;
import java.text.*;
import org.jcb.craps.*;


public class NumExprOp2 extends NumExpr {
	
	private String op;
	private NumExpr arg1;
	private NumExpr arg2;
	
	public NumExprOp2(String op, NumExpr arg1, NumExpr arg2) {
		this.op = op;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public String toString() {
		return ("(" + op + "," + arg1 + "," + arg2 + ")");
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		return (arg1.isInstanciated(localSymbols, globalSymbols, cl) && arg2.isInstanciated(localSymbols, globalSymbols, cl));
	}

	public long getValue(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		long val1 = arg1.getValue(localSymbols, globalSymbols, cl);
		long val2 = arg2.getValue(localSymbols, globalSymbols, cl);
		if (op.equals("+"))
			return (val1 + val2);
		else if (op.equals("-"))
			return (val1 - val2);
		else if (op.equals("*"))
			return (val1 * val2);
		else if (op.equals("/"))
			return (val1 / val2);
		else if (op.equals("%"))
			return (val1 % val2);
		else
			return -1;
	}

}

