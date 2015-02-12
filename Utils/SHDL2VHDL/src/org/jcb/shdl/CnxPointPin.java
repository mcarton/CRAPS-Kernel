
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// connection point attached to an interface pin

public class CnxPointPin extends CnxPoint {

	private int id;
	private Point2D loc;
	// interface
	private Point2D extloc;
	private int orientation = 0; // 0=left, 1=right, 2=up, 3=down
	private boolean inverted = false;
	private boolean clocked = false;

	private Equipotential equi = null;

	public CnxPointPin(int id, Point2D loc, Point2D extloc) {
		this.id = id;
		this.loc = loc;
		this.extloc = extloc;
	}

	public String toString() {
		String seq = "";
		if (getEquipotential() != null) seq = "" + getEquipotential().getId();
		return ("pin#" + getId() + "(eq" + seq + ")");
	}

	public int getId() {
		return id;
	}

	public Point2D getLocation() {
		return loc;
	}

	public void setLocation(Point2D loc) {
		loc.setLocation(loc);
	}

	public Point2D getExtLocation() {
		return extloc;
	}

	public void setExtLocation(Point2D loc) {
		extloc.setLocation(loc);
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public boolean isClocked() {
		return clocked;
	}

	public void setClocked(boolean clocked) {
		this.clocked = clocked;
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

	// <isJunction> has no meaning here
	//
	public void paint(Graphics2D g2, boolean highlighted, boolean isJunction, boolean selected) {

		/*
		if ((equi != null) && (equi.getWidth().eval(null) != 1))
			g2.setStroke(Module.pinBusStroke);
		else
			g2.setStroke(Module.pinCnxStroke);
		*/
		g2.setStroke(Module.cnxStroke);
		g2.setPaint(Module.pinModuleColor);
		double radius = 0.;
		if ((getEquipotential() == null) || (getEquipotential().getWidth().eval(null) == 1)) radius = Module.cnxPinRadius; else radius = Module.busPinRadius;
		g2.fill(new Ellipse2D.Double(loc.getX() - radius, loc.getY() - radius, 2 * radius, 2 * radius));
		/*
		double w = 3.5;
		g2.fill(new Ellipse2D.Double(loc.getX() - radius - w, loc.getY() - radius, 2 * radius, 2 * radius));
		g2.fill(new Rectangle2D.Double(loc.getX() - w, loc.getY() - radius, 2 * w, 2 * radius));
		g2.fill(new Ellipse2D.Double(loc.getX() - radius + w, loc.getY() - radius, 2 * radius, 2 * radius));
		g2.setPaint(Color.white);
		g2.fill(new Ellipse2D.Double(loc.getX() - 1.5, loc.getY() - 1.5, 3., 3.));
		*/

		// select color of rectangle
		if (selected) g2.setPaint(Module.selectedColor);
		else if (highlighted) g2.setPaint(getColor());
		else g2.setPaint(Module.cnxColor);

		// draw rectangle
		if (selected || highlighted) {
			g2.setStroke(Module.interCnxPtRectStroke);
			g2.draw(new Rectangle2D.Double(loc.getX() - Module.cnxPtRectWidth / 2., loc.getY() - Module.cnxPtRectWidth / 2., Module.cnxPtRectWidth, Module.cnxPtRectWidth));
		}
	}

}

