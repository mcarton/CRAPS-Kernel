
package org.jcb.shdl.shdlc.java;


public class SHDLSequentialModifier {

	private int lineNo;
	private SHDLModule module;
	private SHDLSignal signal; // x  for  "x.rst = /y"
	private String modifier; // "rst" for "x.rst = /y"
	private SHDLSignalOccurence signalOccurence; // /y for "x.rst = /y"

	public SHDLSequentialModifier(int lineNo, SHDLModule module) {
		this.lineNo = lineNo;
		this.module = module;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer("(" + lineNo + ": signal=" + signal);
		sb.append(", " + modifier + "=" + signalOccurence + ")");
		return new String(sb);
	}
	public SHDLModule getModule() {
		return module;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setSignal(SHDLSignal signal) {
		// possibly replace signal by its unique representative in the module
		this.signal = module.addModuleSignal(signal);
	}
	public SHDLSignal getSignal() {
		return signal;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getModifier() {
		return modifier;
	}
	public void setSignalOccurence(SHDLSignalOccurence signalOccurence) {
		this.signalOccurence = signalOccurence;
	}
	public SHDLSignalOccurence getSignalOccurence() {
		return signalOccurence;
	}
}

