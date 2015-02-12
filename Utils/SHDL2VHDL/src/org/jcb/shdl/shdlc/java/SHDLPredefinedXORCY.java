
package org.jcb.shdl.shdlc.java;

import java.util.*;
import java.util.regex.*;
import java.io.*;


public class SHDLPredefinedXORCY extends SHDLPredefinedOccurence {


	public SHDLPredefinedXORCY(SHDLModuleOccurence moduleOccurence, Pattern namePattern) {
		super(moduleOccurence, namePattern);
	}

	public boolean isOutput(int index) {
		return (index == 2);
	}
	public boolean isInput(int index) {
		return (index <= 1);
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

		if (moduleOccurence.getArguments().size() != 3) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : it does not have the 3 mandatory arguments <arg1>, <arg2>, <result>");
			return false;
		}
		SHDLSignal arg1 = (SHDLSignal) moduleOccurence.getArguments().get(0);
		if (arg1.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : first argument (arg #1) must be a scaler");
			ok = false;
		}
		SHDLSignal arg2 = (SHDLSignal) moduleOccurence.getArguments().get(1);
		if (arg2.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : second argument (arg #2) must be a scaler");
			ok = false;
		}
		SHDLSignal result = (SHDLSignal) moduleOccurence.getArguments().get(2);
		if (result.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": built-in component '" + moduleOccurence.getName() + "' : third argument (result) must must be a scaler");
			ok = false;
		}
		checked = true;
		return ok;
	}

	public String getVHDLComponentDeclaration() {
		StringBuffer sb = new StringBuffer();
		sb.append("\tcomponent " + getModuleOccurence().getName() + SHDLModule.newline);
		sb.append("\t\tport (" + SHDLModule.newline);
		sb.append("\t\t\tCI   : in std_logic ;" + SHDLModule.newline);
		sb.append("\t\t\tLI   : in std_logic ;" + SHDLModule.newline);
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

