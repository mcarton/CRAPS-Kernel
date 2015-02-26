
// version 4.0

package org.jcb.shdl;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;



public class comm {


	private Pattern wp = Pattern.compile("\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,(-?[0-9]+)\\s*");
	private Pattern dp = Pattern.compile("\\s*([\\w]+)\\s*:\\s*(in|out)\\s*\\[([0-9]+)\\.\\.([0-9]+)\\]\\s*((\\((-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*\\))?)\\s*");

	private DataModel dataModel;
	private File iniFile;
	
	private CommThread commThread;
	private CommFrame commFrame;
	
	private PrintWriter logPR;
	
	private final int N = 128;
	

	public static void main(String[] args) {
		new comm();
	}
	
	
	public comm() {
		try {
			// creation du thread de comm
			commThread = new CommThread();
			if (CommThread.init() < 0) {
				// impossible de capturer une erreur possible (??)
				CommThread.closeData();
				System.exit(0);
			}
			if (CommThread.openData() < 0) {
				JOptionPane.showMessageDialog(null, "Unable to establish USB connection. Possible causes are:\n  - cable unplugged or board not powered\n  - ExPort software is running\n  - board not listed in Device Table (run 'ExPort->Configure')", "Connection error", JOptionPane.ERROR_MESSAGE);
				CommThread.closeData();
				System.exit(0);
			}
			
			// ouvre 'comm.ini' ou le créé vide
			iniFile = new File("comm.ini");
			iniFile.createNewFile();
			
			// lit comm.ini et traduit ses données dans dataModel
//			InitDataDialog initDataDialog = new InitDataDialog();
//			if (!initDataDialog.isReady()) System.exit(1);
			if (!parseCommIni()) System.exit(1);
			
			// creation fichier de log
			logPR = new PrintWriter(new FileWriter("comm.log"));
		
			// lancement de l'interface
			commFrame = new CommFrame("commUSB v4.10");
			
			commFrame.setSize(dataModel.getFrameSize());
			commFrame.setLocation(dataModel.getFrameLocation());
			commFrame.setVisible(true);
			
			// lancement du thread de lecture
			commThread.start();
			
		} catch(Exception ex) {
			ex.printStackTrace();
			CommThread.closeData();
			logPR.close();
			System.exit(0);
		} finally {
		}
	}
		
	
	static class DataPad {
		private String sigName;
		private String type;
		private int n1;
		private int n2;
		private int x;
		private int y;
		private int w;
		private int h;
		private JInternalFrame internalFrame;
	
		public DataPad(String sigName, String type, int n1, int n2, int x, int y, int w, int h) {
			this.sigName = sigName;
			this.type = type;
			this.n1 = n1;
			this.n2 = n2;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		public String getSigName() { return sigName; }
		public void setSigName(String sigName) {
			this.sigName = sigName;
		}
		public String getType() { return type ; }
		public void setType(String type) {
			this.type = type;
		}
		public int getN1() { return n1 ; }
		public void setN1(int n1) {
			this.n1 = n1;
		}
		public int getN2() { return n2 ; }
		public void setN2(int n2) {
			this.n2 = n2;
		}
		public int getX() { return x ; }
		public int getY() { return y ; }
		public Dimension getDimension() { return new Dimension(w, h); }
		public void setInternalFrame(JInternalFrame internalFrame) {
			this.internalFrame = internalFrame;
		}
		public JInternalFrame getInternalFrame() {
			return internalFrame;
		}
	}
	
	static class Scalar {
		public String name;
		public int i;
		public boolean isPartOfVector;
		public String type;
		public int bitnum;
		public Scalar(String name, int i, boolean isPartOfVector, String type, int bitnum) {
			this.name = name;
			this.i = i;
			this.isPartOfVector = isPartOfVector;
			this.type = type;
			this.bitnum = bitnum;
		}
		public String toString() {
			if (isPartOfVector)
				return (name + "[" + i + "]");
			else
				return name;
		}
	}

	
	class DataModel extends DefaultTableModel {
		
		// list of data pads
		private ArrayList dataPads = new ArrayList();
		// commFrame geometry
		private Point loc = new Point(100, 100);
		private Dimension dim = new Dimension(500, 300);

		
		public void add(DataPad dataPad) {
			dataPads.add(dataPad);
		}
		public DataPad get(int i) {
			return ((DataPad) dataPads.get(i));
		}
		public void remove(int i) {
			dataPads.remove(i);
		}
		
//		public DataPad getDataPad(String prefix) {
//			for (int i = 0; i < getRowCount(); i++) {
//				DataPad dp = (DataPad) dataPads.get(i);
//				if (dp.getSigName().equals(prefix)) {
//					return dp;
//				}
//			}
//			return null;
//		}
		
		public Scalar getBoard2pc(int bitNum) {
			for (int i = 0; i < getRowCount(); i++) {
				DataPad dp = (DataPad) dataPads.get(i);
				if (!dp.getType().equals("in")) continue;
				if ((bitNum >= dp.getN2()) && (bitNum <= dp.getN1())) {
					return new Scalar(dp.getSigName(), bitNum - dp.getN2(), (dp.getN1() != dp.getN2()), "in", bitNum);
				}
			}
			return null;
		}
		
		public Scalar getPc2board(int bitNum) {
			for (int i = 0; i < getRowCount(); i++) {
				DataPad dp = (DataPad) dataPads.get(i);
				if (!dp.getType().equals("out")) continue;
				if ((bitNum >= dp.getN2()) && (bitNum <= dp.getN1())) {
						return new Scalar(dp.getSigName(), bitNum - dp.getN2(), (dp.getN1() != dp.getN2()), "out", bitNum);
				}
			}
			return null;
		}
		
		public int getBitNum(String prefix, int index, String type) {
			for (int i = 0; i < getRowCount(); i++) {
				DataPad dp = (DataPad) dataPads.get(i);
				if (!dp.getType().equals(type)) continue;
				if (dp.getSigName().equals(prefix)) {
					return (index + dp.getN2());
				}
			}
			return -1;
		}
		
		public int getColumnCount() {
			return 4;
		}
		public String getColumnName(int col) {
			switch (col) {
				case 0: return "name";
				case 1: return "type";
				case 2: return "n1";
				case 3: return "n2";
				default: return "---";
			}
		}
		public int getRowCount() {
			try {
				return dataPads.size();
			} catch(Exception ex) {
				return 0;
			}
		}
		public Object getValueAt(int row, int col) {
			DataPad dp = (DataPad) dataPads.get(row);
			switch (col) {
				case 0: return dp.getSigName();
				case 1: if (dp.getType().equals("in")) return "board2pc"; else return "pc2board";
				case 2: return dp.getN1();
				case 3: return dp.getN2();
				default: return "---";
			}
		}
		public void setValueAt(Object val, int row, int col) {
			DataPad dp = (DataPad) dataPads.get(row);
			switch (col) {
				case 0: dp.setSigName((String) val); break;
				case 1: if (val.equals("board2pc")) dp.setType("in"); else dp.setType("out"); break;
				case 2: dp.setN1((Integer) val); break;
				case 3: dp.setN2((Integer) val); break;
			}
		}
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
		
		// commFrame geometry
		public Point getFrameLocation() {
			return loc;
		}
		public void setFrameLocation(Point loc) {
			this.loc = loc;
		}
		public Dimension getFrameSize() {
			return dim;
		}
		public void setFrameSize(Dimension dim) {
			this.dim = dim;
		}
	}

	
	boolean parseCommIni() {
		dataModel = new DataModel();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(iniFile));
			String firstLine = br.readLine();
			if (firstLine != null) {
				Matcher m = wp.matcher(firstLine);
				if (m.matches()) {
					int x = Integer.parseInt(m.group(1));
					int y = Integer.parseInt(m.group(2));
					dataModel.setFrameLocation(new Point(x, y));
					int w = Integer.parseInt(m.group(3));
					int h = Integer.parseInt(m.group(4));
					dataModel.setFrameSize(new Dimension(w, h));
					//System.out.println("x=" + x + ", y=" + y + ", w=" + w + ", h=" + h);
				}
				while (true) {
					String line = br.readLine();
					if (line == null) break;
					//System.out.println("line0=" + line);
					m = dp.matcher(line);
					boolean b = m.matches();
					if (b) {
						String sigName = m.group(1);
						String type = m.group(2);
						int n1 = Integer.parseInt(m.group(3));
						int n2 = Integer.parseInt(m.group(4));
						int x = 0;
						int y = 0;
						int w = 150;
						int h = 70;
						if (m.group(5).trim().length() > 0) {
							x = Integer.parseInt(m.group(7));
							y = Integer.parseInt(m.group(8));
							w = Integer.parseInt(m.group(9));
							h = Integer.parseInt(m.group(10));
						}
						//System.out.println("sigName=" + sigName + ", type=" + type + ", n1=" + n1 + ", n2=" + n2 + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h);
						dataModel.add(new DataPad(sigName, type, n1, n2, x, y, w, h));
					} else  {
						System.out.println("syntax error : " + line);
						return false;
					}
				}
			}
		} catch(Exception ex) {
			System.out.println("unknown error : " + ex.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
                                }
				catch (IOException e) {}
			}
		}
		return true;
	}
	
	class InitDataDialog extends JDialog {
		
		private JButton addButton;
		private JButton delButton;
		private JButton okButton;
		private JTable table;
		private boolean ready;
		
		public InitDataDialog() throws Exception {
			super((JFrame) null, "Specify inputs and/or outputs", true);
			JPanel panel = new JPanel(new BorderLayout());
			
			parseCommIni();

			table = new JTable(dataModel);
			table.getColumnModel().getColumn(1).setMaxWidth(70);
			table.getColumnModel().getColumn(2).setMaxWidth(50);
			table.getColumnModel().getColumn(3).setMaxWidth(50);
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("pc2board");
			comboBox.addItem("board2pc");
			table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
			//table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(new DataListSelectionListener());
			panel.add("Center", new JScrollPane(table));
			
			JPanel southPanel = new JPanel();
			addButton = new JButton("add");
			southPanel.add(addButton);
			addButton.addActionListener(new AddActionListener());
			delButton = new JButton("remove");
			southPanel.add(delButton);
			delButton.addActionListener(new DelActionListener());
			okButton = new JButton("OK");
			southPanel.add(okButton);
			okButton.addActionListener(new OKActionListener());
			
			panel.add("South", southPanel);
	
			setContentPane(panel);	
			setLocation(100, 100);
			setSize(400, 400);
			setVisible(true);
		}
		
		public boolean isReady() {
			return ready;
		}
		
		class DataListSelectionListener implements ListSelectionListener {
			public void valueChanged(ListSelectionEvent e) {
				delButton.setEnabled(table.getSelectedRowCount() > 0);
			}
		}
		
		private int xi = 50;
		private int yi = 50;
		class AddActionListener implements ActionListener {
			public void actionPerformed(ActionEvent ev) {
				dataModel.add(new DataPad("XXX", "in", 0, 0, xi, yi, 150, 70));
				xi += 40;
				yi += 40;
				dataModel.fireTableChanged(new TableModelEvent(dataModel));
			}
		}
		
		class DelActionListener implements ActionListener {
			public void actionPerformed(ActionEvent ev) {
				int[] indexes = table.getSelectedRows();
				for (int i = indexes.length - 1; i >= 0; i--) {
					dataModel.remove(indexes[i]);
				}
				dataModel.fireTableChanged(new TableModelEvent(dataModel));
			}
		}
		
		class OKActionListener implements ActionListener {
			public void actionPerformed(ActionEvent ev) {
				/*
				try {
					System.out.println("écriture comm.ini, dataModel.getRowCount()=" + dataModel.getRowCount());
					PrintWriter pr = new PrintWriter(new FileWriter(iniFile));
					for (int i = 0; i < dataModel.getRowCount(); i++) {
						DataPad pad = dataModel.get(i);
						String line = pad.getSigName() + ": " + pad.getType() + "[" +
							pad.getN1() + ".." + pad.getN2() + "] (" +
							pad.getX() + "," + pad.getY() + "," +
							pad.getDimension().getWidth() + "," +
							pad.getDimension().getHeight() + ")";
						pr.println(line);
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				*/
				ready = true;
				setVisible(false);
			}
		}	
	}
	
	
	public boolean parseLine(String line) {
		Matcher m = dp.matcher(line);
		boolean b = m.matches();
		if (b) {
			String sigName = m.group(1);
			String type = m.group(2);
			int n1 = Integer.parseInt(m.group(3));
			int n2 = Integer.parseInt(m.group(4));
			int x = 0;
			int y = 0;
			int w = 150;
			int h = 70;
			if (m.group(5).trim().length() > 0) {
				x = Integer.parseInt(m.group(7));
				y = Integer.parseInt(m.group(8));
				w = Integer.parseInt(m.group(9));
				h = Integer.parseInt(m.group(10));
			}
			System.out.println("sigName=" + sigName + ", type=" + type + ", n1=" + n1 + ", n2=" + n2 + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h);
			dataModel.add(new DataPad(sigName, type, n1, n2, x, y, w, h));
		} else  {
			System.out.println("syntax error : " + line);
		}
		return b;
	}
	
	
	public void writeIniData() {
		System.out.println("écriture comm.ini");
		try {
			PrintWriter pr = new PrintWriter(new FileWriter(iniFile));
			// window geometry
			Point wloc = commFrame.getLocation();
			Dimension wdim = commFrame.getSize();
			pr.println((int)wloc.getX() + "," + (int)wloc.getY() + "," +
				(int)wdim.getWidth() + "," + (int)wdim.getHeight());
			// pad data
			for (int i = 0; i < dataModel.getRowCount(); i++) {
				DataPad pad = dataModel.get(i);
				Point loc = pad.getInternalFrame().getLocation();
				Dimension dim = pad.getInternalFrame().getSize();
				pr.println(pad.getSigName() + ": " + pad.getType() + "[" +
					pad.getN1() + ".." + pad.getN2() + "] (" +
					(int)loc.getX() + "," + (int)loc.getY() +
					"," + (int)dim.getWidth() + "," + (int)dim.getHeight() + ")");
			}
			pr.flush();
			pr.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	
	class CommFrame extends JFrame implements CommListener, WindowListener {
		
		private JFrame myself;
		private JDesktopPane desktop;
		private InButtonModel[] buttonModels;
		private boolean ins[];
		private boolean outs[];
		private JPopupMenu popup;
		
		private JRadioButton[] buttonOutTable;
		private JRadioButton[] buttonInTable;
	
		
		public CommFrame(String title) {
			super(title);
			myself = this;
			commThread.addCommListener(this);
	
			ins = new boolean[N];
			outs = new boolean[N];
			buttonModels = new InButtonModel[N];
			for (int j = 0; j < N; j++) {
				ins[j] = false;
				outs[j] = false;
				buttonModels[j] = new InButtonModel(j);
			}
			
			JFrame.setDefaultLookAndFeelDecorated(true);
			desktop = new JDesktopPane();
			
			buttonOutTable = new JRadioButton[N];
			buttonInTable = new JRadioButton[N];
			for (int i = 0; i < dataModel.getRowCount(); i++) {
				DataPad pad = dataModel.get(i);
				JInternalFrame frame = null;
				if (pad.getType().equals("in")) {
					frame = new InInternalFrame(pad, buttonInTable);
				} else {
					frame = new OutInternalFrame(pad, buttonOutTable);
				}
				pad.setInternalFrame(frame);
				frame.setLocation(pad.getX(), pad.getY());
				frame.setVisible(true);
				desktop.add(frame);
				try {
					frame.setSelected(true);
					//frame.setIcon(true);
				} catch (java.beans.PropertyVetoException e) {
					e.printStackTrace();
				}
			}
			
		    JMenuBar jmb = new JMenuBar();
		    JMenu jm = new JMenu("test");
			JMenuItem loadTestItem = new JMenuItem("load test vector...");
			loadTestItem.addActionListener(new LoadTestVectorListener());
		    jm.add(loadTestItem);
//		    jmb.add(jm);
			JMenuItem commentItem = new JMenuItem("insert comment...");
			commentItem.addActionListener(new InsertCommentListener());
		    jm.add(commentItem);
//		    jmb.add(jm);
			JMenuItem expectItem = new JMenuItem("add expectations");
			expectItem.addActionListener(new AddExpectationsListener());
		    jm.add(expectItem);
		    
		    jmb.add(jm);
		    setJMenuBar(jmb);
		    
			addWindowListener(this);
			setContentPane(desktop);
		}
		
		public class LoadTestVectorListener implements ActionListener {
			public void actionPerformed(ActionEvent ev) {
				JFileChooser chooser = new JFileChooser(".");
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(".log files", "log");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(myself);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
					new TestDialog(myself, chooser.getSelectedFile());
			    }
			}
		}
		
		public class InsertCommentListener implements ActionListener {
			public void actionPerformed(ActionEvent ev) {
				String line = JOptionPane.showInputDialog("Enter comment to be inserted in comm.log");
				logPR.println("// " + line);
				logPR.flush();
			}
		}
		
		public class AddExpectationsListener implements ActionListener {
			public void actionPerformed(ActionEvent ev) {
				for (int i = N - 1; i >= 0; i--) {
					Scalar sc = dataModel.getBoard2pc(i);
					if (sc != null) {
						logPR.println("expect\t" + dataModel.getBoard2pc(i) + "\t" + (ins[i] ? "1" : "0"));
					}
				}
				logPR.flush();
			}
		}
		
		
		class TestDialog extends JDialog {
			
			private BufferedReader br;
			private JScrollPane scrollPane;
			private JTextArea messages = new JTextArea();
			private int nbSteps = 0;
			private int nbErrors = 0;
			private JLabel steps = new JLabel("   line 0");
			private JLabel errors = new JLabel("no error");
			private JButton nextButton = new JButton("next step");
			private JButton nextErrorButton = new JButton("next error");
			private Pattern waitPattern = Pattern.compile("\\s*wait\\s*([0-9]+)\\s*");
			private Pattern setExpectPattern = Pattern.compile("\\s*(set|expect)\\s*([\\S]+)\\s*([0-1])\\s*");
			private Pattern vectSigPattern = Pattern.compile("(\\w*)\\x5B([0-9]+)\\x5D");
			private Pattern sigNamePattern = Pattern.compile("(\\w*)");

			
			public TestDialog(JFrame parent, File testFile) {
				super(parent, "Test Vector", false);
				try {
					br = new BufferedReader(new FileReader(testFile));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// deselection de tous les boutons en entree
				for (int i = 0; i < N; i++) {
					JRadioButton button = buttonOutTable[i];
					if (button == null) continue;
					button.setSelected(false);
					outValueSet(i, 0);
				}


				JPanel northPanel = new JPanel(new GridLayout(1, 0));
				steps.setFont(this.getFont().deriveFont(20.f));
				northPanel.add(steps);
				errors.setFont(this.getFont().deriveFont(20.f));
				northPanel.add(errors);
				northPanel.setBorder(BorderFactory.createLineBorder(Color.black));
				
				JPanel buttonsPanel = new JPanel();
				buttonsPanel.add(nextButton);
				nextButton.addActionListener(new NextTestListener());
				buttonsPanel.add(nextErrorButton);
				nextErrorButton.addActionListener(new NextErrorTestListener());
				
				JPanel panel = new JPanel(new BorderLayout());
				panel.add("North", northPanel);
				messages.setEditable(false);
				scrollPane = new JScrollPane(messages);
				panel.add("Center", scrollPane);
				panel.add("South", buttonsPanel);
				
				setContentPane(panel);	
				setLocation(100, 100);
				setSize(400, 200);
				setVisible(true);
			}
			
			private String setLineToDo = null;
			private int lineNo = 0;
			
			class NextTestListener implements ActionListener {
				public void actionPerformed(ActionEvent ev) {
					Thread thread = new Thread(){ 
						public void run() {
							nextErrorButton.setEnabled(false);
							nextButton.setEnabled(false);
							String res = testStep(true);
							if (res.equals("finished")) {
								nextErrorButton.setEnabled(false);
								nextButton.setEnabled(false);
							} else {
								nextErrorButton.setEnabled(true);
								nextButton.setEnabled(true);
							}
						} 
					};
					thread.start();
				}
			}
			
			class NextErrorTestListener implements ActionListener {
				public void actionPerformed(ActionEvent ev) {
					Thread thread = new Thread(){ 
						public void run() {
							nextErrorButton.setEnabled(false);
							nextButton.setEnabled(false);
							String res = testStep(false);
							if (res.equals("finished")) {
								nextErrorButton.setEnabled(false);
								nextButton.setEnabled(false);
							} else {
								nextErrorButton.setEnabled(true);
								nextButton.setEnabled(true);
							}
						} 
					};
					thread.start();
				}
			}
			
			String testStep(boolean stopEveryStep) {
				// step = set+,wait,expect*
				
				try {
					boolean afterWaitOrExpect = false;
					while (true) {
						String line = null;
						if (setLineToDo != null) {
							line = setLineToDo;
							setLineToDo = null;
						} else {
							line = br.readLine();
							System.out.println("lineno=" + lineNo++ + ", line=" + line);
						}
						if (line == null) return "finished";
						line = line.trim();
						
						// écrit la ligne dans comm.log
						logPR.println(line);
						logPR.flush();

						if (line.startsWith("//")) {
							addMessageLine(line + "\n");
							
						} else if (line.startsWith("set")) {
							if (afterWaitOrExpect) {
								setLineToDo = line;
								afterWaitOrExpect = false;
								if (stopEveryStep) return "step";
							} else {
								String res = processLine(line);
								if (res.equals("error_expect")) return "error_expect";
							}
							
						} else if (line.startsWith("wait")) {
							steps.setText("   line " + lineNo);
							nbSteps += 1;
							afterWaitOrExpect = true;
							processLine(line);
							
						} else if (line.startsWith("expect")) {
							afterWaitOrExpect = true;
							String res = processLine(line);
							if (res.equals("error_expect")) return "error_expect";
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				return "error"; // ne doit jamais aller ici...
			}
			
			
			String processLine(String line) throws Exception {
				addMessageLine(line + "\n");
				Matcher wm = waitPattern.matcher(line);
				if (wm.matches()) {
					// wait
					Thread.sleep(150);
					return "wait";
				} else {
					Matcher sm = setExpectPattern.matcher(line);
					if (sm.matches()) {
						// set or expect
						String type = sm.group(1);
						String sigName = sm.group(2);
						int val = Integer.parseInt(sm.group(3));
						String prefix = null;
						int index = 0;
						int bitnum = -1;
						Matcher vsm = vectSigPattern.matcher(sigName);
						if (vsm.matches()) {
							prefix = vsm.group(1);
							index = Integer.parseInt(vsm.group(2));
						} else {
							Matcher ssm = sigNamePattern.matcher(sigName);
//							System.out.println("signame=" + sigName + "--");
							if (ssm.matches()) {
								prefix = ssm.group(1);
							} else {
								addMessageLine("*** syntax error: " + sigName + "++\n");
							}
						}
						
						if (type.equals("set"))
							bitnum = dataModel.getBitNum(prefix, index, "out");
						else if (type.equals("expect"))
							bitnum = dataModel.getBitNum(prefix, index, "in");

						if ((prefix == null) || (bitnum == -1)) {
							addMessageLine("*** syntax error: " + line + "\n");
							
						} else if (type.equals("set")) {
							// actually apply set value
							JRadioButton button = buttonOutTable[bitnum];
							button.setSelected(val == 1);
							outValueSet(bitnum, val);
							
						} else if (type.equals("expect")) {
							// verify expected value
							int found = (ins[bitnum] ? 1 : 0);
							if (val != found) {
								addMessageLine("*** error: expected " + val + ", found " + found + "\n");
								errors.setText(++nbErrors + " error" + (nbErrors > 1 ? "s" : ""));
								// highlight the problem
								JRadioButton button = buttonInTable[bitnum];
								button.setBackground(Color.red);
								return "error_expect";
							}
						}
						return type;
					} else {
						addMessageLine("*** syntax error: " + line + "\n");
					}
				}
				return "error";
			}
			
			void addMessageLine(String str) {
				messages.append(str);
				// il faut attendre un peu (?) avant de scroller
				try {
					Thread.sleep(10);
					JScrollBar sb = scrollPane.getVerticalScrollBar();
					sb.setValue(sb.getMaximum());
				} catch (Exception e) {
				}
			}
		}

		int getByte(int i) {
			int res = 0;
			if (outs[8*i]) res |= 1;
			if (outs[8*i+1]) res |= 2;
			if (outs[8*i+2]) res |= 4;
			if (outs[8*i+3]) res |= 8;
			if (outs[8*i+4]) res |= 16;
			if (outs[8*i+5]) res |= 32;
			if (outs[8*i+6]) res |= 64;
			if (outs[8*i+7]) res |= 128;
			return res;
		}
	
		private int[] prevBitVector;
		
		public void valueChanged(CommEvent ev) {
			int[] bitVector = ev.getBitVector();
			for (int i = N - 1; i >= 0; i--) {
				ins[i] = (bitVector[i] == 1);
				buttonModels[i].fireChange();
			}
			for (int i = N - 1; i >= 0; i--) {
				if ((prevBitVector == null) || (bitVector[i] != prevBitVector[i])) {
					ins[i] = (bitVector[i] == 1);
				}
			}
			prevBitVector = bitVector.clone();
		}
		
		public void outValueSet(int i, int val) {
			outs[i] = (val == 1);
			repaint();
			try {
				commThread.sendByte(i / 8, getByte(i / 8));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		class ButtonActionListener implements ActionListener {
			private int i;
			public ButtonActionListener(int i) {
				this.i = i;
			}
			public void actionPerformed(ActionEvent ev) {
				JRadioButton button = (JRadioButton) ev.getSource();
				outValueSet(i, (button.isSelected() ? 1 : 0));
				// écrit dans comm.log
				logPR.println("set\t" + dataModel.getPc2board(i) + "\t" + (outs[i] ? 1 : 0));
				logPR.println("wait\t1");
				logPR.flush();
			}
		}
	
		class InButtonModel extends DefaultButtonModel {
			private int i;
			public InButtonModel(int i) {
				this.i = i;
			}
			public boolean isSelected() {
				return ins[i];
			}
			public boolean isEnabled() {
				return false;
			}
			public void fireChange() {
				fireStateChanged();
			}
		}
	
		class InInternalFrame extends JInternalFrame {
			
			public InInternalFrame(DataPad pad, JRadioButton[] buttonInTable) {
				super(pad.getSigName(),
					true, //resizable
					false, //closable
					false, //maximizable
					true);//iconifiable
					
				JPanel panel = new JPanel();
				int n1 = pad.getN1();
				int n2 = pad.getN2();
				if (n1 < n2) {
					for (int j = n1; j <= n2; j++) {
						JRadioButton button = new JRadioButton("");
						button.setModel(buttonModels[j]);
						panel.add(button);
						buttonInTable[j] = button;
					}			
				} else {
					for (int j = n1; j >= n2; j--) {
						JRadioButton button = new JRadioButton("");
						button.setModel(buttonModels[j]);
						panel.add(button);
						buttonInTable[j] = button;
					}			
				}
				setSize(pad.getDimension());
				setContentPane(panel);
			}
		}
			
		class OutInternalFrame extends JInternalFrame {
	
			public OutInternalFrame(DataPad pad, JRadioButton[] buttonOutTab) {
				super(pad.getSigName(),
					true, //resizable
					false, //closable
					false, //maximizable
					true);//iconifiable
				
				JPanel panel = new JPanel();
				int n1 = pad.getN1();
				int n2 = pad.getN2();
				if (n1 < n2) {
					for (int j = n1; j <= n2; j++) {
						JRadioButton button = new JRadioButton("");
						button.addActionListener(new ButtonActionListener(j));
						panel.add(button);
//						System.out.println("sig=" + pad.getSigName() + ", j=" + j);
						buttonOutTab[j] = button;
					}			
				} else {
					for (int j = n1; j >= n2; j--) {
						JRadioButton button = new JRadioButton("");
						button.addActionListener(new ButtonActionListener(j));
						panel.add(button);
//						System.out.println("sig=" + pad.getSigName() + ", j=" + j);
						buttonOutTab[j] = button;
					}			
				}
				setSize(pad.getDimension());
				setContentPane(panel);
			}
		}
		
		public void windowActivated(WindowEvent ev) {}
		public void windowClosed(WindowEvent ev) {}
		public void windowClosing(WindowEvent ev) {
			writeIniData();
			//CommThread.closeData(); // provoque un dump erreur java??
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent ev) {}
		public void windowDeiconified(WindowEvent ev) {}
		public void windowIconified(WindowEvent ev) {}
		public void windowOpened(WindowEvent ev) {}
	}
}

