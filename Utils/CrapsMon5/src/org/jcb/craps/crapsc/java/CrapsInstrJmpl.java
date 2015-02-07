package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsInstrJmpl extends CrapsInstrArithLogMem {

	private SumExpr expr;
	private String rd;

	public CrapsInstrJmpl(SumExpr expr, String rd) {
		this.expr = expr;
		this.rd = rd;
	}

	public String toString() {
		return ("jmpl expr=" + expr + ", rd=" + rd);
	}

	public String format() {
		return ("jmpl  " + expr + ", " + rd);
	}


	public int getCodeop() {
		return 11;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		if (expr.rs2_or_const instanceof String) return true;
		NumExpr num = (NumExpr) expr.rs2_or_const;
		return num.isInstanciated(localSymbols, globalSymbols, this);
	}

	private long cacheConst = 8;
	private long getConst(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheConst != 8) return cacheConst;
System.out.println("expr.rs2_or_const=" + expr.rs2_or_const);
		NumExpr num = (NumExpr) expr.rs2_or_const;
		cacheConst = num.getValue(localSymbols, globalSymbols, this);
		return cacheConst;
	}

	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		if (expr.rs2_or_const instanceof String) return true;
		long vnum = getConst(localSymbols, globalSymbols);
		return ((vnum >= -8) && (vnum <= 7));
	}


	private long cacheWord = 65538;
	private long getWord(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheWord != 65538) return cacheWord;

		cacheWord = 0;
		if (expr.rs2_or_const instanceof String) {
			// register
			int rs2 = Integer.parseInt(((String) expr.rs2_or_const).substring(2));
			cacheWord = rs2;
		} else {
			// imm4
			long imm4 = ((NumExpr) expr.rs2_or_const).getValue(localSymbols, globalSymbols, this);
			// 2-comp negative value
			if (imm4 < 0) imm4 += 16;
			cacheWord = imm4 + 16;
		}
		int nrs1 = Integer.parseInt(expr.rs1.substring(2));
		cacheWord += nrs1 * 32;
		int nrd = Integer.parseInt(rd.substring(2));
		cacheWord += nrd * 256;
		cacheWord += getCodeop() * 2048;
		
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

