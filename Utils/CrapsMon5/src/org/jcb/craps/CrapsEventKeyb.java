
package org.jcb.craps;

public class CrapsEventKeyb extends CrapsEvent {

	private int code;

	public CrapsEventKeyb(int cycle, int code) {
		this.code = code;
		setCycle(cycle);
	}

	public int getCode() {
		return code;
	}

}

