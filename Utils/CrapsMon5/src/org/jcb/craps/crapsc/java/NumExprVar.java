
package org.jcb.craps.crapsc.java;
 
import java.io.*;
import java.util.*;
import java.text.*;
import org.jcb.craps.*;


public class NumExprVar extends NumExpr {
	
	private String var;
	
	public NumExprVar(String var) {
		this.var = var;
	}
	
	public String toString() {
		return var;
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		if (var.equals("*")) return cl.isAddressKnown();
		if (localSymbols.isDefined(var)) return true;
		if (globalSymbols.isDefined(var)) return true;
		return false;
	}

	public long getValue(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		if (var.equals("*")) return cl.getAddress();
		if (localSymbols.isDefined(var)) return localSymbols.getIntVal(var);
		if (globalSymbols.isDefined(var)) return globalSymbols.getIntVal(var);
		return -1;
	}

}

