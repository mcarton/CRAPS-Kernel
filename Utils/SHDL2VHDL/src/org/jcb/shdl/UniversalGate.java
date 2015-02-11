
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class UniversalGate extends Module {

	private int nbInput;

	private ArrayList pinIdList;
	private GeneralPath bodyPath;
	private float height;

	public UniversalGate(int id, NumExpr nb, int nbInput) {
		super(id, nb);
		this.nbInput = nbInput;
		bodyPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		height = nbInput * 10.f;
		bodyPath.moveTo(-20.f, -height/2);
		bodyPath.lineTo(-20.f, height/2);
		bodyPath.lineTo(20.f, height/2);
		bodyPath.lineTo(20.f, -height/2);
		bodyPath.closePath();

		pinIdList = new ArrayList();
		for (int i = 0; i < nbInput + 1; i++)
			pinIdList.add(new Integer(i));
	}

	public String getType() {
		return "univgate";
	}

	public ArrayList getPinIdList() {
		return pinIdList;
	}

	public Point2D getExtPinLocation(int pinId) {
		if (getPinOrientation(pinId) == 0)
			return new Point2D.Double(-20. - Module.pinLength, -height/2 + 10.f/2 + 10.f * pinId);
		else
			return new Point2D.Double(20. + Module.pinLength, 0.f);
	}

	public int getPinOrientation(int pinId) {
		
		if (((Integer)pinIdList.get(pinIdList.size() - 1)).intValue() == pinId) return 1; // right for the last pin
		return 0; // otherwise : left
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
		return (getPinOrientation(pinId) == 0);
	}

	public boolean isOutput(int pinId) {
		return !isInput(pinId);
	}

	public Color getBodyColor() {
		return Module.universalGateColor;
	}

	public GeneralPath getBodyPath() {
		return bodyPath;
	}

	public void setBodyPath(GeneralPath path) {
		this.bodyPath = path;
	}

	public int nbShapes() {
		return 1;
	}

	public int getShape() {
		return 0;
	}

	public void setShape(int shapeIndex) {
	}

}

