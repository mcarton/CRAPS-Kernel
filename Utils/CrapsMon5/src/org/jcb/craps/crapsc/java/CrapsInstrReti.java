package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsInstrReti extends CrapsInstrArithLogMem {

	public CrapsInstrReti() {
	}

	public String toString() {
		return ("reti");
	}

	public String format() {
		return "reti";
	}


	public int getCodeop() {
		return 13;
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
		case 0: return 64;
		case 1: return 0;
		case 2: return 0;
		case 3: return 0;
		}
		return -1;
	}

}

