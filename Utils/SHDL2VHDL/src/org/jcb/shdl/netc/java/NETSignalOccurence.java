
package org.jcb.shdl.netc.java;

public class NETSignalOccurence {

	private NETSignal signal;
	private boolean inverted;

	public NETSignalOccurence(NETSignal signal, boolean inverted) {
		this.signal = signal;
		this.inverted = inverted;
	}
	public String toString() {
		return getWrittenForm();
	}
	public String getWrittenForm() {
		if (signal == null) return "--";
		if (inverted) return ("/" + signal.getNormalizedName()); else return signal.getNormalizedName();
	}

	public void setSignal(NETSignal signal) {
		this.signal = signal;
	}
	public NETSignal getSignal() {
		return signal;
	}
	public boolean isInverted() {
		return inverted;
	}
}

