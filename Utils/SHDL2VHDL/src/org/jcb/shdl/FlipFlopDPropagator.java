
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


public class FlipFlopDPropagator extends Propagator {

	private Matrix matrix;
	private String name;
	private int tp;

	private int[] equiIndexes = new int[4];

	private final Point modLoc = new Point(0, 0);


	public FlipFlopDPropagator(Matrix matrix, String name, int tp) {
		this.matrix = matrix;
		this.name = name;
		this.tp = tp;
		for (int i = 0; i < 4; i++) equiIndexes[i] = -1;
	}

	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public int nbPins() {
		return 4;
	}

	public void setEquiIndex(int propIndex, int equiIndex) {
		equiIndexes[propIndex] = equiIndex;
	}

	public int getEquiIndex(int propIndex) {
		return equiIndexes[propIndex];
	}

	// RST,H,D,Q
	public void propagateChanges(long time, Ev[] changes) {
		Ev changeRST = changes[0];
		Ev changeH = changes[1];
		String prevValRST = matrix.getValueBefore(time, getEquiIndex(0));
		String prevValH = matrix.getValueBefore(time, getEquiIndex(1));
		String valRST = matrix.getValue(time, getEquiIndex(0));
		String valH = matrix.getValue(time, getEquiIndex(1));
		String valD = matrix.getValue(time, getEquiIndex(2));

		// create consequence events (to be verified)

		// Reset
		if ((changeRST != null)  && (valRST.equals("1"))) {
			int equiIndex = getEquiIndex(3);
			Ev ev = new Ev(equiIndex, time + tp, this, matrix.getZeroValue(equiIndex));
			MatrixRow row = matrix.createOrGetRow(ev.org_time);
			row.setEv(equiIndex, ev); // event on Q (pin#3)
		}

		// Rising edge on H
		if ((changeH != null)  && (prevValH.equals("0")) && (valH.equals("1"))) {
			int equiIndex = getEquiIndex(3);
			Ev ev = new Ev(equiIndex, time + tp, this, valD);
			MatrixRow row = matrix.createOrGetRow(ev.org_time);
			row.setEv(equiIndex, ev); // event on Q (pin#3)
		}
	}

}

