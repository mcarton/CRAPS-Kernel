package org.jcb.shdl.netc.java;

import java.util.ArrayList;

public class NETAffectations {
	
	private ArrayList affectations = new ArrayList();
	
	public NETAffectations() {
	}
	
	public void addAffectation(NETAffectation affectation) {
		affectations.add(0, affectation);
	}
	
	public String toString() {
		return "NETAffectations=" + affectations;
	}
	
	public ArrayList getAffectations() {
		return affectations;
	}

}
