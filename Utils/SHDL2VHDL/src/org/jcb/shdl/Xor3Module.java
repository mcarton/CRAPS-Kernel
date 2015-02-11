
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class Xor3Module extends Gate3Module {

	public Xor3Module(int id, NumExpr nb) {
		super(id, nb, false);

		pinE1_0 = new Point2D.Double(-25., -10.);
		pinE2_0 = new Point2D.Double(-25., 0.);
		pinE3_0 = new Point2D.Double(-25., 10.);
		pinS_0 = new Point2D.Double(45., 0.);
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		bodyPath_0.moveTo(-14.f, -15.f);
		bodyPath_0.quadTo(-5f, 0.f, -14.f, 15.f);
		bodyPath_0.quadTo(-5f, 0.f, -14.f, -15.f);
		bodyPath_0.moveTo(-9.f, -15.f);
		bodyPath_0.quadTo(-1f, 0.f, -9.f, 15.f);
		bodyPath_0.lineTo(7.f, 15.f);
		bodyPath_0.quadTo(21.f, 13.f, 30.0f, 0.f);
		bodyPath_0.quadTo(21.f, -13.f, 7.0f, -15.f);
		bodyPath_0.lineTo(-9.f, -15.f);
		bodyPath_0.closePath();

		bodyPath = bodyPath_0;
		pinE1 = pinE1_0;
		pinE2 = pinE2_0;
		pinE3 = pinE3_0;
		pinS = pinS_0;
	}

	public String getType() {
		return "xor3";
	}

}

