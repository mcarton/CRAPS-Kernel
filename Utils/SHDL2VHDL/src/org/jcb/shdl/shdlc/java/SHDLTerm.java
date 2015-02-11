
package org.jcb.shdl.shdlc.java;

import java.util.*;

public class SHDLTerm {

	private SHDLModule module;
	private ArrayList signalOccurences;

	public SHDLTerm(SHDLModule module) {
		this.module = module;
		signalOccurences = new ArrayList();
	}
	public String toString() {
		return getWrittenForm();
	}
	public String getWrittenForm() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < signalOccurences.size(); i++) {
			if (i > 0) sb.append("*");
			sb.append(((SHDLSignalOccurence) signalOccurences.get(i)).getWrittenForm());
		}
		return new String(sb);
	}
	public void addSignalOccurence(SHDLSignalOccurence sig) {
		signalOccurences.add(sig);
	}
	public ArrayList getSignalOccurences() {
		return signalOccurences;
	}

	public boolean containsScalars() {
		for (int i = 0; i < signalOccurences.size(); i++) {
			SHDLSignalOccurence sigOcc = (SHDLSignalOccurence) signalOccurences.get(i);
			if (sigOcc.getSignal().getArity() == 1) return true;
		}
		return false;
	}
	public SHDLTerm getScalars() {
		SHDLTerm res = new SHDLTerm(module);
		for (int i = 0; i < signalOccurences.size(); i++) {
			SHDLSignalOccurence sigOcc = (SHDLSignalOccurence) signalOccurences.get(i);
			if (sigOcc.getSignal().getArity() == 1) res.addSignalOccurence(sigOcc);
		}
		return res;
	}
	public SHDLTerm getBusesAndConsts() {
		SHDLTerm res = new SHDLTerm(module);
		for (int i = 0; i < signalOccurences.size(); i++) {
			SHDLSignalOccurence sigOcc = (SHDLSignalOccurence) signalOccurences.get(i);
			if (sigOcc.getSignal().getArity() != 1) res.addSignalOccurence(sigOcc);
		}
		return res;
	}

}

