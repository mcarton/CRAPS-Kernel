package org.jcb.shdl.shdlc.java;

import java.util.*;
import java.util.regex.*;
import java.io.*;

public abstract class SHDLPredefinedOccurence {

	private SHDLModuleOccurence moduleOccurence;
	private Pattern namePattern;
	protected boolean checked = false;

	protected static final Pattern multiplierPattern = Pattern.compile("umult([0-9]+)x([0-9]+)");
	protected static final Pattern commUSBPattern = Pattern.compile("commusb");
	protected static final Pattern ramsReadFirstPattern = Pattern.compile("rams_read_first([0-9]+)([km]?)x([0-9]+)");
	protected static final Pattern ramsWriteFirstPattern = Pattern.compile("rams_write_first([0-9]+)([km]?)x([0-9]+)");
	protected static final Pattern ramsReadThroughPattern = Pattern.compile("rams_read_through([0-9]+)([km]?)x([0-9]+)");
	protected static final Pattern ramsAsynReadPattern = Pattern.compile("rams_asyn_read([0-9]+)([km]?)x([0-9]+)");
	protected static final Pattern ramsDualAsynReadPattern = Pattern.compile("rams_dual_asyn_read([0-9]+)([km]?)x([0-9]+)");
	protected static final Pattern ram = Pattern.compile("rams_dual_asyn_read([0-9]+)([km]?)x([0-9]+)");
	protected static final Pattern rs232Pattern = Pattern.compile("rs232refcomp");
	protected static final Pattern ramCtrlPattern = Pattern.compile("ramctrl");


	public SHDLPredefinedOccurence(SHDLModuleOccurence moduleOccurence, Pattern namePattern) {
		this.moduleOccurence = moduleOccurence;
		this.namePattern = namePattern;
	}

	public static SHDLPredefinedOccurence getPredefined(SHDLModuleOccurence moduleOccurence) {
		String name = moduleOccurence.getName().toLowerCase();
		//System.out.println("predef name=" + name);
		if (name.equals("xorcy")) {
			return new SHDLPredefinedXORCY(moduleOccurence, null);
		} else if (name.equals("muxcy")) {
			return new SHDLPredefinedMUXCY(moduleOccurence, null);
		} else if (multiplierPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedMultiplier(moduleOccurence, multiplierPattern);
		} else if (commUSBPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedCommUSB(moduleOccurence, commUSBPattern);
		} else if (ramsReadFirstPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedRamsReadFirst(moduleOccurence, ramsReadFirstPattern);
		} else if (ramsWriteFirstPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedRamsWriteFirst(moduleOccurence, ramsWriteFirstPattern);
		} else if (ramsReadThroughPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedRamsReadThrough(moduleOccurence, ramsReadThroughPattern);
		} else if (ramsAsynReadPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedRamsAsynRead(moduleOccurence, ramsAsynReadPattern);
		} else if (ramsDualAsynReadPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedRamsDualAsynRead(moduleOccurence, ramsDualAsynReadPattern);
		} else if (rs232Pattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedRs232RefComp(moduleOccurence, rs232Pattern);
		} else if (ramCtrlPattern.matcher(name.toLowerCase()).matches()) {
			return new SHDLPredefinedRamCtrl(moduleOccurence, ramCtrlPattern);
		}
		return null;
	}

	public SHDLModuleOccurence getModuleOccurence() {
		return moduleOccurence;
	}
	public Pattern getNamePattern() {
		return namePattern;
	}

	public abstract boolean isInput(int index);
	public abstract boolean isOutput(int index);
	public abstract boolean isInputOutput(int index);
	public abstract int getArity(int index);

	public boolean isInLibrary() {
		return false;
	}

	public abstract boolean check(boolean ok, SHDLModule module, PrintStream errorStream);
	public boolean isChecked() {
		return checked;
	}
	public abstract String getVHDLComponentDeclaration();
	public abstract String getVHDLDefinition();

	protected boolean checkArity(SHDLSignal sig, int arity) {
		int sigArity = sig.getArity();
		if (sigArity > 0)
			return (sigArity == arity);
		else
			return (-sigArity <= arity);
	}

}
