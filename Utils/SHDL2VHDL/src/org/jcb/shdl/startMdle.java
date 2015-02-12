
package org.jcb.shdl;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.imageio.*;

import org.jcb.shdl.*;



public class startMdle {

	public static void main(String[] args) {

		MdleFrame frame = new MdleFrame("Matrix");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 900);
		frame.setVisible(true);
	}
}

class MdleFrame extends JFrame {

	private Matrix matrix;
	private MatrixModel matrixModel;
	private JTable table;
	private JButton propagateRowButton;
	private CompoundModule compMod;
	private ModuleDesignPanel modulePanel;
	private ModuleInterfacePanel interfacePanel;
	private JTextField modName;


	public MdleFrame(String title) {
		matrix = new Matrix(new String[] { "H", "X", "WR", "RST", "DATA", "Q" },
						new int[] { 1, 4, 1, 1, 4, 4 });
		// RST,H,D,Q
		Propagator ffd = new FlipFlopDPropagator(matrix, "FFD", 25);
		matrix.connectPropagatorToEqui(3, ffd, 0);
		matrix.connectPropagatorToEqui(0, ffd, 1);
		matrix.connectPropagatorToEqui(1, ffd, 2);
		matrix.connectPropagatorToEqui(5, ffd, 3);
		// E,S,OE
		Propagator b1 = new Buff3StatePropagator(matrix, "B1", true, 10);
		matrix.connectPropagatorToEqui(5, b1, 0);
		matrix.connectPropagatorToEqui(4, b1, 1);
		matrix.connectPropagatorToEqui(2, b1, 2);
		// E,S,OE
		Propagator b2 = new Buff3StatePropagator(matrix, "B2", false, 10);
		matrix.connectPropagatorToEqui(4, b2, 0);
		matrix.connectPropagatorToEqui(1, b2, 1);
		matrix.connectPropagatorToEqui(2, b2, 2);

		Module b1m = new Buff3StateModule(0, new NumExprVal(1), true);
		Module b2m = new Buff3StateModule(1, new NumExprVal(1), true);
		Module b3m = new Buff3StateModule(2, new NumExprVal(1), true);
		Module b4m = new Buff3StateModule(3, new NumExprVal(1), true);
		Module b5m = new Buff3StateModule(4, new NumExprVal(32), true);
		Module b6m = new Buff3StateModule(5, new NumExprVal(32), true);

		MatrixPanel matrixPanel = new MatrixPanel();
		matrixPanel.setPreferredSize(new Dimension(100, 50));

		ResourceBundle labels = ResourceBundle.getBundle("org.jcb.shdl.MdleBundle");
		compMod = new CompoundModule(0, new NumExprVal(1));
		interfacePanel = new ModuleInterfacePanel(compMod, labels);
		modulePanel = new ModuleDesignPanel(compMod, interfacePanel, labels);

		JPanel modPanel = new JPanel(new BorderLayout());
		JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 60, 400, 200);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		scaleSlider.addChangeListener(new ScaleChangeListener());
		JPanel buttonsPanel = new JPanel();
		JButton undo = new JButton("undo");
		undo.addActionListener(new UndoListener());
		buttonsPanel.add(undo);
		JButton redo = new JButton("redo");
		redo.addActionListener(new RedoListener());
		buttonsPanel.add(redo);
		JButton save = new JButton("save");
		save.addActionListener(new SaveListener());
		buttonsPanel.add(save);
		JButton load = new JButton("load");
		load.addActionListener(new LoadListener());
		buttonsPanel.add(load);
		JButton print = new JButton("print");
		print.addActionListener(new PrintListener());
		buttonsPanel.add(print);
		modName = new JTextField(compMod.getModuleName(), 10);
		modName.addActionListener(new ModNameChangeActionListener());
		buttonsPanel.add(modName);
		buttonsPanel.add(scaleSlider);
		modPanel.add("Center", new CenterPanel(modulePanel));
		modPanel.add("North", buttonsPanel);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add("East", new EastPanel(interfacePanel));
		northPanel.add(matrixPanel);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add("North", northPanel);
		panel.add("Center", modPanel);
		setContentPane(panel);
	}

	class EastPanel extends JScrollPane {
		public EastPanel(JPanel panel) {
			super(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		Dimension DIM = new Dimension(400, 200);
		public Dimension getPreferredSize() {
			return DIM;
		}
	}

	class CenterPanel extends JScrollPane {
		public CenterPanel(JPanel panel) {
			super(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
	}

	class MatrixPanel extends JPanel {
	
		public MatrixPanel() {
			matrixModel = new MatrixModel();
			table = new JTable(matrixModel);
			JScrollPane scrollPane = new JScrollPane(table);
			table.getSelectionModel().addListSelectionListener(new RowSelectionListener());
	
			JPanel buttonsPanel = new JPanel();
			JButton newRowButton = new JButton("new");
			newRowButton.addActionListener(new NewRowActionListener());
			buttonsPanel.add(newRowButton);
			propagateRowButton = new JButton("propagate");
			propagateRowButton.setEnabled(false);
			propagateRowButton.addActionListener(new PropagateRowActionListener());
			buttonsPanel.add(propagateRowButton);
	
			this.setLayout(new BorderLayout());
			add("Center", scrollPane);
			add("North", buttonsPanel);
		}
	}
	
	class UndoListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			compMod.undo();
			modulePanel.repaint();
			interfacePanel.repaint();
		}
	}
	
	class RedoListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			compMod.redo();
			modulePanel.repaint();
			interfacePanel.repaint();
		}
	}
	
	class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(modName.getText() + ".sch"));
				compMod.saveStack(bw);
				modulePanel.repaint();
				interfacePanel.repaint();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	class LoadListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(modName.getText() + ".sch"));
				compMod.loadStack(br);
				modulePanel.repaint();
				interfacePanel.repaint();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class PrintListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				BufferedImage bi = new BufferedImage(3000, 4000, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = bi.createGraphics();
				modulePanel.paint2(g2, 3.0);
				ImageIO.write(bi, "png", new File(modName.getText() + ".png"));

			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class ScaleChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider) e.getSource();
			modulePanel.setScale(slider.getValue() / 100.);
			interfacePanel.setScale(slider.getValue() / 100.);
			repaint();
		}
	}

	class ModNameChangeActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			JTextField f = (JTextField) ev.getSource();
			compMod.setModuleName(f.getText());
			interfacePanel.repaint();
		}
	}

	
	class RowSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent ev) {
			int[] rows = table.getSelectedRows();
			propagateRowButton.setEnabled(rows.length > 0);
		}
	}
	
	class NewRowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			long time = 0;
			MatrixRow last = matrix.lastRow();
			if (last != null) time = last.time + 1;
			matrix.createRow(time);
			matrixModel.fireTableChanged(new TableModelEvent(matrixModel));
		}
	}

	class PropagateRowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int[] rows = table.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				MatrixRow row = matrix.getRow(rows[i]);
				if (!row.toPropagate) continue;
				ArrayList props = new ArrayList();
				// collect all propagators connected to changing equis
				for (int j = 0; j < matrix.getNbEqui(); j++) {
					Ev ev = row.vals[j];
					if (ev == null) continue;
					if (row.time != ev.org_time) continue;
					ArrayList lprop = matrix.getConnectedPropagators(j);
					for (int k = 0; k < lprop.size(); k++) {
						Propagator prop = (Propagator) lprop.get(k);
						if (!props.contains(prop)) props.add(prop);
					}
				}
				// propagate changes through these propagators
				for (int j = 0; j < props.size(); j++) {
					Propagator prop = (Propagator) props.get(j);
					Ev[] changes = new Ev[prop.nbPins()];
					for (int k = 0; k < prop.nbPins(); k++) {
						int equiIndex = prop.getEquiIndex(k);
						Ev ev = row.vals[equiIndex];
						if (ev == null) continue;
						if (row.time != ev.org_time) continue;
						changes[k] = ev;
					}
					prop.propagateChanges(row.time, changes);
				}
				row.toPropagate = false;
			}
			matrixModel.fireTableChanged(new TableModelEvent(matrixModel));
		}
	}

	class MatrixModel extends AbstractTableModel {

		public int getColumnCount() {
			return (matrix.getNbEqui() + 2);
		}

		public String getColumnName(int col) {
			if (col == 0) return "time";
			if (col == 1) return "to propagate";
			return (matrix.getEquiName(col - 2));
		}

		public int getRowCount() {
			return matrix.getNbRows();
		}

		public Object getValueAt(int row, int col) {
			MatrixRow mrow = (MatrixRow) matrix.getRow(row);
			if (col == 0) return ("" + mrow.time);
			if (col == 1) {
				if (mrow.toPropagate) return "*"; else return "";
			}
			Ev ev = mrow.vals[col - 2];
			String sev = "";
			if (ev != null) {
				String ssrce = " EXT";
				if (ev.source != null) ssrce = " " + ev.source.getName();
				//sev = ssrce + "->" + ev.val;
			}
			if ((ev != null) && (ev.conflict != null)) {
				StringBuffer sb = new StringBuffer("!!!");
				for (int i = 0; i < ev.conflict.size(); i++) {
					if (i > 0) sb.append(",");
					Ev e = (Ev) ev.conflict.get(i);
					sb.append(e + "");
				}
				return sev + new String(sb);
			}
			String val = matrix.getValue(mrow.time, col - 2);
			return val + sev;
		}

		public boolean isCellEditable(int row, int col) {
			if (col == 0) {
				MatrixRow mrow = (MatrixRow) matrix.getRow(row);
				MatrixRow last = (MatrixRow) matrix.lastRow();
				// only the time of the last row can be changed
				return (mrow == last);
			}
			return (col != 1);
		}

		public void setValueAt(Object val, int row, int col) {
			MatrixRow mrow = (MatrixRow) matrix.getRow(row);
			if (col == 0) {
				if (!mrow.areAllEventsExternal()) {
					System.out.println("time=" + mrow.time + ": time cannot be changed since some events are internally generated");
					return;
				}
				mrow.time = Long.parseLong((String) val);
			} else {
				int equiIndex = col - 2;
				// externally generated: source = null
				Ev ev = new Ev(equiIndex, mrow.time, null, (String) val);
				mrow.setEv(equiIndex, ev);
				matrixModel.fireTableChanged(new TableModelEvent(matrixModel));
			}
		}

	}
}
	
