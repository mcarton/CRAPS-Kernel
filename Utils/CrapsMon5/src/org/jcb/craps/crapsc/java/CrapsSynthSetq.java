package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsSynthSetq extends CrapsInstrDirecSynth {

	private NumExpr simm13;
	private String rd;

	public CrapsSynthSetq(NumExpr simm13, String rd) {
		this.simm13 = simm13;
		this.rd = rd;
	}

	public String toString() {
		return ("SYNTH_SETQ: simm13=" + simm13 + ", rd=" + rd);
	}

	public String format() {
		return ("setq   " + simm13 + ", " + rd);
	}


	// number of bytes
	private NumExpr four = new NumExprInt(4);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return four;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return simm13.isInstanciated(localSymbols, globalSymbols, this);
	}

	private long cacheSimm13 = 2147483648L;
	private long getSimm13(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheSimm13 != 2147483648L) return cacheSimm13;
		cacheSimm13 = simm13.getValue(localSymbols, globalSymbols, this);
		return cacheSimm13;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		long simm13 = getSimm13(localSymbols, globalSymbols);
		if ((simm13 < -4096/*-2^12*/) || (simm13 > 4095/*2^12-1*/)) return false;
		return true;
	}

	private long cacheWord1 = 4294967296L;
	private long getWord1(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord1 != 4294967296L) return cacheWord1;
		long simm13 = getSimm13(localSymbols, globalSymbols);
		if (simm13 < 0) simm13 += 8192;
		cacheWord1 += simm13;
		cacheWord1 += 8192; // 2^13 -> bit 13 = 1
		cacheWord1 +=  18 * 524288; // 2^19, codeop=18 (orcc)
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord1 += nrd * 33554432L; // 2^25 -> insertion au bit 25
		cacheWord1 += 2147483648L; // 2^31 -> bit 31 = 1
		return cacheWord1;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		long word1 = getWord1(localSymbols, globalSymbols);
		int msb1 = (int) (word1 / 65536);
		int lsb1 = (int) (word1 % 65536);
		switch (i) {
			case 0: return (msb1 / 256);
			case 1: return (msb1 % 256);
			case 2: return (lsb1 / 256);
			case 3: return (lsb1 % 256);
		}
		return -1;
	}

}

