
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jcb.shdl.*;


public abstract class Module {

	private int id;

	// number of instances in parallel
	protected NumExpr nb;

	public final static double cnxStrokeWidth = 2.0;
	public final static double busStrokeWidth = 4.0;
	public final static double gateOutlineStrokeWidth = 2.0;
	public final static double cnxPtStrokeWidth = 2.0;
	public final static double cnxJunctionRadius = 3.0;
	public final static double busJunctionRadius = 4.5;
	public final static double cnxPinRadius = 4.0;
	public final static double busPinRadius = 5.;
	public final static double cnxPtRectWidth = 8.0;
	public final static double ctrlPtWidth = 8.0;
	public final static double pinLength= 15.0;
	public final static double invertedDiameter = 7.0;

	public final static Stroke cnxStroke = new BasicStroke((float) cnxStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public final static Stroke busStroke = new BasicStroke((float) busStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	public final static Stroke widthStroke = new BasicStroke(1.5f);
	public final static Stroke labelFrameStroke = new BasicStroke(1.0f);
	public final static Stroke labelLineStroke = new BasicStroke(0.7f);
	public final static Stroke crossStroke = new BasicStroke(0.5f);
	public final static Stroke gateOutlineStroke = new BasicStroke((float)gateOutlineStrokeWidth);
	public final static Stroke moduleOutlineStroke = new BasicStroke(3.0f);
	public final static Stroke groupSelectStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_MITER, 3.0f, new float[] {3.0f}, 0.0f);
	public final static Stroke interCnxPtRectStroke = new BasicStroke((float) cnxPtStrokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	public final static Color backgroundColor = Color.white;
	public final static Color gateOutlineColor = Color.black;
	public final static Color gateColor = new Color(69, 180, 200, 230);
	//public final static Color gateColor = new Color(69, 180, 200);
	public final static Color universalGateColor = new Color(69, 180, 200, 230);
	//public final static Color universalGateColor = new Color(69, 180, 200);
	public final static Color universalGateTextColor = Color.red;
	public final static Color moduleColor = new Color(69, 180, 200, 230);
	//public final static Color moduleColor = new Color(69, 180, 200);
	public final static Color selectedColor = Color.blue;
	public final static Color groupSelectColor = Color.darkGray;
	public final static Color cnxColor = Color.black;
	public final static Color cnxSelectedColor = Color.blue;
	public final static Color widthColor = new Color(150, 150, 150);
	public final static boolean frameLabels = true;
	public final static Color labelBackgroundColor = new Color(214, 216, 120);
	public final static Color labelFrameColor = Color.lightGray;
	public final static Color labelColor = new Color(134, 104, 34);
	public final static Color labelLineColor = Color.lightGray;
	public final static Color pinModuleColor = new Color(50, 20, 80);
	public final static Color interCnxPtRectColor = Color.green;
	public final static Color nbInstanceColor = new Color(134, 104, 34);
	public final static Color interfaceColor = new Color(200, 200, 200);
	public final static Color moduleNameColor = Color.black;
	public final static Color interfaceCrossColor = new Color(100, 100, 100, 160);
	//public final static Color interfaceCrossColor = new Color(100, 100, 100);
	public final static Color interfacePtColor = new Color(255, 0, 0, 160);
	//public final static Color interfacePtColor = new Color(255, 0, 0);
	public final static Color interfacePtOrgColor = new Color(0, 255, 0, 160);
	//public final static Color interfacePtOrgColor = new Color(0, 255, 0);
	public final static Color interfacePtCtrlColor = new Color(255, 200, 0, 160);
	//public final static Color interfacePtCtrlColor = new Color(255, 200, 0);
	public final static Color interfacePinColor = new Color(255, 255, 0, 160);
	//public final static Color interfacePinColor = new Color(255, 255, 0);

	public final static Font widthFont = new Font("Monospaced", Font.PLAIN, 10);
	public final static Font universalGateTextFont = new Font("Monospaced", Font.PLAIN, 10);
	public final static Font nameFont = new Font("Helvetica", Font.PLAIN, 12);
	public final static Font nbInstanceFont = new Font("Helvetica", Font.PLAIN, 12);
	public final static FontMetrics widthFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(widthFont);
	public final static FontMetrics nameFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(nameFont);
	public final static FontMetrics universalGateTextFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(nameFont);
	public final static FontMetrics nbInstanceFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(nbInstanceFont);

	public final static boolean GRID = true;
	public final static double GRIDSTEP = 5.0;


	public Module(int id, NumExpr nb) {
		this.id = id;
		this.nb = nb;
	}


	//////////////////////////////       INTERFACE        //////////////////////////

	public int getId() {
		return id;
	}

	// type

	public abstract String getType();


	// number of superimposed instances
	// it is a string, so it can become a numerical expression and allow for genericity

	public NumExpr getArity() {
		return nb;
	}

	public void setArity(NumExpr nb) {
		this.nb = nb;
	}

	// submodules population and location

	public Module getSubModule(int subModId) {
		return null;
	}

	public Point2D getSubModuleLocation(int subModId) {
		return null;
	}

	// external interface of the module

	public int nbPins() {
		return getPinIdList().size();
	}

	public abstract ArrayList getPinIdList();

	public abstract Point2D getExtPinLocation(int pinId);

	public abstract int getPinOrientation(int pinId); // 0=left, 1=right, 2=up, 3=down
	public abstract boolean isPinInverted(int pinId); // a 'o' sign is drawn with the pin
	public abstract boolean isPinClocked(int pinId); // a 'v' sign is drawn with the pin

	public abstract boolean isInput(int pinId);
	public abstract boolean isOutput(int pinId);

	public abstract boolean isPinScalable(int pinId);

	private static NumExpr VAL1 = new NumExprVal(1);


	public NumExpr getScalingFactor(int pinId) {
		if (isPinScalable(pinId)) return nb; else return VAL1;
	}

	// compMod necessary for forks
	public NumExpr getWidth(int pinId, CompoundModule compModule) {
		if (this instanceof CompoundModule) {
			// for compound modules, pinId is the id of a CnxPointPin
			CompoundModule compMod = (CompoundModule) this;
			CnxPointPin pin = (CnxPointPin) compMod.getCnxPoint(pinId);
			NumExpr w = new NumExprVal(1);
			Equipotential equi = pin.getEquipotential();
			if (equi != null) w = equi.getWidth();
			return new NumExprMul(getScalingFactor(pinId), w);
		} else if (this instanceof ForkModule) {
			CnxPointModule cnxpt = compModule.getCnxPointModule(getId(), pinId);
			NumExpr w = new NumExprVal(1);
			Equipotential equi = cnxpt.getEquipotential();
			if (equi == null) return w;
			return new NumExprMul(w, equi.getWidth());
		} else {
			return getScalingFactor(pinId);
		}
	}

	// compMod necessary for forks
	public Stroke getStroke(int pinId, CompoundModule compMod) {
		if (getWidth(pinId, compMod).eval(null) == 1)
			return Module.cnxStroke;
		else
			return Module.busStroke;
	}

	// selecting and painting

	public abstract GeneralPath getBodyPath();
	public abstract void setBodyPath(GeneralPath path);
	public abstract Color getBodyColor();
	public Color getOutlineColor() { return Module.gateOutlineColor; }

	// different shapes

	public abstract int nbShapes();
	public abstract int getShape();
	public abstract void setShape(int shapeIndex);


	//////////////////////////////////////////////////////////////////////////////////

	public void paintBody(Graphics2D g2, boolean selected, boolean ghosted) {

		GeneralPath bodyPath = getBodyPath();

		// paint body inside
		if (!ghosted) {
			g2.setPaint(getBodyColor());
			g2.fill(bodyPath);
		}

		// draw body outline
		g2.setPaint(getOutlineColor(selected, ghosted));
		g2.setStroke(getOutlineStroke());
		g2.draw(bodyPath);
	}

	public Color getOutlineColor(boolean selected, boolean ghosted) {
		if (selected)
			return Module.selectedColor;
		else if (ghosted)
			return Color.lightGray;
		else
			return getOutlineColor();
	}

	public Stroke getOutlineStroke() {
		if (this instanceof CompoundModule)
			return Module.moduleOutlineStroke;
		else
			return Module.gateOutlineStroke;
	}

	public Color getPinColor(boolean selected, boolean ghosted) {
		if (selected)
			return Module.selectedColor;
		else if (ghosted)
			return Color.lightGray;
		else
			return Module.cnxColor;
	}

	// compMod necessary for forks
	protected void drawPins(Graphics2D g2, CompoundModule compMod, boolean selected, boolean ghosted) {
		ArrayList idList = getPinIdList();
		for (int i = 0; i < nbPins(); i++) {
			int pinId = ((Integer) idList.get(i)).intValue();
			drawPin(g2, pinId, compMod, selected, ghosted);
		}
	}

	// compMod necessary for forks
	public void drawPin(Graphics2D g2, int pinId, CompoundModule compMod, boolean selected, boolean ghosted) {
		Point2D loc = getExtPinLocation(pinId);
		g2.setPaint(getPinColor(selected, ghosted));
		g2.setStroke(getStroke(pinId, compMod));
		switch (getPinOrientation(pinId)) {
			case 0: // left
				if (isPinInverted(pinId))
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), loc.getX() + Module.pinLength - Module.invertedDiameter, loc.getY()));
				else
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), loc.getX() + Module.pinLength, loc.getY()));
				if (isPinClocked(pinId)) {
					g2.draw(new Line2D.Double(loc.getX() + Module.pinLength, loc.getY() - 5., loc.getX() + Module.pinLength + 10., loc.getY()));
					g2.draw(new Line2D.Double(loc.getX() + Module.pinLength, loc.getY() + 5., loc.getX() + Module.pinLength + 10., loc.getY()));
				}
				if (isPinInverted(pinId)) {
					if (!ghosted) {
						g2.setPaint(getBodyColor());
						g2.fill(new Ellipse2D.Double(loc.getX() - Module.invertedDiameter + Module.pinLength, loc.getY() - Module.invertedDiameter/2, Module.invertedDiameter, Module.invertedDiameter));
					}
					g2.setStroke(getOutlineStroke());
					g2.setPaint(getOutlineColor(selected, ghosted));
					g2.draw(new Ellipse2D.Double(loc.getX() - Module.invertedDiameter + Module.pinLength, loc.getY() - Module.invertedDiameter/2, Module.invertedDiameter, Module.invertedDiameter));
				}
				break;
			case 1: // right
				if (isPinInverted(pinId))
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), loc.getX() - Module.pinLength + Module.invertedDiameter, loc.getY()));
				else
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), loc.getX() - Module.pinLength, loc.getY()));
				if (isPinClocked(pinId)) {
					g2.draw(new Line2D.Double(loc.getX() - Module.pinLength, loc.getY() - 5., loc.getX() - Module.pinLength - 10., loc.getY()));
					g2.draw(new Line2D.Double(loc.getX() - Module.pinLength, loc.getY() + 5., loc.getX() - Module.pinLength - 10., loc.getY()));
				}
				if (isPinInverted(pinId)) {
					if (!ghosted) {
						g2.setPaint(getBodyColor());
						g2.fill(new Ellipse2D.Double(loc.getX() - Module.pinLength, loc.getY() - Module.invertedDiameter/2, Module.invertedDiameter, Module.invertedDiameter));
					}
					g2.setStroke(getOutlineStroke());
					g2.setPaint(getOutlineColor(selected, ghosted));
					g2.draw(new Ellipse2D.Double(loc.getX() - Module.pinLength, loc.getY() - Module.invertedDiameter/2, Module.invertedDiameter, Module.invertedDiameter));
				}
				break;
			case 2: // up
				g2.draw(new Line2D.Double(loc.getX(), loc.getY(), loc.getX(), loc.getY() + Module.pinLength));
				break;
			case 3: // down
				if (isPinInverted(pinId))
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), loc.getX(), loc.getY() - Module.pinLength + Module.invertedDiameter));
				else
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), loc.getX(), loc.getY() - Module.pinLength));
				if (isPinClocked(pinId)) {
					g2.draw(new Line2D.Double(loc.getX() - 5., loc.getY() - Module.pinLength, loc.getX(), loc.getY() - Module.pinLength - 10.));
					g2.draw(new Line2D.Double(loc.getX(), loc.getY() - Module.pinLength - 10., loc.getX() + 5., loc.getY() - Module.pinLength));
				}
				if (isPinInverted(pinId)) {
					if (!ghosted) {
						g2.setPaint(getBodyColor());
						g2.fill(new Ellipse2D.Double(loc.getX() - Module.invertedDiameter/2, loc.getY() - Module.pinLength, Module.invertedDiameter, Module.invertedDiameter));
					}
					g2.setStroke(getOutlineStroke());
					g2.setPaint(getOutlineColor(selected, ghosted));
					g2.draw(new Ellipse2D.Double(loc.getX() - Module.invertedDiameter/2, loc.getY() - Module.pinLength, Module.invertedDiameter, Module.invertedDiameter));
				}
				break;
		}
		// draw pin label for CompoundModules
		if (this instanceof CompoundModule) {
			// for compound modules, pinId is the id of a CnxPointPin
			CompoundModule compModule = (CompoundModule) this;
			CnxPointPin pin = (CnxPointPin) compModule.getCnxPoint(pinId);
			if (compModule.isExtPinLabelVisible(pin)) {
				ExtPinLabel label = compModule.getExtPinLabel(pin);
				label.paint2(g2, false);
			}
		}
	}

	protected void drawForkPins(Graphics2D g2, CompoundModule compMod, boolean selected, boolean ghosted) {
		for (int pinId = 0; pinId < 3; pinId++) {
			Point2D loc = getExtPinLocation(pinId);
			g2.setPaint(getPinColor(selected, ghosted));
			g2.setStroke(getStroke(pinId, compMod));
			switch (pinId) {
				case 0:
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), 0., 0.));
					break;
				case 1:
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), 0., 0.));
					break;
				case 2:
					g2.draw(new Line2D.Double(loc.getX(), loc.getY(), 0., 0.));
					break;
			}
		}
	}

	// when selected = true, highlight module
	// when ghosted = true (during move operations), draw only a gray outline
	// compMod necessary for forks

	public void paint(Graphics2D g2, CompoundModule compMod, boolean selected, boolean ghosted) {

		// paint body
		paintBody(g2, selected, ghosted);

		// draw pins last (so they're always visible)
		if (this instanceof ForkModule)
			drawForkPins(g2, compMod, selected, ghosted);
		else
			drawPins(g2, compMod, selected, ghosted);

		// draw module name for CompoundModules
		if (this instanceof CompoundModule) {
			CompoundModule compModule = (CompoundModule) this;
			if (compModule.isModuleNameLabelVisible(0)) {
				ModuleNameLabel moduleNameLabel = compModule.getModuleNameLabel(0);
				moduleNameLabel.paint2(g2, false);
			}
			if (compModule.isModuleNameLabelVisible(1)) {
				ModuleNameLabel moduleNameLabel = compModule.getModuleNameLabel(1);
				moduleNameLabel.paint2(g2, false);
			}
		}
	}
}
