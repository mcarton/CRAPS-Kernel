
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// width label associated to an intermediate point of a connection

public class WidthLabel extends ModuleLabel {

	private CnxPoint cp;

	public WidthLabel(CnxPoint cp, Point2D relLoc) {
		super(relLoc);
		this.cp = cp;
	}

	public CnxPoint getCnxPoint() {
		return cp;
	}

	public String getText() {
		Equipotential equi = cp.getEquipotential();
		return ("" + equi.getWidth());
	}

	public Color getTextColor() {
		return Module.labelColor;
	}

	public Color getBackgroundColor() {
		return Module.labelBackgroundColor;
	}

	public Color getFrameColor() {
		return Module.labelFrameColor;
	}

	public Point2D getAnchorLocation() {
		return cp.getLocation();
	}

	public RoundRectangle2D getRect(Point2D anchorLoc) {
		float width = (float) Module.widthFontMetrics.stringWidth(getText()) + 6.f;
		float height = (float) Module.widthFontMetrics.getHeight();
		return new RoundRectangle2D.Double(anchorLoc.getX() + 5.0, anchorLoc.getY() + 5.0 - height / 2.0, width, height, 4.0, 4.0);
	}

	public void paint(Graphics2D g2) {
		g2.setPaint(Module.widthColor);
		g2.setFont(Module.widthFont);
		g2.setStroke(Module.widthStroke);
		String str = getText();
		Point2D loc = getAnchorLocation();
		RoundRectangle2D rect = getRect(loc);
		g2.draw(new Line2D.Double(loc.getX() + 4.0, loc.getY() - 4.0, loc.getX() - 4.0, loc.getY() + 4.0));
		g2.drawString(str, (float) rect.getX(), (float) (rect.getY() + Module.nameFontMetrics.getAscent()));

	}

}

