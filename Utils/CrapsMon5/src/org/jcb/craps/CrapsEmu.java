
package org.jcb.craps;

import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.net.*;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libegg.base.*;
import org.jcb.tools.*;
import org.jcb.shdl.*;
import org.jcb.craps.crapsc.java.*;


public class CrapsEmu extends JFrame {

	private JFrame frame;
	private JSplitPane crapsMachinePanel;
	private Color buttonBackgroundColor;
	private JFileChooser sourceFileDialog = new JFileChooser();
	private JFileChooser objectFileDialog = new JFileChooser();

	private MessagesFrame messages;

	private JTabbedPane tabbedPane;
	
	private ArrayList sourceContexts;

	private Preferences preferences;
	
	private CrapsMachine crapsMachine;

	private CommThread commThread;
	private int mon_ack = 0;
	private int[] outs = new int[64];
	
	private RunMemoryModel runMemoryModel;
	private JTable runMemoryTable;
	private JScrollPane runMemoryScrollPane;
	private String regsBase = "hexadecimal";
	private RegsModel regsModel;
	private JTable regsTable;
	private FlagModel Nmodel;
	private FlagModel Zmodel;
	private FlagModel Vmodel;
	private FlagModel Cmodel;

	private JButton stepButton;
	private JButton runButton;
	private long breakAddr = 0;

	private JMenuItem closeMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveObjAsMenuItem;


	public CrapsEmu() {

		setTitle("CRAPS assembler & monitor, v4.6");
		frame = this;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new MyWindowListener());
		
		try {		
			commThread = new CommThread();
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		commThread.addCommListener(new CrapsCommListener());
		CommThread.init();
		int error = CommThread.openData();
		if (error < 0) {
			JOptionPane.showMessageDialog(null, "Impossible to connect to CRAPS device");
			System.exit(0);
		}

		sourceFileDialog.setFileFilter(new SourceFilenameFilter());
		sourceFileDialog.setSize(150, 300);
		objectFileDialog.setFileFilter(new ObjFilenameFilter());
		objectFileDialog.setSize(150, 300);

		Dimension size = null;
		Point loc = null;
		ArrayList files = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("crapsemu.ini"));
			size = (Dimension) ois.readObject();
			loc = (Point) ois.readObject();
			preferences = (Preferences) ois.readObject();
			files = (ArrayList) ois.readObject();
			ois.close();
		} catch(Exception ex) {
			preferences = new Preferences();
			loc = new Point(500, 200);
			size = new Dimension(800, 600);
		}
		
		messages = new MessagesFrame();
		
		crapsMachine = new CrapsMachine();
				
		
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.addChangeListener(new TabChangeListener());

		Border dataBorder = BorderFactory.createEtchedBorder();

		// FLAGS PANEL
		JPanel flagsPanel = new JPanel();
		//flagsPanel.setPreferredSize(new Dimension(170, 76));
		flagsPanel.setBorder(BorderFactory.createTitledBorder(dataBorder, "Flags"));
		JRadioButton Nbutton = new JRadioButton("N");
		Nmodel = new FlagModel(0);
		Nbutton.setModel(Nmodel);
		Nbutton.setVerticalTextPosition(AbstractButton.BOTTOM);
		Nbutton.setHorizontalTextPosition(AbstractButton.CENTER);
		flagsPanel.add(Nbutton);
		JRadioButton Zbutton = new JRadioButton("Z");
		Zmodel = new FlagModel(1);
		Zbutton.setModel(Zmodel);
		Zbutton.setVerticalTextPosition(AbstractButton.BOTTOM);
		Zbutton.setHorizontalTextPosition(AbstractButton.CENTER);
		flagsPanel.add(Zbutton);
		JRadioButton Vbutton = new JRadioButton("V");
		Vmodel = new FlagModel(2);
		Vbutton.setModel(Vmodel);
		Vbutton.setVerticalTextPosition(AbstractButton.BOTTOM);
		Vbutton.setHorizontalTextPosition(AbstractButton.CENTER);
		flagsPanel.add(Vbutton);
		JRadioButton Cbutton = new JRadioButton("C");
		Cmodel = new FlagModel(3);
		Cbutton.setModel(Cmodel);
		Cbutton.setVerticalTextPosition(AbstractButton.BOTTOM);
		Cbutton.setHorizontalTextPosition(AbstractButton.CENTER);
		flagsPanel.add(Cbutton);

		// REGISTERS
		regsModel = new RegsModel();
		regsTable = new JTable(regsModel);
		regsTable.setFont(preferences.sourceFont);
		regsTable.setRowHeight(regsTable.getRowHeight() + 10);
		regsTable.getColumnModel().getColumn(0).setMaxWidth(150);
		regsTable.getColumnModel().getColumn(1).setMaxWidth(200);
		JScrollPane regsScrollPane = new JScrollPane(regsTable);

		JPanel regsPanel = new JPanel(new BorderLayout());
		regsPanel.add("West", regsScrollPane);
		JPanel flagsPanel_ = new JPanel();
		flagsPanel_.add(flagsPanel);
		regsPanel.add("East", flagsPanel_);
		regsScrollPane.setPreferredSize(new Dimension(300, 288));
		regsPanel.setBorder(BorderFactory.createTitledBorder(dataBorder, "Registers"));
		
		// RUN BUTTONS
		JPanel runButtonPanel = new JPanel(new GridLayout(0, 1));
		JButton updateButton = new JButton("update");
//		runButtonPanel.add(updateButton);
		updateButton.addActionListener(new UpdateActionListener());
		stepButton = new JButton("step");
		runButtonPanel.add(stepButton);
		stepButton.addActionListener(new StepActionListener());
		runButton = new JButton("run");
		runButton.setActionCommand("run");
		runButtonPanel.add(runButton);
		runButton.addActionListener(new RunActionListener());
		
		// MEMORY PANEL		
		runMemoryModel = new RunMemoryModel();
		runMemoryTable = new JTable(runMemoryModel);
		runMemoryScrollPane = new JScrollPane(runMemoryTable);
		runMemoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		runMemoryTable.getSelectionModel().addListSelectionListener(new RunSelectionListener());
		runMemoryTable.setFont(preferences.memoryFont);
		//runMemoryTable.setShowGrid(false);
		runMemoryTable.setShowHorizontalLines(false);
		//runMemoryTable.setShowVerticalLines(false);
		runMemoryTable.setRowHeight(runMemoryTable.getRowHeight() + 10);
		runMemoryTable.getColumnModel().getColumn(0).setMinWidth(115);
		runMemoryTable.getColumnModel().getColumn(0).setMaxWidth(115);
		runMemoryTable.getColumnModel().getColumn(1).setMinWidth(115);
		runMemoryTable.getColumnModel().getColumn(1).setMaxWidth(115);
		runMemoryTable.getColumnModel().getColumn(2).setMinWidth(25);
		runMemoryTable.getColumnModel().getColumn(2).setMaxWidth(40);
		runMemoryTable.getColumnModel().getColumn(3).setMinWidth(20);
		runMemoryTable.getColumnModel().getColumn(4).setPreferredWidth(200);
		runMemoryTable.getColumnModel().getColumn(4).setMinWidth(100);
		
		JPanel runButtonPanel_ = new JPanel();
		runButtonPanel_.add(runButtonPanel);
		
		JPanel memoryPanel = new JPanel(new BorderLayout());
		memoryPanel.add("East", runButtonPanel_);
		memoryPanel.add("Center", runMemoryScrollPane);
		memoryPanel.setBorder(BorderFactory.createTitledBorder(dataBorder, "Memory"));
		
		crapsMachinePanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, memoryPanel, regsPanel);
		crapsMachinePanel.setOneTouchExpandable(true);


		tabbedPane.addTab("CRAPS monitor", crapsMachinePanel);
		
		JMenuBar mb = new JMenuBar();
		JMenu mFile = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("new");
		newMenuItem.addActionListener(new NewSourceActionListener());
		mFile.add(newMenuItem);
		JMenuItem openMenuItem = new JMenuItem("open...");
		openMenuItem.addActionListener(new OpenSourceActionListener());
		mFile.add(openMenuItem);
		mFile.addSeparator();
		closeMenuItem = new JMenuItem("close");
		closeMenuItem.setEnabled(false);
		closeMenuItem.addActionListener(new CloseActionListener());
		mFile.add(closeMenuItem);
		saveMenuItem = new JMenuItem("save");
		saveMenuItem.setEnabled(false);
		saveMenuItem.addActionListener(new SaveActionListener());
		mFile.add(saveMenuItem);
		saveAsMenuItem = new JMenuItem("save as...");
		saveAsMenuItem.setEnabled(false);
		saveAsMenuItem.addActionListener(new SaveAsActionListener());
		mFile.add(saveAsMenuItem);
		mFile.addSeparator();
		JMenuItem loadObjMenuItem = new JMenuItem("load object file...");
		loadObjMenuItem.addActionListener(new LoadObjActionListener());
		mFile.add(loadObjMenuItem);
		saveObjAsMenuItem = new JMenuItem("save object file as...");
		saveObjAsMenuItem.setEnabled(false);
		saveObjAsMenuItem.addActionListener(new SaveObjActionListener());
		mFile.add(saveObjAsMenuItem);
		mFile.addSeparator();
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new QuitActionListener());
		mFile.add(quitMenuItem);

		JMenu mEdit = new JMenu("Edit");
		//mEdit.add(new JMenuItem("Copy"));
		//mEdit.add(new JMenuItem("Cut"));
		//mEdit.add(new JMenuItem("Paste"));
		//mEdit.add(new JMenuItem("-"));
		//mEdit.add(new JMenuItem("Delete"));

		JMenu mOptions = new JMenu("Options");

		JMenu mExamples = new JMenu("Examples");
		JMenuItem ex0MenuItem = new JMenuItem("Greatest Common Divisor");
		mExamples.add(ex0MenuItem);
		ex0MenuItem.addActionListener(new ExampleActionListener("pgcd.asm"));
		
		mb.add(mFile);
		//mb.add(mEdit);
		mb.add(mOptions);
		mb.add(mExamples);
		setJMenuBar(mb);

		// SOURCE CONTEXTS
		sourceContexts = new ArrayList();
		if (files == null) {
			// create an 'untitled' panel
			SourceContext sc = new SourceContext();
			sourceContexts.add(sc);
			JPanel sourcePanel = createSourcePanel(sc);
			tabbedPane.add(sourcePanel, 0);
			tabbedPane.setTitleAt(0, "untitled");
		} else {
			int nb = 0;
			for (int i = 0; i < files.size(); i++) {
				File file = (File) files.get(i);
				boolean err = loadSource(nb, (File) files.get(i), null);
				if (err) continue;
				nb += 1;
			}
		}

		setContentPane(tabbedPane);
		setSize(size);
		setLocation(loc);
		crapsMachinePanel.setDividerLocation(preferences.dividerLocation);
		
		// lancement de la communication
		try {
			commThread.start();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	

	class SourceContext {

		// full context of a source panel: buffer, associated file, object module, etc.

		private File sourceFile;
		private JTextPane sourceEditor;
		private JButton assembleButton;
		private JButton loadButton;
		private boolean sourceModified;
		private boolean highlightsExists;
		private ObjModule symbolTable;
		private ObjModule objModule;

		// create an 'untitled' source file
		public SourceContext() {
			sourceFile = null;
			sourceEditor = new JTextPane();
			sourceModified = false;
			highlightsExists = false;
			symbolTable = new ObjModule();
			objModule = new ObjModule();
		}

		public SourceContext(File sourceFile) {
			this.sourceFile = sourceFile;
			sourceEditor = new JTextPane();
			sourceModified = false;
			highlightsExists = false;
			symbolTable = new ObjModule();
			objModule = new ObjModule();
		}

		public String toString() {
			if (sourceFile == null) return "null";
			return sourceFile.toString();
		}

		public File getSourceFile() {
			return sourceFile;
		}

		public void setSourceFile(File sourceFile) {
			this.sourceFile = sourceFile;
		}

		public String getTitle() {
			if (sourceFile == null) return "untitled";
			return sourceFile.getName();
		}

		public JTextPane getSourceEditor() {
			return sourceEditor;
		}

		public void setSourceEditor(JTextPane sourceEditor) {
			this.sourceEditor = sourceEditor;
		}

		public JButton getLoadButton() {
			return loadButton;
		}

		public void setUploadButton(JButton loadButton) {
			this.loadButton = loadButton;
		}

		public JButton getAssembleButton() {
			return assembleButton;
		}

		public void setAssembleButton(JButton assembleButton) {
			this.assembleButton = assembleButton;
		}

		public boolean isSourceModified() {
			return sourceModified;
		}

		public void setSourceModified(boolean sourceModified) {
			this.sourceModified = sourceModified;
		}

		public boolean existHighlights() {
			return highlightsExists;
		}

		public void setHighlightsExist(boolean highlightsExists) {
			this.highlightsExists = highlightsExists;
		}

		public ObjModule getObjModule() {
			return symbolTable;
		}

	}


	class Preferences implements Serializable {
//		Color sourceColor = new Color(240, 240, 240);
		Color sourceColor = new Color(140, 140, 140);
		Color logColor = new Color(220, 220, 220);
		Font sourceFont = new Font("Courier", Font.PLAIN, 18);
		Font memoryFont = new Font("Courier", Font.PLAIN, 18);
		Font logFont = new Font("Courier", Font.PLAIN, 16);
		File currDir = new File(".");
		int dividerLocation = 250;
		Point logLocation = new Point(50, 50);
		Dimension logSize = new Dimension(500, 400);

		private void writeObject(ObjectOutputStream out) throws IOException {
			out.writeObject(sourceColor);
			out.writeObject(logColor);
			out.writeObject(sourceFont);
			out.writeObject(memoryFont);
			out.writeObject(logFont);
			out.writeObject(currDir);
			out.writeInt(dividerLocation);
			out.writeObject(logLocation);
			out.writeObject(logSize);
		}

		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
			sourceColor = (Color) in.readObject();
			logColor = (Color) in.readObject();
			sourceFont = (Font) in.readObject();
			memoryFont = (Font) in.readObject();
			logFont = (Font) in.readObject();
			currDir = (File) in.readObject();
			dividerLocation = in.readInt();
			logLocation = (Point) in.readObject();
			logSize = (Dimension) in.readObject();
		}

		public void setLogLocation(Point logLocation) {
			this.logLocation = logLocation;
		}
		public void setLogSize(Dimension logSize) {
			this.logSize = logSize;
		}
	}
	

	class NewSourceActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			loadSource(0, null, null);
		}
	}


	class TabChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent ev) {
			JTabbedPane tabpane = (JTabbedPane) ev.getSource();
			int idx = tabpane.getSelectedIndex();
			setTabIndex(idx);
		}
	}


	void addSource(File sourceFile, InputStream is) {
		int idx = sourceContexts.size() - 1;
		if (idx != -1) {
			SourceContext sc = (SourceContext) sourceContexts.get(idx);
			if ((sc.getSourceFile() == null) && !sc.isSourceModified()) {
				// remove the 'untitled' panel and context
				sourceContexts.remove(sc);
				tabbedPane.remove(0);
				idx = -1;
			}
		}
		loadSource(idx + 1, sourceFile, is);
	}


	class OpenSourceActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (preferences.currDir != null) sourceFileDialog.setCurrentDirectory(preferences.currDir);
			sourceFileDialog.showOpenDialog(frame);
			preferences.currDir = sourceFileDialog.getCurrentDirectory();
			// cancel?
			if (sourceFileDialog.getSelectedFile() == null) return;

			try {
				addSource(sourceFileDialog.getSelectedFile(), null);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class CloseActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int idx = getCurrentSourceIndex();
			if (idx == -1) return;
			boolean cancel = promptModifiedSource(idx);
			if (cancel) return;

			// remove code ?
			SourceContext sc = (SourceContext) sourceContexts.get(idx);
			/*
			if (crapsMachine.probeObjModule(sc.getObjModule())) {
				int option = JOptionPane.showConfirmDialog(frame, "Remove associated code ?", "---", JOptionPane.YES_NO_OPTION);
				try {
					if (option == JOptionPane.YES_OPTION) {
						crapsMachine.removeObjModule(sc.getObjModule());
						runMemoryModel.fireTableChanged(new TableModelEvent(runMemoryModel));
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			*/
			// remove associated data
			sourceContexts.remove(idx);
			// remove tabbed pane
			tabbedPane.remove(idx);
		}
	}

	class SaveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int idx = getCurrentSourceIndex();
			if (idx == -1) return;
			saveSource(idx, false);
		}
	}

	class SaveAsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int idx = getCurrentSourceIndex();
			if (idx == -1) return;
			saveSource(idx, true);
		}
	}

	class LoadObjActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (preferences.currDir != null) objectFileDialog.setCurrentDirectory(preferences.currDir);
			objectFileDialog.showOpenDialog(frame);
			preferences.currDir = objectFileDialog.getCurrentDirectory();
			// cancel?
			if (objectFileDialog.getSelectedFile() == null) return;

			try {
				File file = objectFileDialog.getSelectedFile();
				ObjModule objModule = ObjModule.load(file);
				crapsMachine.addObjModule(objModule);
				// update views
				updateViews();

			} catch(ObjConflictException ex) {
				JOptionPane.showMessageDialog(frame, "Le code de ce module entre en conflit avec celui des autres, a partir de l'adresse : " + ex.getAddr());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class SaveObjActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (preferences.currDir != null) objectFileDialog.setCurrentDirectory(preferences.currDir);
			objectFileDialog.showSaveDialog(frame);
			preferences.currDir = objectFileDialog.getCurrentDirectory();
			// cancel?
			if (objectFileDialog.getSelectedFile() == null) return;

			int idx = getCurrentSourceIndex();
			if (idx == -1) return; // normally impossible...
			SourceContext sc = (SourceContext) sourceContexts.get(idx);
			try {
				File file = objectFileDialog.getSelectedFile();
				sc.getObjModule().save(file);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// <idx> is the index of a source file
	// If the source file has not been modified since last save, return false
	// If it has been modified, prompt to save. Return true if prompt has a 'cancel' answer
	boolean promptModifiedSource(int idx) {
		SourceContext sc = (SourceContext) sourceContexts.get(idx);
		if (!sc.isSourceModified()) return false;
		String name = "untitled";
		if (sc.getSourceFile() != null) name = sc.getSourceFile().getName();
		int option = JOptionPane.showConfirmDialog(frame, "save '" + name + "' ?", "modified source file", JOptionPane.YES_NO_CANCEL_OPTION);
		if (option == JOptionPane.CANCEL_OPTION) return true;
		if (option == JOptionPane.YES_OPTION) saveSource(idx, false);
		return false;
	}

	// open a new source editor panel in tabbedPane at index <i>, with file name <sourceFile>
	// if <sourceFile> is null, create an 'untitled' panel
	// if <is> is not null, text is taken from <is> (usually an example from a jar file)
	// return true in case of error
	boolean loadSource(int i, File sourceFile, InputStream is) {

		StringBuffer st = new StringBuffer();
		if (sourceFile != null) {
			// load file source text into editor
			try {
				InputStreamReader isr = null;
				if (is != null) {
					isr = new InputStreamReader(is);
				} else {
					isr = new FileReader(sourceFile);
				}
				LineNumberReader sourceReader = new LineNumberReader(isr);
				while (true) {
					String line = sourceReader.readLine();
					if (line == null) break;
					st.append(line + "\n");
				}
			} catch(Exception ex) {
				// file does not exist
				return true;
			}
		}

		// create and insert source context at <i>
		SourceContext sc = new SourceContext(sourceFile);
		sourceContexts.add(i, sc);
		
		// create source panel
		JPanel sourcePanel = createSourcePanel(sc);

		// insert a new tab
		tabbedPane.add(sourcePanel, i);
		String title = "untitled";
		if (sc.getSourceFile() != null) title = sc.getSourceFile().getName();
		tabbedPane.setTitleAt(i, title);
		setTabIndex(i);

		// for 'untitled' panel
		if (sc.getSourceFile() == null) return false;

		sc.getSourceEditor().setText(new String(st));
		sc.setSourceModified(false);

		return false;
	}

	public void setTabIndex(int idx) {
		tabbedPane.setSelectedIndex(idx);
		try {
			if (idx < tabbedPane.getTabCount() - 1) {
				closeMenuItem.setEnabled(true);
				saveMenuItem.setEnabled(true);
				saveAsMenuItem.setEnabled(true);
				saveObjAsMenuItem.setEnabled(true);
			} else {
				closeMenuItem.setEnabled(false);
				saveMenuItem.setEnabled(false);
				saveAsMenuItem.setEnabled(false);
				saveObjAsMenuItem.setEnabled(false);
			}
		} catch(Exception ex) {
		}
	}

	// save source file of index <i>. If it has no name yet (untitled), ask for one
	void saveSource(int i, boolean saveAs) {
		SourceContext sc = (SourceContext) sourceContexts.get(i);
		if ((sc.getSourceFile() == null) || saveAs) {
			if (preferences.currDir != null) sourceFileDialog.setCurrentDirectory(preferences.currDir);
			sourceFileDialog.showSaveDialog(frame);
			preferences.currDir = sourceFileDialog.getCurrentDirectory();
			// return if not file is selected
			File file = sourceFileDialog.getSelectedFile();
			if (file == null) return;
			String path = file.getAbsolutePath();
			if (! path.endsWith(".asm")) path = path + ".asm";
			file = new File(path);
			sc.setSourceFile(file);
		}
		
		try {
			BufferedWriter sourceWriter = new BufferedWriter(new FileWriter(sc.getSourceFile()));
			sourceWriter.write(sc.getSourceEditor().getText());
			sourceWriter.close();
			sc.setSourceModified(false);
			tabbedPane.setTitleAt(i, sc.getSourceFile().getName());
		} catch(Exception ex) {
			messages.addMessage("*** ERROR: " + ex.getMessage() + "\n");
			ex.printStackTrace();
		}
	}
	
	class SourceFilenameFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			if (file.isDirectory()) return true;
			return file.getName().endsWith(".asm");
		}
		public String getDescription() {
			return "CRAPS source files (*.asm)";
		}
	}
	
	class ObjFilenameFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			if (file.isDirectory()) return true;
			return file.getName().endsWith(".obj");
		}
		public String getDescription() {
			return "CRAPS object files (*.obj)";
		}
	}
	
	JPanel createSourcePanel(SourceContext sc) {
		JTextPane sourceEditor = sc.getSourceEditor();
		sourceEditor.setFont(preferences.sourceFont);
		sourceEditor.addKeyListener(new SourceKeyListener(sc));
		JScrollPane jsp = new JScrollPane(sourceEditor);

		JPanel sourceButtonPanel = new JPanel();
		JButton assembleButton = new JButton("assemble");
		assembleButton.addActionListener(new AssembleActionListener(sc));
		sourceButtonPanel.add(assembleButton);
		sc.setAssembleButton(assembleButton);
		JButton uploadButton = new JButton("upload");
		sc.setUploadButton(uploadButton);
		uploadButton.setEnabled(false);
		uploadButton.addActionListener(new UploadActionListener(sc));
		sourceButtonPanel.add(uploadButton);
		
		JPanel sourcePanel = new JPanel(new BorderLayout());
		sourcePanel.add("Center", jsp);
		sourcePanel.add("North", sourceButtonPanel);

		return sourcePanel;
	}


	class ExampleActionListener implements ActionListener {
		private String fileName;
		public ExampleActionListener(String fileName) {
			this.fileName = fileName;
		}
		public void actionPerformed(ActionEvent event) {
			InputStream is = CrapsEmu.class.getResourceAsStream(fileName); 
			File sourceFile = new File(fileName);
			addSource(sourceFile, is);
		}
	}



	///////////////           SOURCE EDITORS MANAGEMENT          ////////////////


	// return index of currently visible source panel. Return -1 if current panel
	// is not a source panel
	int getCurrentSourceIndex() {
		int idx = tabbedPane.getSelectedIndex();
		if (idx >= sourceContexts.size()) return -1;
		return idx;
	}

	void removeAllHighlights(SourceContext sc) {
		sc.getSourceEditor().getHighlighter().removeAllHighlights();
		sc.setHighlightsExist(false);
	}

	void highlightLine(SourceContext sc, int lineno) {
		try {
			String text = sc.getSourceEditor().getText();
			int i = 0;
			int i_ = -1;
			int no = 0;
			while ((i = text.indexOf("\n", i_ + 1)) != -1) {
				no += 1;
				if (no == lineno) break;
				i_ = i;
			}
			if (i == -1) i = text.length(); // error in last line, with no \n

			Highlighter h = sc.getSourceEditor().getHighlighter();
			h.addHighlight(i_+1, i, new DefaultHighlighter.DefaultHighlightPainter(Color.red));
			sc.setHighlightsExist(true);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}



	class QuitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			exitCrapsEmu();
		}
	}


	class SourceKeyListener extends KeyAdapter {
		private SourceContext sc;
		public SourceKeyListener(SourceContext sc) {
			this.sc = sc;
		}
		public void keyTyped(KeyEvent ev) {
			if (sc.existHighlights()) removeAllHighlights(sc);
			sc.setSourceModified(true);
			//sc.getAssembleButton().setEnabled(true);
			tabbedPane.setTitleAt(getCurrentSourceIndex(), sc.getTitle() + "*");
		}
	}
	
	
	class AssembleActionListener implements ActionListener {
		private SourceContext sc;
		public AssembleActionListener(SourceContext sc) {
			this.sc = sc;
		}
		public void actionPerformed(ActionEvent event) {
			messages.addMessage("\nStart assembling...\n");
			try {
				crapsMachine.removeObjModule(sc.getObjModule());
				sc.getObjModule().reset();
			} catch(Exception ex) {
				ex.printStackTrace();
			}

			int nbErr = assemble(sc);
			messages.addMessage("Assembling finished, " + nbErr + " errors\n");
			sc.getLoadButton().setEnabled(nbErr == 0);
		}
	}
	
	class UploadActionListener implements ActionListener {
		private SourceContext sc;
		public UploadActionListener(SourceContext sc) {
			this.sc = sc;
		}
		public void actionPerformed(ActionEvent event) {
			messages.addMessage("\nUploading code of " + sc.getTitle() + " ...\n");
			try {
				ObjModule objModule = sc.getObjModule();
				// remove previous version of the module, code & global symbols
				crapsMachine.removeObjModule(objModule);
				
				// add new version, code & global symbols
				crapsMachine.addObjModule(objModule);
				
				// upload code in memory
				Long [] newKeys = (Long[]) objModule.getKeySet().toArray(new Long[0]);
				Map.Entry[] newEntries = (Map.Entry[]) objModule.getEntrySet().toArray(new Map.Entry[0]);
				for (int i = 0; i < newEntries.length; i++) {
					int addr = newKeys[i].intValue();
					ObjEntry oe = objModule.get(addr);
					//System.out.println("addr=" + addr + ", word=" + oe.word);
					long val = Long.parseLong(oe.word, 2);
					runMemoryModel.setValue((long) addr, val);
				}

			} catch(ObjConflictException ex) {
				JOptionPane.showMessageDialog(frame, "Le code de ce module entre en conflit avec celui des autres, a partir de l'adresse : " + ex.getAddr());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			runMemoryModel.fireTableChanged(new TableModelEvent(runMemoryModel));
			TableColumn col1 = runMemoryTable.getColumnModel().getColumn(1);
			col1.setPreferredWidth(100);

			//sc.getAssembleButton().setEnabled(false);
			//sc.getLoadButton().setEnabled(false);
			setTabIndex(tabbedPane.getTabCount() - 1);
			// update views
			updateViews();
		}
	}


	// assemble source and return the total number of errors
	// put into <objModule> the new code and symbols

	int assemble(SourceContext sc) {
		int nbErr = 0;

		ObjModule objModule = sc.getObjModule();
		ObjModule symbolTable = objModule;

		ObjModule globalSymbolTable = crapsMachine.getMemImage();

		if (sc.getSourceEditor() == null) return 0;
		String source = sc.getSourceEditor().getText();
		removeAllHighlights(sc);

		// build <lines>, all source lines
		ArrayList lines = new ArrayList();
		int i__ = 0;
		int i_ = -1;
		while ((i__ = source.indexOf("\n", i_ + 1)) != -1) {
			String line = source.substring(i_ + 1, i__);
			i_ = i__;
			lines.add(line);
		}
		lines.add(source.substring(i_ + 1));
				
		boolean syntaxError = false;
		ArrayList sourceLines = null;
		try {
			Options opts = new Options(null);
			CRAPS compilo = new CRAPS(opts, source) ;
			compilo.compile();
			sourceLines = compilo.get_lines();
		} catch(EGGException e){
			syntaxError = true;
			nbErr += 1;
			if (e.getLine() == -1)
				messages.addMessage("*** syntax error: " + e.getMsg() + "\n");
			else
				messages.addMessage("*** syntax error: " + e.getLine() + " : " + e.getMsg() + "\n");
		}
		if (syntaxError) return nbErr;

		// second pass: try and resolve all references
		boolean adrKnown = true;
		boolean undefSymOrExpr = false;
		boolean changed = false;
		long adr = 0;
		while (true) {
			adr = 0;
			adrKnown = true;
			undefSymOrExpr = false;
			changed = false;
			for (int k = 0; k < sourceLines.size(); k++) {
				SourceLine sl = (SourceLine) sourceLines.get(k);
				org.jcb.craps.crapsc.java.CrapsInstrDirecSynth cl = sl.instr_or_direc_or_synth;

				// line with a label: what is its value?
				if (sl.label != null) {
					// value of a label: EQU is a special case
					if (cl instanceof CrapsDirecEqu) {
						// .EQU directive
						CrapsDirecEqu direc = (CrapsDirecEqu) sl.instr_or_direc_or_synth;
						if (!direc.expr.isInstanciated(symbolTable, globalSymbolTable, cl)) {
							undefSymOrExpr = true;
						} else if (!symbolTable.isDefined(sl.label)) {
							symbolTable.set(sl.label, direc.expr.getValue(symbolTable, globalSymbolTable, cl), sl.lineno);
							changed = true;
						} else if (symbolTable.getLineNo(sl.label) != sl.lineno) {
							messages.addMessage("*** symbol '" + sl.label + "' already defined, line #" + sl.lineno + "\n");
							highlightLine(sc, symbolTable.getLineNo(sl.label));
							highlightLine(sc, sl.lineno);
							nbErr += 1;
						}
					} else if (adrKnown && !symbolTable.isDefined(sl.label)) {
						symbolTable.set(sl.label, adr, sl.lineno);
						changed = true;
					}
				}
				if (cl == null) continue;

				// don't try to set addresses when current address no longer known
				// but keep assembling, to get symbols and expressions values
				if (!adrKnown) continue;

				// line with instruction / directive / synthetic instruction: sets its address
				// with <adr> (which is known so far)
				if (!cl.isAddressKnown()) {
					cl.setAddress(adr);
					changed = true;
				}

				if (!cl.isInstanciated(symbolTable, globalSymbolTable))
					undefSymOrExpr = true;

				// compute next <adr> if possible
				// .ORG is a special case
				if (cl instanceof CrapsDirecOrg) {
					// .ORG directive
					CrapsDirecOrg org = (CrapsDirecOrg) sl.instr_or_direc_or_synth;
					if (org.getExpr().isInstanciated(symbolTable, globalSymbolTable, cl)) {
						long newadr = org.getExpr().getValue(symbolTable, globalSymbolTable, cl);
						if (newadr < adr) {
							messages.addMessage("*** program addresses must go in ascending order, line #" + sl.lineno + "\n");
							highlightLine(sc, sl.lineno);
							nbErr += 1;
						} else {
							adr = newadr;
						}
					} else {
						adrKnown = false;
						undefSymOrExpr = true;
					}
				} else if (cl.getLength(symbolTable, globalSymbolTable).isInstanciated(symbolTable, globalSymbolTable, cl)) {
					adr += cl.getLength(symbolTable, globalSymbolTable).getValue(symbolTable, globalSymbolTable, cl) / 4;
					// realign after a .BYTE
					if ((cl instanceof CrapsDirecByte) && (adr % 2 == 1))
						adr += 1;
				} else {
					adrKnown = false;
					undefSymOrExpr = true;
				}

			}

			// stop assembling when solved
			if (adrKnown && !undefSymOrExpr) break;


			if (!changed) {
				// not solved and no change: some symbols are undefined
				// rescan all lines and highlight whose which have undefined labels,
				// or with uninstanciated content
				for (int m = 0; m < sourceLines.size(); m++) {
					SourceLine sl = (SourceLine) sourceLines.get(m);
					CrapsInstrDirecSynth cl = sl.instr_or_direc_or_synth;
					if (sl.label != null) {
						if (!symbolTable.isDefined(sl.label)) {
							messages.addMessage("*** symbol '" + sl.label + "' undefined, line #" + sl.lineno + "\n");
							highlightLine(sc, sl.lineno);
							nbErr += 1;
							continue;
						}
					}
					if (cl == null) continue;

					if (cl instanceof CrapsDirecOrg) {
						CrapsDirecOrg clo = (CrapsDirecOrg) cl;
						if (! clo.isInstanciated(symbolTable, globalSymbolTable)) {
							messages.addMessage("*** .org address undefined, line #" + sl.lineno + "\n");
							highlightLine(sc, sl.lineno);
							nbErr += 1;
						}

					} else if (cl instanceof CrapsDirecEqu) {
						CrapsDirecEqu cle = (CrapsDirecEqu) cl;
						if (! cle.isInstanciated(symbolTable, globalSymbolTable)) {
							messages.addMessage("*** .equ expression undefined, line #" + sl.lineno + "\n");
							highlightLine(sc, sl.lineno);
							nbErr += 1;
						}

					} else if (cl instanceof CrapsDirecGlobal) {
						CrapsDirecGlobal cle = (CrapsDirecGlobal) cl;
						if (! cle.isInstanciated(symbolTable, globalSymbolTable)) {
							messages.addMessage("*** " + cle.getSymbol() + " undefined, line #" + sl.lineno + "\n");
							highlightLine(sc, sl.lineno);
							nbErr += 1;
						}

					} else if (!cl.isInstanciated(symbolTable, globalSymbolTable)) {
						messages.addMessage("*** symbol undefined, line #" + sl.lineno + "\n");
						highlightLine(sc, sl.lineno);
						nbErr += 1;
						continue;
					}
				}
				return nbErr;
			}
		}

		// third pass: check validity of all instr/synth/direc
		for (int k = 0; k < sourceLines.size(); k++) {
			SourceLine sl = (SourceLine) sourceLines.get(k);
			CrapsInstrDirecSynth cl = sl.instr_or_direc_or_synth;
			if (cl == null) continue;

			if (cl instanceof CrapsInstrSetHi) {
				// check range for 24-bit const
				CrapsInstrSetHi instr = (CrapsInstrSetHi) cl;
				if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** imm24 value out of range [0,2^24-1], line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsInstrArithLog3) {
				// check range for 13-bit disp
				CrapsInstrArithLog3 instr = (CrapsInstrArithLog3) cl;
				if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** simm13 value out of range [-4096,+4095], line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsInstrLd) {
				// check range for 13-bit disp
				CrapsInstrLd instr = (CrapsInstrLd) cl;
				if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** simm13 value out of range [-4096,+4095], line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsInstrSt) {
				// check range for 13-bit disp
				CrapsInstrSt instr = (CrapsInstrSt) cl;
				if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** simm13 value out of range [-4096,+4095], line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsInstrBr) {
				// check range for 24-bit disp
				CrapsInstrBr instr = (CrapsInstrBr) cl;
				if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** branching address too far, line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsSynthCall) {
				// check range for 22-bit disp
				CrapsSynthCall instr = (CrapsSynthCall) cl;
				if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** branching address too far, line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsDirecWord) {
				// check range for 16-bit const
				CrapsDirecWord word = (CrapsDirecWord) cl;
				if (word.isInstanciated(symbolTable, globalSymbolTable) && !word.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** word values out of range [-32768,+32767] or [0,65535], line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsDirecByte) {
				// check range for 8-bit const
				CrapsDirecByte byt = (CrapsDirecByte) cl;
				if (byt.isInstanciated(symbolTable, globalSymbolTable) && !byt.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** byte values out of range [-128,+127] or [0,256], line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsSynthSet) {
				// check range for 32-bit const
				CrapsSynthSet synth = (CrapsSynthSet) cl;
				if (synth.isInstanciated(symbolTable, globalSymbolTable) && !synth.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** 32-bit value out of range, line #" + sl.lineno + "\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsSynthSetq) {
				// check range for 13-bit const
				CrapsSynthSetq synth = (CrapsSynthSetq) cl;
				if (synth.isInstanciated(symbolTable, globalSymbolTable) && !synth.isContentValid(symbolTable, globalSymbolTable)) {
					messages.addMessage("*** 13-bit constant out of range [-4096, +4095], line #" + sl.lineno + "; use 'set' instead\n");
					highlightLine(sc, sl.lineno);
					nbErr += 1;
				}
			} else if (cl instanceof CrapsDirecGlobal) {
				// add global symbol
				CrapsDirecGlobal cdg = (CrapsDirecGlobal) cl;
				symbolTable.addGlobalSymbol(cdg.getSymbol());
			}
		}
		if (nbErr > 0) return nbErr;

//System.out.println("symbolTable=" + symbolTable);
//System.out.println("globalSymbolTable=" + globalSymbolTable);
	
		// fourth pass: build hex image
		for (int i = 0; i < sourceLines.size(); i++) {
			SourceLine sl = (SourceLine) sourceLines.get(i);
			CrapsInstrDirecSynth cl = sl.instr_or_direc_or_synth;
			if (cl != null) {
				//objModule.addInstr(cl.getAddress(), sl);
				int nb = (int) cl.getLength(symbolTable, globalSymbolTable).getValue(symbolTable, globalSymbolTable, cl);
				long word = 0;
				SourceLine s = sl;
				for (int j = 0; j < nb; j++) {
					//if (j >= 4) s = null;
					long b = (long) cl.getByte(j, symbolTable, globalSymbolTable);
					switch (j % 4) {
						case 0:
							word = b * 16777216L;
							break;
						case 1:
							word += b * 65536L;
							break;
						case 2:
							word += b * 256L;
							break;
						case 3:
							word += b;
							objModule.add(null, cl.getAddress() + (j - 3)/4, BinNum.formatUnsigned32(word), s);
							break;
					}
				}
				if (nb % 2 == 1)
					objModule.add(null, cl.getAddress() + nb - 1, BinNum.formatUnsigned32(word), null);
			}
		}
		System.out.println("objModule=" + objModule);
		
		return nbErr;
	}


	class RunSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			// update select line in run view
			int li = (int) (readRegister(62)) ;
			if (li == -1) return;
			try {
				runMemoryTable.setRowSelectionInterval(li, li);
			} catch(Exception ex) {
			}
		}
	}

	
	public String disassemble(long addr, long instr) {
		long op = instr / 1073741824L; // 2^30
		switch ((int) op) {
			case 0: // branch ou sethi
				long ir29 = (instr / 536870912L) % 2; // 2^29
				if (ir29 == 1) {
					// branch
					long disp24 = instr % 16777216L; // 2^24
					if (disp24 >= 8388608L) disp24 -= 16777216L;
					long cond = (instr / 33554432L) % 16; // 2^25
					String codeop = "";
					switch ((int) cond) {
						case 8:	codeop = "ba    "; break;
						case 1: codeop = "be    "; break;
						case 9: codeop = "bne   "; break;
						case 5: codeop = "bcs   "; break;
						case 13:codeop = "bcc   "; break;
						case 14:codeop = "bpos  "; break;
						case 6:	codeop = "bneg  "; break;
						case 7: codeop = "bvs   "; break;
						case 15:codeop = "bvc   "; break;
						case 10:codeop = "bg    "; break;
						case 2: codeop = "ble   "; break;
						case 11:codeop = "bge   "; break;
						case 3:	codeop = "bl    "; break;
						case 12:codeop = "bgu   "; break;
						case 4:	codeop = "bleu  "; break;
					}
					String relDisp = disp24 + "";
					if (disp24 >= 0) relDisp = "+" + relDisp;
					ObjModule objModule = crapsMachine.getModule(addr);
					if (objModule == null) {
						return codeop + relDisp;
					} else {
						long brAddr  = addr + disp24;
						String sym = objModule.getSym(brAddr);
						if (sym == null) return codeop + relDisp;
						return codeop + sym + " (" + relDisp + ")";
					}
				} else {
					// sethi
					long imm24 = instr % 16777216L; // 2^24
					long rd = (instr / 16777216L) % 32; // 2^24
					return "sethi  0x" + HexNum.formatUnsignedHexa(imm24) + ", %r" + rd;
				}
			case 1:	// reti
				return "reti";
			case 2:	// arithmetique & logique
			case 3:	// acces memoire
				long op3 = (instr / 524288L) % 64; // 2^19
				long rs1 = (instr / 16384L) % 32; // 2^14
				long rs2 = instr % 32;
				long rd = (instr / 33554432L) % 32; // 2^25
				long ir13 = (instr / 8192L) % 2; // 2^13
				long simm13 = instr % 8192L; // 2^13
				if (simm13 >= 4096L) simm13 -= 8192L;
				String codeop = "";
				switch ((int) op3) {
					case 0: codeop = "add   "; break;
					case 16: codeop = "addcc "; break;
					case 4: codeop = "sub   "; break;
					case 20: codeop = "subcc "; break;
					case 26: codeop = "umulcc "; break;
					case 1: codeop = "and "; break;
					case 2: codeop = "or  "; break;
					case 3: codeop = "xor  "; break;
					case 7: codeop = "xnor  "; break;
					case 17: codeop = "andcc "; break;
					case 18: codeop = "orcc  "; break;
					case 19: codeop = "xorcc  "; break;
					case 23: codeop = "xnorcc  "; break;
					case 13: codeop = "srl   "; break;
					case 14: codeop = "sll   "; break;
					case 56: codeop = "jmpl  "; break;
				}
				String arg2 = "";
				if (ir13 != 0) {
					arg2 = "" + simm13;
				} else {
					arg2 = "%r" + rs2;
				}
				if (op == 2)
					return codeop + "%r" + rs1 + ", " + arg2 + ", %r" + rd;
				else {
					if (((op3 / 4) % 2) != 0)
						return "st    %r" + rd + ", [%r" + rs1 + "+" + arg2 + "]";
					else
						return "ld    [%r" + rs1 + "+" + arg2 + "], %r" + rd;
				}
		}
		return "---";
	}
				
	private void updateViews() {
		try {
			// on vide les caches car apres un run ou un step,
			// les caches n'ont pas ete prevenus des changements
			regCache.clear();
			memCache.clear();
			
			// update breakAddr
			breakAddr = readRegister(26);
			
			// update views contents
			runMemoryModel.fireTableChanged(new TableModelEvent(runMemoryModel));
			regsModel.fireTableChanged(new TableModelEvent(regsModel));
			
			// update flags
			Nmodel.fireChange();
			Zmodel.fireChange();
			Vmodel.fireChange();
			Cmodel.fireChange();
			
			// update select line in run view
			int li = (int) (readRegister(30)) ;
			if ((li < 0) || (li > 100)) {
				System.out.println("*** ligne trop lointaine li=" + li);
				return;
			}
			try {
				runMemoryTable.setRowSelectionInterval(li, li);
			} catch(Exception ex) {
			}
			
			// move viewport if selected line is not visible
			int viewHeight = (int) runMemoryScrollPane.getViewport().getExtentSize().getHeight();
			int h = li * runMemoryTable.getRowHeight();
			int start = (int) runMemoryScrollPane.getViewport().getViewPosition().getY();
			int end = start + viewHeight;
			if ((h < start) || (h > end))
				runMemoryScrollPane.getViewport().setViewPosition(new Point(0, Math.max(0, h - viewHeight / 2)));
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}


	class UpdateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// update views
			updateViews();
		}
	}

	class StepActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			// mon_cmd <- "0100" (step)
			setBits(60, 63, 4);
			// mon_req <- 1
			outs[59] = 1;
			CommThread.sendByte(7, getByte(7));
			waitN(5);
			// mon_req <- 0
			outs[59] = 0;
			CommThread.sendByte(7, getByte(7));
			
			// update views
			updateViews();
		}
	}


	class RunActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (runButton.getActionCommand().equals("run")) {
				// cliquer sur run, c'est demander un 'step' puis mettre run a 1
				runButton.setText("stop");
				runButton.setActionCommand("stop");
				stepButton.setEnabled(false);
				// run <- 1
				outs[55] = 1;
				commThread.sendByte(6, getByte(6));
			} else {
				runButton.setText("run");
				runButton.setActionCommand("run");
				stepButton.setEnabled(true);
				// run <- 0
				outs[55] = 0;
				commThread.sendByte(6, getByte(6));
				updateViews();
			}
		}
	}


	/////////////////         MEMORY         //////////////////


	private HashMap memCache;
	
	class RunMemoryModel extends AbstractTableModel {

		public int getColumnCount() {
			return 5;
		}

		public String getColumnName(int col) {
			switch (col) {
				case 0: return "address";
				case 1: return "value";
				case 2: return "break";
				case 3: return "label";
				case 4: return "dissassembly";
			}
			return "???";
		}

		public int getRowCount() {
			if (memCache == null) memCache = new HashMap();
			return 8192; // RAM = 8192x32
		}
		
		private long getValue(long addr) {
			Long val = (Long) memCache.get(addr + "");
			if (val != null) return val.longValue();
			long lval1 = readMemory(addr);
			memCache.put(addr + "", new Long(lval1));
			return lval1;
		}
		
		private void setValue(long addr, long value) {
			memCache.put(addr + "", new Long(value));
			writeMemory(addr, value);
		}

		public Object getValueAt(int row, int col) {
//			long addr = (long) (4 * row);
			long addr = (long) (1 * row); // craps4
			switch (col) {
				case 0: return HexNum.formatUnsigned8Hexa(addr, "", " ");
				case 1: return HexNum.formatUnsigned8Hexa(getValue(addr), "", " ");
				case 2: return new Boolean(addr == breakAddr);
				case 3:	ObjModule objModule = crapsMachine.getModule(addr);
					if (objModule == null) return "";
					String sym = objModule.getSym(addr);
					return sym;
				case 4:	return disassemble(addr, getValue(addr));
				default: return "";
			}
		}
		
		public boolean isCellEditable(int row, int col) {
			return ((col == 1) || (col == 2));
		}
		
		public void setValueAt(Object val, int row, int col) {
//			long addr = (long) (4 * row);
			long addr = (long) (1 * row); // craps4
			switch (col) {
				case 1:	try {
						String str = (String) val;
						str = str.replaceAll(" ", "");
						long ival = Long.parseLong(str, 16);
						setValue(addr, ival);
					} catch(Exception ex) {
					}
					break;
				case 2:
					writeRegister(26, addr);
					breakAddr = addr;
					updateViews();
					break;
			}
			runMemoryModel.fireTableRowsUpdated(row, row);
		}

		public Class getColumnClass(int c) {
			if (c == 3) return String.class; else return getValueAt(0, c).getClass();
		}
	}
	
	synchronized void writeMemory(long addr, long val) {
		
		// mon_cmd <- "0010" (write memory)
		setBits(60, 63, 2);
		// address on pc2board[31..0]
		setBits(0, 31, addr);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
		do { } while ((CommThread.readdByte(7) & 128) == 0);

		// value to write on pc2board[31..0]
		setBits(0, 31, val);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 0
		do { } while ((CommThread.readdByte(7) & 128) != 0);
	}	
	
	synchronized long readMemory(long addr) {

		// mon_cmd <- "0000" (read memory)
		setBits(60, 63, 0);
		// address on pc2board[31..0]
		setBits(0, 31, addr);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
		do { } while ((CommThread.readdByte(7) & 128) == 0);

		// get read data
		long value = 0;
		long pow8 = 1;
		for (int i = 0; i < 4; i++) {
			int b = CommThread.readdByte(i);
			value += pow8 * b;
			pow8 *= 256;
		}

		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
		return value;
	}	

	void waitN(long n) {
		try {
			Thread.sleep(n);
		} catch(Exception ex) {
		}
	}
	
	int getByte(int i) {
		int res = 0;
		if (outs[8*i] == 1) res |= 1;
		if (outs[8*i+1] == 1) res |= 2;
		if (outs[8*i+2] == 1) res |= 4;
		if (outs[8*i+3] == 1) res |= 8;
		if (outs[8*i+4] == 1) res |= 16;
		if (outs[8*i+5] == 1) res |= 32;
		if (outs[8*i+6] == 1) res |= 64;
		if (outs[8*i+7] == 1) res |= 128;
		return res;
	}
	
	void setBits(int first, int last, long val) {
		for (int i = first; i <= last; i++) {
			outs[i] = (int) (val % 2);
			val = val / 2;
		}
	}
	
	class CrapsCommListener implements CommListener {
		public void valueChanged(CommEvent ev) {
			int[] bitVector = ev.getBitVector();
			int brk = bitVector[62];
			int rst = bitVector[61];
			
			if ((brk == 1) && runButton.getActionCommand().equals("stop")) {
				System.out.println("****************** break!!!");
				// run <- 0
				outs[55] = 0;
				commThread.sendByte(6, getByte(6));
				runButton.setText("run");
				runButton.setActionCommand("run");
				stepButton.setEnabled(true);
				updateViews();
			}
			
			if (rst == 1) {
				System.out.println("****************** reset!!!");
				// run <- 0
				outs[55] = 0;
				commThread.sendByte(6, getByte(6));
				runButton.setText("run");
				runButton.setActionCommand("run");
				stepButton.setEnabled(true);
				updateViews();
			}
		}
	}



	/////////////////         REGISTERS         //////////////////


	private HashMap regCache;

	class RegsModel extends AbstractTableModel {

		public int getColumnCount() {
			return 2;
		}

		public String getColumnName(int col) {
			return ("");
		}

		public int getRowCount() {
			if (regCache == null) regCache = new HashMap();
			return 32;
		}

		public Object getValueAt(int row, int col) {
			switch (col) {
				case 0: String str = "%r" + row;
					if (row == 0) str += " (=0)";
					else if (row == 20) str += " (=1)";
					else if (row == 21) str += " (tmp1)";
					else if (row == 22) str += " (tmp2)";
					else if (row == 26) str += " (brk)";
					else if (row == 27) str += " (fp)";
					else if (row == 28) str += " (@ret)";
					else if (row == 29) str += " (sp)";
					else if (row == 30) str += " (pc)";
					else if (row == 31) str += " (ir)";
					return str;
				case 1:	return HexNum.formatUnsigned8Hexa(getValue(row), "", " ");
				//case 1: return "---";
			}
			return "";
		}
		
		long getValue(int numreg) {
			Long val = (Long) regCache.get(numreg + "");
			if (val != null) return val.longValue();
			long lval = readRegister(numreg);
			regCache.put(numreg + "", new Long(lval));
			return lval;
		}
			
		
		public boolean isCellEditable(int row, int col) {
			return (col == 1);
		}
		
		public void setValueAt(Object val, int row, int col) {
			try {
				long ival = Long.parseLong((String) val, 16);
				writeRegister(row, ival);
				regCache.put(row + "", new Long(ival));
				regsModel.fireTableRowsUpdated(row, row);
			} catch(Exception ex) {
			}
		}
	}

	class FlagModel extends DefaultButtonModel {
		private int i;
		public FlagModel(int i) {
			this.i = i;
		}
		public boolean isSelected() {
			int b = CommThread.readdByte(7);
			switch (i) {
			case 3: return ((b & 2) != 0);
			case 2: return ((b & 4) != 0);
			case 1: return ((b & 8) != 0);
			case 0: return ((b & 16) != 0);
			}

			return false;
		}
		public void fireChange() {
			fireStateChanged();
		}
	}
	
	synchronized long readRegister(int numreg) {		
		// mon_cmd <- "0001" (read register)
		setBits(60, 63, 1);
		CommThread.sendByte(7, getByte(7));
		// reg # on pc2board[4..0]
		setBits(0, 4, numreg);
		CommThread.sendByte(0, getByte(0));
		
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
		do { }
		while ((CommThread.readdByte(7) & 128) == 0);

		// get read data
		long value = 0;
		long pow8 = 1;
		for (int i = 0; i < 4; i++) {
			int b = CommThread.readdByte(i);
			value += pow8 * b;
			pow8 *= 256;
		}

		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
		return value;
	}
	
	synchronized void writeRegister(int numreg, long val) {		
		// mon_cmd <- "0011" (write register)
		setBits(60, 63, 3);
		CommThread.sendByte(7, getByte(7));
		// reg # on pc2board[36..32]
		setBits(32, 36, numreg);
		CommThread.sendByte(4, getByte(4));
		// value to write on pc2board[31..0]
		setBits(0, 31, val);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
		do { } while ((CommThread.readdByte(7) & 128) == 0);

		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
	}


	/////////////////////////////////////////////////////////////////
	//
	class MessagesFrame extends JDialog {

		private JTextArea logTextArea;

		public MessagesFrame() {
			super (frame, "messages", false);
		
			// LOG
			logTextArea = new JTextArea(10, 10);
			logTextArea.setFont(preferences.logFont);
			logTextArea.setBackground(preferences.logColor);
			logTextArea.setEditable(false);
			JPanel logTextPanel = new JPanel(new BorderLayout());
			logTextPanel.add(logTextArea);
			JScrollPane logScrollPane = new JScrollPane(logTextPanel);

			setContentPane(logScrollPane);
			setLocation(preferences.logLocation);
			setSize(preferences.logSize);
		}

		public void addMessage(String text) {
			logTextArea.append(text);
			setVisible(true);
		}
	}



	class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			exitCrapsEmu();
		}
	}
	
	
	void exitCrapsEmu() {
		for (int i = 0; i < sourceContexts.size(); i++) {
			SourceContext sc = (SourceContext) sourceContexts.get(i);
			if (!sc.isSourceModified()) continue;
			boolean cancel = promptModifiedSource(i);
			// abort exit when cancelled
			if (cancel) return;
		}
		preferences.dividerLocation = crapsMachinePanel.getDividerLocation();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("crapsemu.ini"));
			oos.writeObject(frame.getSize());
			oos.writeObject(frame.getLocation());
			preferences.setLogLocation(messages.getLocation());
			preferences.setLogSize(messages.getSize());
			oos.writeObject(preferences);
			ArrayList files = new ArrayList();
			for (int i = 0; i < sourceContexts.size(); i++) {
				SourceContext sc = (SourceContext) sourceContexts.get(i);
				files.add(sc.getSourceFile());
			}
			oos.writeObject(files);
			oos.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}
	

	public static void main(String[] args) {
		CrapsEmu emu = new CrapsEmu();
		emu.setVisible(true);
	}



}

