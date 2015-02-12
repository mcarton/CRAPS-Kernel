
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// equaation label associated to a universal gate

public class UnivGateLabel extends ModuleLabel {

	private CompoundModule compModule;
	private UniversalGate ug;

	private String text = "S=/A+B";

	public UnivGateLabel(CompoundModule compModule, UniversalGate ug, Point2D relLoc) {
		super(relLoc);
		this.compModule = compModule;
		this.ug = ug;
	}

	public UniversalGate getUnivGate() {
		return ug;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
		return compModule.getSubModuleLocation(ug.getId());
	}

	public RoundRectangle2D getRect(Point2D anchorLoc) {
		float width = (float) Module.widthFontMetrics.stringWidth(getText()) + 6.f;
		float height = (float) Module.widthFontMetrics.getHeight();
		return new RoundRectangle2D.Double(anchorLoc.getX() + relLoc.getX(), anchorLoc.getY() + relLoc.getY() - height / 2.0, width, height, 4.0, 4.0);
	}

	public void paint(Graphics2D g2) {
		String str = getText();
		Point2D loc = getAnchorLocation();
		// draw label line
		RoundRectangle2D rect = getRect(loc);
		g2.setStroke(Module.labelLineStroke);
		g2.setPaint(Module.labelLineColor);
		g2.draw(new Line2D.Double(loc.getX(), loc.getY(), rect.getX() + rect.getWidth() / 2., rect.getY() + rect.getHeight() / 2.0));
		// draw name into background color rectangle
		g2.setPaint(Module.backgroundColor);
		g2.fill(rect);
		g2.setPaint(Module.universalGateTextColor);
		g2.setFont(Module.universalGateTextFont);
		g2.drawString(str, (float) rect.getX(), (float) (rect.getY() + Module.universalGateTextFontMetrics.getAscent()));

	}

}

