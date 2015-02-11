
package org.jcb.shdl;

import java.util.*;
import java.io.*;

import org.jcb.shdl.shdlc.java.*;

public abstract class SHDLBoard {

	public SHDLBoard() {
	}

	public static SHDLBoard getBoard(String boardName) {
		if (boardName.equalsIgnoreCase("DigilentS3")) {
			return new SHDLDigilentS3Board();
		} else if (boardName.startsWith("Nexys")) {
			return new SHDLDigilentNexysBoard();
		}
		return null;
	}

	public abstract String getBoardName();
	public abstract String getBoardModuleName();
	public abstract String[] getBoardPrefixes();
	public abstract String[] getBoardIOStatus();
	public abstract int[] getBoardN1();
	public abstract int[] getBoardN2();
	public abstract String[] getBoardDefaultValues();


	protected int prefixIndex(String name, String[] nameList) {
		String[] prefixes = getBoardPrefixes();
		for (int i = 0; i < prefixes.length; i++) {
			if (name.equalsIgnoreCase(prefixes[i])) return i;
		}
		return -1;
	}

	protected String strPrefixes() {
		String[] prefixes = getBoardPrefixes();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < prefixes.length; i++) {
			if (i > 0) sb.append(", ");
			sb.append(prefixes[i]);
		}
		return new String(sb);
	}
	
	// classe le (top) module: 0=board only IO, 1=hybrid IO, 2=distant only IO
	public int getModuleIOStatus(SHDLModule topModule) {
//		StringBuffer sb = new StringBuffer();
		boolean containsBoardSignals = false;
		boolean containsNoBoardSignals = false;
		for (int i = 0; i < topModule.getInterfaceSignals().size(); i++) {
			SHDLSignal signal = (SHDLSignal) topModule.getInterfaceSignals().get(i);
			String prefix = signal.getPrefix();
			if (contains(getBoardPrefixes(), prefix)) {
				containsBoardSignals = true;
//				if (sb.length() > 0) sb.append(", ");
//				sb.append(signal.getNormalizedName());
			} else containsNoBoardSignals = true;
		}
		if (containsBoardSignals) {
//			System.out.println("*** board signals: " + sb);
			if (containsNoBoardSignals) return 1; else return 0;
		} else {
			return 2;
		}
	}
	
	private static boolean contains(String[] tab, String elt) {
		for (int i = 0; i < tab.length; i++) {
			if (tab[i].equals(elt)) return true;
		}
		return false;
	}

}

