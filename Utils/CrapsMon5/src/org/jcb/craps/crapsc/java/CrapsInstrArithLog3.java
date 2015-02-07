package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsInstrArithLog3 extends CrapsInstrArithLogMem {

	private String codeop;
	private String rs1;
	private Object arg2;
	private String rd;

	public CrapsInstrArithLog3(String codeop, String rs1, Object arg2, String rd) {
		this.codeop = codeop;
		this.rs1 = rs1;
		this.arg2 = arg2;
		this.rd = rd;
	}

	public String toString() {
		return ("INSTR3: codeop=" + codeop + ", rs1=" + rs1 + ", arg2=" + arg2 + ", rd=" + rd);
	}

	public String format() {
		return (codeop + "   " + rs1 + ", " + arg2 + ", " + rd);
	}


	private int cacheCodeop = -1;
	public int getCodeop() {
		if (cacheCodeop != -1) return cacheCodeop;
		if (codeop.equals("add")) cacheCodeop = 0x00;
		else if (codeop.equals("addcc")) cacheCodeop = 0x10;
		else if (codeop.equals("sub")) cacheCodeop = 0x04;
		else if (codeop.equals("subcc")) cacheCodeop = 0x14;
		else if (codeop.equals("umulcc")) cacheCodeop = 0x1A;
		else if (codeop.equals("and")) cacheCodeop = 0x01;
		else if (codeop.equals("or")) cacheCodeop = 0x02;
		else if (codeop.equals("xor")) cacheCodeop = 0x03;
		else if (codeop.equals("xnor")) cacheCodeop = 0x07;
		else if (codeop.equals("andcc")) cacheCodeop = 0x11;
		else if (codeop.equals("orcc")) cacheCodeop = 0x12;
		else if (codeop.equals("xorcc")) cacheCodeop = 0x13;
		else if (codeop.equals("xnorcc")) cacheCodeop = 0x17;
		else if (codeop.equals("srl")) cacheCodeop = 13;
		else if (codeop.equals("sll")) cacheCodeop = 14;
		else if (codeop.equals("jmpl")) cacheCodeop = 0x38;
		return cacheCodeop;
	}

	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		if (arg2 instanceof String) return true;
		NumExpr imm13 = (NumExpr) arg2;
		return imm13.isInstanciated(localSymbols, globalSymbols, this);
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		if (arg2 instanceof String) return true;
		NumExpr imm13 = (NumExpr) arg2;
		int vimm13 = (int) imm13.getValue(localSymbols, globalSymbols, this);
		return ((vimm13 >= -4096) && (vimm13 <= 4095));
	}

	private long cacheWord = 4294967296L;
	private long getWord(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord != 4294967296L) return cacheWord;

		cacheWord = 0;
		if (arg2 instanceof String) {
			// register
			int rs2 = Integer.parseInt(((String) arg2).substring(2));
			cacheWord = rs2;
		} else {
			// imm13
			int imm13 = (int) ((NumExpr) arg2).getValue(localSymbols, globalSymbols, this);
			// 2's-comp negative value
			if (imm13 < 0) imm13 += 8192;
			cacheWord = imm13 + 8192; //2^13;
		}
		int nrs1 = Integer.parseInt(rs1.substring(2));
		cacheWord += nrs1 * 16384; //2^14;
		cacheWord += getCodeop() * 524288; //2^19;
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord += nrd * 33554432; //2^25;
		cacheWord += 2147483648L; //2^31;
		
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

