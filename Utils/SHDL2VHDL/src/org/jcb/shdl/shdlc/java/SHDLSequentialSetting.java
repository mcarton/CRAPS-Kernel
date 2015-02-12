
package org.jcb.shdl.shdlc.java;


public class SHDLSequentialSetting {

	private int lineNo;
	private SHDLModule module;
	private SHDLSignal signal; // x  for  x := ...
	private SHDLSignalOccurence sig1; // =s1  for q := s1*s2+s3*s4 or =d  for  q := d
	private SHDLSignalOccurence sig2; // =s2  for q := s1*s2+s3*s4
	private SHDLSignalOccurence sig3; // =s3  for q := s1*s2+s3*s4
	private SHDLSignalOccurence sig4; // =s4  for q := s1*s2+s3*s4

	private SHDLSequentialModifier rst;
	private SHDLSequentialModifier set;
	private SHDLSequentialModifier clk;
	private SHDLSequentialModifier ena;


	public SHDLSequentialSetting(int lineNo, SHDLModule module) {
		this.lineNo = lineNo;
		this.module = module;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer("(" + lineNo + ": signal=");
		sb.append(signal + ", evolution=");
		if (sig1 == null) sb.append("null");
		else if (sig2 == null) sb.append(sig1.toString());
		else if (sig2 != null) sb.append(sig1.toString() + "*" + sig2.toString() + "+" + sig3.toString() + "*" + sig4.toString());
		sb.append(", clk=" + getCLK());
		sb.append(", rst=" + getRST());
		sb.append(", set=" + getSET());
		sb.append(", ena=" + getENA());
		return new String(sb);
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
	public int getLineNo() {
		return lineNo;
	}
	public void setSig1(SHDLSignalOccurence sig1) {
		this.sig1 = sig1;
	}
	public SHDLSignalOccurence getSig1() {
		return sig1;
	}
	public void setSig2(SHDLSignalOccurence sig2) {
		this.sig2 = sig2;
	}
	public SHDLSignalOccurence getSig2() {
		return sig2;
	}
	public void setSig3(SHDLSignalOccurence sig3) {
		this.sig3 = sig3;
	}
	public SHDLSignalOccurence getSig3() {
		return sig3;
	}
	public void setSig4(SHDLSignalOccurence sig4) {
		this.sig4 = sig4;
	}
	public SHDLSignalOccurence getSig4() {
		return sig4;
	}

	public boolean isDFlipFlop() {
		return (sig2 == null);
	}

	public void setRST(SHDLSequentialModifier rst) {
		this.rst = rst;
	}
	public SHDLSequentialModifier getRST() {
		return rst;
	}
	public void setSET(SHDLSequentialModifier set) {
		this.set = set;
	}
	public SHDLSequentialModifier getSET() {
		return set;
	}
	public void setCLK(SHDLSequentialModifier clk) {
		this.clk = clk;
	}
	public SHDLSequentialModifier getCLK() {
		return clk;
	}
	public void setENA(SHDLSequentialModifier ena) {
		this.ena = ena;
	}
	public SHDLSequentialModifier getENA() {
		return ena;
	}

	SHDLTermsSum getEvolution() {
		SHDLTermsSum res = new SHDLTermsSum(module);
		if (sig2 == null) {
			SHDLTerm term = new SHDLTerm(module);
			term.addSignalOccurence(sig1);
			res.addTerm(term);
		} else {
			SHDLTerm term1 = new SHDLTerm(module);
			term1.addSignalOccurence(sig1);
			term1.addSignalOccurence(sig2);
			res.addTerm(term1);
			SHDLTerm term2 = new SHDLTerm(module);
			term2.addSignalOccurence(sig3);
			term2.addSignalOccurence(sig4);
			res.addTerm(term2);
		}
		return res;
	}
}

