
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// label associated to the module name in the interface view

public class ModuleNameLabel extends ModuleLabel {

	private String text;

	public ModuleNameLabel(String text, Point2D relLoc) {
		super(relLoc);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getTextColor() {
		return Module.moduleNameColor;
	}

	public Color getBackgroundColor() {
		return Module.labelBackgroundColor;
	}

	public Color getFrameColor() {
		return Module.labelFrameColor;
	}

	private final Point2D ZERO = new Point2D.Double(0., 0.);
	public Point2D getAnchorLocation() {
		return ZERO;
	}

	public RoundRectangle2D getRect(Point2D anchorLoc) {
		float width = (float) Module.nameFontMetrics.stringWidth(getText()) + 6.f;
		float height = (float) Module.nameFontMetrics.getHeight();
		return new RoundRectangle2D.Double(relLoc.getX() - width/2.0, relLoc.getY() - height / 2.0, width, height, 4.0, 4.0);
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
		g2.setPaint(getTextColor());
		g2.setFont(Module.nameFont);
		String str = getText();
		g2.drawString(str, (float) rect.getX() + 3.f, (float) (rect.getY() + Module.nameFontMetrics.getAscent()));
	}

}

