package org.jcb.shdl.netc.java;

import java.util.ArrayList;

public class NETInterface {
	
	private String moduleName ;
	private NETSignalOccurence reset ;
	private NETSignalOccurence clock ;
	private NETSignals inputs;
	private NETSignals outputs;
	private NETSignals added_outputs;
	
	public NETInterface(String moduleName, NETSignalOccurence reset, NETSignalOccurence clock, NETSignals inputs, NETSignals outputs, NETSignals added_outputs) {
		this.moduleName = moduleName ;
		this.reset = reset ;
		this.clock = clock ;
		this.inputs = inputs;
		this.outputs = outputs;
		this.added_outputs = added_outputs;
	}
	
	public String toString() {
		return "NETInterface moduleName=" + moduleName + ", reset=" + reset + ", clock=" + clock + ", inputs=" + inputs + ", outputs=" + outputs + ", added_outputs=" + added_outputs ;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public NETSignalOccurence getReset() {
		return reset;
	}
	
	public NETSignalOccurence getClock() {
		return clock;
	}
	
	public NETSignals getInputs() {
		return inputs;
	}
	
	public NETSignals getOutputs() {
		return outputs;
	}
	
	public NETSignals getAddedOutputs() {
		return added_outputs;
	}

}
