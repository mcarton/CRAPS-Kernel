package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsInstrSt extends CrapsInstrArithLogMem {

	private String codeop;
	private String rd;
	private AddrContent addr_content;

	public CrapsInstrSt(String codeop, String rd, AddrContent addr_content) {
		this.codeop = codeop;
		this.rd = rd;
		this.addr_content = addr_content;
		if (addr_content.rs1 == null) addr_content.rs1 = "%r0";
		if (addr_content.rs2_or_disp == null) addr_content.rs2_or_disp = "%r0";
	}

	public String toString() {
		return ("INSTR_ST: codeop=" + codeop + ", rd=" + rd + ", addr_content=" + addr_content);
	}

	public String format() {
		return (codeop + "   " + rd + ", " + addr_content);
	}


	public int getCodeop() {
		return -1;
	}

	public void setAddrContent(AddrContent addr_content) {
		this.addr_content = addr_content;
		if (addr_content.rs1 == null) addr_content.rs1 = "%r0";
		if (addr_content.rs2_or_disp == null) addr_content.rs2_or_disp = "%r0";
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		if (addr_content.rs2_or_disp instanceof String) return true;
		NumExpr disp = (NumExpr) addr_content.rs2_or_disp;
		return disp.isInstanciated(localSymbols, globalSymbols, this);
	}

	private int cacheDisp = 8;
	private int getDisp(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheDisp != 8) return cacheDisp;
		NumExpr disp = (NumExpr) addr_content.rs2_or_disp;
		cacheDisp = (int) disp.getValue(localSymbols, globalSymbols, this);
		return cacheDisp;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		if (addr_content.rs2_or_disp instanceof String) return true;
		int vdisp = getDisp(localSymbols, globalSymbols);
		return ((vdisp >= -4096) && (vdisp <= 4095));
	}

	private long cacheWord = 4294967296L;
	private long getWord(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord != 4294967296L) return cacheWord;

		cacheWord = 0;
		if (addr_content.rs2_or_disp instanceof String) {
			// register
			int rs2 = Integer.parseInt(((String) addr_content.rs2_or_disp).substring(2));
			cacheWord = rs2;
		} else {
			// imm13
			long imm13 = ((NumExpr) addr_content.rs2_or_disp).getValue(localSymbols, globalSymbols, this);
			// 2-comp negative value
			if (imm13 < 0) imm13 += 8192;
			cacheWord = imm13 + 8192; // bit 13 = 1
		}
		int nrs1 = Integer.parseInt(addr_content.rs1.substring(2));
		cacheWord += nrs1 * 16384; // 2^14;
		if (codeop.equals("st"))
			cacheWord += 4 * 524288; // 2^19; (st, codeop = 000100)
		else
			cacheWord += 5 * 524288; // 2^19; (stb, codeop = 000101)
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord += nrd * 33554432; // 2^25;
		cacheWord += 3 * 1073741824L; // 2^30 (type=11)
		
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

