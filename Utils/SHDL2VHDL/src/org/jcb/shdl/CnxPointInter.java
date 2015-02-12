
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// intermediate connection point

public class CnxPointInter extends CnxPoint {

	private int id;
	private Point2D loc;

	private Equipotential equi;
	private String name;


	public CnxPointInter(int id, Point2D loc) {
		this.id = id;
		this.loc = loc;
		setLocation(loc);
	}

	public String toString() {
		String seq = "";
		if (getEquipotential() != null) seq = "" + getEquipotential().getId();
		return "cp" + getId() + "(eq" + seq + ")";
	}

	public int getId() {
		return id;
	}

	public Point2D getLocation() {
		return loc;
	}

	public void setLocation(Point2D newloc) {
		double x = 0.;
		double y = 0.;
		if (Module.GRID) {
			x = ((int) Math.round(newloc.getX() / Module.GRIDSTEP)) * Module.GRIDSTEP;
			y = ((int) Math.round(newloc.getY() / Module.GRIDSTEP)) * Module.GRIDSTEP;
		} else {
			x = newloc.getX();
			y = newloc.getY();
		}
		loc.setLocation(x, y);
	}

	public Equipotential getEquipotential() {
		return equi;
	}

	public void setEquipotential(Equipotential equi) {
		this.equi = equi;
	}

	public Color getColor() {
		return Module.interCnxPtRectColor;
	}

}

