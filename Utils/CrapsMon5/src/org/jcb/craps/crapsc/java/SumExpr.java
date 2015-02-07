package org.jcb.craps.crapsc.java;

import org.jcb.tools.*;


public class SumExpr {

	public String rs1; // ex: %r3
	public Object rs2_or_const; // ex: NumExprInt(-2), "%r6"

	// ex: %r3-2, %r3+%r6
	public SumExpr(String rs1, Object rs2_or_const) {
		this.rs1 = rs1;
		this.rs2_or_const = rs2_or_const;
	}

	public String toString() {
		return (rs1 + "+" + rs2_or_const);
	}

}

