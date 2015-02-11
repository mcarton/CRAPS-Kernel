
package org.jcb.shdl.netc.java;

import java.util.*;

public class NETTermsSum {

	private ArrayList terms;

	public NETTermsSum() {
		terms = new ArrayList();
	}
	public String toString() {
		return getWrittenForm();
	}
	public String getWrittenForm() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < terms.size(); i++) {
			if (i > 0) sb.append("+");
			sb.append(((NETTerm) terms.get(i)).getWrittenForm());
		}
		return new String(sb);
	}
	public void addTerm(NETTerm term) {
		terms.add(term);
	}
	public ArrayList getTerms() {
		return terms;
	}

	public boolean containsScalars() {
		for (int i = 0; i < terms.size(); i++) {
			NETTerm term = (NETTerm) terms.get(i);
			if (term.containsScalars()) return true;
		}
		return false;
	}

	public boolean isZeros() {
		for (int i = 0; i < terms.size(); i++) {
			NETTerm term = (NETTerm) terms.get(i);
			if (!term.isZero()) return false;
		}
		return true;
	}
}

