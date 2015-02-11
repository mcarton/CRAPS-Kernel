
package org.jcb.shdl.shdlc.java;

import java.util.*;

public class SHDLModuleOccurence {

	private String moduleName; // module name of the occurence itself
	private int beginLine;
	private SHDLModule module; // module into which the occurence is inserted
	private ArrayList arguments;
	private ArrayList uniqueArguments;
	private SHDLPredefinedOccurence predefined;

	public SHDLModuleOccurence(String moduleName, int beginLine, SHDLModule module) {
		this.moduleName = moduleName;
		this.beginLine = beginLine;
		this.module = module;
		arguments = new ArrayList();
	}
	public String toString() {
		return "(" + beginLine + ": module=" + moduleName + ", arguments=" + arguments + ")";
	}
	public String getName() {
		return moduleName;
	}
	public SHDLModule getModule() {
		return module;
	}
	public int getBeginLine() {
		return beginLine;
	}
	public ArrayList getArguments() {
		if (uniqueArguments == null) {
			// replace each argument signal by its unique representative
			uniqueArguments = new ArrayList();
			for (int i = 0; i < arguments.size(); i++) {
				SHDLSignal arg = (SHDLSignal) arguments.get(i);
				uniqueArguments.add(module.addModuleSignal(arg));
			}
		}
		return uniqueArguments;
	}
	// called at parse time: arg is possibly not the unique representative signal
	public void addArgument(SHDLSignal signal) {
		arguments.add(signal);
	}

	public void setPredefined(SHDLPredefinedOccurence predefined) {
		this.predefined = predefined;
	}
	public SHDLPredefinedOccurence getPredefined() {
		return predefined;
	}
	public boolean isPredefined() {
		return (predefined != null);
	}

}
