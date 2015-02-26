
package org.jcb.shdl.shdlc.java;

import org.jcb.tools.*;
import java.util.*;
import java.math.*;

public class SHDLSignal {

	private static int nb_ = 0;

	private SHDLModule module;
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
	//
	// true when the signal is read as part of an equation, or is the output of a combin. or seq. setting
	// when the signal is one of a module occurrence arguments, the same meaning applies (a recursive search
	// through the module's calling hierarchy is done to assess these values)
	private boolean read = false;
	private ArrayList sources = new ArrayList();

	private boolean isAdded; // true for a 'whole' bus signal which has been added

	// constructor for ordinary signal (called from parser)
	public SHDLSignal(String first, boolean isInterface, SHDLModule module) {
		this.module = module;
		this.first = first;
		this.isInterface = isInterface;
		this.isConstant = false;
	}
	// constructor for litteral constants
	public SHDLSignal(String value, SHDLModule module) {
		this.module = module;
		this.value = value;
		this.isConstant = true;
	}
	// general constructor for bus signal
	public SHDLSignal(String prefix, int n1, int n2, SHDLModule module) {
		this.module = module;
		this.first = prefix;
		setN1(n1 + "");
		if (n2 != n1) setN2(n2 + "");
		checkSignal();
	}
	public String toString() {
		//return getName();
		StringBuffer sb = new StringBuffer();
		sb.append(module + ":" + getName());
		if (getRead()) sb.append(",r");
		if (getSources().size() > 0) sb.append(",w");
		//if (getWhole() != null) sb.append(",p");
		return new String(sb);
	}
	public boolean equals(Object obj) {
		SHDLSignal sig = (SHDLSignal) obj;
		return sig.getNormalizedName().equals(this.getNormalizedName());
	}
	
	// true when <this> is a bus including the scalar or sub-bus <sig>
	public boolean includes(SHDLSignal sig) {
		if (!getPrefix().equals(sig.getPrefix())) return false;
		if (getArity() == 1) {
			//return this.equals(sig);
			//return true;
			if (sig.isPartOfBus()) {
				return (this.getN1() == sig.getN1());
			} else {
				return true;
			}
		} else {
			if (sig.getArity() == 1)
				return ((sig.getLowestIndex() >= getLowestIndex()) && (sig.getLowestIndex() <= getHighestIndex()));
			else
				return ((sig.getLowestIndex() >= getLowestIndex()) && (sig.getHighestIndex() <= getHighestIndex()));
		}
	}
	
	// true when <this> is included in one of the signals of <signalList>
	public boolean containedIn(ArrayList signalList) {
		//System.out.println("contained this=" + this + ", list=" + signalList);
		for (int i = 0; i < signalList.size(); i++) {
			SHDLSignal signal = (SHDLSignal) signalList.get(i);
			if (signal.includes(this)) return true;
			//if (signal.getPrefix().equals(getPrefix())) return true;
		}
		return false;
	}
	
	
	public SHDLModule getModule() {
		return module;
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
	public void setIsAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}
	public boolean isAdded() {
		return isAdded;
	}
	public boolean isPartOfBus() {
		if (!checked) checkSignal();
		return isPartOfBus;
	}
	// its name as it appeared in source file
	public String getName() {
		return getNormalizedName();
		/*
		if (isConstant)
			return value;
		else {
			StringBuffer sb = new StringBuffer();
			sb.append(first);
			if (n1 != null && n2 != null) sb.append("[" + n1 + ".." + n2 + "]");
			if (n1 != null && n2 == null) sb.append("[" + n1 + "]");
			return new String(sb);
		}
		*/
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

	// get/set 'local' read and source attributes, which do not take into account
	// bus and module occurence issues. Use module.getSIgnalSource(signal) and
	// module.getRead(signal) for methods which take everything into account
	public boolean getRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public ArrayList getSources() {
		return sources;
	}
	public void addSource(Object source) {
		if (!sources.contains(source)) sources.add(source);
	}

	public boolean isScalar() {
		if (!isPartOfBus()) return true;
		if (getLowestIndex() == getHighestIndex()) return true;
		return false;
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

