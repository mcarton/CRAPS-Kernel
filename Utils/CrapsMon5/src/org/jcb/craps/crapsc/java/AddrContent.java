package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;


public class AddrContent {

	public String rs1; // ex: %r3
	public Object rs2_or_disp; // ex: NumExprInt(-2), "%r6"

	// ex: [%r3-2], [%r3+%r6]
	public AddrContent(String rs1, Object rs2_or_disp) {
		this.rs1 = rs1;
		this.rs2_or_disp = rs2_or_disp;
	}

	public AddrContent() {
	}

	public void setRs1(String rs1) {
		this.rs1 = rs1;
	}

	public void setRs2_or_disp(Object rs2_or_disp) {
		this.rs2_or_disp = rs2_or_disp;
	}

	public String toString() {
		return ("[" + rs1 + "+" + rs2_or_disp + "]");
	}

}

