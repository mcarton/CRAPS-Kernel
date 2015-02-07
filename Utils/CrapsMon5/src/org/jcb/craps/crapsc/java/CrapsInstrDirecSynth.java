package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


// instruction, synthetic instruction or directive
public abstract class CrapsInstrDirecSynth {

	public SourceLine sourceLine;		// associated source line
	private boolean addressKnown = false;
	private long adr;			// address of first byte

	public boolean isAddressKnown() {
		return addressKnown;
	}

	public long getAddress() {
		return adr;
	}
		
	public void setAddress(long adr) {
		this.adr = adr;
		addressKnown = true;
	}

	public abstract String format();

	// expression giving the number of bytes
	public abstract NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols);

	// true when all content bytes are instanciated, or all symbols or expressions (for .org, etc.)
	public abstract boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols);

	// valid only when content is instanciated
	public abstract int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols);
		
}

