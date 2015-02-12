
package org.jcb.shdl;

public abstract class NumExpr {

	public abstract boolean isVal();

	public abstract int eval(Object context);

	public static NumExpr parse(String str) {
		// litteral value ?
		try {
			int val = Integer.parseInt(str.trim());
			return new NumExprVal(val);
		} catch(NumberFormatException ex) {
		}
		if ((str.indexOf('*') == -1) && (str.indexOf('+') == -1)) {
			// ni '*' ni '+' : c'est un nom de variable...
			return new NumExprVar(str.trim());
		}
		// expr * expr ?
		int index = str.indexOf('*');
		if (index != -1) {
			try {
				NumExpr e1 = NumExpr.parse(str.substring(0, index).trim());
				NumExpr e2 = NumExpr.parse(str.substring(index + 1).trim());
				if (e1.isVal() && (e1.eval(null) == 1))
					return e2;
				else if (e2.isVal() && (e2.eval(null) == 1))
					return e1;
				else if (e1.isVal() && e2.isVal())
					return new NumExprVal(e1.eval(null) * e2.eval(null));
				else
					return new NumExprMul(e1, e2);
			} catch(NumberFormatException ex) {
			}
		}
		// expr + expr ?
		index = str.indexOf('+');
		if (index != -1) {
			try {
				NumExpr e1 = NumExpr.parse(str.substring(0, index).trim());
				NumExpr e2 = NumExpr.parse(str.substring(index + 1).trim());
				return new NumExprAdd(e1, e2);
			} catch(NumberFormatException ex) {
			}
		}
		return null;	
	}

}
