
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// intermediate point in a connection
// CnxPointModule is attached to a pin module
// CnxPointInter is an intermediate point

public abstract class CnxPoint {

	public abstract int getId();

	public abstract Point2D getLocation();
	public abstract void setLocation(Point2D loc);

	public abstract Equipotential getEquipotential();
	public abstract void setEquipotential(Equipotential equi);


	public boolean hit(double x, double y) {
		return (getLocation().distance(x, y) <= Module.cnxPtRectWidth / 2.0);
	}

	public abstract Color getColor();

	public void paint(Graphics2D g2, boolean highlighted, boolean isJunction, boolean selected) {
		// do nothing if it is not highlighted, nor is it a junction point
		if (!isJunction && !highlighted && !selected) return;

		// draw possible junction
		Point2D loc = getLocation();
		if (isJunction) {
			g2.setPaint(Module.cnxColor);
			double radius = 0.;
			if (getEquipotential().getWidth().eval(null) == 1) radius = Module.cnxJunctionRadius; else radius = Module.busJunctionRadius;
			g2.fill(new Ellipse2D.Double(loc.getX() - radius, loc.getY() - radius, 2 * radius, 2 * radius));
		}

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

