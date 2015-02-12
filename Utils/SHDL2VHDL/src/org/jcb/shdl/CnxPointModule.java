
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// connection point attached to a pin module

public class CnxPointModule extends CnxPoint {

	private int id;
	private Module mainMod;
	private int subModId;
	private int pinIndex;

	private Equipotential equi = null;


	public CnxPointModule(int id, Module mainMod, int subModId, int pinIndex) {
		this.id = id;
		this.mainMod = mainMod;
		this.subModId = subModId;
		this.pinIndex = pinIndex;
	}

	public String toString() {
		String seq = "";
		if (getEquipotential() != null) seq = "" + getEquipotential().getId();
		return ("cp" + getId() + "(eq" + seq + ")");
	}

	public int getId() {
		return id;
	}

	public int getSubModId() {
		return subModId;
	}

	public int getPinIndex() {
		return pinIndex;
	}

	public Point2D getLocation() {
		Module mod = mainMod.getSubModule(subModId); 
		Point2D modLoc = mainMod.getSubModuleLocation(subModId);
		Point2D pinLoc = mod.getExtPinLocation(pinIndex);
		return new Point2D.Double(modLoc.getX() + pinLoc.getX(), modLoc.getY() + pinLoc.getY());
	}

	public void setLocation(Point2D loc) {
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

