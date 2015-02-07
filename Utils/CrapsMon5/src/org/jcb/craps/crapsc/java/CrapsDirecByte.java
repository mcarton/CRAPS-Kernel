package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;


public class CrapsDirecByte extends CrapsInstrDirecSynth {

	private ArrayList list;

	public CrapsDirecByte(ArrayList list) {
		this.list = list;
	}

	public CrapsDirecByte() {
		this.list = new ArrayList();
	}

	public void add(Object elt) {
		list.add(elt);
	}

	public String toString() {
		return (".BYTE: list=" + list);
	}

	public String format() {
		StringBuffer sb = new StringBuffer();
		sb.append(".BYTE   ");
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) sb.append(", ");
			sb.append(list.get(i) + "");
		}
		return new String(sb);
	}

	private NumExpr cacheLength = null;
	private boolean allInstanciated = true;
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheLength != null) return cacheLength;
		int n = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof NumExpr) {
				allInstanciated = allInstanciated & ((NumExpr) list.get(i)).isInstanciated(localSymbols, globalSymbols, this);
				n += 1;
			} else if (list.get(i) instanceof String) {
				n += ((String) list.get(i)).length();
			}
		}
		cacheLength = new NumExprInt(n);
		return cacheLength;
	}

	// true when all content ints are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		getLength(localSymbols, globalSymbols);
		return allInstanciated;
	}

	// true when content is valid: values in [-128,+128] or in [0,255]
	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof NumExpr) {
				NumExpr ne = (NumExpr) list.get(i);
				long val = ne.getValue(localSymbols, globalSymbols, this);
				if ((val < -128) || (val > 255)) return false;
			}
		}
		return true;
	}


	// valid only when content is instanciated
	private int[] ints = null;
	public int getByte(int idx, ObjModule localSymbols, ObjModule globalSymbols) {
		if (ints != null) return ints[idx];
		int nb = (int) getLength(localSymbols, globalSymbols).getValue(localSymbols, globalSymbols, this);
		ints = new int[nb];
		int n = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof NumExpr) {
				NumExpr ne = (NumExpr) list.get(i);
				ints[n++] = (int) ne.getValue(localSymbols, globalSymbols, this);
			} else if (list.get(i) instanceof String) {
				String str = (String) list.get(i);
				for (int j = 0; j < str.length(); j++) {
					ints[n++] = (int) str.charAt(j);
				}
			}
		}
		return ints[idx];
	}

}

