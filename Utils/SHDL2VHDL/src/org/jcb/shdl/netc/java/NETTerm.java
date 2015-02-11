
package org.jcb.shdl.netc.java;

import java.util.*;

public class NETTerm {

	private ArrayList signalOccurences;

	public NETTerm() {
		signalOccurences = new ArrayList();
	}
	public String toString() {
		return getWrittenForm();
	}
	public String getWrittenForm() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < signalOccurences.size(); i++) {
			if (i > 0) sb.append("*");
			sb.append(((NETSignalOccurence) signalOccurences.get(i)).getWrittenForm());
		}
		return new String(sb);
	}
	
	public void addSignalOccurence(NETSignalOccurence sig) {
		signalOccurences.add(sig);
	}
	
	public ArrayList getSignalOccurences() {
		return signalOccurences;
	}

	public boolean containsScalars() {
		for (int i = 0; i < signalOccurences.size(); i++) {
			NETSignalOccurence sigOcc = (NETSignalOccurence) signalOccurences.get(i);
			if (sigOcc.getSignal().getArity() == 1) return true;
		}
		return false;
	}

	public boolean isZero() {
		for (int i = 0; i < signalOccurences.size(); i++) {
			NETSignalOccurence sigOcc = (NETSignalOccurence) signalOccurences.get(i);
			NETSignal sig = sigOcc.getSignal();
			String xxx = "" + sig + sig.isConstant(); // nécessaire?!
			if (sig.isConstant() && (sig.getConstantValue().intValue() == 0)) return true;
		}
		return false;
	}
	
	public NETTerm getScalars() {
		NETTerm res = new NETTerm();
		for (int i = 0; i < signalOccurences.size(); i++) {
			NETSignalOccurence sigOcc = (NETSignalOccurence) signalOccurences.get(i);
			if (sigOcc.getSignal().getArity() == 1) res.addSignalOccurence(sigOcc);
		}
		return res;
	}
	
	public NETTerm getBusesAndConsts() {
		NETTerm res = new NETTerm();
		for (int i = 0; i < signalOccurences.size(); i++) {
			NETSignalOccurence sigOcc = (NETSignalOccurence) signalOccurences.get(i);
			if (sigOcc.getSignal().getArity() != 1) res.addSignalOccurence(sigOcc);
		}
		return res;
	}
}

