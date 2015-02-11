
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// equipotential label associated to an intermediate point of a connection

public class EquiLabel extends ModuleLabel {

	private CnxPoint cp;

	public EquiLabel(CnxPoint cp, Point2D relLoc) {
		super(relLoc);
		this.cp = cp;
	}

	public CnxPoint getCnxPoint() {
		return cp;
	}

	public String getText() {
		Equipotential equi = cp.getEquipotential();
		if (equi == null) return "---";
		return equi.getName();
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
		float width = (float) Module.nameFontMetrics.stringWidth(getText()) + 6.f;
		float height = (float) Module.nameFontMetrics.getHeight();
		return new RoundRectangle2D.Double(anchorLoc.getX() + relLoc.getX() - width/2.0, anchorLoc.getY() + relLoc.getY() - height / 2.0, width, height, 4.0, 4.0);
        }

	public void paint(Graphics2D g2) {
		Point2D pinLoc = getAnchorLocation();
		RoundRectangle2D rect = getRect(pinLoc);
		// draw line
		g2.setStroke(Module.labelLineStroke);
		g2.setPaint(Module.labelLineColor);
		g2.draw(new Line2D.Double(pinLoc.getX(), pinLoc.getY(), rect.getX() + rect.getWidth()/2., rect.getY() + rect.getHeight()/2.0));
		// draw label frame
		if (Module.frameLabels) {
			g2.setStroke(Module.labelFrameStroke);
			g2.setPaint(Module.labelBackgroundColor);
			g2.fill(rect);
			g2.setPaint(Module.labelFrameColor);
			g2.draw(rect);
		}
		// draw name into label frame
		g2.setPaint(Module.labelColor);
		g2.setFont(Module.nameFont);
		String str = getText();
		g2.drawString(str, (float) rect.getX() + 3.f, (float) (rect.getY() + Module.nameFontMetrics.getAscent()));
	}

}

