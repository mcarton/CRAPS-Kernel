
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import org.jcb.shdl.*;


public abstract class ModulePanel extends JPanel {

	protected CompoundModule compModule;

	protected ArrayList subModules = new ArrayList();
	protected double scale = 2.0;


	public ModulePanel(CompoundModule compModule) {
		this.compModule = compModule;
	}


	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	private int hitModId;
	private CnxPoint hitCnxPoint;
	private Cnx hitCnx;
	private ModuleLabel hitLabel;

	// Test whether a submodule has been hit
	public boolean isModuleHit(double x, double y) {
		hitModId = -1;
		boolean res = false;
		Iterator subModIterator = compModule.getSubModulesIterator();
		while (subModIterator.hasNext()) {
			Module subMod = (Module) subModIterator.next();
			Point2D locMod = compModule.getSubModuleLocation(subMod.getId());
			GeneralPath subModSelShape = (GeneralPath) subMod.getBodyPath().clone();
			AffineTransform at = new AffineTransform();
			at.setToTranslation(locMod.getX(), locMod.getY());
			subModSelShape.transform(at);
			// test compModule body
			if (subModSelShape.contains(x, y)) {
				// subMod has been hit
				hitModId = subMod.getId();
				res = true;
			}
		}
		return res;
	}


	// Test whether a connexion has been hit
	public boolean isCnxHit(double x, double y) {
		hitCnx = null;
		ArrayList cnxList = compModule.getCnxList();
		for (int i = 0; i < cnxList.size(); i++) {
			Cnx cnx = (Cnx) cnxList.get(i);
			if (cnx.hit(x, y)) {
				hitCnx = cnx;
				return true;
			}
		}
		return false;
	}


	// Test whether a connexion point has been hit
	public boolean isCnxPointHit(double x, double y) {
		hitCnxPoint = null;
		ArrayList cnxPointList = compModule.getCnxPointList();
		for (int i = 0; i < cnxPointList.size(); i++) {
			CnxPoint cp = (CnxPoint) cnxPointList.get(i);
			if (cp.hit(x, y)) {
				hitCnxPoint = cp;
				return true;
			}
		}
		return false;
	}


	// Test whether a label has been hit
	public boolean isLabelHit(double x, double y) {
		hitLabel  = null;
		ArrayList labelList = compModule.getLabelList();
		for (int i = 0; i < labelList.size(); i++) {
			ModuleLabel label = (ModuleLabel) labelList.get(i);
			RoundRectangle2D rect = label.getRect(label.getAnchorLocation());
			if (rect.contains(x, y)) {
				hitLabel = label;
				return true;
			}
		}
		return false;
	}


	public Point2D getPinLocation(int modIndex, int pinIndex) {
		Module subMod = (Module) compModule.getSubModule(modIndex);
		Point2D modLoc = (Point2D) compModule.getSubModuleLocation(modIndex);
		Point2D pinLoc = subMod.getExtPinLocation(pinIndex);
		return new Point2D.Double(modLoc.getX() + pinLoc.getX(), modLoc.getY() + pinLoc.getY());
	}

	public Rectangle2D getPinRect(int modIndex, int pinIndex) {
		Module subMod = (Module) compModule.getSubModule(modIndex);
		Point2D modLoc = (Point2D) compModule.getSubModuleLocation(modIndex);
		Point2D pinLoc = subMod.getExtPinLocation(pinIndex);
		return new Rectangle2D.Double(modLoc.getX() + pinLoc.getX() - 5.0, modLoc.getY() + pinLoc.getY() - 5.0, 10., 10.);
	}


	public int getHitModuleId() {
		return hitModId;
	}

	public Cnx getHitCnx() {
		return hitCnx;
	}

	public CnxPoint getHitCnxPoint() {
		return hitCnxPoint;
	}

	public ModuleLabel getHitLabel() {
		return hitLabel;
	}


	// returns a list of all submodules indexes contained in the rectangle (x, y, w, h)
	public ArrayList selectedGroup(double x, double y, double w, double h) {
		ArrayList list = new ArrayList();
		Rectangle2D.Double selectRectangle = new Rectangle2D.Double(x, y, w, h);
		Iterator subModIterator = compModule.getSubModulesIterator();
		while (subModIterator.hasNext()) {
			Module subMod = (Module) subModIterator.next();
			Point2D locMod = compModule.getSubModuleLocation(subMod.getId());
			Rectangle2D subModSelRect = subMod.getBodyPath().getBounds2D();
			subModSelRect.setRect(subModSelRect.getX() + locMod.getX(), subModSelRect.getY() + locMod.getY(), subModSelRect.getWidth(), subModSelRect.getHeight());
			if (selectRectangle.contains(subModSelRect)) {
				Integer I = new Integer(subMod.getId());
				if (list.contains(I))
					list.remove(I);
				else
					list.add(I);
			}
		}
		return list;
	}

	// returns a list of all connections contained in the rectangle (x, y, w, h)
	public ArrayList selectedCnxGroup(double x, double y, double w, double h) {
		ArrayList list = new ArrayList();
		Rectangle2D.Double selectRectangle = new Rectangle2D.Double(x, y, w, h);
		ArrayList cnxList = compModule.getCnxList();
		for (int i = 0; i < cnxList.size(); i++) {
			Cnx cnx = (Cnx) cnxList.get(i);
			if (selectRectangle.contains(cnx.getCp1().getLocation()) && selectRectangle.contains(cnx.getCp2().getLocation())) {
				if (list.contains(cnx))
					list.remove(cnx);
				else
					list.add(cnx);
			}
		}
		return list;
	}

	// returns a list of all intermediate connection points contained in the rectangle (x, y, w, h)
	public ArrayList selectedCnxPointGroup(double x, double y, double w, double h) {
		ArrayList list = new ArrayList();
		Rectangle2D.Double selectRectangle = new Rectangle2D.Double(x, y, w, h);
		ArrayList cnxPointList = compModule.getCnxPointList();
		for (int i = 0; i < cnxPointList.size(); i++) {
			CnxPoint cnxPoint = (CnxPoint) cnxPointList.get(i);
			if (!(cnxPoint instanceof CnxPointInter)) continue;
			if (selectRectangle.contains(cnxPoint.getLocation()) && selectRectangle.contains(cnxPoint.getLocation())) {
				if (list.contains(cnxPoint))
					list.remove(cnxPoint);
				else
					list.add(cnxPoint);
			}
		}
		return list;
	}


	public boolean shift(MouseEvent ev) {
		if ((ev.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) {
			return true;
		} else
			return false;
	}

	public boolean ctrlOrShift(MouseEvent ev) {
		if (((ev.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) ||
				((ev.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0)) {
			return true;
		} else
			return false;
	}

}

