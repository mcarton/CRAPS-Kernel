
package org.jcb.shdl;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import org.jcb.shdl.*;


public class ModuleInterfacePanel extends JPanel implements MouseListener, MouseMotionListener {

	private CompoundModule compModule;
	private ResourceBundle labels;

	private double orgX;
	private double orgY;
	private double scale;
	private double dX;
	private double dY;

	private static AffineTransform AFFINE_ID = new AffineTransform();

	public ModuleInterfacePanel(CompoundModule compModule, ResourceBundle labels) {
		this.compModule = compModule;
		this.labels = labels;
		orgX = 200.;
		orgY = 200.;
		scale = 2.0;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private Dimension DIM = new Dimension(1000, 1000);
	public Dimension getPreferredSize() {
		return DIM;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	private int pathNodeIndex = -1; // selected node of the body path, if any
	private boolean ctrlNode; // indicates this is a Quad control node

	private int selectedPinId = -1; // id of selected pin, if any

	private ExtPinLabel extPinLabelHit = null; // external pin hit, if any

	private int moduleNameLabelHitIndex = -1; // 0 or 1

	private double prevX;
	private double prevY;
	private double curX;
	private double curY;

	public void mousePressed(MouseEvent e) {
		double x = (e.getX() - orgX) / scale;
		double y = (e.getY() - orgY) / scale;
		if (Module.GRID) {
			x = ((int) Math.round(x / Module.GRIDSTEP)) * Module.GRIDSTEP;
			y = ((int) Math.round(y / Module.GRIDSTEP)) * Module.GRIDSTEP;
		}
		prevX = x; prevY = y;

		// outline hit ?
		PathIterator pathIt = compModule.getBodyPath().getPathIterator(AFFINE_ID);
		double res[] = new double[6];
		int index = -1;
		while (!pathIt.isDone()) {
			int type = pathIt.currentSegment(res);
			index += 1;
			switch (type) {
				case PathIterator.SEG_MOVETO:
				case PathIterator.SEG_LINETO:
				case PathIterator.SEG_QUADTO:
					if (((type != PathIterator.SEG_QUADTO) &&
							((Math.abs(x - res[0]) < Module.ctrlPtWidth / 2) &&
							(Math.abs(y - res[1]) < Module.ctrlPtWidth / 2))) ||
						((type == PathIterator.SEG_QUADTO) &&
							((Math.abs(x - res[2]) < Module.ctrlPtWidth / 2) &&
							(Math.abs(y - res[3]) < Module.ctrlPtWidth / 2)))) {

						if (e.getButton() == MouseEvent.BUTTON3) {
							JPopupMenu popup = new JPopupMenu();
							if (type != PathIterator.SEG_MOVETO) {
								JMenuItem mc1 = new JMenuItem(labels.getString("delete-node"));
								mc1.addActionListener(new DeleteNodeActionListener(index, x, y));
								popup.add(mc1);
							}
							JMenuItem mc2 = new JMenuItem(labels.getString("add-line-after"));
							mc2.addActionListener(new AddLineAfterActionListener(index, x + 20., y + 20.));
							popup.add(mc2);
							JMenuItem mc3 = new JMenuItem(labels.getString("add-quad-after"));
							mc3.addActionListener(new AddQuadAfterActionListener(index, x + 10., y + 10.));
							popup.add(mc3);
							popup.show(this, e.getX(), e.getY());
							return;
						} else {
							pathNodeIndex = index;
							ctrlNode = false;
							return;
						}
					} else if ((type == PathIterator.SEG_QUADTO) &&
							(Math.abs(x - res[0]) < Module.ctrlPtWidth / 2) &&
							(Math.abs(y - res[1]) < Module.ctrlPtWidth / 2)) {
						pathNodeIndex = index;
						ctrlNode = true;
						return;
					}
					break;
				case PathIterator.SEG_CLOSE:
					break;
			}
			pathIt.next();
		}
		pathNodeIndex = -1;

		// pin hit ?
		for (int i = 0; i < compModule.nbPins(); i++) {
			int pinId = ((Integer) compModule.getPinIdList().get(i)).intValue();
			CnxPointPin pin = (CnxPointPin) compModule.getCnxPoint(pinId);
			Point2D loc = compModule.getExtPinLocation(pinId);
			if ((Math.abs(x - loc.getX()) < Module.ctrlPtWidth / 2) &&
					(Math.abs(y - loc.getY()) < Module.ctrlPtWidth / 2)) {
				selectedPinId = pinId;
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popup = new JPopupMenu();
					JMenu mo = new JMenu(labels.getString("orientation"));
					JMenuItem mo1 = new JMenuItem(labels.getString("left"));
					mo.add(mo1);
					mo1.addActionListener(new PinOrientationActionListener(pin, 0));
					JMenuItem mo2 = new JMenuItem(labels.getString("right"));
					mo.add(mo2);
					mo2.addActionListener(new PinOrientationActionListener(pin, 1));
					JMenuItem mo3 = new JMenuItem(labels.getString("up"));
					mo.add(mo3);
					mo3.addActionListener(new PinOrientationActionListener(pin, 2));
					JMenuItem mo4 = new JMenuItem(labels.getString("down"));
					mo.add(mo4);
					mo4.addActionListener(new PinOrientationActionListener(pin, 3));
					popup.add(mo);
					JCheckBoxMenuItem mi1 = new JCheckBoxMenuItem(labels.getString("inverted"));
					mi1.addActionListener(new InvertedActionListener(pin));
					mi1.setState(pin.isInverted());
					popup.add(mi1);
					JCheckBoxMenuItem mi2 = new JCheckBoxMenuItem(labels.getString("clocked"));
					mi2.addActionListener(new ClockedActionListener(pin));
					mi2.setState(pin.isClocked());
					popup.add(mi2);
					popup.addSeparator();
					JCheckBoxMenuItem mi3 = new JCheckBoxMenuItem(labels.getString("visible-pin-label"));
					mi3.setState(compModule.isExtPinLabelVisible(pin));
					mi3.addActionListener(new SetExtPinLabelVisibilityActionListener(pin));
					popup.add(mi3);
					popup.show(this, e.getX(), e.getY());
				} else {
				}
				return;
			}
		}

		// pin label hit ?
		extPinLabelHit  = null;
		ArrayList extPinLabelList = compModule.getExtPinLabelList();
		for (int i = 0; i < extPinLabelList.size(); i++) {
			ExtPinLabel label = (ExtPinLabel) extPinLabelList.get(i);
			CnxPointPin pin = (CnxPointPin) label.getCnxPoint();
			RoundRectangle2D rect = label.getRect(label.getAnchorLocation());
			if (rect.contains(x, y)) {
				extPinLabelHit = label;
				Point2D loc = label.getLocation();
				dX = x - loc.getX();
				dY = y - loc.getY();
				if (e.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popup = new JPopupMenu();
					JMenuItem mi1 = new JMenuItem(labels.getString("hide"));
					mi1.addActionListener(new SetExtPinLabelVisibilityActionListener(pin));
					popup.add(mi1);
					popup.show(this, e.getX(), e.getY());
				}
				return;
			}
		}

		// module name label hit ?
		moduleNameLabelHitIndex = -1;
		for (int indx = 0; indx < 2; indx++) {
			if (compModule.isModuleNameLabelVisible(indx)) {
				RoundRectangle2D rect = compModule.getModuleNameLabel(indx).getRect(null);
				if (rect.contains(x, y)) {
					ModuleNameLabel label = compModule.getModuleNameLabel(indx);
					Point2D loc = label.getLocation();
					dX = x - loc.getX();
					dY = y - loc.getY();
					moduleNameLabelHitIndex = indx;
					if (e.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popup = new JPopupMenu();
						JMenuItem mi1 = new JMenuItem(labels.getString("hide"));
						mi1.addActionListener(new SetModuleNameLabelVisibilityActionListener(indx));
						popup.add(mi1);
						JMenuItem mi2 = new JMenuItem(labels.getString("edit"));
						mi2.addActionListener(new EditModuleNameLabelTextActionListener(indx));
						popup.add(mi2);
						popup.show(this, e.getX(), e.getY());
					}
					return;
				}
			}
		}

		// hit background
		if (e.getButton() == MouseEvent.BUTTON3) {
			JPopupMenu popup = new JPopupMenu();
			JCheckBoxMenuItem mi1 = new JCheckBoxMenuItem(labels.getString("module-name1-visible"));
			mi1.setState(compModule.isModuleNameLabelVisible(0));
			mi1.addActionListener(new SetModuleNameLabelVisibilityActionListener(0));
			popup.add(mi1);
			JCheckBoxMenuItem mi2 = new JCheckBoxMenuItem(labels.getString("module-name2-visible"));
			mi2.setState(compModule.isModuleNameLabelVisible(1));
			mi2.addActionListener(new SetModuleNameLabelVisibilityActionListener(1));
			popup.add(mi2);
			popup.show(this, e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
		double x = (e.getX() - orgX) / scale;
		double y = (e.getY() - orgY) / scale;
		if (Module.GRID) {
			x = ((int) Math.round(x / Module.GRIDSTEP)) * Module.GRIDSTEP;
			y = ((int) Math.round(y / Module.GRIDSTEP)) * Module.GRIDSTEP;
		}
		if ((pathNodeIndex != -1) && (e.getButton() != MouseEvent.BUTTON3)) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("outlineMoveNode\t" + pathNodeIndex + "\t" + ctrlNode + "\t" + prevX + "\t" + prevY + "\t" + curX + "\t" + curY);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			pathNodeIndex = -1;
			repaint();
		}
		if ((selectedPinId != -1) && (e.getButton() != MouseEvent.BUTTON3)) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("extPinMove\t" + selectedPinId + "\t" + prevX + "\t" + prevY + "\t" + curX + "\t" + curY);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			selectedPinId = -1;
			repaint();
		}
		if (extPinLabelHit != null) {
			CnxPointPin pin = (CnxPointPin) extPinLabelHit.getCnxPoint();
			Point2D loc = pin.getExtLocation();

			ArrayList cmdList = new ArrayList();
			double nx = x - dX;
			double ny = y - dY;
			cmdList.add("moveExtPinLabel\t" + pin.getId() + "\t" +
				loc.getX() + "\t" + loc.getY() + "\t" + nx + "\t" + ny);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			extPinLabelHit = null;
			repaint();
		}
		if (moduleNameLabelHitIndex != -1) {
			Point2D loc = compModule.getModuleNameLabel(moduleNameLabelHitIndex).getLocation();
			ArrayList cmdList = new ArrayList();
			double nx = x - dX;
			double ny = y - dY;
			cmdList.add("moveModuleNameLabel\t" + moduleNameLabelHitIndex + "\t" + 
				loc.getX() + "\t" + loc.getY() + "\t" + nx + "\t" + ny);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			moduleNameLabelHitIndex = -1;
			repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		double x = (e.getX() - orgX) / scale;
		double y = (e.getY() - orgY) / scale;
		if (Module.GRID) {
			x = ((int) Math.round(x / Module.GRIDSTEP)) * Module.GRIDSTEP;
			y = ((int) Math.round(y / Module.GRIDSTEP)) * Module.GRIDSTEP;
		}
		curX = x; curY = y;

		if (pathNodeIndex != -1) {
			GeneralPath newPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
			PathIterator pathIt = compModule.getBodyPath().getPathIterator(AFFINE_ID);
			double res[] = new double[6];
			int index = -1;
			while (!pathIt.isDone()) {
				int type = pathIt.currentSegment(res);
				index += 1;
				switch (type) {
					case PathIterator.SEG_MOVETO:
						if (index == pathNodeIndex)
							newPath.moveTo((float) x, (float) y);
						else
							newPath.moveTo((float) res[0], (float) res[1]);
						break;
					case PathIterator.SEG_LINETO:
						if (index == pathNodeIndex)
							newPath.lineTo((float) x, (float) y);
						else
							newPath.lineTo((float) res[0], (float) res[1]);
						break;
					case PathIterator.SEG_QUADTO:
						if (index == pathNodeIndex) {
							if (ctrlNode) {
								newPath.quadTo((float) x, (float) y, (float) res[2], (float) res[3]);
							} else {
								newPath.quadTo((float) res[0], (float) res[1], (float) x, (float) y);
							}
						} else
							newPath.quadTo((float) res[0], (float) res[1], (float) res[2], (float) res[3]);
						break;
					case PathIterator.SEG_CLOSE:
						newPath.closePath();
						break;
				}
				pathIt.next();
			}
			compModule.setBodyPath(newPath);
			repaint();
		}

		if (selectedPinId != -1) {
			CnxPointPin pin = (CnxPointPin) compModule.getCnxPoint(selectedPinId);
			Point2D loc = pin.getExtLocation();
			loc.setLocation(new Point2D.Double(x, y));
			repaint();
		}

		if (extPinLabelHit != null) {
			Point2D anchorLoc = extPinLabelHit.getAnchorLocation();
			extPinLabelHit.setRelativeLocation(x - anchorLoc.getX() - dX, y - anchorLoc.getY() - dY);
			repaint();
		}

		if (moduleNameLabelHitIndex != -1) {
			Point2D loc = compModule.getModuleNameLabel(moduleNameLabelHitIndex).getRelativeLocation();
			double nx = x - dX;
			double ny = y - dY;
			loc.setLocation(nx, ny);
			repaint();
		}
	}

	public void mouseMoved(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}

	class PinOrientationActionListener implements ActionListener {
		private CnxPointPin pin;
		private int orientation;
		public PinOrientationActionListener(CnxPointPin pin, int orientation) {
			this.pin = pin;
			this.orientation = orientation;
		}
		public void actionPerformed(ActionEvent e) {
			int prevOrientation = pin.getOrientation();
			ArrayList cmdList = new ArrayList();
			cmdList.add("setOrientation\t" + pin.getId() + "\t" + prevOrientation + "\t" + orientation);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class InvertedActionListener implements ActionListener {
		private CnxPointPin pin;
		public InvertedActionListener(CnxPointPin pin) {
			this.pin = pin;
		}
		public void actionPerformed(ActionEvent e) {
			boolean inverted = pin.isInverted();
			ArrayList cmdList = new ArrayList();
			cmdList.add("setInverted\t" + pin.getId() + "\t" + inverted + "\t" + !inverted);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class ClockedActionListener implements ActionListener {
		private CnxPointPin pin;
		public ClockedActionListener(CnxPointPin pin) {
			this.pin = pin;
		}
		public void actionPerformed(ActionEvent e) {
			boolean clocked = pin.isClocked();
			ArrayList cmdList = new ArrayList();
			cmdList.add("setClocked\t" + pin.getId() + "\t" + clocked + "\t" + !clocked);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class SetExtPinLabelVisibilityActionListener implements ActionListener {
		private CnxPointPin cp;
		public SetExtPinLabelVisibilityActionListener(CnxPointPin cp) {
			this.cp = cp;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			boolean exists = compModule.isExtPinLabelVisible(cp);
			if (exists)
				cmdList.add("delExtPinLabel\t" + cp.getId());
			else
				cmdList.add("addExtPinLabel\t" + cp.getId());
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class SetModuleNameLabelVisibilityActionListener implements ActionListener {
		private int index;
		public SetModuleNameLabelVisibilityActionListener(int index) {
			this.index = index;
		}
		public void actionPerformed(ActionEvent e) {
			ArrayList cmdList = new ArrayList();
			boolean exists = compModule.isModuleNameLabelVisible(index);
			if (exists)
				cmdList.add("delModuleNameLabel\t" + index);
			else
				cmdList.add("addModuleNameLabel\t" + index);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class EditModuleNameLabelTextActionListener implements ActionListener {
		private int index;
		public EditModuleNameLabelTextActionListener(int index) {
			this.index = index;
		}
		public void actionPerformed(ActionEvent e) {
			ModuleNameLabel label = compModule.getModuleNameLabel(index);
			String prevText = label.getText();
			String newText = JOptionPane.showInputDialog("Enter text", prevText);
			if (newText != null) {
				ArrayList cmdList = new ArrayList();
				cmdList.add("editModuleNameLabel\t" + index + "\t" + prevText + "\t" + newText);
				String[] cmds = (String[]) cmdList.toArray(new String[0]);
				compModule.do_(cmds);
				repaint();
			}
		}
	}

	class DeleteNodeActionListener implements ActionListener {
		private int index;
		private double x;
		private double y;
		public DeleteNodeActionListener(int index, double x, double y) {
			this.index = index;
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent e) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("outlineDelLine\t" + index + "\t" + x + "\t" + y);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class AddLineAfterActionListener implements ActionListener {
		private int index;
		private double x;
		private double y;
		public AddLineAfterActionListener(int index, double x, double y) {
			this.index = index;
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("outlineAddLineAfter\t" + index + "\t" + x + "\t" + y);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}

	class AddQuadAfterActionListener implements ActionListener {
		private int index;
		private double x;
		private double y;
		public AddQuadAfterActionListener(int index, double x, double y) {
			this.index = index;
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent ev) {
			ArrayList cmdList = new ArrayList();
			cmdList.add("outlineAddQuadAfter\t" + index + "\t" + x + "\t" + y);
			String[] cmds = (String[]) cmdList.toArray(new String[0]);
			compModule.do_(cmds);
			repaint();
		}
	}


	public void paint(Graphics g) {
		g.setColor(Module.interfaceColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D) g;

		g2.translate(orgX, orgY);
		g2.scale(scale, scale);

		AffineTransform aft = new AffineTransform();
		aft.translate(orgX, orgY);
		aft.scale(scale, scale);

		// draw and paint the body
		GeneralPath bodyPath = compModule.getBodyPath();
		g.setColor(compModule.getBodyColor());
		g2.fill(bodyPath);
		g2.setStroke(Module.moduleOutlineStroke);
		g.setColor(Color.black);
		g2.draw(bodyPath);

		// paint the control points
		PathIterator pathIt = bodyPath.getPathIterator(AFFINE_ID);
		double res[] = new double[6];
		while (!pathIt.isDone()) {
			int type = pathIt.currentSegment(res);
			switch (type) {
				case PathIterator.SEG_MOVETO:
					{
					Rectangle2D r = new Rectangle2D.Double(res[0] - Module.ctrlPtWidth / 2, res[1] - Module.ctrlPtWidth / 2, Module.ctrlPtWidth, Module.ctrlPtWidth);
					g.setColor(Module.interfacePtOrgColor);
					g2.fill(r);
					break;
					}
				case PathIterator.SEG_LINETO:
					{
					Rectangle2D r = new Rectangle2D.Double(res[0] - Module.ctrlPtWidth / 2, res[1] - Module.ctrlPtWidth / 2, Module.ctrlPtWidth, Module.ctrlPtWidth);
					g.setColor(Module.interfacePtColor);
					g2.fill(r);
					break;
					}
				case PathIterator.SEG_QUADTO:
					{
					Rectangle2D r1 = new Rectangle2D.Double(res[0] - Module.ctrlPtWidth / 2, res[1] - Module.ctrlPtWidth / 2, Module.ctrlPtWidth, Module.ctrlPtWidth);
					Rectangle2D r2 = new Rectangle2D.Double(res[2] - Module.ctrlPtWidth / 2, res[3] - Module.ctrlPtWidth / 2, Module.ctrlPtWidth, Module.ctrlPtWidth);
					g.setColor(Module.interfacePtCtrlColor);
					g2.fill(r1);
					g.setColor(Module.interfacePtColor);
					g2.fill(r2);
					break;
					}
				case PathIterator.SEG_CLOSE:
					break;
			}
			pathIt.next();
		}

		// draw pins
		for (int i = 0; i < compModule.nbPins(); i++) {
			int pinId = ((Integer) compModule.getPinIdList().get(i)).intValue();
			CnxPointPin pin = (CnxPointPin) compModule.getCnxPoint(pinId);
			Point2D loc = compModule.getExtPinLocation(pinId);
			Equipotential equi = pin.getEquipotential();

			// paint a dot
			g2.setStroke(Module.busStroke);
			g2.setPaint(Module.cnxColor);
			double radius = Module.cnxStrokeWidth * 1.2;
			g2.fill(new Ellipse2D.Double(loc.getX() - radius, loc.getY() - radius, 2 * radius, 2 * radius));
			// paint pin
			compModule.drawPin(g2, pinId, compModule, false, false);

			// paint control rectangle
			g2.setPaint(Module.interfacePinColor);
			g2.fill(new Rectangle2D.Double(loc.getX() - Module.cnxPtRectWidth / 2., loc.getY() - Module.cnxPtRectWidth / 2., Module.cnxPtRectWidth, Module.cnxPtRectWidth));

			// paint label
			if (compModule.isExtPinLabelVisible(pin)) {
				ExtPinLabel label = compModule.getExtPinLabel(pin);
				label.paint2(g2, true);
			}
		}

		// paint module name labels
		if (compModule.isModuleNameLabelVisible(0)) {
			ModuleNameLabel moduleNameLabel = compModule.getModuleNameLabel(0);
			moduleNameLabel.paint2(g2, true);
		}
		if (compModule.isModuleNameLabelVisible(1)) {
			ModuleNameLabel moduleNameLabel = compModule.getModuleNameLabel(1);
			moduleNameLabel.paint2(g2, true);
		}

		// draw the point of origin cross
		g.setColor(Module.interfaceCrossColor);
		g2.setStroke(Module.crossStroke);
		g2.draw(new Line2D.Double(0., -20., 0., 20.));
		g2.draw(new Line2D.Double(-20., 0., 20., 0.));
	}
}

