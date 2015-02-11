package org.jcb.shdl.netc.java;

import java.util.ArrayList;

public class NETStatements {
	
	private ArrayList statements = new ArrayList() ;
	
	public NETStatements() {
	}
	
	public void addStatement(NETStatement statement) {
		statements.add(0, statement) ;
	}
	
	public String toString() {
		return "NETStatements=" + statements ;
	}
	
	public ArrayList getStatements() {
		return statements;
	}

}
