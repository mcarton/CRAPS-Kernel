
package org.jcb.shdl.shdlc.java;

import java.util.*;
import java.util.regex.*;
import java.io.*;


public class SHDLPredefinedMUXCY extends SHDLPredefinedOccurence {


	public SHDLPredefinedMUXCY(SHDLModuleOccurence moduleOccurence, Pattern namePattern) {
		super(moduleOccurence, namePattern);
	}

	public boolean isOutput(int index) {
		return (index == 3);
	}
	public boolean isInput(int index) {
		return (index <= 2);
	}
	public boolean isInputOutput(int index) {
		return false;
	}
	public int getArity(int index) {
		return 1;
	}
	
	public boolean isInLibrary() {
		return true;
	}

	public boolean check(boolean ok, SHDLModule module, PrintStream errorStream) {
		SHDLModuleOccurence moduleOccurence = getModuleOccurence();
		String name = moduleOccurence.getName().toLowerCase();

		if (moduleOccurence.getArguments().size() != 4) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : it does not have the 4 mandatory arguments DI, CI, S, 0");
			return false;
		}
		SHDLSignal O = (SHDLSignal) moduleOccurence.getArguments().get(0);
		if (O.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : first argument (O) must be a scaler");
			ok = false;
		}
		SHDLSignal DI = (SHDLSignal) moduleOccurence.getArguments().get(1);
		if (DI.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : second argument (CI) must be a scaler");
			ok = false;
		}
		SHDLSignal LI = (SHDLSignal) moduleOccurence.getArguments().get(2);
		if (LI.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : third argument (LI) must must be a scaler");
			ok = false;
		}
		SHDLSignal S = (SHDLSignal) moduleOccurence.getArguments().get(3);
		if (S.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : fourth argument (S) must must be a scaler");
			ok = false;
		}
		checked = true;
		return ok;
	}

	public String getVHDLComponentDeclaration() {
		StringBuffer sb = new StringBuffer();
		sb.append("\tcomponent " + getModuleOccurence().getName() + SHDLModule.newline);
		sb.append("\t\tport (" + SHDLModule.newline);
		sb.append("\t\t\tDI   : in std_logic ;" + SHDLModule.newline);
		sb.append("\t\t\tCI   : in std_logic ;" + SHDLModule.newline);
		sb.append("\t\t\tS    : in std_logic ;" + SHDLModule.newline);
		sb.append("\t\t\tO    : out std_logic" + SHDLModule.newline);
		sb.append("\t\t) ;" + SHDLModule.newline);
		sb.append("\tend component ;");
		return new String(sb);
	}

	// no need to define since it is in library
	public String getVHDLDefinition() {
		return null;
	}
}

