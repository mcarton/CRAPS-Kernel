
package org.jcb.shdl;

import java.util.*;


public class MatrixRow {

	private Matrix matrix;
	public long time;
	public boolean toPropagate = false;
	public Ev[] vals;

	public MatrixRow(Matrix matrix) {
		this.matrix = matrix;
	}

	public String toString() {
		StringBuffer st = new StringBuffer();
		st.append("time=" + time + ", vals=");
		for (int i = 0; i < vals.length; i++) {
			if (i > 0) st.append(",");
			st.append(vals[i] + "");
		}
		return new String(st);
	}

	public void setEv(int equiIndex, Ev ev) {
		toPropagate = true;
		Ev prevEv = matrix.getEventBefore(ev.org_time, equiIndex);
System.out.println("prevEv=" + prevEv + ", ev=" + ev);
		//if (prevEv == null)
			//vals[equiIndex] = ev;
		//else {
			if ((/*!ev.val.equals(matrix.getUndef(equiIndex)) &&*/ (ev != null) && !ev.val.equals(matrix.getHighZValue(equiIndex))) &&
				(/*!prevEv.val.equals(matrix.getUndef(equiIndex)) &&*/ (prevEv != null) && !prevEv.val.equals(matrix.getHighZValue(equiIndex))) &&
					(prevEv.source != ev.source)) {
				// conflict
				vals[equiIndex].conflict = new ArrayList();
				vals[equiIndex].conflict.add(prevEv);
				vals[equiIndex].conflict.add(ev);
			} else
				vals[equiIndex] = ev;
		//}
	}

	public boolean areAllEventsExternal() {
		for (int i = 0; i < vals.length; i++) {
			Ev ev = vals[i];
			if (ev == null) continue;
			if (ev.org_time != time) continue;
			if (ev.source != null) continue;
			return false;
		}
		return true;
	}
}

