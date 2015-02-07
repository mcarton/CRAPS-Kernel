package org.jcb.craps.crapsc.java;

import java.util.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsDirecWord extends CrapsInstrDirecSynth {

	private ArrayList list;

	public CrapsDirecWord(ArrayList list) {
		this.list = list;
	}

	public String toString() {
		return (".WORD: list=" + list);
	}

	public String format() {
		StringBuffer sb = new StringBuffer();
		sb.append(".WORD   ");
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) sb.append(", ");
			sb.append(list.get(i) + "");
		}
		return new String(sb);
	}

	private NumExpr cacheLength = null;
	public NumExpr getLength(ObjModule localSymbols, ObjModule globalSymbols) {
		if (cacheLength != null) return cacheLength;
		int n = 0;
		for (int i = 0; i < list.size(); i++) {
			NumExpr ne = (NumExpr) list.get(i);
			n += 4;
		}
		cacheLength = new NumExprInt(n);
		return cacheLength;
	}

	// true when all content bytes are instanciated
	public boolean isInstanciated(ObjModule localSymbols, ObjModule globalSymbols) {
		for (int i = 0; i < list.size(); i++) {
			NumExpr ne = (NumExpr) list.get(i);
			if (! ne.isInstanciated(localSymbols, globalSymbols, this)) return false;
		}
		return true;
	}

	// true when content is valid: values in [-2^31,+2^31] or in [0,2^32-1]
	public boolean isContentValid(ObjModule localSymbols, ObjModule globalSymbols) {
		for (int i = 0; i < list.size(); i++) {
			NumExpr ne = (NumExpr) list.get(i);
			long val = ne.getValue(localSymbols, globalSymbols, this);
			if ((val < -2147483648L) || (val > 4294967296L)) return false;
		}
		return true;
	}

	// valid only when content is instanciated
	private int[] bytes = null;
	public int getByte(int i, ObjModule localSymbols, ObjModule globalSymbols) {
		if (bytes != null) return bytes[i];
		int nb = (int) getLength(localSymbols, globalSymbols).getValue(localSymbols, globalSymbols, this);
		bytes = new int[nb];
		nb = 0;
		for (int j = 0; j < list.size(); j++) {
			NumExpr ne = (NumExpr) list.get(j);
			long val = ne.getValue(localSymbols, globalSymbols, this);
			int msb = (int) (val / 65536);
			int lsb = (int) (val % 65536);
			bytes[nb++] = msb / 256;
			bytes[nb++] = msb % 256;
			bytes[nb++] = lsb / 256;
			bytes[nb++] = lsb % 256;
		}
		return bytes[i];
	}

}

