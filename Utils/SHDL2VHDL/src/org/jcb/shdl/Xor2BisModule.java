
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class Xor2BisModule extends Gate2Module {

	public Xor2BisModule(int id, NumExpr nb) {
		super(id, nb, false);

		pinE1_0 = new Point2D.Double(-25., -5.);
		pinE2_0 = new Point2D.Double(-25., 5.);
		pinS_0 = new Point2D.Double(35., 0.);
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		bodyPath_0.moveTo(-14.f, -12.f);
		bodyPath_0.quadTo(-5f, 0.f, -14.f, 12.f);
		bodyPath_0.quadTo(-5f, 0.f, -14.f, -12.f);
		bodyPath_0.moveTo(-9.f, -12.f);
		bodyPath_0.quadTo(-1f, 0.f, -9.f, 12.f);
		bodyPath_0.lineTo(2.f, 12.f);
		bodyPath_0.quadTo(11.f, 11.f, 20.0f, 0.f);
		bodyPath_0.quadTo(11.f, -9.f, 2.0f, -12.f);
		bodyPath_0.lineTo(-9.f, -12.f);
		bodyPath_0.closePath();

		bodyPath = bodyPath_0;
		pinE1 = pinE1_0;
		pinE2 = pinE2_0;
		pinS = pinS_0;
	}

	public String getType() {
		return "xor2bis";
	}

	public boolean isPinScalable(int pinId) {
		return (pinId != 1);
	}

}

