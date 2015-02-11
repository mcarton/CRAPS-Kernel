
package org.jcb.shdl;

public class NumExprAdd extends NumExpr {

	private NumExpr m1;
	private NumExpr m2;

	public NumExprAdd(NumExpr m1, NumExpr m2) {
		this.m1 = m1;
		this.m2 = m2;
	}

	public int eval(Object context) {
		int val1 = m1.eval(context);
		if (val1 == -1) return -1;
		int val2 = m2.eval(context);
		if (val2 == -1) return -1;
		return (val1 + val2);
	}

	public boolean isVal() {
		return false;
	}

	public String toString() {
		return (m1.toString() + " + " + m2.toString());
	}


}
