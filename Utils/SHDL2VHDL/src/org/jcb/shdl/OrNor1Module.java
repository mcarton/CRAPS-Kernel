
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class OrNor1Module extends Module {

	private boolean inverting;

	private ArrayList pinIdList;
	private int shapeIndex = 0;
	private Point2D pinE;
	private Point2D pinS;
	private GeneralPath bodyPath;
	private AffineTransform transform;

	private final Point2D pinE_0 = new Point2D.Double(-25., 0.);
	private final Point2D pinS_0 = new Point2D.Double(35., 0.);
	private final GeneralPath bodyPath_0;
	private final AffineTransform ID = new AffineTransform();
	private final AffineTransform rotate90 = new AffineTransform();
	private final AffineTransform rotate_90 = new AffineTransform();
	private final AffineTransform rotate180 = new AffineTransform();


	public OrNor1Module(int id, NumExpr nb, boolean inverting) {
		super(id, nb);
		pinIdList = new ArrayList();
		pinIdList.add(new Integer(0));
		pinIdList.add(new Integer(1));
		this.inverting = inverting;
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);

		bodyPath_0.moveTo(-14.f, -12.f);
		bodyPath_0.quadTo(-5f, 0.f, -14.f, 12.f);
		bodyPath_0.lineTo(2.f, 12.f);
		bodyPath_0.quadTo(11.f, 11.f, 20.0f, 0.f);
		bodyPath_0.quadTo(11.f, -11.f, 2.0f, -12.f);
		bodyPath_0.lineTo(-14.f, -12.f);
		bodyPath_0.closePath();

		bodyPath_0.closePath();
		rotate90.setToRotation(Math.PI/2);
		rotate_90.setToRotation(-Math.PI/2);
		rotate180.setToRotation(Math.PI);

		bodyPath = bodyPath_0;
		pinE = pinE_0;
		pinS = pinS_0;
	}

	public String getType() {
		if (inverting) return "nor1";
		return "or1";
	}

	public ArrayList getPinIdList() {
		return pinIdList;
	}

	public Point2D getExtPinLocation(int pinId) {
		switch (pinId) {
			case 0 : return pinE;
			case 1 : return pinS;
		}
		return null;
	}

	public int getPinOrientation(int pinId) {
		switch (shapeIndex) {
			case 0:
				switch (pinId) {
					case 0 : return 0; // left
					case 1 : return 1; // right
				}
				break;
			case 1:
				switch (pinId) {
					case 0 : return 1; // right
					case 1 : return 0; // left
				}
				break;
		}
		return 0;
	}

	public boolean isPinInverted(int pinId) {
		if ((pinId == 1) && inverting) return true;
		return false;
	}

	public boolean isPinClocked(int pinId) {
		return false;
	}

	public boolean isPinScalable(int pinId) {
		return (pinId == 0);
	}

	public boolean isInput(int pinId) {
		return (pinId == 0);
	}

	public boolean isOutput(int pinId) {
		return (pinId == 1);
	}

	public Color getBodyColor() {
		return Module.gateColor;
	}

	public GeneralPath getBodyPath() {
		return bodyPath;
	}

	public void setBodyPath(GeneralPath path) {
		this.bodyPath = path;
	}

	public int nbShapes() {
		return 2;
	}

	public int getShape() {
		return shapeIndex;
	}

	public void setShape(int shapeIndex) {
		this.shapeIndex = shapeIndex;
		switch (shapeIndex) {
			case 0: transform = ID; break;
			case 1: transform = rotate180; break;
		}
		bodyPath = (GeneralPath) bodyPath_0.clone();
		bodyPath.transform(transform);
		pinE = transform.transform(pinE_0, null);
		pinS = transform.transform(pinS_0, null);
	}

}

