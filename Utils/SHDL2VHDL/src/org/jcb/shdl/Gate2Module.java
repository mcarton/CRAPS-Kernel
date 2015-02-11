
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public abstract class Gate2Module extends Module {

	protected boolean inverting;

	private ArrayList pinIdList;
	private int shapeIndex = 0;
	protected Point2D pinE1;
	protected Point2D pinE2;
	protected Point2D pinS;
	protected GeneralPath bodyPath;
	private AffineTransform transform;

	protected Point2D pinE1_0;
	protected Point2D pinE2_0;
	protected Point2D pinS_0;
	protected GeneralPath bodyPath_0;

	private final AffineTransform ID = new AffineTransform();
	private final AffineTransform rotate90 = new AffineTransform();
	private final AffineTransform rotate_90 = new AffineTransform();
	private final AffineTransform rotate180 = new AffineTransform();


	public Gate2Module(int id, NumExpr nb, boolean inverting) {
		super(id, nb);
		this.inverting = inverting;

		pinIdList = new ArrayList();
		pinIdList.add(new Integer(0));
		pinIdList.add(new Integer(1));
		pinIdList.add(new Integer(2));
		this.inverting = inverting;
		rotate90.setToRotation(Math.PI/2);
		rotate_90.setToRotation(-Math.PI/2);
		rotate180.setToRotation(Math.PI);
	}

	public ArrayList getPinIdList() {
		return pinIdList;
	}

	public Point2D getExtPinLocation(int pinId) {
		switch (pinId) {
			case 0 : return pinE1;
			case 1 : return pinE2;
			case 2 : return pinS;
		}
		return null;
	}

	public int getPinOrientation(int pinId) {
		switch (shapeIndex) {
			case 0:
				// angle 0
				switch (pinId) {
					case 0 : return 0; // left
					case 1 : return 0; // left
					case 2 : return 1; // right
				}
				break;
			case 1:
				// angle 180
				switch (pinId) {
					case 0 : return 1; // right
					case 1 : return 1; // right
					case 2 : return 0; // left
				}
				break;
			case 2:
				// angle 90
				switch (pinId) {
					case 0 : return 2; // top
					case 1 : return 2; // top
					case 2 : return 3; // bottom
				}
				break;
			case 3:
				// angle -90
				switch (pinId) {
					case 0 : return 3; // bottom
					case 1 : return 3; // bottom
					case 2 : return 2; // top
				}
				break;
		}
		return 0;
	}

	public boolean isPinInverted(int pinId) {
		if ((pinId == 2) && inverting) return true;
		return false;
	}

	public boolean isPinClocked(int pinId) {
		return false;
	}

	public boolean isPinScalable(int pinId) {
		return true;
	}

	public boolean isInput(int pinId) {
		return (pinId < 2);
	}

	public boolean isOutput(int pinId) {
		return (pinId == 2);
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
			case 1: transform = rotate180; break;
			case 2: transform = rotate90; break;
			case 3: transform = rotate_90; break;
		}
		bodyPath = (GeneralPath) bodyPath_0.clone();
		bodyPath.transform(transform);
		pinE1 = transform.transform(pinE1_0, null);
		pinE2 = transform.transform(pinE2_0, null);
		pinS = transform.transform(pinS_0, null);
	}

}

