
package org.jcb.craps;

public abstract class CrapsEvent {

	private int cycle;

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public int getCycle() {
		return cycle;
	}

}

