
package org.jcb.shdl.shdlc.java;

public class SHDLSignalOccurence {

	private SHDLSignal signal;
	private boolean inverted;

	public SHDLSignalOccurence(SHDLSignal signal, boolean inverted, SHDLModule module) {
		// possibly replace signal by its unique representative in the module
		this.signal = module.addModuleSignal(signal);
		this.inverted = inverted;
	}
	public String toString() {
		return getWrittenForm();
	}
	public String getWrittenForm() {
		if (signal == null) return "--";
		if (inverted) return ("/" + signal.getName()); else return signal.getName();
	}

	public void setSignal(SHDLSignal signal) {
		this.signal = signal;
	}
	public SHDLSignal getSignal() {
		return signal;
	}
	public boolean isInverted() {
		return inverted;
	}
}

