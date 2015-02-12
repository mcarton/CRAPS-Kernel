
package org.jcb.shdl.netc.java;

import java.util.*;
import java.math.*;

public class NETSignal {

	private String first; // x -> x, a[7..0] -> a
	private boolean isInterface; // is part of the module interface
	private String n1; // a[7..0] -> n1 = 7, a[7] -> 7
	private String n2; // a[7..0] -> n2 = 0, a[7] -> null
	private boolean isConstant; // true for litteral constants
	private String value; // constant value (e.g. 0b1101, 0xFE5A, 1234)

	private boolean checked = false;
	private boolean isPartOfBus = false;
	private int arity = -1;
	private int n1_ = -1;
	private int n2_ = -1;
	private String prefix;
	private String numEnding;
	private BigInteger constantValue;
	private char constantBase; // 'D'=decimal, 'H'=hexa, 'B'=binary

	// constructor for ordinary signal (called from parser)
	public NETSignal(String first, boolean isInterface) {
		this.first = first;
		this.isInterface = isInterface;
		this.isConstant = false;
	}
	// constructor for litteral constants
	public NETSignal(String value) {
		this.value = value;
		this.isConstant = true;
	}
	// general constructor for bus signal
	public NETSignal(String prefix, int n1, int n2) {
		this.first = prefix;
		setN1(n1 + "");
		if (n2 != n1) setN2(n2 + "");
		checkSignal();
	}
	public String toString() {
		return getNormalizedName();
	}
	public boolean equals(Object obj) {
		NETSignal sig = (NETSignal) obj;
		return sig.getNormalizedName().equals(this.getNormalizedName());
	}

	public boolean isConstant() {
		return isConstant;
	}
	public boolean isInterface() {
		return isInterface;
	}
	public void setIsInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	public boolean isPartOfBus() {
		if (!checked) checkSignal();
		return isPartOfBus;
	}
	// normalized form (e.g. Sig -> sig, A[3] -> a[3] ; a[0..7] -> a[0..7] ; A[0..7] -> a[0..7] )
	public String getNormalizedName() {
		if (!checked) checkSignal();
		if (isConstant)
			return value;
		else if (!isPartOfBus)
			return getPrefix().toLowerCase();
		else if (n1_ == n2_)
			return getPrefix().toLowerCase() + "[" + n1_ + "]";
		else
			return getPrefix().toLowerCase() + "[" + n1_ + ".." + n2_ + "]";
	}

	// called from the parser
	public void setN1(String n1) {
		this.n1 = n1;
	}
	// called from the parser
	public void setN2(String n2) {
		this.n2 = n2;
	}
	public String getNumEnding() {
		return numEnding;
	}

	// use only after having called checkSignal
	public String getPrefix() {
		if (!checked) checkSignal();
		return prefix.toLowerCase();
	}

	public BigInteger getConstantValue() {
		return constantValue;
	}
	public char getConstantBase() {
		return constantBase;
	}

	// use only after having called checkSignal
	// a negative arity is for constants, and is a lower bound
	// for example, arity of 123 is -7, since it needs at least 7 bits to encode it
	public int getArity() {
		if (!checked) checkSignal();
		return arity;
	}

	// returns normalized value after having called checkSignal
	// e.g. 'a[7..0]' -> 7
	public int getN1() {
		if (!checked) checkSignal();
		return n1_;
	}
	// returns normalized value after having called checkSignal
	// e.g. 'a[7..0]' -> 0
	public int getN2() {
		if (!checked) checkSignal();
		return n2_;
	}

	public int getLowestIndex() {
		if (!checked) checkSignal();
		//if (n2 == null) return n1_;
		return Math.min(n1_, n2_);
	}
	public int getHighestIndex() {
		if (!checked) checkSignal();
		return Math.max(n1_, n2_);
	}
	public boolean containsIndex(int i) {
		int low = getLowestIndex();
		int high = getHighestIndex();
		return ((i >= low) && (i <= high));
	}

	// check signal syntax and set its arity, prefix and bounds
	public boolean checkSignal() {
		checked = true;
		if (isConstant) {
			// compute constant value
			if (value.toLowerCase().startsWith("0b")) {
				try {
					constantBase = 'B';
					//constantValue = BinNum.parseUnsigned(value.substring(2));
					constantValue = new BigInteger(value.substring(2), 2);
				} catch(Exception ex) {}
			} else if (value.toLowerCase().startsWith("0x")) {
				try {
					constantBase = 'H';
					//constantValue = HexNum.parseUnsigned(value.substring(2));
					constantValue = new BigInteger(value.substring(2), 16);
				} catch(Exception ex) {}
			} else {
				try {
					constantBase = 'D';
					//constantValue = Integer.parseInt(value);
					constantValue = new BigInteger(value);
				} catch(Exception ex) {}
			}
			// compute arity (< 0, lower bound)
			//arity = - BinNum.nbBinDigits(Math.abs(constantValue));
			String binStr = constantValue.toString(2);
			arity = - binStr.length();
			return !isInterface; // no constants in interface signals
		} else {
			if (n1 == null) {
				// one-word signal
				arity = 1;
				prefix = first;

			} else if (n2 != null) {
				// e.g. 'a[7..0]'
				isPartOfBus = true;
				n1_ = Math.abs(Integer.parseInt(n1));
				n2_ = Math.abs(Integer.parseInt(n2));
				arity = Math.abs(n1_ - n2_) + 1;
				prefix = first;
			} else {
				// e.g. 'a[3]'
				isPartOfBus = true;
				n1_ = Math.abs(Integer.parseInt(n1));
				n2_ = n1_;
				arity = 1;
				prefix = first;
			}
			return true;
		}
	}
}

