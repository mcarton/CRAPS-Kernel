
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


public class Equipotential {

	private int id;
	private String name;
	private NumExpr width;

	public Equipotential(int id, String name, NumExpr width) {
		this.id = id;
		this.name = name;
		this.width = width;
	}

	public String toString() {
		return name;
	}

	public int getId() {
		return id;
	}

	public NumExpr getWidth() {
		return width;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getStrokeWidth() {
		if (width.eval(null) == 1)
			return Module.cnxStrokeWidth;
		else
			return Module.busStrokeWidth;
	}

	public Stroke getStroke() {
		if (width.eval(null) == 1)
			return Module.cnxStroke;
		else
			return Module.busStroke;
	}

}

