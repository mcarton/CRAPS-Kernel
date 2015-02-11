
package org.jcb.shdl;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.print.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import org.jcb.shdl.*;


public class ModuleDesignPanel extends ModulePanel implements FileFilter {

	private ModuleInterfacePanel interfacePanel;
	private ResourceBundle labels;

	// mouse automaton (MEALY model)
	private int state = 0;

	private ModuleDesignPanel mainPanel;

	// group selection
	private ArrayList selected = new ArrayList();
	private ArrayList selectedCnxPoint = new ArrayList();
	private double startX = 0;
	private double startY = 0;
	private double curX = 0;
	private double curY = 0;
	private double dX = 0;
	private double dY = 0;

	// connections
	private Cnx cnxHit;
	private CnxPoint cnxPointHit;
	private Point2D cnxCp1Loc;
	private Point2D cnxCp2Loc;

	private CnxPoint startCnxPoint;
	private CnxPoint endCnxPoint;

	// sub mmodules
	private int modHitId;
	private Point2D modHitLoc;

	// labels
	private ModuleLabel labelHit;
	private Point2D labelHitLoc;


	public ModuleDesignPanel(CompoundModule compModule, ModuleInterfacePanel interfacePanel, ResourceBundle labels) {
		super(compModule);
		this.interfacePanel = interfacePanel;
		this.labels = labels;
		mainPanel = this;
		addMouseListener(new MyMouseAdapter());
		addMouseMotionListener(new MyMouseMotionAdapter());
	}

	private Dimension DIM = new Dimension(2000, 2000);
	public Dimension getPreferredSize() {
		return DIM;
	}

	// Only unconnected interface pins (CnxPointPin) have a null equipotential
	//
	void pinConnection(CnxPoint startCnxPoint, CnxPoint endCnxPoint) {
		Equipotential startEqui = startCnxPoint.getEquipotential();
		Equipotential endEqui = endCnxPoint.getEquipotential();

		ArrayList cmdList = new ArrayList();

		if ((startEqui == null) && (endEqui == null)) {
			// connexion de deux pins d'interface non affectees : non permis
			JOptionPane.showMessageDialog(mainPanel, labels.getString("error1"));

		} else if ((startEqui != null) && (endEqui == null)) {
			cmdList.add("addCnx\t" + startCnxPoint.getId() + "\t" + endCnxPoint.getId());
			cmdList.add("setEquipotential\t" + endCnxPoint.getId() + "\t-1\t" + startEqui.getId());

		} else if ((startEqui == null) && (endEqui != null)) {
			cmdList.add("addCnx\t" + startCnxPoint.getId() + "\t" + endCnxPoint.getId());
			cmdList.add("setEquipotential\t" + startCnxPoint.getId() + "\t-1\t" + endEqui.getId());

		} else if ((startEqui != null) && (endEqui != null)) {
System.out.println("startEqui.getWidth=" + startEqui.getWidth() + ", endEqui.getWidth=" + endEqui.getWidth());
			if (!startEqui.getWidth().toString().equals(endEqui.getWidth().toString())) {
				JOptionPane.showMessageDialog(mainPanel, "Widths are different");
			} else {
				cmdList.add("addCnx\t" + startCnxPoint.getId() + "\t" + endCnxPoint.getId());
				if (!startEqui.equals(endEqui)) {
					// impose l'equipotentielle de <startCnxPoint> a tous les points de
					// meme equipotentielle que <endCnxPoint>
					ArrayList list = compModule.getCnxPoints(endEqui);
					for (int i = 0; i < list.size(); i++) {
						CnxPoint cp = (CnxPoint) list.get(i);
						cmdList.add("setEquipotential\t" + cp.getId() + "\t" + endEqui.getId() + "\t" + startEqui.getId());
					}
					cmdList.add("delEquipotential\t" + endEqui.getId() + "\t" + endEqui.getName() + "\t" + endEqui.getWidth());
				}
			}
		}
		String[] cmds = (String[]) cmdList.toArray(new String[0]);
		compModule.do_(cmds);
		repaint();
	}

	class SetEquiLabelVisibilityActionListener implements ActionListener {
		private CnxPoint cp;
		public SetEquiLabelVisibilityActionListener(CnxPoint cp) {
			this.cp = cp;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			boolean exists = compModule.isEquiLabelVisible(cp);
			if (exists)
				cmdList.add("delEquiLabel\t" + cp.getId());
			else
				cmdList.add("addEquiLabel\t" + cp.getId());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class setWidthLabelVisibilityActionListener implements ActionListener {
		private CnxPoint cp;
		public setWidthLabelVisibilityActionListener(CnxPoint cp) {
			this.cp = cp;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			boolean exists = compModule.isWidthLabelVisible(cp);
			//cmdList.add("setWidthLabelVisibility\t" + cp.getId() + "\t" + oldVal + "\t" + !oldVal);
			if (exists)
				cmdList.add("delWidthLabel\t" + cp.getId());
			else
				cmdList.add("addWidthLabel\t" + cp.getId());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class SetPinWidthActionListener implements ActionListener {
		private CnxPoint cp;
		public SetPinWidthActionListener(CnxPoint cp) {
			this.cp = cp;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList list = compModule.getCnxFrom(cp);
			if ((list != null) && (list.size() > 0)) {
				JOptionPane.showMessageDialog(mainPanel, "the pin must be disconnected");
				return;
			}

			// get associated equipotential
			Equipotential equi = cp.getEquipotential();

			NumExpr oldWidth = equi.getWidth();
			NumExpr newWidth = NumExpr.parse(JOptionPane.showInputDialog("Enter new width", oldWidth + ""));
			if (newWidth == null) return;


			ArrayList cmdList = new ArrayList();

			// disconnect pin from previous equipotential
			cmdList.add("setEquipotential\t" + cp.getId() + "\t" + equi.getId() + "\t-1");
			// delete previous equipotential
			cmdList.add("delEquipotential\t" + equi.getId() + "\t" + equi.getName() + "\t" + oldWidth);
			// add another with the new width and the same name (since it is not used elsewhere)
			cmdList.add("addEquipotential\t" + equi.getId() + "\t" + equi.getName() + "\t" + newWidth);
			// connect pin to it
			cmdList.add("setEquipotential\t" + cp.getId() + "\t-1\t" + equi.getId());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
		}
	}


	///////////////////////      POPUP MENUS      ///////////////////////

	JPopupMenu getModulePopupMenu(CompoundModule compMod, int subModId) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem mc1 = new JMenuItem(labels.getString("copy"));
		mc1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		mc1.addActionListener(new CopyActionListener(subModId));
		popup.add(mc1);
		JMenuItem md = new JMenuItem("delete");
		md.addActionListener(new DeleteSubModuleActionListener(subModId));
		popup.add(md);
		popup.addSeparator();
		JCheckBoxMenuItem mi1 = new JCheckBoxMenuItem("visible arity");
		mi1.setState(compModule.isArityLabelVisible(subModId));
		mi1.addActionListener(new SetArityLabelVisibleActionListener(subModId));
		popup.add(mi1);
		JMenuItem mi2 = new JMenuItem("set arity");
		mi2.addActionListener(new SetArityActionListener(subModId));
		popup.add(mi2);
		return popup;
	}

	JPopupMenu getCnxPointPopupMenu(CnxPoint cp) {
		JPopupMenu popup = new JPopupMenu();
		JCheckBoxMenuItem mi1 = new JCheckBoxMenuItem("visible equipotential label");
		mi1.setState(compModule.getEquiLabel(cp) != null);
		mi1.addActionListener(new SetEquiLabelVisibilityActionListener(cp));
		popup.add(mi1);
		JCheckBoxMenuItem mi3 = new JCheckBoxMenuItem("visible width label");
		mi3.setState(compModule.isWidthLabelVisible(cp));
		mi3.addActionListener(new setWidthLabelVisibilityActionListener(cp));
		popup.add(mi3);
		if (cp instanceof CnxPointPin) {
			JMenuItem mi4 = new JMenuItem("set width");
			mi4.addActionListener(new SetPinWidthActionListener(cp));
			popup.add(mi4);
		} else if (cp instanceof CnxPointModule) {
			Module subMod = compModule.getSubModule(((CnxPointModule)cp).getSubModId());
			if (subMod instanceof ForkModule) {
				JMenuItem mi5 = new JMenuItem("set branch width");
				mi5.addActionListener(new SetPinWidthActionListener(cp));
				popup.add(mi5);
			}
		}
		return popup;
	}

	JPopupMenu getEquiLabelPopupMenu(EquiLabel label) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem mi1 = new JMenuItem(labels.getString("edit"));
		mi1.addActionListener(new EditEquiLabelActionListener(label));
		popup.add(mi1);
		JMenuItem mi2 = new JMenuItem(labels.getString("delete"));
		mi2.addActionListener(new DeleteEquiLabelActionListener(label));
		popup.add(mi2);
		return popup;
	}

	JPopupMenu getWidthLabelPopupMenu(WidthLabel label, int x, int y) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem mi2 = new JMenuItem(labels.getString("delete"));
		mi2.addActionListener(new DeleteWidthLabelActionListener(label));
		popup.add(mi2);
		return popup;
	}

	JPopupMenu getArityLabelPopupMenu(ArityLabel label, int x, int y) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem mi2 = new JMenuItem(labels.getString("delete"));
		mi2.addActionListener(new DeleteArityLabelActionListener(label));
		popup.add(mi2);
		return popup;
	}

	JPopupMenu getCnxPopupMenu(Cnx cnx, int x, int y) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem mi1 = new JMenuItem(labels.getString("delete"));
		mi1.addActionListener(new DeleteCnxActionListener(cnx));
		popup.add(mi1);
		return popup;
	}

	JPopupMenu getBackgroundPopupMenu(double x, double y) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem mc1 = new JMenuItem(labels.getString("paste"));
		mc1.setEnabled(copiedIndexesModules.size() > 0);
		mc1.addActionListener(new PasteActionListener(x, y));
		mc1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		popup.add(mc1);
		popup.addSeparator();
		JMenuItem mio1 = new JMenuItem(labels.getString("add-pin"));
		mio1.addActionListener(new AddPinActionListener(x, y));
		popup.add(mio1);
		JMenuItem mio2 = new JMenuItem(labels.getString("add-fork"));
		mio2.addActionListener(new AddBuiltInModuleActionListener("fork", x, y));
		popup.add(mio2);
		popup.addSeparator();
		JMenu builtInMenu = new JMenu("add built-in gate");
		JMenuItem mi00 = new JMenuItem(labels.getString("buffer"));
		mi00.addActionListener(new AddBuiltInModuleActionListener("buff", x, y));
		builtInMenu.add(mi00);
		JMenuItem mi0 = new JMenuItem(labels.getString("not"));
		mi0.addActionListener(new AddBuiltInModuleActionListener("not", x, y));
		builtInMenu.add(mi0);
		JMenuItem mi1 = new JMenuItem(labels.getString("buffer-3-state"));
		mi1.addActionListener(new AddBuiltInModuleActionListener("buff3", x, y));
		builtInMenu.add(mi1);
		JMenuItem mi2 = new JMenuItem(labels.getString("buffer-3-state-inverted"));
		mi2.addActionListener(new AddBuiltInModuleActionListener("buff3Inv", x, y));
		builtInMenu.add(mi2);
		popup.add(builtInMenu);
		JMenuItem mi3 = new JMenuItem(labels.getString("and2"));
		mi3.addActionListener(new AddBuiltInModuleActionListener("and2", x, y));
		builtInMenu.add(mi3);
		JMenuItem mi3bis = new JMenuItem("and2bis");
		mi3bis.addActionListener(new AddBuiltInModuleActionListener("and2bis", x, y));
		builtInMenu.add(mi3bis);
		JMenuItem mi3b = new JMenuItem(labels.getString("and3"));
		mi3b.addActionListener(new AddBuiltInModuleActionListener("and3", x, y));
		builtInMenu.add(mi3b);
		JMenuItem mi4 = new JMenuItem(labels.getString("nand2"));
		mi4.addActionListener(new AddBuiltInModuleActionListener("nand2", x, y));
		builtInMenu.add(mi4);
		JMenuItem mi4b = new JMenuItem(labels.getString("nand3"));
		mi4b.addActionListener(new AddBuiltInModuleActionListener("nand3", x, y));
		builtInMenu.add(mi4b);
		JMenuItem mi5 = new JMenuItem(labels.getString("or2"));
		mi5.addActionListener(new AddBuiltInModuleActionListener("or2", x, y));
		builtInMenu.add(mi5);
		JMenuItem mi5b = new JMenuItem(labels.getString("or3"));
		mi5b.addActionListener(new AddBuiltInModuleActionListener("or3", x, y));
		builtInMenu.add(mi5b);
		JMenuItem mi5c = new JMenuItem("or1");
		mi5c.addActionListener(new AddBuiltInModuleActionListener("or1", x, y));
		builtInMenu.add(mi5c);
		JMenuItem mi6 = new JMenuItem(labels.getString("nor2"));
		mi6.addActionListener(new AddBuiltInModuleActionListener("nor2", x, y));
		builtInMenu.add(mi6);
		JMenuItem mi6a = new JMenuItem(labels.getString("nor3"));
		mi6a.addActionListener(new AddBuiltInModuleActionListener("nor3", x, y));
		builtInMenu.add(mi6a);
		JMenuItem mi7 = new JMenuItem(labels.getString("xor2"));
		mi7.addActionListener(new AddBuiltInModuleActionListener("xor2", x, y));
		builtInMenu.add(mi7);
		JMenuItem mi7bis = new JMenuItem(labels.getString("xor2bis"));
		mi7bis.addActionListener(new AddBuiltInModuleActionListener("xor2bis", x, y));
		builtInMenu.add(mi7bis);
		JMenuItem mi8 = new JMenuItem(labels.getString("xor3"));
		mi8.addActionListener(new AddBuiltInModuleActionListener("xor3", x, y));
		builtInMenu.add(mi8);
		popup.add(builtInMenu);
		JMenu builtInFFMenu = new JMenu("add built-in flip-flop");
		JMenuItem mf0 = new JMenuItem("D flip-flop");
		mf0.addActionListener(new AddBuiltInModuleActionListener("flipflopD", x, y));
		builtInFFMenu.add(mf0);
		popup.add(builtInFFMenu);
		JMenu univMenu = new JMenu("add universal gate");
		JMenuItem mu2 = new JMenuItem("2-input gate");
		mu2.addActionListener(new AddUniversalGateActionListener(2, x, y));
		univMenu.add(mu2);
		JMenuItem mu3 = new JMenuItem("3-input gate");
		mu3.addActionListener(new AddUniversalGateActionListener(3, x, y));
		univMenu.add(mu3);
		JMenuItem mum = new JMenuItem("n-input gate...");
		univMenu.add(mum);
		popup.add(univMenu);
		JMenu modulesMenu = new JMenu("add module");
		File curdir = new File(".");
		File[] files = curdir.listFiles(this);
		for (int i = 0; i < files.length; i++) {
			String moduleName = files[i].getName();
			moduleName = moduleName.substring(0, moduleName.indexOf(".sch"));
			JMenuItem mif = new JMenuItem(moduleName);
			mif.addActionListener(new AddModuleActionListener(moduleName, x, y));
			modulesMenu.add(mif);
		}
		popup.add(modulesMenu);
		return popup;
	}

	public boolean accept(File f) {
		return f.getName().endsWith(".sch");
	}
	

	class AddUniversalGateActionListener implements ActionListener {
		private int nbInput;
		private double x;
		private double y;
		public AddUniversalGateActionListener(int nbInput, double x, double y) {
			this.nbInput = nbInput;
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			int subModId = compModule.getNewSubmodId();
			cmdList.add("addUniversalGate\t" + subModId + "\t" + nbInput + "\t" + x + "\t" + y);
			cmdList.add("addUnivGateLabel\t" + subModId);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);

			cmdList = new ArrayList();
			Module subMod = compModule.getSubModule(subModId);
			ArrayList pinIdList = subMod.getPinIdList();
			for (int i = 0; i < subMod.nbPins(); i++) {
				int pinId = ((Integer) pinIdList.get(i)).intValue();
				int equiId = compModule.getNewEquiId();
				//String equiName = "eq#" + equiId;
				String equiName = compModule.getNewVarName();
				NumExpr width = subMod.getWidth(pinId, compModule);
				cmdList.add("addEquipotential\t" + equiId + "\t" + equiName + "\t" + width);
				int newId = compModule.getNewCnxPointId();
				cmdList.add("addCnxPointModule\t" + newId + "\t" + subModId + "\t" + pinId);
				cmdList.add("setEquipotential\t" + newId + "\t-1\t" + equiId);
			}
			cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			// to group all operations
			compModule.mergeLastTwo();
			repaint();
		}
	}

	class DeleteSubModuleActionListener implements ActionListener {
		private int subModId;
		public DeleteSubModuleActionListener(int subModId) {
			this.subModId = subModId;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();

			// delete clicked module and the rest
			deleteSubModule(subModId, cmdList);

			// delete all other selected modules
			for (int i = 0; i < selected.size(); i++) {
				Integer subModIndex = (Integer) selected.get(i);
				if (subModIndex.intValue() == subModId) continue; // already dealt with
				// delete
			}
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	void deleteSubModule(int subModId, ArrayList cmdList) {
		Module subMod = compModule.getSubModule(subModId);
		ArrayList list = new ArrayList();
		for (int i = 0; i < subMod.nbPins(); i++) {
			CnxPointModule cpm = compModule.getCnxPointModule(subModId, i);
			list.add(cpm);
		}
		// recursively delete connections up to junctions
		deleteCnxPointGroup(new ArrayList(), list, cmdList);

		if (compModule.isArityLabelVisible(subModId))
			cmdList.add("delArityLabel\t" + subModId);
		// delete module cnxPoints
		for (int i = 0; i < subMod.nbPins(); i++) {
			CnxPointModule cp = compModule.getCnxPointModule(subModId, i);
			Equipotential equi = cp.getEquipotential();
			cmdList.add("setEquipotential\t" + cp.getId() + "\t" + equi.getId() + "\t-1");
			cmdList.add("delCnxPointModule\t" + cp.getId() + "\t" + subModId + "\t" + i);
		}
		// delete submodule
		Point2D loc = compModule.getSubModuleLocation(subModId);
		cmdList.add("delBuiltInModule\t" + subModId + "\t" + subMod.getType() + "\t" + subMod.getArity() + "\t" + loc.getX() + "\t" + loc.getY());
	}

	// recursively delete connections and cnxPoints up to junctions
	// do not delete module cnxPoints
	void deleteCnxPointGroup(ArrayList done, ArrayList toDo, ArrayList cmdList) {
		if (toDo.size() == 0) return;
System.out.println("toDo=" + toDo + ", done=" + done);
		ArrayList nextToDo = new ArrayList();
		for (int i = 0; i < toDo.size(); i++) {
			CnxPoint cp = (CnxPoint) toDo.get(i);
			if (done.contains(cp)) continue;
			if (compModule.isJunction(cp)) {
				if (! done.contains(cp)) done.add(cp);
				continue;
			}
			ArrayList cnxList = compModule.getCnxFrom(cp);
			if (cnxList != null) {
				for (int j = 0; j < cnxList.size(); j++) {
					Cnx cnx = (Cnx) cnxList.get(j);
					if (done.contains(cnx.getCp1()) || done.contains(cnx.getCp2())) continue;
					cmdList.add("delCnx\t" + cnx.getCp1().getId() + "\t" + cnx.getCp2().getId());
					if (! nextToDo.contains(cnx.getCp2())) nextToDo.add(cnx.getCp2());
					if (! nextToDo.contains(cnx.getCp1())) nextToDo.add(cnx.getCp1());
				}
			}
			// remove associated labels
			if (compModule.isEquiLabelVisible(cp))
				cmdList.add("delEquiLabel\t" + cp.getId());
			if (compModule.isWidthLabelVisible(cp))
				cmdList.add("delWidthLabel\t" + cp.getId());

			if (cp instanceof CnxPointInter) {
				Point2D loc = ((CnxPointInter) cp).getLocation();
				Equipotential equi = cp.getEquipotential();
				cmdList.add("setEquipotential\t" + cp.getId() + "\t" + equi.getId() + "\t-1");
				cmdList.add("delCnxPointInter\t" + cp.getId() + "\t" + loc.getX() + "\t" + loc.getY());
			}
			if (! done.contains(cp)) done.add(cp);
		}
		// go on recursively
		deleteCnxPointGroup(done, nextToDo, cmdList);
	}

	private ArrayList copiedIndexesModules = new ArrayList();

	class CopyActionListener implements ActionListener {
		private int subModId;
		public CopyActionListener(int subModId) {
			this.subModId = subModId;
		}
		public void actionPerformed(ActionEvent ev) {
			copiedIndexesModules.clear();
			copiedIndexesModules.add(new Integer(subModId));
			for (int i = 0; i < selected.size(); i++) {
				Integer subModIndex = (Integer) selected.get(i);
				if (i == subModIndex.intValue()) continue; // already dealt with
				copiedIndexesModules.add(new Integer(subModIndex.intValue()));
			}
		}
	}

	class SetArityLabelVisibleActionListener implements ActionListener {
		private int subModId;
		public SetArityLabelVisibleActionListener(int subModId) {
			this.subModId = subModId;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			boolean exists = compModule.isArityLabelVisible(subModId);
			if (exists)
				cmdList.add("delArityLabel\t" + subModId);
			else
				cmdList.add("addArityLabel\t" + subModId);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class SetArityActionListener implements ActionListener {
		private int subModId;
		public SetArityActionListener(int subModId) {
			this.subModId = subModId;
		}
		public void actionPerformed(ActionEvent ev) {
			Module subMod = compModule.getSubModule(subModId);
			if (subMod instanceof CompoundModule) {
				JOptionPane.showMessageDialog(mainPanel, "Only applicable to built-in modules");
				return;
			}
			for (int i = 0; i < subMod.nbPins(); i++) {
				CnxPointModule cp = compModule.getCnxPointModule(subModId, i);
				ArrayList list = compModule.getCnxFrom(cp);
				if ((list != null) && (list.size() > 0)) {
					JOptionPane.showMessageDialog(mainPanel, "All pins must be disconnected");
					return;
				}
			}
			NumExpr oldArity = subMod.getArity();
			NumExpr newArity = NumExpr.parse(JOptionPane.showInputDialog("Enter new arity", oldArity + ""));
			if (newArity == null) return;
			Point2D loc = compModule.getSubModuleLocation(subModId);


			ArrayList cmdList = new ArrayList();
			// delete previous submodule
			deleteSubModule(subModId, cmdList);

			// add same submodule, with new arity
			cmdList.add("addBuiltInModule\t" + subModId + "\t" + subMod.getType()+ "\t" + newArity + "\t" + loc.getX() + "\t" + loc.getY());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);

			cmdList = new ArrayList();
			Module newSubMod = compModule.getSubModule(subModId);
			ArrayList pinIdList = subMod.getPinIdList();
			for (int i = 0; i < subMod.nbPins(); i++) {
				int pinId = ((Integer) pinIdList.get(i)).intValue();
				int equiId = compModule.getNewEquiId();
				//String equiName = "eq#" + equiId;
				String equiName = compModule.getNewVarName();
				if (newSubMod.isPinScalable(pinId))
					cmdList.add("addEquipotential\t" + equiId + "\t" + equiName + "\t" + newArity);
				else
					cmdList.add("addEquipotential\t" + equiId + "\t" + equiName + "\t1");
				int newId = compModule.getNewCnxPointId();
				cmdList.add("addCnxPointModule\t" + newId + "\t" + subModId + "\t" + pinId);
				cmdList.add("setEquipotential\t" + newId + "\t-1\t" + equiId);
			}
			cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			// to group all operations
			compModule.mergeLastTwo();
			repaint();
		}
	}

	private double prevXi = -40.;
	private double prevYi = -30.;

	class AddPinActionListener implements ActionListener {
		private double x;
		private double y;
		public AddPinActionListener(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			// interface coordinates
			double xi = -40.;
			double yi = -30. + 30. * compModule.nbPins();

			int pinId = compModule.getNewCnxPointId();
			cmdList.add("addPin\t" + pinId + "\t" + x + "\t" + y + "\t" + xi + "\t" + yi);
			prevYi += 30.;
			cmdList.add("addExtPinLabel\t" + pinId);
			cmdList.add("addEquiLabel\t" + pinId);
			int equiId = compModule.getNewEquiId();
			//String equiName = "eq#" + equiId;
			String equiName = compModule.getNewVarName();
			cmdList.add("addEquipotential\t" + equiId + "\t" + equiName + "\t" + 1);
			cmdList.add("setEquipotential\t" + pinId + "\t-1\t" + equiId);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
			interfacePanel.repaint();
		}
	}

	class PasteActionListener implements ActionListener {
		private double x;
		private double y;
		public PasteActionListener(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			Integer first = (Integer) copiedIndexesModules.get(0);
			Point2D firstLoc = compModule.getSubModuleLocation(first.intValue());
			double dx = x - firstLoc.getX()/scale;
			double dy = y - firstLoc.getY()/scale;
			for (int i = 0; i < copiedIndexesModules.size(); i++) {
				Integer subModIndex = (Integer) copiedIndexesModules.get(i);
				Module subMod = compModule.getSubModule(subModIndex.intValue());
				Point2D loc = compModule.getSubModuleLocation(subModIndex.intValue());
				int subModId = compModule.getNewSubmodId();
				cmdList.add("addBuiltInModule\t" + subModId + "\t" + subMod.getType() + "\t" + subMod.getArity() + "\t" + (loc.getX() + dx) + "\t" + (loc.getY() + dy));
			}
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class AddBuiltInModuleActionListener implements ActionListener {
		private String type;
		private double x;
		private double y;
		public AddBuiltInModuleActionListener(String type, double x, double y) {
			this.type = type;
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent ev) {
			int subModId = compModule.getNewSubmodId();

			ArrayList cmdList = new ArrayList();
			cmdList.add("addBuiltInModule\t" + subModId + "\t" + type + "\t1\t" + x + "\t" + y);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);

			cmdList = new ArrayList();
			Module subMod = compModule.getSubModule(subModId);
			ArrayList pinIdList = subMod.getPinIdList();
			for (int i = 0; i < subMod.nbPins(); i++) {
				int pinId = ((Integer) pinIdList.get(i)).intValue();
				int newId = compModule.getNewCnxPointId();
				cmdList.add("addCnxPointModule\t" + newId + "\t" + subModId + "\t" + pinId);
			}
			cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			// to group all operations
			compModule.mergeLastTwo();

			cmdList = new ArrayList();
			for (int i = 0; i < subMod.nbPins(); i++) {
				int pinId = ((Integer) pinIdList.get(i)).intValue();
				int equiId = compModule.getNewEquiId();
				String equiName = compModule.getNewVarName();
				NumExpr width = subMod.getWidth(pinId, compModule);
				cmdList.add("addEquipotential\t" + equiId + "\t" + equiName + "\t" + width);
				CnxPointModule cpm = compModule.getCnxPointModule(subModId, pinId);
				cmdList.add("setEquipotential\t" + cpm.getId() + "\t-1\t" + equiId);
			}
			cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			// to group all operations
			compModule.mergeLastTwo();
			repaint();
		}
	}

	class AddModuleActionListener implements ActionListener {
		private String modName;
		private double x;
		private double y;
		public AddModuleActionListener(String modName, double x, double y) {
			this.modName = modName;
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent ev) {
			int subModId = compModule.getNewSubmodId();

			ArrayList cmdList = new ArrayList();
			cmdList.add("addModule\t" + subModId + "\t" + modName + "\t" + x + "\t" + y);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);

			cmdList = new ArrayList();
			Module subMod = compModule.getSubModule(subModId);
			ArrayList pinIdList = subMod.getPinIdList();
			for (int i = 0; i < subMod.nbPins(); i++) {
				int pinId = ((Integer) pinIdList.get(i)).intValue();
				int equiId = compModule.getNewEquiId();
				//String equiName = "eq#" + equiId;
				String equiName = compModule.getNewVarName();
				NumExpr width = subMod.getWidth(pinId, compModule);
				cmdList.add("addEquipotential\t" + equiId + "\t" + equiName + "\t" + width);
				int newId = compModule.getNewCnxPointId();
				cmdList.add("addCnxPointModule\t" + newId + "\t" + subModId + "\t" + pinId);
				cmdList.add("setEquipotential\t" + newId + "\t-1\t" + equiId);
			}
			cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			// to group all operations
			compModule.mergeLastTwo();
			repaint();
		}
	}

	void addSubModulePins(int subModId, int nbPins, int arity, ArrayList cmdList) {
		for (int i = 0; i < nbPins; i++) {
			int equiId = compModule.getNewEquiId();
			//String equiName = "eq#" + equiId;
			String equiName = compModule.getNewVarName();
			cmdList.add("addEquipotential\t" + equiId + "\t" + equiName + "\t" + arity);
			int newId = compModule.getNewCnxPointId();
			cmdList.add("addCnxPointModule\t" + newId + "\t" + subModId + "\t" + i);
			cmdList.add("setEquipotential\t" + newId + "\t-1\t" + equiId);
		}
	}

	class DeleteEquiLabelActionListener implements ActionListener {
		private EquiLabel label;
		public DeleteEquiLabelActionListener(EquiLabel label) {
			this.label = label;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("delEquiLabel\t" + label.getCnxPoint().getId());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class EditEquiLabelActionListener implements ActionListener {
		private EquiLabel label;
		public EditEquiLabelActionListener(EquiLabel label) {
			this.label = label;
		}
		public void actionPerformed(ActionEvent ev) {
			editEquiLabel(label);
			repaint();
		}
	}

	class DeleteWidthLabelActionListener implements ActionListener {
		private WidthLabel label;
		public DeleteWidthLabelActionListener(WidthLabel label) {
			this.label = label;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("delWidthLabel\t" + label.getCnxPoint().getId());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class DeleteArityLabelActionListener implements ActionListener {
		private ArityLabel label;
		public DeleteArityLabelActionListener(ArityLabel label) {
			this.label = label;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("delArityLabel\t" + label.getSubModuleId());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class DeleteCnxActionListener implements ActionListener {
		private Cnx cnx;
		public DeleteCnxActionListener(Cnx cnx) {
			this.cnx = cnx;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			ArrayList toDelete = new ArrayList();
			toDelete.add(cnx.getCp1());
			// recursively delete connections up to junctions
			deleteCnxPointGroup(new ArrayList(), toDelete, cmdList);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	/////////////////        Mouse listeners          /////////////////

	public class MyMouseAdapter extends MouseAdapter {

		public void mousePressed(MouseEvent ev) {
nbPaint = 0;
			startX = ev.getX() / scale;
			startY = ev.getY() / scale;
			if (Module.GRID) {
				startX = ((int) Math.round(startX / Module.GRIDSTEP)) * Module.GRIDSTEP;
				startY = ((int) Math.round(startY / Module.GRIDSTEP)) * Module.GRIDSTEP;
			}

			if (state == 0) {
				boolean mhit = isModuleHit(startX, startY);
				boolean chit = isCnxHit(startX, startY);
				boolean cphit = isCnxPointHit(startX, startY);
				boolean lhit = isLabelHit(startX, startY);

				// hits must be tested in the reverse order of painting : labels, then modules
				// and finally connections
				if (!mhit && !chit && !cphit && !lhit) {
					state = 1;
					modHitId = -1;
					if (ev.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popup = getBackgroundPopupMenu(startX, startY);
						popup.show(mainPanel, ev.getX(), ev.getY());
					}
				} else if (lhit) {
					ModuleLabel label = getHitLabel();
					if ((label instanceof EquiLabel) && (ev.getClickCount() > 1)) {
						EquiLabel equiLabel = (EquiLabel) label;
						editEquiLabel(equiLabel);
						state = 0;
					} else if ((label instanceof EquiLabel) && (ev.getButton() == MouseEvent.BUTTON3)) {
						EquiLabel equiLabel = (EquiLabel) label;
						JPopupMenu popup = getEquiLabelPopupMenu(equiLabel);
						popup.show(mainPanel, ev.getX(), ev.getY());
					} else if ((label instanceof WidthLabel) && (ev.getButton() == MouseEvent.BUTTON3)) {
						WidthLabel widthLabel = (WidthLabel) label;
						JPopupMenu popup = getWidthLabelPopupMenu(widthLabel, ev.getX(), ev.getY());
						popup.show(mainPanel, ev.getX(), ev.getY());
					} else if ((label instanceof ArityLabel) && (ev.getButton() == MouseEvent.BUTTON3)) {
						ArityLabel nbInstLabel = (ArityLabel) label;
						JPopupMenu popup = getArityLabelPopupMenu(nbInstLabel, ev.getX(), ev.getY());
						popup.show(mainPanel, ev.getX(), ev.getY());
					} else if ((label instanceof UnivGateLabel) && (ev.getClickCount() > 1)) {
						UnivGateLabel univGateLabel = (UnivGateLabel) label;
						editUnivGateLabel(univGateLabel);
						state = 0;
					} else {
						labelHit = getHitLabel();
						labelHitLoc = (Point2D) labelHit.getLocation().clone();
						curX = startX;
						curY = startY;
						dX = curX - labelHitLoc.getX();
						dY = curY - labelHitLoc.getY();
						state = 11;
					}
				} else if (mhit) {
					if (ev.getButton() == MouseEvent.BUTTON1) {
						modHitId = getHitModuleId();
						modHitLoc = compModule.getSubModuleLocation(modHitId);
						dX = curX - modHitLoc.getX();
						dY = curY - modHitLoc.getY();
						state = 2;
					} else if (ev.getButton() == MouseEvent.BUTTON2) {
						Module subMod = compModule.getSubModule(getHitModuleId());
						int nbShapes = subMod.nbShapes();
						int prevShapeIndex = subMod.getShape();
						String cmd = "setShape\t" + getHitModuleId() + "\t" + prevShapeIndex + "\t" + ((prevShapeIndex + 1) % nbShapes);
						String[] cmds = new String[] { cmd };
						compModule.do_(cmds);
						state = 0;
					} else if (ev.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popup = getModulePopupMenu(compModule, getHitModuleId());
						popup.show(mainPanel, ev.getX(), ev.getY());
						state = 0;
					}
				} else if (cphit) {
					curX = startX;
					curY = startY;
					if (ev.getButton() == MouseEvent.BUTTON1) {
						if (ev.getClickCount() > 1) {
							startCnxPoint = getHitCnxPoint();
							state = 5;
						} else if (shift(ev)) {
							startCnxPoint = getHitCnxPoint();
							state = 15;
						} else {
							cnxPointHit = getHitCnxPoint();
							state = 9;
						}
					} else if (ev.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popup = getCnxPointPopupMenu(getHitCnxPoint());
						popup.show(mainPanel, ev.getX(), ev.getY());
						state = 0;
					}
				} else if (chit) {
					cnxHit = getHitCnx();
					curX = startX;
					curY = startY;
					if (ev.getButton() == MouseEvent.BUTTON1) {
						cnxCp1Loc = (Point2D) cnxHit.getCp1().getLocation().clone();
						cnxCp2Loc = (Point2D) cnxHit.getCp2().getLocation().clone();
						state = 10;
					} else if (ev.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popup = getCnxPopupMenu(cnxHit, ev.getX(), ev.getY());
						popup.show(mainPanel, ev.getX(), ev.getY());
					} else if (ev.getButton() == MouseEvent.BUTTON2) {
						// break cnxHit in two parts and create inter cnx point
						ArrayList cmdList = new ArrayList();
						int newCpId = compModule.getNewCnxPointId();
						Equipotential equi = cnxHit.getCp1().getEquipotential();
						cmdList.add("delCnx\t" + cnxHit.getCp1().getId() + "\t" + cnxHit.getCp2().getId());
						cmdList.add("addCnxPointInter\t" + newCpId + "\t" + curX + "\t" + curY);
						cmdList.add("setEquipotential\t" + newCpId + "\t-1\t" + equi.getId());
						cmdList.add("addCnx\t" + cnxHit.getCp1().getId() + "\t" + newCpId);
						cmdList.add("addCnx\t" + newCpId + "\t" + cnxHit.getCp2().getId());
						CnxPoint cpi = compModule.getCnxPoint(newCpId);
						String[] cmds = (String[]) cmdList.toArray(new String[0]);
						compModule.do_(cmds);
	
						cnxPointHit = compModule.getCnxPoint(newCpId);
						
						state = 8;
					} else if (ev.getButton() == MouseEvent.BUTTON3) {
						state = 0;
					}
				}
	
			} else if ((state == 7) || (state == 17)) {
				// pin connection
				state = 0;
				pinConnection(startCnxPoint, endCnxPoint);
			} else {
				state = 0;
			}
			repaint();
			interfacePanel.repaint();
		}


		public void mouseReleased(MouseEvent ev) {
System.out.println("nbPaint=" + nbPaint);
			curX = ev.getX() / scale;
			curY = ev.getY() / scale;
			if (Module.GRID) {
				curX = ((int) Math.round(curX / Module.GRIDSTEP)) * Module.GRIDSTEP;
				curY = ((int) Math.round(curY / Module.GRIDSTEP)) * Module.GRIDSTEP;
			}

			if (state == 1) {
				state = 0;
				selected.clear();
				selectedCnxPoint.clear();

			} else if (state == 2) {
				state = 0;
				Integer I = new Integer(modHitId);
				if (!ctrlOrShift(ev)) {
					selected.clear();
					selected.add(I);
				} else if (selected.contains(I)) {
					selected.remove(I);
				} else {
					selected.add(I);
				}

			} else if (state == 3) {
				// move operation
				state = 0;
				ArrayList cmdList = new ArrayList();
				Point2D hitLoc = compModule.getSubModuleLocation(modHitId);
				// move only <modHitId>, which may not be among <selected>
				cmdList.add("moveModule\t" + modHitId + "\t" + hitLoc.getX() + "\t" + hitLoc.getY() + "\t" + (curX - dX) + "\t" + (curY - dY));
				for (int i = 0; i < selected.size(); i++) {
					Integer subModIndex = (Integer) selected.get(i);
					if (subModIndex.intValue() == modHitId) continue; // already dealt with
					Point2D loc = compModule.getSubModuleLocation(subModIndex.intValue());
					cmdList.add("moveModule\t" + subModIndex + "\t" + loc.getX() + "\t" + loc.getY() + "\t" + (loc.getX() + curX - dX - hitLoc.getX()) + "\t" + (loc.getY() + curY - dY - hitLoc.getY()));
				}
				for (int i = 0; i < selectedCnxPoint.size(); i++) {
					CnxPoint cp = (CnxPoint) selectedCnxPoint.get(i);
					Point2D loc = cp.getLocation();
					cmdList.add("moveCnxPoint\t" + cp.getId() + "\t" + loc.getX() + "\t" + loc.getY() + "\t" + (loc.getX() + curX - hitLoc.getX()) + "\t" + (loc.getY() + curY - hitLoc.getY()));
				}
				String[] cmds = (String[]) cmdList.toArray(new String[0]);
				compModule.do_(cmds);

			} else if (state == 4) {
				// group select operation
				state = 0;
				double x = startX;
				double y = startY;
				double w = (double) (curX - startX);
				double h = (double) (curY - startY);
				ArrayList list = selectedGroup(x, y, w, h);
				if (!ctrlOrShift(ev))
					selected.clear();
				selected.addAll(list);
				ArrayList listCnxPoint = selectedCnxPointGroup(x, y, w, h);
				if (!ctrlOrShift(ev))
					selectedCnxPoint.clear();
				selectedCnxPoint.addAll(listCnxPoint);

			} else if ((state == 6) || (state == 8)) {
				state = 0;

			} else if (state == 9) {
				if (!(cnxPointHit instanceof CnxPointModule)) {
					ArrayList cmdList = new ArrayList();
					cmdList.add("moveCnxPoint\t" + cnxPointHit.getId() + "\t" + startX + "\t" + startY + "\t" + curX + "\t" + curY);
					String[] cmds = (String[]) cmdList.toArray(new String[0]);
					compModule.do_(cmds);
				}
				state = 0;

			} else if (state == 10) {
				// the mouse must be released on the same connection
				boolean chit = isCnxHit(curX, curY);
				if (chit) {
					if (cnxHit.equals(getHitCnx())) {
						// clicked on a connection (so what?)
					}
				}
				state = 0;

			} else if (state == 11) {
				state = 0;

			} else if (state == 12) {
				double nx = curX - dX;
				double ny = curY - dY;
				ArrayList cmdList = new ArrayList();
				if (labelHit instanceof EquiLabel) {
					CnxPoint cp = ((EquiLabel)labelHit).getCnxPoint();
					cmdList.add("moveEquiLabel\t" + cp.getId() + "\t" +
						labelHitLoc.getX() + "\t" +
						labelHitLoc.getY() + "\t" +
						nx + "\t" + ny);
				} else if (labelHit instanceof ArityLabel) {
					int subModId = ((ArityLabel)labelHit).getSubModuleId();
					cmdList.add("moveArityLabel\t" + subModId + "\t" +
						labelHitLoc.getX() + "\t" +
						labelHitLoc.getY() + "\t" +
						nx + "\t" + ny);
				} else if (labelHit instanceof UnivGateLabel) {
					int ugId = ((UnivGateLabel)labelHit).getUnivGate().getId();
					cmdList.add("moveUnivGateLabel\t" + ugId + "\t" +
						labelHitLoc.getX() + "\t" +
						labelHitLoc.getY() + "\t" +
						nx + "\t" + ny);
				}
;
				String[] cmds = (String[]) cmdList.toArray(new String[0]);
				compModule.do_(cmds);
				state = 0;

			} else if (state == 17) {
				state = 0;
				pinConnection(startCnxPoint, endCnxPoint);

			} else if (state == 18) {
				CnxPoint cp1 = cnxHit.getCp1();
				CnxPoint cp2 = cnxHit.getCp2();
				if ((cp1 instanceof CnxPointInter) && (cp1 instanceof CnxPointInter)) {
					ArrayList cmdList = new ArrayList();
					cmdList.add("moveCnxPoint\t" + cp1.getId() + "\t" + cnxCp1Loc.getX() + "\t" + cnxCp1Loc.getY() + "\t" + (cnxCp1Loc.getX() + curX - startX) + "\t" + (cnxCp1Loc.getY() + curY - startY));
					cmdList.add("moveCnxPoint\t" + cp2.getId() + "\t" + cnxCp2Loc.getX() + "\t" + cnxCp2Loc.getY() + "\t" + (cnxCp2Loc.getX() + curX - startX) + "\t" + (cnxCp2Loc.getY() + curY - startY));
					String[] cmds = (String[]) cmdList.toArray(new String[0]);
					compModule.do_(cmds);
				}
				state = 0;
			}
			repaint();
			interfacePanel.repaint();
		}
	}

	public class MyMouseMotionAdapter extends MouseMotionAdapter {

		public void mouseMoved(MouseEvent ev) {
			curX = ev.getX() / scale;
			curY = ev.getY() / scale;
			if (Module.GRID) {
				curX = ((int) Math.round(curX / Module.GRIDSTEP)) * Module.GRIDSTEP;
				curY = ((int) Math.round(curY / Module.GRIDSTEP)) * Module.GRIDSTEP;
			}

			switch (state) {
				case 5: state = 6; break;
				case 6:
				case 7: boolean cphit = isCnxPointHit(curX, curY);
					if (cphit) {
						endCnxPoint = getHitCnxPoint();
						state = 7;
					} else {
						state = 6;
					}
					break;
				case 15: state = 16; break;
				case 16:
				case 17: cphit = isCnxPointHit(curX, curY);
					if (cphit) {
						endCnxPoint = getHitCnxPoint();
						state = 17;
					} else {
						state = 16;
					}
					break;
			}
			repaint();
			interfacePanel.repaint();
		}

		public void mouseDragged(MouseEvent ev) {
			curX = ev.getX() / scale;
			curY = ev.getY() / scale;
			if (Module.GRID) {
				curX = ((int) Math.round(curX / Module.GRIDSTEP)) * Module.GRIDSTEP;
				curY = ((int) Math.round(curY / Module.GRIDSTEP)) * Module.GRIDSTEP;
			}
			switch (state) {
				case 1: state = 4; break;
				case 2: state = 3; break;
				case 3: state = 3; break;
				case 4: state = 4; break;
				case 8: state = 9;
					break;
				case 9: // move cnx point
					compModule.moveCnxPoint(cnxPointHit, curX, curY);
					state = 9;
					break;
				case 10:
					state = 18;
					break;

				case 11:
				case 12: Point2D anchorLoc = labelHit.getAnchorLocation();
					labelHit.setRelativeLocation(curX - anchorLoc.getX() - dX, curY - anchorLoc.getY() - dY);
					state = 12;
					break;
				case 15: state = 16; break;
				case 16:
				case 17: boolean cphit = isCnxPointHit(curX, curY);
					if (cphit) {
						endCnxPoint = getHitCnxPoint();
						state = 17;
					} else {
						state = 16;
					}
					break;

				case 18: // move both ends
					CnxPoint cp1 = cnxHit.getCp1();
					CnxPoint cp2 = cnxHit.getCp2();
					if ((cp1 instanceof CnxPointInter) && (cp1 instanceof CnxPointInter)) {
						cp1.setLocation(new Point2D.Double(cnxCp1Loc.getX() + curX - startX, cnxCp1Loc.getY() + curY - startY));
						cp2.setLocation(new Point2D.Double(cnxCp2Loc.getX() + curX - startX, cnxCp2Loc.getY() + curY - startY));
					}
					state = 18;
					break;
			}
			repaint();
			interfacePanel.repaint();
		}
	}

	void editEquiLabel(EquiLabel equiLabel) {
		String newName = JOptionPane.showInputDialog("Enter equipotential name", equiLabel.getText());
		if (newName != null) {
			ArrayList cmdList = new ArrayList();
//System.out.println("equiLabel.getCnxPoint().getEqui=" + equiLabel.getCnxPoint().getEquipotential());
			cmdList.add("setEquipotentialName\t" + equiLabel.getCnxPoint().getEquipotential().getId() + "\t" + equiLabel.getText() + "\t" + newName);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			// update currVarName
			compModule.setCurrVarName(newName);
			repaint();
		}
	}

	void editUnivGateLabel(UnivGateLabel label) {
		String newTxt = JOptionPane.showInputDialog("Enter new equation", label.getText());
		if (newTxt != null) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("setUnivGateEquation\t" + label.getUnivGate().getId() + "\t" + label.getText() + "\t" + newTxt);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

private int nbPaint = 0;
private double tt = 0.;

	public void paint(Graphics g) {
nbPaint += 1;
tt = System.currentTimeMillis();
		Graphics2D g2 = (Graphics2D) g;
		paint2(g2, scale);
	}

	public void paint2(Graphics2D g2, double scale) {
		g2.scale(scale, scale);
		g2.setColor(Module.backgroundColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		// paint from bottom to top : connections, modules and finally labels

                // paint connections
                ArrayList cnxList = compModule.getCnxList();
                for (int i = 0; i < cnxList.size(); i++) {
                        Cnx cnx = (Cnx) cnxList.get(i);
                        cnx.paint(g2);
                }

		ArrayList toHighlight = null;
		if (state == 10) {
			// prepare to highlight all intermediate points of the equipotential
			Equipotential equi = cnxHit.getEquipotential();
			toHighlight = compModule.getCnxPoints(equi);
		} else if ((state == 8) || (state == 9)) {
			// creation or drag of an intermediate cnx point -> highlight it
			toHighlight = new ArrayList();
			toHighlight.add(cnxPointHit);
		}
                for (int i = 0; i < compModule.getCnxPointList().size(); i++) {
                        CnxPoint cp = (CnxPoint) compModule.getCnxPointList().get(i);
			boolean highlight = ((toHighlight != null) && toHighlight.contains(cp));
			boolean isJunction = compModule.isJunction(cp);
			boolean select = selectedCnxPoint.contains(cp);
                        cp.paint(g2, highlight, isJunction, select);
		}

                // paint subcompModules
		Iterator subModIterator = compModule.getSubModulesIterator();
		while (subModIterator.hasNext()) {
			Module subMod = (Module) subModIterator.next();
                        Point2D locMod = compModule.getSubModuleLocation(subMod.getId());
                        boolean sel = selected.contains(new Integer(subMod.getId()));
                        g2.translate(locMod.getX(), locMod.getY());
                        // paint submodule
                        subMod.paint(g2, compModule, sel, false);
                        g2.translate(-locMod.getX(), -locMod.getY());
                        if ((sel || (modHitId == subMod.getId())) && (state == 3)) {
				Point2D hitLoc = compModule.getSubModuleLocation(modHitId);
                                // paint ghost
                                double x = curX;
                                double y = curY;
				if (Module.GRID) {
                                	x = ((int) Math.round(x / Module.GRIDSTEP)) * Module.GRIDSTEP;
                                	y = ((int) Math.round(y / Module.GRIDSTEP)) * Module.GRIDSTEP;
				}
				double nx = locMod.getX() + x - dX - hitLoc.getX();
				double ny = locMod.getY() + y - dY - hitLoc.getY();
                                g2.translate(nx, ny);
                                subMod.paint(g2, compModule, false, true);
                                g2.translate(-nx, -ny);
                        }
                }

		// paint labels
		ArrayList labelList = compModule.getLabelList();
		for (int i = 0; i < labelList.size(); i++) {
			ModuleLabel label = (ModuleLabel) labelList.get(i);
			label.paint(g2);
		}

		if (state == 4) {
			// group select operation in progress
			double x = startX;
			double y = startY;
			double w = curX - startX;
			double h = curY - startY;
			g2.setStroke(Module.groupSelectStroke);
			g2.setColor(Module.groupSelectColor);
			g2.draw(new Rectangle2D.Double(x, y, w, h));

		} else if ((state == 5) || (state == 6) || (state == 7) || (state == 15) || (state == 16) || (state == 17)) {
			// pin connection in progress
			g2.setStroke(Module.cnxStroke);
			g2.setColor(Module.interCnxPtRectColor);
			startCnxPoint.paint(g2, true, false, false);

			if ((state == 7) || (state == 17)) {
				endCnxPoint.paint(g2, true, false, false);
			}

			g2.setColor(Color.darkGray);
			Point2D startPinLoc = startCnxPoint.getLocation();
			g2.draw(new Line2D.Double(startPinLoc.getX(), startPinLoc.getY(), curX, curY));
		}
//System.out.println("delta time=" + (System.currentTimeMillis() - tt));
	}
}

