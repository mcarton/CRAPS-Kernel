package org.jcb.shdl.netc.java;

public class NETStatement {
	
	private NETTransition transition;
	private NETMooreOutputs mooreOutputs;
	private String added;
	
	public NETStatement(NETTransition transition) {
		this.transition = transition;
	}
	
	public NETStatement(NETMooreOutputs mooreOutputs) {
		this.mooreOutputs = mooreOutputs;
	}
	
	public NETStatement(String added) {
		// on enlève "++" en tête
		this.added = added.substring(2);
	}
	
	public String toString() {
		if (transition != null)
			return "NETStatement=" + transition;
		else if (mooreOutputs != null)
			return "NETStatement=" + mooreOutputs;
		else
			return "NETStatement=" + added;
	}
	
	public NETTransition getTransition() {
		return transition;
	}
	
	public NETMooreOutputs getMooreOutputs() {
		return mooreOutputs;
	}
	
	public String getAdded() {
		return added;
	}
}
