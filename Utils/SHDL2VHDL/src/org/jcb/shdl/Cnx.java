
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


public class Cnx {

	private CnxPoint cp1;
	private CnxPoint cp2;


	public Cnx(CnxPoint cp1, CnxPoint cp2) {
		this.cp1 = cp1;
		this.cp2 = cp2;
	}

	public String toString() {
		String seq = "";
		if (getEquipotential() != null) seq = "" + getEquipotential().getId();
		return ("cp" + cp1.getId() + "-cp" + cp2.getId() + "(eq" + seq + ")");
	}

	public CnxPoint getCp1() {
		return cp1;
	}

	public void setCp1(CnxPoint cp1) {
		this.cp1 = cp1;
	}

	public CnxPoint getCp2() {
		return cp2;
	}

	public void setCp2(CnxPoint cp2) {
		this.cp2 = cp2;
	}

	public Equipotential getEquipotential() {
		return cp1.getEquipotential();
	}


	// hit when closer than width/2
	public boolean hit(double x, double y) {
		double width = getEquipotential().getStrokeWidth();
		Line2D seg = new Line2D.Double(cp1.getLocation(), cp2.getLocation());
		double dist = seg.ptSegDist(x, y);
		return (dist <= width * 0.9);
	}


	public void paint(Graphics2D g2) {
		Line2D seg = new Line2D.Double(cp1.getLocation(), cp2.getLocation());
if (getEquipotential() == null) System.out.println("null cnx=" + this);
		g2.setStroke(getEquipotential().getStroke());
		g2.setColor(Module.cnxColor);
		g2.draw(seg);
	}
}

