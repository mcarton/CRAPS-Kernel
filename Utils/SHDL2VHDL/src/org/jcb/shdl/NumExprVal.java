
package org.jcb.shdl;

public class NumExprVal extends NumExpr {

	protected int val;

	public NumExprVal(int val) {
		this.val = val;
	}

	public int eval(Object context) {
		return val;
	}

	public boolean isVal() {
		return true;
	}

	public String toString() {
		return ("" + val);
	}

}
