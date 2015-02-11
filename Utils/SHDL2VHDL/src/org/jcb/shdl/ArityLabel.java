
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


// label associated to a submodule, which gives its number of parallel instances

public class ArityLabel extends ModuleLabel {

	private CompoundModule compMod;
	private int subModId;
	private Module subMod;

	public ArityLabel(CompoundModule compMod, int subModId, Point2D relLoc) {
		super(relLoc);
		this.compMod = compMod;
		this.subModId = subModId;
		subMod = compMod.getSubModule(subModId);
	}

	public int getSubModuleId() {
		return subModId;
	}

	public String getText() {
		return ("X " + subMod.getArity());
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
		return compMod.getSubModuleLocation(subModId);
	}

	public RoundRectangle2D getRect(Point2D anchorLoc) {
                float width = (float) Module.nbInstanceFontMetrics.stringWidth(getText()) + 6.f;
                float height = (float) Module.nbInstanceFontMetrics.getHeight();
                return new RoundRectangle2D.Double(anchorLoc.getX() + relLoc.getX() - width / 2.0, anchorLoc.getY() + relLoc.getY() - height / 2.0, width, height, 4.0, 4.0);
        }

	public void paint(Graphics2D g2) {
		Point2D pinLoc = getAnchorLocation();
		RoundRectangle2D rect = getRect(pinLoc);
		// draw line
		g2.setStroke(Module.labelLineStroke);
		g2.setPaint(Module.labelLineColor);
		g2.draw(new Line2D.Double(pinLoc.getX(), pinLoc.getY(), rect.getX() + rect.getWidth() / 2., rect.getY() + rect.getHeight() / 2.0));
		// draw name into background color rectangle
		g2.setPaint(Module.backgroundColor);
		g2.fill(rect);
		g2.setPaint(Module.nbInstanceColor);
		g2.setFont(Module.nbInstanceFont);
		String str = getText();
		g2.drawString(str, (float) rect.getX() + 3.f, (float) (rect.getY() + Module.nbInstanceFontMetrics.getAscent()));
	}

}

