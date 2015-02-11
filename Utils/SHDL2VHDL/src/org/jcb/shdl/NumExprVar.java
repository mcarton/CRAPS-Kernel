
package org.jcb.shdl;

public class NumExprVar extends NumExpr {

	protected String varName;

	public NumExprVar(String varName) {
		this.varName = varName;
	}

	public int eval(Object context) {
		return -1;
	}

	public boolean isVal() {
		return false;
	}

	public String toString() {
		return ("" + varName);
	}

}
