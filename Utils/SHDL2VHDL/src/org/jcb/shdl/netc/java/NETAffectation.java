package org.jcb.shdl.netc.java;

public class NETAffectation {
	
	private NETSignal signal;
	private NETTermsSum termsSum;
	
	public NETAffectation(NETSignal signal, NETTermsSum termsSum) {
		this.signal = signal ;
		this.termsSum = termsSum ;
	}

	public String toString() {
		return "NETAffectation signal=" + signal + ", termsSum=" + termsSum;
	}
	
	public NETSignal getSignal() {
		return signal;
	}
	
	public NETTermsSum getTermsSum() {
		return termsSum;
	}

}
