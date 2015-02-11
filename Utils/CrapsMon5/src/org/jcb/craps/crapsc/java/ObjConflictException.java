package org.jcb.craps.crapsc.java;

import org.jcb.craps.crapsc.java.*;

public class ObjConflictException extends Exception {

	private long addr;
	private String word;
	private SourceLine sl;

	public ObjConflictException(long addr, String word, SourceLine sl) {
		this.addr = addr;
		this.word = word;
		this.sl = sl;
	}

	public long getAddr() {
		return addr;
	}

}

