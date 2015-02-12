
package org.jcb.shdl;

import java.util.*;
import org.jcb.shdl.*;


public class Ev {
	public int equiIndex;

	public long org_time;		// starting time of event
	public Propagator source;	// source of event; null = external source (operator)
	public String val;		// value produced
	public ArrayList conflict;	// propagators in conflict when not null

	public Ev(int equiIndex, long org_time, Propagator source, String val) {
		this.equiIndex = equiIndex;
		this.org_time = org_time;
		this.source = source;
		this.val = val;
	}

	public String toString() {
		return (equiIndex + "(" + org_time + ")" + source + "/" + val);
	}

}

