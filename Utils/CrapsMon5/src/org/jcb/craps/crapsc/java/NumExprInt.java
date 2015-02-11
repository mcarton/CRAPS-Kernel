
package org.jcb.craps.crapsc.java;
 
import java.io.*;
import java.util.*;
import java.text.*;
import org.jcb.craps.*;


public class NumExprInt extends NumExpr {
	
	private long val;
	
	public NumExprInt(long val) {
		this.val = val;
	}
	
	public NumExprInt(String sval) {
		this.val = Long.parseLong(sval, 10);
	}
	
	public NumExprInt(String sval, int radix) {
		switch (radix) {
			case 2:
				this.val = Long.parseLong(sval.substring(2), 2);
				break;
			case 10:
				this.val = Long.parseLong(sval, 10);
				break;
			case 16:
				this.val = Long.parseLong(sval.substring(2), 16);
				break;
		}
	}
	
	public String toString() {
		return ("" + val);
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		return true;
	}

	public long getValue(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl) {
		return val;
	}

}

