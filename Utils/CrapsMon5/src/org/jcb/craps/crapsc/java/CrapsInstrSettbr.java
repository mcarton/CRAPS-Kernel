package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsInstrSettbr extends CrapsInstrArithLogMem {

	private String rs1;

	public CrapsInstrSettbr(String rs1) {
		this.rs1 = rs1;
	}

	public String toString() {
		return ("settbr, rs1=" + rs1);
	}

	public String format() {
		return ("settbr  " + rs1);
	}


	public int getCodeop() {
		return 12;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return true;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		return true;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		switch (i) {
			case 0: return 96;
			case 1: int nrs1 = Integer.parseInt(rs1.substring(2));
				return nrs1 * 32;
		}
		return -1;
	}

}

