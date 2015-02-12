package org.jcb.shdl.netc.java;

public class NETStateDiagram {
	
	private NETInterface interf;
	private NETStatements statements;
	
	public NETStateDiagram(NETInterface interf, NETStatements statements) {
		this.interf = interf ;
		this.statements = statements ;
	}
	
	public String toString() {
		return "NETStateDiagram interface=" + interf + ", statements=" + statements;
	}
	
	public NETInterface getInterface() {
		return interf;
	}
	
	public NETStatements getStatements() {
		return statements;
	}

}

