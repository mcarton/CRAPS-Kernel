
package org.jcb.shdl;


public class CommEvent {
	
	private int byteNum;
	private int byteData;
	private int[] bitVector;
	
	public CommEvent(int byteNum, int byteData, int[] bitVector) {
		this.byteNum = byteNum;
		this.byteData = byteData;
		this.bitVector = bitVector;
	}
	
	public String toString() {
		return ("byteNum=" + byteNum + ", byteData=" + byteData + ", bitVector= " + bitVector);
	}
	
	public int getByteNum() {
		return byteNum;
	}
	public int getByteData() {
		return byteData;
	}
	public int[] getBitVector() {
		return bitVector;
	}
}