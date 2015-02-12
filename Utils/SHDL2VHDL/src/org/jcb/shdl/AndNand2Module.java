
package org.jcb.shdl;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import org.jcb.shdl.*;


public class AndNand2Module extends Gate2Module {

	public AndNand2Module(int id, NumExpr nb, boolean inverting) {
		super(id, nb, inverting);

		pinE1_0 = new Point2D.Double(-25., -5.);
		pinE2_0 = new Point2D.Double(-25., 5.);
		pinS_0 = new Point2D.Double(35., 0.);
		bodyPath_0 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		bodyPath_0.moveTo(-10.f, -12.f);
		bodyPath_0.lineTo(-10.f, 12.f);
		bodyPath_0.lineTo(5.f, 12.f);
		bodyPath_0.quadTo(20.f, 12.f, 20.0f, 0.f);
		bodyPath_0.quadTo(20.f, -12.f, 5.f, -12.f);
		bodyPath_0.lineTo(-10.f, -12.f);
		bodyPath_0.closePath();

		bodyPath = bodyPath_0;
		pinE1 = pinE1_0;
		pinE2 = pinE2_0;
		pinS = pinS_0;
	}

	public String getType() {
		if (inverting) return "nand2";
		return "and2";
	}

}

