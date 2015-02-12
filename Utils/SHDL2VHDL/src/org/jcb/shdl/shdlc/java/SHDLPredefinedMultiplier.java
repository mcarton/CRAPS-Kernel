package org.jcb.shdl.shdlc.java;

import java.util.*;
import java.util.regex.*;
import java.io.*;


public class SHDLPredefinedMultiplier extends SHDLPredefinedOccurence {

	private int nbit;

	public SHDLPredefinedMultiplier(SHDLModuleOccurence moduleOccurence, Pattern namePattern) {
		super(moduleOccurence, namePattern);
	}

	public boolean isInput(int index) {
		return (index <= 1);
	}
	public boolean isOutput(int index) {
		return (index > 1);
	}
	public boolean isInputOutput(int index) {
		return false;
	}
	public int getArity(int index) {
		switch (index) {
			case 0: return nbit;
			case 1: return nbit;
			case 2: return 2*nbit;
		}
		return -1;
	}
	

	public boolean check(boolean ok, SHDLModule module, PrintStream errorStream) {
		SHDLModuleOccurence moduleOccurence = getModuleOccurence();
		String name = moduleOccurence.getName().toLowerCase();
		Matcher m = getNamePattern().matcher(name);
		m.find();
		nbit = Integer.parseInt(m.group(1));
		int nbit2 = Integer.parseInt(m.group(2));
		if (nbit != nbit2) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined multiplier '" + moduleOccurence.getName() + "' : both bit width must be equal");
			return false;
		}

		if (moduleOccurence.getArguments().size() != 3) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined multiuplier '" + moduleOccurence.getName() + "' : it does not have the 3 mandatory arguments <arg1>, <arg2>, <result>");
			return false;
		}
		SHDLSignal arg1 = (SHDLSignal) moduleOccurence.getArguments().get(0);
		if (arg1.getArity() != nbit) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined multiplier '" + moduleOccurence.getName() + "' : first argument (arg #1) must have an arity of " + nbit);
			ok = false;
		}
		SHDLSignal arg2 = (SHDLSignal) moduleOccurence.getArguments().get(1);
		if (arg2.getArity() != nbit) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined multiplier '" + moduleOccurence.getName() + "' : second argument (arg #2) must have an arity of " + nbit);
			ok = false;
		}
		SHDLSignal result = (SHDLSignal) moduleOccurence.getArguments().get(2);
		if (result.getArity() != 2 * nbit) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined multiplier '" + moduleOccurence.getName() + "' : third argument (result) must have an arity of " + (2 * nbit));
			ok = false;
		}
		checked = true;
		return ok;
	}

	public String getVHDLComponentDeclaration() {
		StringBuffer sb = new StringBuffer();
		sb.append("\tcomponent " + getModuleOccurence().getName() + SHDLModule.newline);
		sb.append("\t\tport (" + SHDLModule.newline);
		sb.append("\t\t\targ1  : in std_logic_vector(" + (nbit - 1) + " downto 0);" + SHDLModule.newline);
		sb.append("\t\t\targ2  : in std_logic_vector(" + (nbit - 1) + " downto 0);" + SHDLModule.newline);
		sb.append("\t\t\tres  : out std_logic_vector(" + (2*nbit - 1) + " downto 0)" + SHDLModule.newline);
		sb.append("\t\t) ;" + SHDLModule.newline);
		sb.append("\tend component ;");
		return new String(sb);
	}

	public String getVHDLDefinition() {
		StringBuffer sb = new StringBuffer();
		sb.append("library ieee ;" + SHDLModule.newline);
		sb.append("use ieee.std_logic_1164.all ;" + SHDLModule.newline);
		sb.append("use ieee.std_logic_unsigned.all ;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("-- multiplier" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("entity " + getModuleOccurence().getName() + " is" + SHDLModule.newline);
		sb.append("\tport (" + SHDLModule.newline);
		sb.append("\t\targ1  : in std_logic_vector(" + (nbit - 1) + " downto 0);" + SHDLModule.newline);
		sb.append("\t\targ2  : in std_logic_vector(" + (nbit - 1) + " downto 0);" + SHDLModule.newline);
		sb.append("\t\tres  : out std_logic_vector(" + (2*nbit - 1) + " downto 0)" + SHDLModule.newline);
		sb.append("\t) ;" + SHDLModule.newline);
		sb.append("end " + getModuleOccurence().getName() + " ;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("architecture synthesis of " + getModuleOccurence().getName() + " is" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("begin" + SHDLModule.newline);
		sb.append("\tres <= arg1 * arg2 ;" + SHDLModule.newline);
		sb.append(SHDLModule.newline);
		sb.append("end synthesis ;" + SHDLModule.newline);
		return new String(sb);
	}
}

