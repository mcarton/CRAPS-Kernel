
package org.jcb.shdl.shdlc.java;

public class SHDLCombinatorialSetting {

	private int lineNo;
	private SHDLModule module;
	private SHDLSignal signal;
	private SHDLTermsSum termsSum;
	private SHDLSignalOccurence sigOE; // /oe1 for "x = y:/oe1"

	public SHDLCombinatorialSetting(int lineNo, SHDLModule module) {
		this.lineNo = lineNo;
		this.module = module;
	}
	public String toString() {
		return "(" + lineNo + ": signal=" + signal + ", termsum=" + termsSum + ", oe=" + sigOE + ")";
	}
	public SHDLModule getModule() {
		return module;
	}
	public SHDLSignal getSignal() {
		return signal;
	}
	public void setSignal(SHDLSignal signal) {
		// possibly replace signal by its unique representative in the module
		this.signal = module.addModuleSignal(signal);
	}
	public SHDLTermsSum getEquation() {
		return termsSum;
	}
	public void setEquation(SHDLTermsSum termsSum) {
		this.termsSum = termsSum;
	}
	public void setOE(SHDLSignalOccurence sigOE) {
		this.sigOE = sigOE;
	}
	public SHDLSignalOccurence getOE() {
		return sigOE;
	}
	public int getLineNo() {
		return lineNo;
	}
}
