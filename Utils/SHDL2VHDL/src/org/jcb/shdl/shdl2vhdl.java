
package org.jcb.shdl;

import org.jcb.shdl.shdlc.java.*;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libegg.base.*;
import java.util.*;
import java.io.*;

public class shdl2vhdl {

	public static void main(String[] args){

		if (args.length == 0) printUsageAndExit();

		// parse options
		int nextIndex = 0;
		boolean verbose = false;
		boolean check = false;
		String shdlpath = ".";
		String destdir = ".";
		while (args[nextIndex].startsWith("-")) {
			if (args[nextIndex].equals("-h")) {
				printUsageAndExit();
			} else if (args[nextIndex].equals("-v")) {
				verbose = true;
				nextIndex += 1;
			} else if (args[nextIndex].equals("-check")) {
				check = true;
				nextIndex += 1;
			} else if (args[nextIndex].startsWith("-sp=")) {
				shdlpath = args[nextIndex].substring(4);
				nextIndex += 1;
			} else if (args[nextIndex].startsWith("-d=")) {
				destdir = args[nextIndex].substring(3);
				File ddir = new File(destdir);
				if (!ddir.exists()) {
					System.out.println("** destination dir '" + destdir + "' does not exist");
					printUsageAndExit();
				}
				nextIndex += 1;
			} else {
				System.out.println("** option " + args[nextIndex] + ", not recognized");
				System.out.println("** try java org.jcb.shdl.shdl2vhdl -h for help");
				System.exit(1);
			}
		}
		// are there arguments left ?
		if ((args.length - nextIndex) == 0) printUsageAndExit();
		// create libManager from shdlpath
		LibManager libManager = new LibManager();
		libManager.setPath(shdlpath);

		// collect shdl source file names
		ArrayList sourceFiles = new ArrayList();
		for (int i = nextIndex; i < args.length; i++) {
			File file = libManager.lookFor(args[i]);
			if (file == null) {
				System.out.println("** file '" + args[i] + "' not found in shdlpath");
				System.exit(1);
			}
			sourceFiles.add(file);
			//System.out.println("file=" + file);
		}

		// Build a design with the modules defined in these source files
		ShdlDesign design = new ShdlDesign(libManager, verbose, System.out);
		// parse them, and collect all modules referenced from main statements
		boolean collectOk = design.collect(sourceFiles);
		//System.out.println("collectOk=" + collectOk);
		if (!collectOk) return;
		// check for loops
		boolean loopsOk = design.checkModuleDependences();
		if (!loopsOk) return;
		//System.out.println("loopsOk=" + loopsOk);
		// check all design
		boolean checkOK = design.check();
		//System.out.println("checkOk=" + checkOk);
		// generate VHDL text
		SHDLBoard board = SHDLBoard.getBoard("Nexys");
		if (checkOK && !check) design.generateVHDL(board, new File(destdir));
	}

	static void printUsageAndExit() {
		System.out.println("** usage : java org.jcb.shdl.shdl2vhdl [options] file1[.shd] file2[.shd] ...");
		System.out.println("** options :");
		System.out.println("      -sp=path         set shdl path");
		System.out.println("      -d=dir           set destination directory");
		System.out.println("      -v               verbose");
		System.out.println("      -check           just check, does not write files");
		System.out.println("      -h               print this help");
		System.exit(1);
	}
}

