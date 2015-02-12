package org.jcb.shdl.netc.java;

import java.util.ArrayList;

public class NETSignals {
	
	private ArrayList signals = new ArrayList();
	
	public NETSignals() {
	}
	
	public void addSignal(NETSignal signal) {
		signals.add(0, signal);
	}

	public String toString() {
		return "NETSignals=" + signals ;
	}
	
	public ArrayList getSignals() {
		return signals;
	}

}
