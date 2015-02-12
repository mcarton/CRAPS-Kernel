package org.jcb.shdl.netc.java;

public class NETTransition {
	
	private String src;
	private String dest;
	private NETTermsSum condition;
	private NETAffectations affectations;
	
	public NETTransition(String src, String dest, NETTermsSum condition, NETAffectations affectations) {
		this.src = src;
		this.dest = dest;
		this.condition = condition;
		this.affectations = affectations;
	}
	
	public String toString() {
		return "NETTransition src=" + src + ", dest=" + dest + ", condition=" + condition + ", affectations=" + affectations;
	}
	
	public String getSrc() {
		return src;
	}
	
	public String getDest() {
		return dest;
	}
	
	public NETTermsSum getCondition() {
		return condition;
	}
	
	public NETAffectations getAffectations() {
		return affectations;
	}

}
