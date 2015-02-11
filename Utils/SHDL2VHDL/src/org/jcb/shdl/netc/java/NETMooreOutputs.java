package org.jcb.shdl.netc.java;

public class NETMooreOutputs {
	
	private String state;
	private NETAffectations affectations;
	
	public NETMooreOutputs(String state, NETAffectations affectations) {
		this.state = state;
		this.affectations = affectations;
	}
	
	public String toString() {
		return "NETMooreOutputs state=" + state + ", affectations=" + affectations;
	}
	
	public String getState() {
		return state;
	}
	
	public NETAffectations getAffectations() {
		return affectations;
	}

}
