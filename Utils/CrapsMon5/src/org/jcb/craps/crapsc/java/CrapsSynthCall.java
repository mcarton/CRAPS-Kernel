package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthCall extends CrapsInstrDirecSynth {

	private NumExpr targetAddr;

	public CrapsSynthCall(NumExpr targetAddr) {
		this.targetAddr = targetAddr;
	}

	public String toString() {
		return ("INSTR_CALL: targetAddr=" + targetAddr);
	}

	public String format() {
		return ("call    " + targetAddr);
	}


	// number of bytes
	private NumExpr eight = new NumExprInt(8);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return eight;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return (targetAddr.isInstanciated(localSymbols, globalSymbols, this) && isAddressKnown());
	}

	// get displacement, a 25-bit 2's-comp integer
	private long cacheDisp = 33554432L; // 2^25
	private long getDisp(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheDisp != 33554432L) return cacheDisp;
		cacheDisp = targetAddr.getValue(localSymbols, globalSymbols, this) - getAddress() - 1;
		System.out.println("target=" + targetAddr.getValue(localSymbols, globalSymbols, this) + ", addr=" + getAddress());
		return cacheDisp;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		long disp = getDisp(localSymbols, globalSymbols);
		// range is [-2^24,2^24-1]
		return ((disp >= -16777216L) && (disp < 16777215L));
	}
/*
	// disp must be a multiple of 4
	public boolean isDispMultiple4(ObjModule localSymbols, ObjModule globalSymbols) {
		long disp = getDisp(localSymbols, globalSymbols);
		return (disp % 4 == 0);
	}
*/
	// word1 = or	%r0, %r30, %r28 = 0xB810 001E

	// ba	<addr>
	private long cacheWord2 = 4294967296L;
	private long getWord2(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord2 != 4294967296L) return cacheWord2;
		long disp = getDisp(localSymbols, globalSymbols);
		if (disp < 0) disp += 33554432L; // 2^25
		cacheWord2 = disp;
		cacheWord2 += 8 * 33554432L; // 2^25, 8=ba
		cacheWord2 += 536870912L; // 2^29
		return cacheWord2;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		long word2 = getWord2(localSymbols, globalSymbols);
		int msb2 = (int) (word2 / 65536);
		int lsb2 = (int) (word2 % 65536);
		switch (i) {
			// word1 = or %r0, %r30, %r28 = 0xB810 001E
			case 0: return 0xB8;
			case 1: return 0x10;
			case 2: return 0x00;
			case 3: return 0x1E;
			
			case 4: return (msb2 / 256);
			case 5: return (msb2 % 256);
			case 6: return (lsb2 / 256);
			case 7: return (lsb2 % 256);
		}
		return -1;
	}

}

