package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;
import java.util.*;

	
public class ObjEntry {
	public ObjModule om;
	public String word;
	public SourceLine sl;
	public Boolean breakp;

	public ObjEntry(ObjModule om, String word, SourceLine sl) {
		this.om = om;
		this.word = word;
		this.sl = sl;
		breakp = new Boolean(false);
	}

	public String toString() {
		return ("word=" + word + ", sl=" + sl + ", breakp=" + breakp);
	}
}

