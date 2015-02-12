
package org.jcb.shdl;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import org.jcb.shdl.*;


public class CompoundModule extends Module {

	private String modName;
	private ModuleNameLabel moduleNameLabel1 = null;
	private ModuleNameLabel moduleNameLabel2 = null;

	private ArrayList pinIdList = new ArrayList();
	private ArrayList cnxPointList = new ArrayList();
	private ArrayList cnxList = new ArrayList();
	private ArrayList labelList = new ArrayList();
	private ArrayList extPinLabelList = new ArrayList();

	private HashMap id2submod = new HashMap();
	private HashMap id2submodloc = new HashMap();
	private HashMap id2cnx = new HashMap();
	private HashMap equi2cnxpt = new HashMap();
	private HashMap id2equi = new HashMap();
	private HashMap id2cnxpt = new HashMap();
	private HashMap subModxpin2cp = new HashMap();
	private HashMap cnxpt2cnx = new HashMap();
	private HashMap cnxpt2equiLabel = new HashMap();
	private HashMap cnxpt2extPinLabel = new HashMap();
	private HashMap subMod2nbInstLabel = new HashMap();
	private HashMap cnxpt2widthLabel = new HashMap();
	private HashMap univGate2Label = new HashMap();

	private GeneralPath bodyPath;

	private static AffineTransform AFFINE_ID = new AffineTransform();


	public CompoundModule(int id, NumExpr nb) {
		super(id, nb);
		modName = "mod#" + id;
		bodyPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		bodyPath.moveTo(0.f, 0.f);
		bodyPath.closePath();
	}

	public String getModuleName() {
		return modName;
	}

	public void setModuleName(String name) {
		this.modName = name;
	}


	public boolean isEmpty() {
		return (id2equi.size() + id2cnxpt.size() == 0);
	}


	//////////////////////           Submodules              /////////////////////


	private int maxSubmodId = -1;;

	public int getNewSubmodId() {
		maxSubmodId += 1;
		return maxSubmodId;
	}

	public void addSubModule(Module subMod, Point2D location) {
		int subModId = subMod.getId();
		id2submod.put(subModId + "", subMod);
		id2submodloc.put(subModId + "", location);
		setSubModuleLocation(subModId, location); // apply grid rules
		if (subMod.getId() > maxSubmodId) maxSubmodId = subMod.getId();
	}

	public void delSubModule(Module subMod) {
		int subModId = subMod.getId();
		id2submod.remove(subModId + "");
		id2submodloc.remove(subModId + "");
	}


	public void moveSubModule(int subModId, double x, double y) {
		Point2D locMod = getSubModuleLocation(subModId);
		setSubModuleLocation(subModId, new Point2D.Double(x, y));
	}

	public void moveCnxPoint(CnxPoint cp, double x, double y) {
		Point2D loc = cp.getLocation();
		setCnxPointLocation(cp, new Point2D.Double(x, y));
	}

	private StringBuffer currVarName = new StringBuffer("a");

	public void setCurrVarName(String name) {
		currVarName = new StringBuffer(name);
		getNewVarName();
	}

	public String getNewVarName() {
		String res = new String(currVarName);
		
		for (int i = 0; i < res.length(); i++) {
			char c = res.charAt(i);
			if (c == 'z') {
				currVarName.setCharAt(i, 'a');
				if (i == res.length() - 1) {
					currVarName.append('a');
				} else {
					char c1 = res.charAt(i + 1);
					currVarName.setCharAt(i + 1, (char) (c1 + 1));
				}
			} else {
				currVarName.setCharAt(i, (char) (c + 1));
				break;
			}
		}
		return res;
	}

	//////////////////           Submodules            ///////////////////

	public String getType() {
		return "compound";
	}

	public Iterator getSubModulesIterator() {
		return id2submod.values().iterator();
	}

	public Module getSubModule(int subModId) {
		return ((Module) id2submod.get(subModId + ""));
	}

	public Point2D getSubModuleLocation(int subModId) {
		return ((Point2D) id2submodloc.get(subModId + ""));
	}

	public void setSubModuleLocation(int subModId, Point2D loc) {
		double x = 0.;
		double y = 0.;
		if (Module.GRID) {
			x = ((int) Math.round(loc.getX() / Module.GRIDSTEP)) * Module.GRIDSTEP;
			y = ((int) Math.round(loc.getY() / Module.GRIDSTEP)) * Module.GRIDSTEP;
		} else {
			x = loc.getX();
			y = loc.getY();
		}
		loc = getSubModuleLocation(subModId);
		loc.setLocation(x, y);
	}


	//////////////////           Pins            ///////////////////


	public ArrayList getPinIdList() {
		return pinIdList;
	}

	public Point2D getExtPinLocation(int pinId) {
		CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
		return pin.getExtLocation();
	}

	public boolean isInput(int pinId) {
		return false;
	}

	public boolean isOutput(int pinId) {
		return false;
	}

	public boolean isPinScalable(int pinId) {
		return false;
	}

	public int getPinOrientation(int pinId) {
		CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
		return pin.getOrientation();
	}

	public boolean isPinInverted(int pinId) {
		CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
		return pin.isInverted();
	}

	public boolean isPinClocked(int pinId) {
		CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
		return pin.isClocked();
	}


	//////////////////           Painting            ///////////////////

	public Color getBodyColor() {
		return Module.moduleColor;
	}

	public GeneralPath getBodyPath() {
		return bodyPath;
	}

	public void setBodyPath(GeneralPath path) {
		this.bodyPath = path;
	}

	public int nbShapes () {
		return 1;
	}

	public int getShape() {
		return 0;
	}

	public void setShape(int shapeIndex) {
	}

	public void paintArity(Graphics2D g2) {
		g2.drawString("X " + nb, 10.f, 10.f);
	}



	//////////////////////           Equipotentials              /////////////////////

	private int maxEquiId = -1;;

	public int getNewEquiId() {
		maxEquiId += 1;
		return maxEquiId;
	}

	public void addEquipotential(Equipotential equi) {
		id2equi.put("" + equi.getId(), equi);
		if (equi.getId() > maxEquiId) maxEquiId = equi.getId();
	}

	public void delEquipotential(Equipotential equi) {
		id2equi.remove("" + equi.getId());
		equi2cnxpt.remove(equi);
	}

	public Equipotential getEqui(int id) {
		return ((Equipotential) id2equi.get("" + id));
	}

	//////////////////////        Connection points         /////////////////////

	private int maxCnxPointId = -1;;

	public ArrayList getCnxPointList() {
		return cnxPointList;
	}

	public int getNewCnxPointId() {
		maxCnxPointId += 1;
		return maxCnxPointId;
	}

	public void addCnxPoint(CnxPoint cnxPoint) {
		cnxPointList.add(cnxPoint);
		id2cnxpt.put(cnxPoint.getId() + "", cnxPoint);
		if (cnxPoint.getId() > maxCnxPointId) maxCnxPointId = cnxPoint.getId();
	}

	public void delCnxPoint(CnxPoint cnxPoint) {
		cnxPointList.remove(cnxPoint);
		id2cnxpt.remove(cnxPoint.getId() + "");
	}

	public void addCnxPointModule(CnxPointModule cp, int subModId, int pinId) {
		addCnxPoint(cp);
		subModxpin2cp.put(subModId + "," + pinId, cp);
	}

	public void delCnxPointModule(CnxPointModule cp, int subModId, int pinId) {
		delCnxPoint(cp);
		subModxpin2cp.remove(subModId + "," + pinId);
	}

	public CnxPointModule getCnxPointModule(int subModId, int pinId) {
		return ((CnxPointModule) subModxpin2cp.get(subModId + "," + pinId));
	}

	public CnxPoint getCnxPoint(int id) {
		return ((CnxPoint) id2cnxpt.get(id + ""));
	}

	public ArrayList getCnxPoints(Equipotential equi) {
		ArrayList list = (ArrayList) equi2cnxpt.get(equi);
		if (list == null) list = new ArrayList();
		equi2cnxpt.put(equi, list);
		return list;
	}

	public void setCnxPointLocation(CnxPoint cnxPoint, Point2D loc) {
		double x = 0.;
		double y = 0.;
		if (Module.GRID) {
			x = ((int) Math.round(loc.getX() / Module.GRIDSTEP)) * Module.GRIDSTEP;
			y = ((int) Math.round(loc.getY() / Module.GRIDSTEP)) * Module.GRIDSTEP;
		} else {
			x = loc.getX();
			y = loc.getY();
		}
		Point2D location = cnxPoint.getLocation();
		location.setLocation(x, y);
	}

	// when <cnxPoint> already belongs to equipotential <equi>, do nothing
	// when <cnxPoint> belongs to no equipotential (module pins before being connected) set it
	// when <cnxPoint> belongs to an equipotential <eq> different than <equi>, change to <equi>

	public void setEquipotential(CnxPoint cnxPoint, Equipotential equi) {
		Equipotential eq = cnxPoint.getEquipotential();
		if (eq == null) {
			cnxPoint.setEquipotential(equi);
			ArrayList newList = (ArrayList) equi2cnxpt.get(equi);
			if (newList == null) { newList = new ArrayList(); equi2cnxpt.put(equi, newList); }
			newList.add(cnxPoint);
		} else if (! eq.equals(equi)) {
			cnxPoint.setEquipotential(equi);
			ArrayList oldList = (ArrayList) equi2cnxpt.get(eq);
			oldList.remove(cnxPoint);
			ArrayList newList = (ArrayList) equi2cnxpt.get(equi);
			if (newList == null) { newList = new ArrayList(); equi2cnxpt.put(equi, newList); }
			if (!newList.contains(cnxPoint)) newList.add(cnxPoint);
		}
	}

	// an intermediate connection point is a junction when it is part of 3 connections or more
	// a module connection point is a junction when it is part of 2 connections or more
	public boolean isJunction(CnxPoint cnxPoint) {
		ArrayList list = (ArrayList) cnxpt2cnx.get(cnxPoint);
		if (list == null) return false;
		if (list.size() > 2) return true;
		if ((cnxPoint instanceof CnxPointModule) && (list.size() > 1)) return true;
		return false;
	}

	//////////////////////          Connections            /////////////////////

	public ArrayList getCnxList() {
		return cnxList;
	}

	public void addCnx(Cnx cnx) {
		id2cnx.put(cnx.getCp1().getId() + "," + cnx.getCp2().getId(), cnx);
		cnxList.add(cnx);
		ArrayList list = (ArrayList) cnxpt2cnx.get(cnx.getCp1());
		if (list == null) { list = new ArrayList(); cnxpt2cnx.put(cnx.getCp1(), list); }
		if (!list.contains(cnx)) list.add(cnx);
		list = (ArrayList) cnxpt2cnx.get(cnx.getCp2());
		if (list == null) { list = new ArrayList(); cnxpt2cnx.put(cnx.getCp2(), list); }
		if (!list.contains(cnx)) list.add(cnx);
	}

	public void delCnx(Cnx cnx) {
		id2cnx.remove(cnx.getCp1().getId() + "," + cnx.getCp2().getId());
		cnxList.remove(cnx);
		ArrayList list = (ArrayList) cnxpt2cnx.get(cnx.getCp1());
		if (list == null) { list = new ArrayList(); cnxpt2cnx.put(cnx.getCp1(), list); }
		if (list.contains(cnx)) list.remove(cnx);
		list = (ArrayList) cnxpt2cnx.get(cnx.getCp2());
		if (list == null) { list = new ArrayList(); cnxpt2cnx.put(cnx.getCp2(), list); }
		if (list.contains(cnx)) list.remove(cnx);
	}

	public Cnx getCnx(int idCp1, int idCp2) {
		return ((Cnx) id2cnx.get(idCp1 + "," + idCp2));
	}

	public ArrayList getCnxFrom(CnxPoint cp) {
		return (ArrayList) cnxpt2cnx.get(cp);
	}

	//////////////////////          Labels            /////////////////////

	public ArrayList getLabelList() {
		return labelList;
	}

	public EquiLabel getEquiLabel(CnxPoint cp) {
		return ((EquiLabel) cnxpt2equiLabel.get(cp));
	}

	public boolean isEquiLabelVisible(CnxPoint cp) {
		return (cnxpt2equiLabel.get(cp) != null);
	}

	public void addEquiLabel(CnxPoint cp) {
		EquiLabel label = new EquiLabel(cp, new Point2D.Double(5., -20.));
		labelList.add(label);
		cnxpt2equiLabel.put(label.getCnxPoint(), label);
	}

	public void delEquiLabel(CnxPoint cp) {
		EquiLabel label = getEquiLabel(cp);
		labelList.remove(label);
		cnxpt2equiLabel.remove(label.getCnxPoint());
	}


	public ArityLabel getArityLabel(int subModId) {
		return ((ArityLabel) subMod2nbInstLabel.get(subModId + ""));
	}

	public boolean isArityLabelVisible(int subModId) {
		return (subMod2nbInstLabel.get(subModId + "") != null);
	}

	public void addArityLabel(int subModId) {
		ArityLabel label = new ArityLabel(this, subModId, new Point2D.Double(20., 20.));
		labelList.add(label);
		subMod2nbInstLabel.put(subModId + "", label);
	}

	public void delArityLabel(int subModId) {
		ArityLabel label = getArityLabel(subModId);
		labelList.remove(label);
		subMod2nbInstLabel.remove(subModId + "");
	}


	public WidthLabel getWidthLabel(CnxPoint cp) {
		return ((WidthLabel) cnxpt2widthLabel.get(cp));
	}

	public boolean isWidthLabelVisible(CnxPoint cp) {
		return (cnxpt2widthLabel.get(cp) != null);
	}

	public void addWidthLabel(CnxPoint cp) {
		WidthLabel label = new WidthLabel(cp, new Point2D.Double(5., -20.));
		labelList.add(label);
		cnxpt2widthLabel.put(label.getCnxPoint(), label);
	}

	public void delWidthLabel(CnxPoint cp) {
		WidthLabel label = getWidthLabel(cp);
		labelList.remove(label);
		cnxpt2widthLabel.remove(label.getCnxPoint());
	}

	public UnivGateLabel getUnivGateLabel(int ugId) {
		return ((UnivGateLabel) univGate2Label.get(ugId + ""));
	}

	public boolean isUnivGateLabelVisible(int ugId) {
		return (univGate2Label.get(ugId + "") != null);
	}

	public void addUnivGateLabel(UniversalGate ug) {
		UnivGateLabel label = new UnivGateLabel(this, ug, new Point2D.Double(30., -30.));
		labelList.add(label);
		univGate2Label.put(ug.getId() + "", label);
	}

	public void delUnivGateLabel(UniversalGate ug) {
		UnivGateLabel label = getUnivGateLabel(ug.getId());
		labelList.remove(label);
		univGate2Label.remove(ug.getId() + "");
	}

	public ArrayList getExtPinLabelList() {
		return extPinLabelList;
	}

	public ExtPinLabel getExtPinLabel(CnxPointPin cp) {
		return ((ExtPinLabel) cnxpt2extPinLabel.get(cp));
	}

	public boolean isExtPinLabelVisible(CnxPointPin cp) {
		return (cnxpt2extPinLabel.get(cp) != null);
	}

	public void addExtPinLabel(CnxPointPin cp) {
		ExtPinLabel label = new ExtPinLabel(cp, new Point2D.Double(5., -15.));
		extPinLabelList.add(label);
		cnxpt2extPinLabel.put(label.getCnxPoint(), label);
	}

	public void delExtPinLabel(CnxPointPin cp) {
		ExtPinLabel label = getExtPinLabel(cp);
		extPinLabelList.remove(label);
		cnxpt2extPinLabel.remove(label.getCnxPoint());
	}

	public void addModuleNameLabel(int index) {
		switch (index) {
			case 0: moduleNameLabel1 = new ModuleNameLabel("to edit...", new Point2D.Double(0., 0.)); break;
			case 1: moduleNameLabel2 = new ModuleNameLabel("to edit...", new Point2D.Double(0., 0.)); break;
		}
	}

	public void delModuleNameLabel(int index) {
		switch (index) {
			case 0: moduleNameLabel1 = null; break;
			case 1: moduleNameLabel2 = null; break;
		}
	}

	public ModuleNameLabel getModuleNameLabel(int index) {
		switch (index) {
			case 0: return moduleNameLabel1;
			case 1: return moduleNameLabel2;
		}
		return null;
	}

	public void editModuleNameLabel(int index, String text) {
		switch (index) {
			case 0: moduleNameLabel1.setText(text); break;
			case 1: moduleNameLabel2.setText(text); break;
		}
	}

	public boolean isModuleNameLabelVisible(int index) {
		switch (index) {
			case 0: return (moduleNameLabel1 != null);
			case 1: return (moduleNameLabel2 != null);
		}
		return false;
	}

	//////////////////////////////     INTERFACE     ///////////////////////////////

	void outlineMoveNode(int index, boolean ctrlNode, double x, double y) {
		GeneralPath newPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		PathIterator pathIt = getBodyPath().getPathIterator(AFFINE_ID);
		double res[] = new double[6];
		int indx = -1;
		while (!pathIt.isDone()) {
			int type = pathIt.currentSegment(res);
			indx += 1;
			switch (type) {
				case PathIterator.SEG_MOVETO:
					if (indx == index)
						newPath.moveTo((float) x, (float) y);
					else
						newPath.moveTo((float) res[0], (float) res[1]);
					break;
				case PathIterator.SEG_LINETO:
					if (indx == index)
						newPath.lineTo((float) x, (float) y);
					else
						newPath.lineTo((float) res[0], (float) res[1]);
					break;
				case PathIterator.SEG_QUADTO:
					if (indx == index) {
						if (ctrlNode)
							newPath.quadTo((float) x, (float) y, (float) res[2], (float) res[3]);
						else
							newPath.quadTo((float) res[0], (float) res[1], (float) x, (float) y);
					} else
						newPath.quadTo((float) res[0], (float) res[1], (float) res[2], (float) res[3]);
					break;
				case PathIterator.SEG_CLOSE:
					newPath.closePath();
					break;
			}
			pathIt.next();
		}
		setBodyPath(newPath);
	}

	void outlineDelNode(int segIndex) {
		GeneralPath newPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		PathIterator pathIt = getBodyPath().getPathIterator(AFFINE_ID);
		double res[] = new double[6];
		int indx = -1;
		while (!pathIt.isDone()) {
			int type = pathIt.currentSegment(res);
			indx += 1;
			switch (type) {
				case PathIterator.SEG_MOVETO:
					newPath.moveTo((float) res[0], (float) res[1]);
					break;
				case PathIterator.SEG_LINETO:
					if (indx != segIndex)
						newPath.lineTo((float) res[0], (float) res[1]);
					break;
				case PathIterator.SEG_QUADTO:
					if (indx != segIndex)
						newPath.quadTo((float) res[0], (float) res[1], (float) res[2], (float) res[3]);
					break;
				case PathIterator.SEG_CLOSE:
					newPath.closePath();
					break;
			}
			pathIt.next();
		}
		setBodyPath(newPath);
	}

	void outlineAddLineAfter(int segIndex, double x, double y) {
		GeneralPath newPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		PathIterator pathIt = getBodyPath().getPathIterator(AFFINE_ID);
		double res[] = new double[6];
		int indx = -1;
		while (!pathIt.isDone()) {
			int type = pathIt.currentSegment(res);
			indx += 1;
			switch (type) {
				case PathIterator.SEG_MOVETO:
					if (indx != segIndex)
						newPath.moveTo((float) res[0], (float) res[1]);
					else {
						newPath.moveTo((float) res[0], (float) res[1]);
						newPath.lineTo((float) x, (float) y);
					}
					break;
				case PathIterator.SEG_LINETO:
					if (indx != segIndex)
						newPath.lineTo((float) res[0], (float) res[1]);
					else {
						newPath.lineTo((float) res[0], (float) res[1]);
						newPath.lineTo((float) x, (float) y);
					}
					break;
				case PathIterator.SEG_QUADTO:
					if (indx != segIndex)
						newPath.quadTo((float) res[0], (float) res[1], (float) res[2], (float) res[3]);
					else {
						newPath.quadTo((float) res[0], (float) res[1], (float) res[2], (float) res[3]);
						newPath.lineTo((float) x, (float) y);
					}
					break;
				case PathIterator.SEG_CLOSE:
					newPath.closePath();
					break;
			}
			pathIt.next();
		}
		setBodyPath(newPath);
	}

	void outlineAddQuadAfter(int segIndex, double x, double y) {
		GeneralPath newPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
		PathIterator pathIt = getBodyPath().getPathIterator(AFFINE_ID);
		double res[] = new double[6];
		int indx = -1;
		while (!pathIt.isDone()) {
			int type = pathIt.currentSegment(res);
			indx += 1;
			switch (type) {
				case PathIterator.SEG_MOVETO:
					if (indx != segIndex)
						newPath.moveTo((float) res[0], (float) res[1]);
					else {
						newPath.moveTo((float) res[0], (float) res[1]);
						newPath.quadTo((float) x, (float) y, (float) (x + 10.), (float) (y + 10.));
					}
					break;
				case PathIterator.SEG_LINETO:
					if (indx != segIndex)
						newPath.lineTo((float) res[0], (float) res[1]);
					else {
						newPath.lineTo((float) res[0], (float) res[1]);
						newPath.quadTo((float) (res[0] + 10.), (float) (res[1] + 10.), (float) (res[0] + 20.), (float) (res[1] + 20.));
					}
					break;
				case PathIterator.SEG_QUADTO:
					if (indx != segIndex)
						newPath.quadTo((float) res[0], (float) res[1], (float) res[2], (float) res[3]);
					else {
						newPath.quadTo((float) res[0], (float) res[1], (float) res[2], (float) res[3]);
						newPath.quadTo((float) x, (float) y, (float) (x + 10.), (float) (y + 10.));
					}
					break;
				case PathIterator.SEG_CLOSE:
					newPath.closePath();
					break;
			}
			pathIt.next();
		}
		setBodyPath(newPath);
	}


	//////////////////       DO / UNDO / REDO         ///////////////////

	private ArrayList cmdStack = new ArrayList();
	private int stackIndex = 0;

	public void saveStack(BufferedWriter bw) throws Exception {
		for (int i = 0; i < stackIndex; i++) {
			String[] cmds = (String[]) cmdStack.get(i);
			for (int j = 0; j < cmds.length; j++) {
				bw.write(cmds[j]);
				bw.newLine();
			}
			bw.newLine();
		}
		bw.flush();
	}

	public void loadStack(BufferedReader br) throws Exception {
		if (!isEmpty()) return;
		ArrayList cmdList = new ArrayList();
		while (true) {
			String line = br.readLine();
			if ((line == null) || line.equals("")) {
				if (cmdList.size() == 0) break;
				// end of commands group
				String[] cmds = (String[]) cmdList.toArray(new String[0]);
				do_(cmds);
				cmdList = new ArrayList();
			} else {
				cmdList.add(line);
			}
			if (line == null) break;
		}
	}

	public void do_(String[] cmds) {
		// remove elements from stackIndex (included) to the end 
		int n = cmdStack.size() - stackIndex;
		for (int i = 0; i < n; i++) {
			cmdStack.remove(cmdStack.size() - 1);
		}
		cmdStack.add(cmds);
		stackIndex = cmdStack.size();
		do_undo(cmds, false);
	}

	public void undo() {
		if (stackIndex <= 0) return;
		stackIndex -= 1;
		String[] cmds = (String[]) cmdStack.get(stackIndex);
		do_undo(cmds, true);
	}

	public void redo() {
		if (stackIndex >= cmdStack.size()) return;
		String[] cmds = (String[]) cmdStack.get(stackIndex);
		stackIndex += 1;
		do_undo(cmds, false);
	}

	public boolean isUndoPossible() {
		return (stackIndex <= 0);
	}

	public boolean isRedoPossible() {
		return (stackIndex >= cmdStack.size());
	}

	// merge the last two groups of commands
	// (used when adding built-in and compound modules)
	//
	public void mergeLastTwo() {
		String[] beforeLast = (String[]) cmdStack.get(stackIndex - 2);
		String[] last = (String[]) cmdStack.get(stackIndex - 1);
		String[] grouped = new String[beforeLast.length + last.length];
		int idx = 0;
		for (int i = 0; i < beforeLast.length; i++) {
			grouped[idx++] = beforeLast[i];
		}
		for (int i = 0; i < last.length; i++) {
			grouped[idx++] = last[i];
		}
		cmdStack.set(stackIndex - 2, grouped);
		cmdStack.remove(stackIndex - 1);
		stackIndex -= 1;
	}

	private void do_undo(String[] cmds, boolean undo) {
		for (int i = 0; i < cmds.length; i++) {
			String cmd = null;
			if (undo) cmd = cmds[cmds.length - i - 1]; else cmd = cmds[i];
System.out.println("cmd=" + cmd);
			StringTokenizer st = new StringTokenizer(cmd, "\t");
			String op = st.nextToken();

			if (op.equals("moveModule")) {
				int subModId = Integer.parseInt(st.nextToken());
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				if (undo)
					moveSubModule(subModId, px, py);
				else
					moveSubModule(subModId, nx, ny);

			} else if (op.equals("setArity")) {
				int subModId = Integer.parseInt(st.nextToken());
				NumExpr oldArity = NumExpr.parse(st.nextToken());
				NumExpr newArity = NumExpr.parse(st.nextToken());
				Module subMod = getSubModule(subModId);
				if (undo)
					subMod.setArity(oldArity);
				else
					subMod.setArity(newArity);

			} else if (op.equals("addEquipotential")) {
				int equiId = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				NumExpr width = NumExpr.parse(st.nextToken());
				if (undo) {
					Equipotential equi = getEqui(equiId);
					delEquipotential(equi);
				} else {
					Equipotential equi = new Equipotential(equiId, name, width);
					addEquipotential(equi);
				}

			} else if (op.equals("delEquipotential")) {
				int equiId = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				NumExpr width = NumExpr.parse(st.nextToken());
				if (undo) {
					Equipotential equi = new Equipotential(equiId, name, width);
					addEquipotential(equi);
				} else {
					Equipotential equi = getEqui(equiId);
					delEquipotential(equi);
				}

			} else if (op.equals("setEquipotential")) {
				int cpId = Integer.parseInt(st.nextToken());
				int prevEquiId = Integer.parseInt(st.nextToken());
				int newEquiId = Integer.parseInt(st.nextToken());
				CnxPoint cp = getCnxPoint(cpId);
				Equipotential newEqui = getEqui(newEquiId);
				Equipotential prevEqui = getEqui(prevEquiId);
				if (undo) {
					setEquipotential(cp, prevEqui);
				} else {
					setEquipotential(cp, newEqui);
				}

			} else if (op.equals("setEquipotentialName")) {
				int equiId = Integer.parseInt(st.nextToken());
				String prevName = st.nextToken();
				String newName = st.nextToken();
				Equipotential equi = getEqui(equiId);
				if (undo) {
					equi.setName(prevName);
				} else {
					equi.setName(newName);
				}

			} else if (op.equals("addCnxPointInter")) {
				int id = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				if (undo) {
					CnxPoint cp = getCnxPoint(id);
					delCnxPoint(cp);
				} else {
					CnxPointInter cpi = new CnxPointInter(id, new Point2D.Double(x, y));
					addCnxPoint(cpi);
				}

			} else if (op.equals("delCnxPointInter")) {
				int id = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				if (undo) {
					CnxPointInter cpi = new CnxPointInter(id, new Point2D.Double(x, y));
					addCnxPoint(cpi);
				} else {
					CnxPoint cp = getCnxPoint(id);
					delCnxPoint(cp);
				}

			} else if (op.equals("addCnxPointModule")) {
				int id = Integer.parseInt(st.nextToken());
				int subModId = Integer.parseInt(st.nextToken());
				int pinId = Integer.parseInt(st.nextToken());
				if (undo) {
					CnxPoint cp = getCnxPoint(id);
					delCnxPointModule((CnxPointModule)cp, subModId, pinId);
				} else {
					CnxPointModule cpm = new CnxPointModule(id, this, subModId, pinId);
					addCnxPointModule(cpm, subModId, pinId);
				}

			} else if (op.equals("delCnxPointModule")) {
				int id = Integer.parseInt(st.nextToken());
				int subModId = Integer.parseInt(st.nextToken());
				int pinId = Integer.parseInt(st.nextToken());
				if (undo) {
					CnxPointModule cpm = new CnxPointModule(id, this, subModId, pinId);
					addCnxPointModule(cpm, subModId, pinId);
				} else {
					CnxPoint cp = getCnxPoint(id);
					delCnxPointModule((CnxPointModule)cp, subModId, pinId);
				}

			} else if (op.equals("addPin")) {
				int pinId = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				double xi = Double.parseDouble(st.nextToken());
				double yi = Double.parseDouble(st.nextToken());

				if (undo) {
					CnxPoint cp = getCnxPoint(pinId);
					delCnxPoint(cp);
					pinIdList.remove(new Integer(pinId));
				} else {
					// add pin on the design inside
					CnxPointPin pin = new CnxPointPin(pinId, new Point2D.Double(x, y), new Point2D.Double(xi, yi));
					addCnxPoint(pin);
					// add pin on the design outside
					pin.setExtLocation(new Point2D.Double(xi, yi));
					pinIdList.add(new Integer(pinId));
				}

			} else if (op.equals("moveCnxPoint")) {
				int id = Integer.parseInt(st.nextToken());
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				CnxPoint cp = getCnxPoint(id);
				if (undo)
					moveCnxPoint(cp, px, py);
				else
					moveCnxPoint(cp, nx, ny);

			} else if (op.equals("addCnx")) {
				int idCp1 = Integer.parseInt(st.nextToken());
				int idCp2 = Integer.parseInt(st.nextToken());
				CnxPoint cp1 = getCnxPoint(idCp1);
				CnxPoint cp2 = getCnxPoint(idCp2);
				if (undo) {
					Cnx cnx = getCnx(idCp1, idCp2);
					delCnx(cnx);
				} else {
					Cnx cnx = new Cnx(cp1, cp2);
					addCnx(cnx);
				}

			} else if (op.equals("delCnx")) {
				int idCp1 = Integer.parseInt(st.nextToken());
				int idCp2 = Integer.parseInt(st.nextToken());
				CnxPoint cp1 = getCnxPoint(idCp1);
				CnxPoint cp2 = getCnxPoint(idCp2);
				if (undo) {
					Cnx cnx = new Cnx(cp1, cp2);
					addCnx(cnx);
				} else {
					Cnx cnx = getCnx(idCp1, idCp2);
					delCnx(cnx);
				}

			} else if (op.equals("setShape")) {
				int subModId = Integer.parseInt(st.nextToken());
				int prevShapeIndex = Integer.parseInt(st.nextToken());
				int nextShapeIndex = Integer.parseInt(st.nextToken());
				Module mod = getSubModule(subModId);
				if (undo)
					mod.setShape(prevShapeIndex);
				else
					mod.setShape(nextShapeIndex);

			} else if (op.equals("addEquiLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPoint cp = getCnxPoint(idCp);
				if (undo)
					delEquiLabel(cp);
				else
					addEquiLabel(cp);

			} else if (op.equals("delEquiLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPoint cp = getCnxPoint(idCp);
				if (undo)
					addEquiLabel(cp);
				else
					delEquiLabel(cp);

			} else if (op.equals("moveEquiLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPoint cp = getCnxPoint(idCp);
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				EquiLabel label = getEquiLabel(cp);
				if (undo) {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(px - anchorLoc.getX(), py - anchorLoc.getY());
				} else {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(nx - anchorLoc.getX(), ny - anchorLoc.getY());
				}

			} else if (op.equals("addExtPinLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPointPin cp = (CnxPointPin) getCnxPoint(idCp);
				if (undo)
					delExtPinLabel(cp);
				else
					addExtPinLabel(cp);

			} else if (op.equals("delExtPinLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPointPin cp = (CnxPointPin) getCnxPoint(idCp);
				if (undo)
					addExtPinLabel(cp);
				else
					delExtPinLabel(cp);

			} else if (op.equals("moveExtPinLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPointPin cp = (CnxPointPin) getCnxPoint(idCp);
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				ExtPinLabel label = getExtPinLabel(cp);
				if (undo) {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(px - anchorLoc.getX(), py - anchorLoc.getY());
				} else {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(nx - anchorLoc.getX(), ny - anchorLoc.getY());
				}

			} else if (op.equals("addModuleNameLabel")) {
				int index = Integer.parseInt(st.nextToken());
				if (undo)
					delModuleNameLabel(index);
				else
					addModuleNameLabel(index);

			} else if (op.equals("delModuleNameLabel")) {
				int index = Integer.parseInt(st.nextToken());
				if (undo)
					addModuleNameLabel(index);
				else
					delModuleNameLabel(index);

			} else if (op.equals("editModuleNameLabel")) {
				int index = Integer.parseInt(st.nextToken());
				String prevText = st.nextToken();
				String newText = st.nextToken();
				if (undo)
					editModuleNameLabel(index, prevText);
				else
					editModuleNameLabel(index, newText);

			} else if (op.equals("moveModuleNameLabel")) {
				int index = Integer.parseInt(st.nextToken());
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				ModuleNameLabel label = getModuleNameLabel(index);
				Point2D loc = label.getRelativeLocation();
				if (undo) {
					loc.setLocation(px, py);
				} else {
					loc.setLocation(nx, ny);
				}

			} else if (op.equals("addArityLabel")) {
				int subModId = Integer.parseInt(st.nextToken());
				if (undo)
					delArityLabel(subModId);
				else
					addArityLabel(subModId);

			} else if (op.equals("delArityLabel")) {
				int subModId = Integer.parseInt(st.nextToken());
				if (undo)
					addArityLabel(subModId);
				else
					delArityLabel(subModId);

			} else if (op.equals("moveArityLabel")) {
				int subModId = Integer.parseInt(st.nextToken());
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				ArityLabel label = getArityLabel(subModId);
				if (undo) {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(px - anchorLoc.getX(), py - anchorLoc.getY());
				} else {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(nx - anchorLoc.getX(), ny - anchorLoc.getY());
				}

			} else if (op.equals("addWidthLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPoint cp = getCnxPoint(idCp);
				if (undo)
					delWidthLabel(cp);
				else
					addWidthLabel(cp);

			} else if (op.equals("delWidthLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPoint cp = getCnxPoint(idCp);
				if (undo)
					addWidthLabel(cp);
				else
					delWidthLabel(cp);

			} else if (op.equals("moveWidthLabel")) {
				int idCp = Integer.parseInt(st.nextToken());
				CnxPoint cp = getCnxPoint(idCp);
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				WidthLabel label = getWidthLabel(cp);
				if (undo) {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(px - anchorLoc.getX(), py - anchorLoc.getY());
				} else {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(nx - anchorLoc.getX(), ny - anchorLoc.getY());
				}

			} else if (op.equals("addUnivGateLabel")) {
				int id = Integer.parseInt(st.nextToken());
				UniversalGate ug = (UniversalGate) getSubModule(id);
				if (undo)
					delUnivGateLabel(ug);
				else
					addUnivGateLabel(ug);

			} else if (op.equals("delUnivGateLabel")) {
				int id = Integer.parseInt(st.nextToken());
				UniversalGate ug = (UniversalGate) getSubModule(id);
				if (undo)
					addUnivGateLabel(ug);
				else
					delUnivGateLabel(ug);

			} else if (op.equals("moveUnivGateLabel")) {
				int id = Integer.parseInt(st.nextToken());
				UnivGateLabel label = getUnivGateLabel(id);
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				if (undo) {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(px - anchorLoc.getX(), py - anchorLoc.getY());
				} else {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(nx - anchorLoc.getX(), ny - anchorLoc.getY());
				}

			} else if (op.equals("addBuiltInModule")) {
				int subModId = Integer.parseInt(st.nextToken());
				String type = st.nextToken();
				NumExpr nbInstance = NumExpr.parse(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				if (undo) {
					Module subMod = getSubModule(subModId);
					delSubModule(subMod);
				} else {
					Module bm = null;
					if (type.equals("buff")) {
						bm = new BuffNotModule(subModId, nbInstance, false);
					} else if (type.equals("not")) {
						bm = new BuffNotModule(subModId, nbInstance, true);
					} else if (type.equals("buff3")) {
						bm = new Buff3StateModule(subModId, nbInstance, false);
					} else if (type.equals("buff3Inv")) {
						bm = new Buff3StateModule(subModId, nbInstance, true);
					} else if (type.equals("and2")) {
						bm = new AndNand2Module(subModId, nbInstance, false);
					} else if (type.equals("and2bis")) {
						bm = new AndNand2BisModule(subModId, nbInstance, false);
					} else if (type.equals("and3")) {
						bm = new AndNand3Module(subModId, nbInstance, false);
					} else if (type.equals("nand2")) {
						bm = new AndNand2Module(subModId, nbInstance, true);
					} else if (type.equals("nand3")) {
						bm = new AndNand3Module(subModId, nbInstance, true);
					} else if (type.equals("or2")) {
						bm = new OrNor2Module(subModId, nbInstance, false);
					} else if (type.equals("or3")) {
						bm = new OrNor3Module(subModId, nbInstance, false);
					} else if (type.equals("or1")) {
						bm = new OrNor1Module(subModId, nbInstance, false);
					} else if (type.equals("nor2")) {
					} else if (type.equals("nor2")) {
						bm = new OrNor2Module(subModId, nbInstance, true);
					} else if (type.equals("nor3")) {
						bm = new OrNor3Module(subModId, nbInstance, true);
					} else if (type.equals("xor2")) {
						bm = new Xor2Module(subModId, nbInstance);
					} else if (type.equals("xor2bis")) {
						bm = new Xor2BisModule(subModId, nbInstance);
					} else if (type.equals("xor3")) {
						bm = new Xor3Module(subModId, nbInstance);
					} else if (type.equals("fork")) {
						bm = new ForkModule(subModId, nbInstance);
					} else if (type.equals("flipflopD")) {
						bm = new FlipFlopDModule(subModId, nbInstance);
					}
					addSubModule(bm, new Point2D.Double(x, y));
				}

			} else if (op.equals("delBuiltInModule")) {
				int subModId = Integer.parseInt(st.nextToken());
				String type = st.nextToken();
				NumExpr nbInstance = NumExpr.parse(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				if (undo) {
					Module bm = null;
					if (type.equals("buff3")) {
						bm = new Buff3StateModule(subModId, nbInstance, false);
					} else if (type.equals("buff3Inv")) {
						bm = new Buff3StateModule(subModId, nbInstance, true);
					}
					addSubModule(bm, new Point2D.Double(x, y));
				} else {
					Module subMod = getSubModule(subModId);
					delSubModule(subMod);
				}

			} else if (op.equals("outlineMoveNode")) {
				int index = Integer.parseInt(st.nextToken());
				boolean ctrlNode = Boolean.parseBoolean(st.nextToken());
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());

				if (undo) {
					outlineMoveNode(index, ctrlNode, px, py);
				} else {
					outlineMoveNode(index, ctrlNode, nx, ny);
				}

			} else if (op.equals("outlineAddLineAfter")) {
				int segIndex = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());

				if (undo) {
					outlineDelNode(segIndex + 1);
				} else {
					outlineAddLineAfter(segIndex, x, y);
				}

			} else if (op.equals("outlineDelLine")) {
				int segIndex = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());

				if (undo) {
					outlineAddLineAfter(segIndex, x, y);
				} else {
					outlineDelNode(segIndex);
				}

			} else if (op.equals("outlineAddQuadAfter")) {
				int segIndex = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				if (undo) {
					outlineDelNode(segIndex + 1);
				} else {
					outlineAddQuadAfter(segIndex, x, y);
				}

			} else if (op.equals("addModule")) {
				int subModId = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				if (undo) {
					Module subMod = getSubModule(subModId);
					delSubModule(subMod);
				} else {
					CompoundModule mod = new CompoundModule(subModId, new NumExprVal(1));
					try {
						BufferedReader br = new BufferedReader(new FileReader(name + ".sch"));
						mod.loadStack(br);
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					// add module
					addSubModule(mod, new Point2D.Double(x, y));
					mod.setModuleName(name);
				}

			} else if (op.equals("setOrientation")) {
				int pinId = Integer.parseInt(st.nextToken());
				int prevOrientation= Integer.parseInt(st.nextToken());
				int newOrientation= Integer.parseInt(st.nextToken());
				CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
				if (undo) {
					pin.setOrientation(prevOrientation);
				} else {
					pin.setOrientation(newOrientation);
				}

			} else if (op.equals("setInverted")) {
				int pinId = Integer.parseInt(st.nextToken());
				boolean prevInverted = Boolean.parseBoolean(st.nextToken());
				boolean newInverted = Boolean.parseBoolean(st.nextToken());
				CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
				if (undo) {
					pin.setInverted(prevInverted);
				} else {
					pin.setInverted(newInverted);
				}

			} else if (op.equals("setClocked")) {
				int pinId = Integer.parseInt(st.nextToken());
				boolean prevClocked = Boolean.parseBoolean(st.nextToken());
				boolean newClocked = Boolean.parseBoolean(st.nextToken());
				CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
				if (undo) {
					pin.setClocked(prevClocked);
				} else {
					pin.setClocked(newClocked);
				}

			} else if (op.equals("extPinMove")) {
				int pinId = Integer.parseInt(st.nextToken());
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());

				CnxPointPin pin = (CnxPointPin) getCnxPoint(pinId);
				if (undo) {
					pin.setExtLocation(new Point2D.Double(px, py));
				} else {
					pin.setExtLocation(new Point2D.Double(nx, ny));
				}

			} else if (op.equals("addUniversalGate")) {
				int gateId = Integer.parseInt(st.nextToken());
				int nbInput = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());

				if (undo) {
					Module subMod = getSubModule(gateId);
					delSubModule(subMod);
				} else {
					UniversalGate gate = new UniversalGate(gateId, new NumExprVal(1), nbInput);
					addSubModule(gate, new Point2D.Double(x, y));
				}

			} else if (op.equals("setUnivGateEquation")) {
				int ugId = Integer.parseInt(st.nextToken());
				UnivGateLabel label = getUnivGateLabel(ugId);
				String prevTxt = st.nextToken();
				String newTxt = st.nextToken();

				if (undo) {
					label.setText(prevTxt);
				} else {
					label.setText(newTxt);
				}

			} else if (op.equals("moveUnivGateLabel")) {
				int ugId = Integer.parseInt(st.nextToken());
				double px = Double.parseDouble(st.nextToken());
				double py = Double.parseDouble(st.nextToken());
				double nx = Double.parseDouble(st.nextToken());
				double ny = Double.parseDouble(st.nextToken());
				UnivGateLabel label = getUnivGateLabel(ugId);
				if (undo) {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(px - anchorLoc.getX(), py - anchorLoc.getY());
				} else {
					Point2D anchorLoc = label.getAnchorLocation();
					label.setRelativeLocation(nx - anchorLoc.getX(), ny - anchorLoc.getY());
				}
			}
		}
	}

}

