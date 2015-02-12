
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class Buff3StateModule extends Module {

	private boolean inverting;

	private ArrayList pinIdList;
	private int shapeIndex = 0;
	private Point2D pinE;
	private Point2D pinS;
	private Point2D pinOE;
	private GeneralPath bodyPath;
	private AffineTransform transform;

	private final Point2D pinE_0 = new Point2D.Double(-30., 0.);
	private final Point2D pinS_0 = new Point2D.Double(25., 0.);
	private final Point2D pinOE_0 = new Point2D.Double(0., -20.);
	private final GeneralPath bodyPath_0;
	private final AffineTransform ID = new AffineTransform();
	private final AffineTransform rotate90 = new AffineTransform();
	private final AffineTransform rotate_90 = new AffineTransform();
	private final AffineTransform rotate180 = new AffineTransform();


	public Buff3StateModule(int id, NumExpr nb, boolean inverting) {
		super(id, nb);
		pinIdList = new ArrayList();
		pinIdList.add(new Integer(0));
		pinIdList.add(new Integer(1));
		pinIdList.add(new Integer(2));
		this.inverting = inverting;
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		bodyPath_0.moveTo(-15.f, -12.f);
		bodyPath_0.lineTo(10.f, 0.f);
		bodyPath_0.lineTo(-15.f, 12.f);
		bodyPath_0.closePath();
		rotate90.setToRotation(Math.PI/2);
		rotate_90.setToRotation(-Math.PI/2);
		rotate180.setToRotation(Math.PI);

		bodyPath = bodyPath_0;
		pinE = pinE_0;
		pinS = pinS_0;
		pinOE = pinOE_0;
	}

	public String getType() {
		if (inverting) return "buff3Inv";
		return "buff3";
	}

	public ArrayList getPinIdList() {
		return pinIdList;
	}

	public Point2D getExtPinLocation(int pinId) {
		switch (pinId) {
			case 0 : return pinE;
			case 1 : return pinS;
			case 2 : return pinOE;
		}
		return null;
	}

	public int getPinOrientation(int pinId) {
		switch (shapeIndex) {
			case 0:
				switch (pinId) {
					case 0 : return 0; // left
					case 1 : return 1; // right
					case 2 : return 2; // up
				}
				break;
			case 1:
				switch (pinId) {
					case 0 : return 2; // up
					case 1 : return 3; // down
					case 2 : return 1; // right
				}
				break;
			case 2:
				switch (pinId) {
					case 0 : return 1; // right
					case 1 : return 0; // left
					case 2 : return 3; // down
				}
				break;
			case 3:
				switch (pinId) {
					case 0 : return 3; // down
					case 1 : return 2; // up
					case 2 : return 0; // left
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

	// only OE is not scalable
	public boolean isPinScalable(int pinId) {
		return (pinId != 2);
	}

	public boolean isInput(int pinId) {
		return (pinId != 1);
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
		return 4;
	}

	public int getShape() {
		return shapeIndex;
	}

	public void setShape(int shapeIndex) {
		this.shapeIndex = shapeIndex;
		switch (shapeIndex) {
			case 0: transform = ID; break;
			case 1: transform = rotate90; break;
			case 2: transform = rotate180; break;
			case 3: transform = rotate_90; break;
		}
		bodyPath = (GeneralPath) bodyPath_0.clone();
		bodyPath.transform(transform);
		pinE = transform.transform(pinE_0, null);
		pinS = transform.transform(pinS_0, null);
		pinOE = transform.transform(pinOE_0, null);
	}

}

