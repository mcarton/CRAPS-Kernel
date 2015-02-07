package org.jcb.craps.crapsc.java;

import java.util.*;
import java.text.*;
import org.jcb.tools.*;
import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;

	
public class SourceLine {
	public int lineno;
	public String text;
	public String label = "";
	public CrapsInstrDirecSynth instr_or_direc_or_synth;
	public String comment = "";

	private ArrayList memoryBlocks;


	public SourceLine() {
	}

	public SourceLine(String label, CrapsInstrDirecSynth instr_or_direc_or_synth, String comment) {
		this.label = label;
		this.instr_or_direc_or_synth = instr_or_direc_or_synth;
		this.comment = comment;
	}


	public void setLineno(int lineno) {
		this.lineno = lineno;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setInstr(CrapsInstrDirecSynth instr_or_direc_or_synth) {
		this.instr_or_direc_or_synth = instr_or_direc_or_synth;
	}
		
	public String toString() {
		return ("lineno=" + lineno + ", label=" + label + ", instr_or_direc_or_synth=" + instr_or_direc_or_synth + ", comment=" + comment);
	}

	public String format() {
		String slineno = Strings.formatd0(lineno + "", 5);
		String slabel = Strings.formats(label + ":", 10);
		String sinstr = "";
		return (slineno + "  " + slabel + "     " + sinstr + "   ;" + comment);
	}

}

