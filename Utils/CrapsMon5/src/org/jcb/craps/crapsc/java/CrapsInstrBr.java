package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsInstrBr extends CrapsInstr {

	private String codeop;
	private NumExpr targetAddr;

	public CrapsInstrBr(String codeop, NumExpr targetAddr) {
		this.codeop = codeop;
		this.targetAddr = targetAddr;
	}

	public String toString() {
		return ("INSTR_BR: codeop=" + codeop + ", targetAddr=" + targetAddr);
	}

	public String format() {
		return (codeop + "    " + targetAddr);
	}

	public int getCodeop() {
		return -1;
	}

	public int getCond() {
		if (codeop.equals("ba"))
			return 8;
		else if (codeop.equals("be"))
			return 1;
		else if (codeop.equals("beq"))
			return 1;
		else if (codeop.equals("bz"))
			return 1;
		else if (codeop.equals("bne"))
			return 9;
		else if (codeop.equals("bnz"))
			return 9;
		else if (codeop.equals("bcs"))
			return 5;
		else if (codeop.equals("blu"))
			return 5;
		else if (codeop.equals("bcc"))
			return 13;
		else if (codeop.equals("bgeu"))
			return 13;
		else if (codeop.equals("bpos"))
			return 14;
		else if (codeop.equals("bnn"))
			return 14;
		else if (codeop.equals("bneg"))
			return 6;
		else if (codeop.equals("bn"))
			return 6;
		else if (codeop.equals("bvs"))
			return 7;
		else if (codeop.equals("bvc"))
			return 15;
		else if (codeop.equals("bg"))
			return 10;
		else if (codeop.equals("bgt"))
			return 10;
		else if (codeop.equals("ble"))
			return 2;
		else if (codeop.equals("bge"))
			return 11;
		else if (codeop.equals("bl"))
			return 3;
		else if (codeop.equals("blt"))
			return 3;
		else if (codeop.equals("bgu"))
			return 12;
		else if (codeop.equals("bleu"))
			return 4;
		return 0;
	}


	// targetAddression giving the number of bytes
	private NumExpr four = new NumExprInt(4);
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		return four;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		return (targetAddr.isInstanciated(localSymbols, globalSymbols, this) && isAddressKnown());
	}

	// get displacement in words, as a 25-bit 2's-complement integer
	private long cacheDisp = 33554432L; // 2^25
	private long getDisp(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheDisp != 33554432L) return cacheDisp;
		cacheDisp = targetAddr.getValue(localSymbols, globalSymbols, this) - getAddress();
		return cacheDisp;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		long disp = getDisp(localSymbols, globalSymbols);
		return ((disp >= -16777216L) && (disp < 16777216L));
	}


	private long cacheWord = 4294967296L;
	private long getWord(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord != 4294967296L) return cacheWord;
		long disp = getDisp(localSymbols, globalSymbols);
		if (disp < 0) disp += 33554432L; // 2^25
		cacheWord = disp;
		cacheWord += getCond() * 33554432L; // 2^25
		cacheWord += 536870912L; // 2^29
		return cacheWord;
	}

	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		long word = getWord(localSymbols, globalSymbols);
		int msb = (int) (word / 65536);
		int lsb = (int) (word % 65536);
		switch (i) {
			case 0: return (msb / 256);
			case 1: return (msb % 256);
			case 2: return (lsb / 256);
			case 3: return (lsb % 256);
		}
		return -1;
	}


}

