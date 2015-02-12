
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class FlipFlopDModule extends Module {

	private ArrayList pinIdList;
	private int shapeIndex = 0;
	protected Point2D pinD;
	protected Point2D pinH;
	protected Point2D pinRST;
	protected Point2D pinQ;
	protected GeneralPath bodyPath;
	private AffineTransform transform;

	protected Point2D pinD_0 = new Point2D.Double(-30., -30.);
	protected Point2D pinH_0 = new Point2D.Double(-30., -10.);
	protected Point2D pinRST_0 = new Point2D.Double(0., 25.);
	protected Point2D pinQ_0 = new Point2D.Double(30., -30.);
	protected GeneralPath bodyPath_0;

	private final AffineTransform ID = new AffineTransform();
	private final AffineTransform rotate90 = new AffineTransform();
	private final AffineTransform rotate_90 = new AffineTransform();
	private final AffineTransform rotate180 = new AffineTransform();


	public FlipFlopDModule(int id, NumExpr nb) {
		super(id, nb);
		pinIdList = new ArrayList();
		pinIdList.add(new Integer(0));
		pinIdList.add(new Integer(1));
		pinIdList.add(new Integer(2));
		pinIdList.add(new Integer(3));
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		bodyPath_0.moveTo(-15.f, -40.f);
		bodyPath_0.lineTo(-15.f, 10.f);
		bodyPath_0.lineTo(15.f, 10.f);
		bodyPath_0.lineTo(15.f, -40.f);
		bodyPath_0.closePath();
		rotate90.setToRotation(Math.PI/2);
		rotate_90.setToRotation(-Math.PI/2);
		rotate180.setToRotation(Math.PI);

		bodyPath = bodyPath_0;
		pinD = pinD_0;
		pinH = pinH_0;
		pinRST = pinRST_0;
		pinQ = pinQ_0;
	}

	public String getType() {
		return "flipflopD";
	}

	public ArrayList getPinIdList() {
		return pinIdList;
	}

	public Point2D getExtPinLocation(int pinId) {
		switch (pinId) {
			case 0 : return pinD;
			case 1 : return pinH;
			case 2 : return pinRST;
			case 3 : return pinQ;
		}
		return null;
	}

	public int getPinOrientation(int pinId) {
		switch (shapeIndex) {
			case 0:
				switch (pinId) {
					case 0 : return 0; // left
					case 1 : return 0; // left
					case 2 : return 3; // down
					case 3 : return 1; // right
				}
				break;
		}
		return 0;
	}

	public boolean isPinInverted(int pinId) {
		return false;
	}

	public boolean isPinClocked(int pinId) {
		return (pinId == 1);
	}

	public boolean isPinScalable(int pinId) {
		return ((pinId == 0) || (pinId == 3));
	}

	public boolean isInput(int pinId) {
		return (pinId < 3);
	}

	public boolean isOutput(int pinId) {
		return (pinId == 3);
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
		return 1;
	}

	public int getShape() {
		return shapeIndex;
	}

	public void setShape(int shapeIndex) {
		this.shapeIndex = shapeIndex;
		switch (shapeIndex) {
			case 0: transform = ID; break;
		}
		bodyPath = (GeneralPath) bodyPath_0.clone();
		bodyPath.transform(transform);
		pinD = transform.transform(pinD_0, null);
		pinH = transform.transform(pinH_0, null);
		pinRST = transform.transform(pinRST_0, null);
		pinQ = transform.transform(pinQ_0, null);
	}

}

