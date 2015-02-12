
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class ForkModule extends Module {

	private ArrayList pinIdList;
	private int shapeIndex = 0;
	private Point2D pinE;
	private Point2D pinS1;
	private Point2D pinS2;
	private GeneralPath bodyPath;
	private AffineTransform transform;

	private final Point2D pinE_0 = new Point2D.Double(-15., 0.);
	private final Point2D pinS1_0 = new Point2D.Double(15., -15.);
	private final Point2D pinS2_0 = new Point2D.Double(15., 15.);
	private final GeneralPath bodyPath_0;
	private final AffineTransform ID = new AffineTransform();
	private final AffineTransform rotate90 = new AffineTransform();
	private final AffineTransform rotate_90 = new AffineTransform();
	private final AffineTransform rotate180 = new AffineTransform();


	public ForkModule(int id, NumExpr nb) {
		super(id, nb);
		pinIdList = new ArrayList();
		pinIdList.add(new Integer(0));
		pinIdList.add(new Integer(1));
		pinIdList.add(new Integer(2));
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		//bodyPath_0.moveTo(-5.f, -(float)Module.busStrokeWidth / 2.f);
		//bodyPath_0.lineTo(10.f, -10.f - (float)Module.busStrokeWidth / 2.f + (float)Module.gateOutlineStrokeWidth / 2.f);
		//bodyPath_0.lineTo(10.f, -10.f + (float)Module.busStrokeWidth / 2.f - (float)Module.gateOutlineStrokeWidth / 2.f);
		//bodyPath_0.lineTo(-5.f, (float)Module.busStrokeWidth / 2.f);
		//bodyPath_0.lineTo(10.f, 10.f + (float)Module.busStrokeWidth / 2.f);
		//bodyPath_0.lineTo(10.f, 10.f - (float)Module.busStrokeWidth / 2.f);
		bodyPath_0.moveTo(-10.f, -10.f);
		bodyPath_0.lineTo(-10.f, 10.f);
		bodyPath_0.lineTo(10.f, 10.f);
		bodyPath_0.lineTo(10.f, -10.f);
		bodyPath_0.closePath();
		rotate90.setToRotation(Math.PI/2);
		rotate_90.setToRotation(-Math.PI/2);
		rotate180.setToRotation(Math.PI);

		bodyPath = bodyPath_0;
		pinE = pinE_0;
		pinS1 = pinS1_0;
		pinS2 = pinS2_0;
	}

	public String getType() {
		return "fork";
	}

	public ArrayList getPinIdList() {
		return pinIdList;
	}

	public Point2D getExtPinLocation(int pinId) {
		switch (pinId) {
			case 0 : return pinE;
			case 1 : return pinS1;
			case 2 : return pinS2;
		}
		return null;
	}

	public int getPinOrientation(int pinId) {
		switch (shapeIndex) {
			case 0:
				// angle 0
				switch (pinId) {
					case 0 : return 0; // left
					case 1 : return 1; // right
					case 2 : return 1; // right
				}
				break;
			case 1:
				// angle 180
				switch (pinId) {
					case 0 : return 1; // right
					case 1 : return 0; // left
					case 2 : return 0; // left
				}
				break;
			case 2:
				// angle 90
				switch (pinId) {
					case 0 : return 2; // top
					case 1 : return 3; // bottom
					case 2 : return 3; // bottom
				}
				break;
			case 3:
				// angle -90
				switch (pinId) {
					case 0 : return 3; // bottom
					case 1 : return 2; // top
					case 2 : return 2; // top
				}
				break;
		}
		return 0;
	}

	public boolean isPinInverted(int pinId) {
		return false;
	}

	public boolean isPinClocked(int pinId) {
		return false;
	}

	public boolean isPinScalable(int pinId) {
		return false;
	}

	public boolean isInput(int pinId) {
		return false;
	}

	public boolean isOutput(int pinId) {
		return false;
	}

	public Color getBodyColor() {
		return Module.backgroundColor;
	}

	public Color getOutlineColor() {
		return Module.backgroundColor;
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
			case 1: transform = rotate180; break;
			case 2: transform = rotate90; break;
			case 3: transform = rotate_90; break;
		}
		bodyPath = (GeneralPath) bodyPath_0.clone();
		bodyPath.transform(transform);
		pinE = transform.transform(pinE_0, null);
		pinS1 = transform.transform(pinS1_0, null);
		pinS2 = transform.transform(pinS2_0, null);
	}

}

