
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// label associated to an external pin

public class ExtPinLabel extends ModuleLabel {

	private CnxPointPin cp;

	public ExtPinLabel(CnxPointPin cp, Point2D relLoc) {
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
		return cp.getExtLocation();
	}

	public RoundRectangle2D getRect(Point2D anchorLoc) {
		float width = (float) Module.nameFontMetrics.stringWidth(getText()) + 6.f;
		float height = (float) Module.nameFontMetrics.getHeight();
		return new RoundRectangle2D.Double(anchorLoc.getX() + relLoc.getX() - width/2.0, anchorLoc.getY() + relLoc.getY() - height / 2.0, width, height, 4.0, 4.0);
        }

	public void paint(Graphics2D g2) {
	}
	public void paint2(Graphics2D g2, boolean drawAnchorLine) {
		Point2D pinLoc = getAnchorLocation();
		RoundRectangle2D rect = getRect(pinLoc);
		// draw line
		 if (drawAnchorLine) {
			g2.setStroke(Module.labelLineStroke);
			g2.setPaint(Module.labelLineColor);
			g2.draw(new Line2D.Double(pinLoc.getX(), pinLoc.getY(), rect.getX() + rect.getWidth()/2., rect.getY() + rect.getHeight()/2.0));
		 }
		// paint name
		g2.setPaint(Module.labelColor);
		g2.setFont(Module.nameFont);
		String str = getText();
		g2.drawString(str, (float) rect.getX() + 3.f, (float) (rect.getY() + Module.nameFontMetrics.getAscent()));
	}

}

