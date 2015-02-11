
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class OrNor3Module extends Gate3Module {

	public OrNor3Module(int id, NumExpr nb, boolean inverting) {
		super(id, nb, inverting);

		pinE1_0 = new Point2D.Double(-25., -10.);
		pinE2_0 = new Point2D.Double(-25., 0.);
		pinE3_0 = new Point2D.Double(-25., 10.);
		pinS_0 = new Point2D.Double(45., 0.);
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		bodyPath_0.moveTo(-10.f, -15.f);
		bodyPath_0.quadTo(-5f, 0.f, -10.f, 15.f);
		bodyPath_0.lineTo(-10.f, 15.f);
		bodyPath_0.lineTo(5.f, 15.f);
		bodyPath_0.quadTo(20.f, 15.f, 30.0f, 0.f);
		bodyPath_0.quadTo(20.f, -15.f, 5.f, -15.f);
		bodyPath_0.lineTo(-10.f, -15.f);
		bodyPath_0.closePath();

		bodyPath = bodyPath_0;
		pinE1 = pinE1_0;
		pinE2 = pinE2_0;
		pinE3 = pinE3_0;
		pinS = pinS_0;
	}

	public String getType() {
		if (inverting) return "nor3";
		return "or3";
	}

}

