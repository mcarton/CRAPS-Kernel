
package org.jcb.shdl;

import java.util.*;
import java.io.*;


public class LibManager {

	private ArrayList dirs;

	public LibManager() {
	}

	public void setPath(String shdlpath) {
		dirs = new ArrayList();
		StringTokenizer st = new StringTokenizer(shdlpath, File.pathSeparator);
		while (st.hasMoreTokens()) {
			String dir = st.nextToken();
			File file = new File(dir);
			if (file.exists())
				dirs.add(file);
			else
			       System.err.println("*** directory '" + dir + "' in shdlpath does not exist");
		}
		//System.out.println("dirs=" + dirs);
	}

	// <name> can be a file name or a module name
	// return the first File found, or null if not found
	public File lookFor(String name) {
		for (int i = 0; i < dirs.size(); i++) {
			File dir = (File) dirs.get(i);
			// look for the file name
			File file0 = new File(dir, name);
			if (file0.exists()) return file0;
			// look for a file starting with <name> and ending with ".net" or ".shd" etc.
			File fileN = new File(dir, name + ".net");
			if (fileN.exists()) return fileN;
			File file1 = new File(dir, name + ".shd");
			if (file1.exists()) return file1;
			File file2 = new File(dir, name + ".SHD");
			if (file2.exists()) return file2;
			File file3 = new File(dir, name + ".shdl");
			if (file3.exists()) return file3;
			File file4 = new File(dir, name + ".SHDL");
			if (file4.exists()) return file4;
		}
		return null;
	}
}

