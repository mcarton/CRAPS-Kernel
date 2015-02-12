
package org.jcb.shdl.shdlc.java;

import java.util.*;

public class SHDLTermsSum {

	private SHDLModule module;
	private ArrayList terms;

	public SHDLTermsSum(SHDLModule module) {
		this.module = module;
		terms = new ArrayList();
	}
	public String toString() {
		return getWrittenForm();
	}
	public String getWrittenForm() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < terms.size(); i++) {
			if (i > 0) sb.append("+");
			sb.append(((SHDLTerm) terms.get(i)).getWrittenForm());
		}
		return new String(sb);
	}
	public void addTerm(SHDLTerm term) {
		terms.add(term);
	}
	public ArrayList getTerms() {
		return terms;
	}

	public boolean containsScalars() {
		for (int i = 0; i < terms.size(); i++) {
			SHDLTerm term = (SHDLTerm) terms.get(i);
			if (term.containsScalars()) return true;
		}
		return false;
	}
}

