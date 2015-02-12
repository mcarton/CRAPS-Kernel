
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import org.jcb.shdl.*;


public class Buff3StatePropagator extends Propagator {

	private Matrix matrix;
	private String name;
	private boolean inverted;
	private int tp;

	private int[] equiIndexes = new int[3];

	private final Point locE = new Point(0, 1);
	private final Point locS = new Point(4, 1);
	private final Point locOE = new Point(2, 2);
	private final Rectangle selectArea = new Rectangle(1, 0, 2, 2);


	public Buff3StatePropagator(Matrix matrix, String name, boolean inverted, int tp) {
		this.matrix = matrix;
		this.name = name;
		this.inverted = inverted;
		this.tp = tp;
		for (int i = 0; i < 3; i++) equiIndexes[i] = -1;
	}

	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public int nbPins() {
		return 3;
	}

	public void setEquiIndex(int propIndex, int equiIndex) {
		equiIndexes[propIndex] = equiIndex;
	}

	public int getEquiIndex(int propIndex) {
		return equiIndexes[propIndex];
	}

	// E,S,OE
	public void propagateChanges(long time, Ev[] changes) {
		Ev changeE = changes[0];
		Ev changeS = changes[1];
		Ev changeOE = changes[2];
		String prevValE = matrix.getValueBefore(time, getEquiIndex(0));
		String prevValS = matrix.getValueBefore(time, getEquiIndex(1));
		String prevValOE = matrix.getValueBefore(time, getEquiIndex(2));
		String valE = matrix.getValue(time, getEquiIndex(0));
		String valS = matrix.getValue(time, getEquiIndex(1));
		String valOE = matrix.getValue(time, getEquiIndex(2));

		// create consequence events (to be verified)

		// OE going from * to active
		if ((changeOE != null) &&
				((!inverted && (valOE.equals("1"))) ||
				(inverted && (valOE.equals("0"))))) {
			int equiIndex = getEquiIndex(1);
			Ev ev = new Ev(equiIndex, time + tp, this, valE);
			MatrixRow row = matrix.createOrGetRow(ev.org_time);
			row.setEv(getEquiIndex(1), ev); // event on S (pin#1)
		}

		// OE going from * to inactive
		if ((changeOE != null) &&
				((!inverted && (valOE.equals("0"))) ||
				(inverted && (valOE.equals("1"))))) {
			int equiIndex = getEquiIndex(1);
			// High-Z
			Ev ev = new Ev(equiIndex, time + tp, this, matrix.getHighZValue(equiIndex));
			MatrixRow row = matrix.createOrGetRow(ev.org_time);
			row.setEv(equiIndex, ev); // event on S (pin#1)
		}

		// E changes while OE is active
		if ((changeE != null) &&
				((!inverted && (valOE.equals("1"))) ||
				(inverted && (valOE.equals("0"))))) {
			int equiIndex = getEquiIndex(1);
			Ev ev = new Ev(equiIndex, time + tp, this, valE);
			MatrixRow row = matrix.createOrGetRow(ev.org_time);
			row.setEv(equiIndex, ev); // event on S (pin#1)
		}
	}
}

