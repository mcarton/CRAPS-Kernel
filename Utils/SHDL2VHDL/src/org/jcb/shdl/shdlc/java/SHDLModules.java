
package org.jcb.shdl.shdlc.java;

import java.util.*;

public class SHDLModules {

	private ArrayList modules;

	public SHDLModules() {
		//System.out.println("new modules");
		modules = new ArrayList();
	}
	public String toString() {
		return "" + modules;
	}

	public void addModule(SHDLModule module) {
		//System.out.println("add module=" + module);
		modules.add(module);
	}

	public ArrayList getModules() {
		return modules;
	}
}

