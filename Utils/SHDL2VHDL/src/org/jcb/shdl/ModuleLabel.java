
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


public abstract class ModuleLabel {

	protected Point2D relLoc; // relative position to its anchor


	public ModuleLabel(Point2D relLoc) {
		this.relLoc = relLoc;
	}

	public Point2D getRelativeLocation() {
		return relLoc;
	}

	public void setRelativeLocation(Point2D relLoc) {
		relLoc.setLocation(relLoc);
	}

	public void setRelativeLocation(double x, double y) {
		relLoc.setLocation(x, y);
	}

	public Point2D getLocation() {
		return new Point2D.Double(getAnchorLocation().getX() + relLoc.getX(),
			getAnchorLocation().getY() + relLoc.getY());
	}

	public void setLocation(double x, double y) {
		Point2D anchor = getAnchorLocation();
		double rx = x - anchor.getX();
		double ry = y - anchor.getY();
		relLoc.setLocation(rx, ry);
	}

	public abstract String getText();


	public abstract Color getTextColor();
	public abstract Color getBackgroundColor();
	public abstract Color getFrameColor();


	public abstract RoundRectangle2D getRect(Point2D anchorLoc);

	public abstract Point2D getAnchorLocation();

	public abstract void paint(Graphics2D g2);

}

