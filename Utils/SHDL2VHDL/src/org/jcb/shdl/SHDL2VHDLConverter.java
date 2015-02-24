package org.jcb.shdl;

import org.jcb.filedrop.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jcb.shdl.comm.CommFrame;
import org.jcb.shdl.comm.DataModel;
import org.jcb.shdl.comm.DataPad;
import org.jcb.shdl.comm.Scalar;
import org.jcb.shdl.comm.CommFrame.AddExpectationsListener;
import org.jcb.shdl.comm.CommFrame.ButtonActionListener;
import org.jcb.shdl.comm.CommFrame.InButtonModel;
import org.jcb.shdl.comm.CommFrame.InInternalFrame;
import org.jcb.shdl.comm.CommFrame.InsertCommentListener;
import org.jcb.shdl.comm.CommFrame.LoadTestVectorListener;
import org.jcb.shdl.comm.CommFrame.OutInternalFrame;
import org.jcb.shdl.comm.CommFrame.TestDialog;
import org.jcb.shdl.comm.CommFrame.TestDialog.NextErrorTestListener;
import org.jcb.shdl.comm.CommFrame.TestDialog.NextTestListener;
import org.jcb.shdl.comm.InitDataDialog.AddActionListener;
import org.jcb.shdl.comm.InitDataDialog.DataListSelectionListener;
import org.jcb.shdl.comm.InitDataDialog.DelActionListener;
import org.jcb.shdl.comm.InitDataDialog.OKActionListener;
import org.jcb.shdl.shdlc.java.*;


public class SHDL2VHDLConverter extends JFrame {

    private SHDL2VHDLConverter frame;
    private JTextField mainModField = new JTextField("", 15);
    private JTextField shdlPath = new JTextField("", 30);
    private JCheckBox synthesizeCheck = new JCheckBox("synthesize");
    private JCheckBox verboseCheck = new JCheckBox("verbose");
    private String boardName = "Nexys-1000";
    private JTextArea messagesArea = new JTextArea();
    private JScrollPane scrollPane;
    private ByteArrayOutputStream baos;
    private PrintStream errorStream;
    private LibManager libManager;
    private JButton ioButton;

    private ShdlDesign design;
    private SHDLModule topModule;
    private SHDLBoard board;
    private File shdlDir;

    private final Font LARGEFONT = new Font("Dialog", Font.BOLD, 18);
    private final String newline = System.getProperty("line.separator");
    private final String pathSeparator = System.getProperty("path.separator");

    private Pattern wp = Pattern.compile("\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,(-?[0-9]+)\\s*");
    private Pattern dp = Pattern.compile("\\s*([\\w]+)\\s*:\\s*(in|out)\\s*\\[([0-9]+)\\.\\.([0-9]+)\\]\\s*((\\((-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*,\\s*(-?[0-9]+)\\s*\\))?)\\s*");
    private DataModel dataModel;
    private File iniFile;
    private CommThread commThread;
    private CommFrame commFrame;
    private PrintWriter logPR;
    private final int N = 128;



    public static void main(String[] args ) {

        JFrame frame = new SHDL2VHDLConverter("SHDL2VHDLConverter v2.2.1");

        frame.setBounds(100, 300, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        frame.show();
    }


    public SHDL2VHDLConverter(String title) {
        super(title);
        frame = this;
        baos = new ByteArrayOutputStream();
        errorStream = new PrintStream(baos);
        getContentPane().add(new SHDL2VHDLConverterPanel());
    }

    class SHDL2VHDLConverterPanel extends JPanel {
        public SHDL2VHDLConverterPanel() {
            setLayout(new BorderLayout());
            Container northPanel = Box.createVerticalBox();

            JLabel mainModLabel = new JLabel("drop .shd or .net file here :");
            mainModLabel.setFont(LARGEFONT);
            mainModField.setFont(LARGEFONT);
            mainModField.setBackground(Color.lightGray);
            MainModActionListener mainModActionListener = new MainModActionListener();
            mainModField.addActionListener(mainModActionListener);
            JButton okButton = new JButton("run");
            okButton.addActionListener(mainModActionListener);
            JButton resetButton = new JButton("reset");
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    mainModField.setText("");
                    shdlPath.setText("");
                    //					destDir.setText("");
                    synthesizeCheck.setSelected(false);
                    verboseCheck.setSelected(false);
                    ioButton.setVisible(false);
                }
            });
            JPanel mainModPanel = new JPanel(new GridLayout(2, 1));
            JPanel p1 = new JPanel(); p1.add(mainModLabel);
            mainModPanel.add(p1);
            JPanel p2 = new JPanel(); p2.add(mainModField); p2.add(okButton); p2.add(resetButton);
            mainModPanel.add(p2);
            northPanel.add(mainModPanel);

            JPanel checksPanel = new JPanel();
            checksPanel.add(synthesizeCheck);
            checksPanel.add(verboseCheck);
            JComboBox cardComboBox = new JComboBox(new String[] {"Nexys-1000", "Nexys II-1200"});
            cardComboBox.addActionListener(new CardListener());
            cardComboBox.setSelectedIndex(1);
            checksPanel.add(cardComboBox);
            ioButton = new JButton("Distant I/O");
            ioButton.setVisible(false);
            ioButton.addActionListener(new IOListener());
            checksPanel.add(ioButton);
            northPanel.add(checksPanel);

            JPanel pathPanel = new JPanel();
            pathPanel.add(new JLabel("SHDL path :"));
            pathPanel.add(shdlPath);
            northPanel.add(pathPanel);

            // create libmanager
            libManager = new LibManager();

            new FileDrop(null, mainModField, new MainModDropListener());
            new FileDrop(null, shdlPath, new PathDropListener());

            add("North", northPanel);
            messagesArea.setEditable(false);
            scrollPane = new JScrollPane(messagesArea);
            add("Center", scrollPane);
        }
    }

    class MainModActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (shdlPath.getText().length() > 0) libManager.setPath(shdlPath.getText());
            ArrayList files = new ArrayList();
            try {
                File file = libManager.lookFor(mainModField.getText());
                if (!file.isFile())
                    addMessage("** '" + file.getCanonicalPath() + "' is not a file" + newline);
                else {
                    files.add(file);
                }

                processFile(file) ;

            } catch(Exception e) {
                // program exception are reported in console
                addMessage("** program exception: '" + e.getMessage() + "'" + newline);
            }
        }
    }

    class MainModDropListener implements FileDrop.Listener {
        public void filesDropped(File[] files_) {
            if (shdlPath.getText().length() > 0) libManager.setPath(shdlPath.getText());
            if (files_.length > 1)
                addMessage("** drop only one file here" + newline);
            else {
                try {
                    if (!files_[0].isFile()) {
                        addMessage("** '" + files_[0].getCanonicalPath() + "' is not a file" + newline);
                        return;
                    }
                    File file = files_[0];
                    mainModField.setText(file.getName());

                    processFile(file) ;

                } catch(Exception e) {
                    // program exception are reported in console
                    addMessage("** program exception: '" + e.getMessage() + "'" + newline);
                }
            }
        }
    }

    class CardListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            JComboBox cb = (JComboBox) ev.getSource();
            boardName = (String) cb.getSelectedItem();
        }
    }


    void addMessage(String message) {
        messagesArea.append(message);
        // il faut attendre un peu (?) avant de scroller
        try {
            Thread.sleep(10);
            messagesArea.repaint();
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        } catch (Exception e) {
        }
    }

    static String StringArray2String(String[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(array[i]);
        }
        return sb.toString();
    }


    // If <shdlPath> and <destDir> are empty, set them to the directory of <file> and its ../vhdl
    // check also that it has rights to write there

    boolean checkDirectories(File file) throws Exception {
        // get parent dir
        shdlDir = file.getParentFile();

        // check for write access in parent dir by trying to create a temporary file
        File tempFile = new File(shdlDir, "file.tmp");
        if (tempFile.exists()) tempFile.delete();
        try {
            tempFile.createNewFile();
            tempFile.delete();
        } catch(Exception ex) {
            return false;
        }

        // if shdl path empty, set it
        if (shdlPath.getText().trim().length() == 0) {
            shdlPath.setText(shdlDir.getPath());
            libManager.setPath(shdlPath.getText());
        }
        // create vhdl dir if it does not exist
        File vhdlDir = new File(shdlDir.getParentFile(), "vhdl");
        if (!vhdlDir.exists()) vhdlDir.mkdir();

        if (synthesizeCheck.isSelected() && ((shdlPath.getText().indexOf(" ") != -1) || (shdlPath.getText().indexOf(" ") != -1))) {
            JOptionPane.showMessageDialog(frame, "Your paths contain blanks (' ') which may cause errors when synthesizing with WebPACK 8");
        }
        return true;
    }


    void processFile(File file) {
        try {
            ioButton.setVisible(false);

            // traduction d'une description de graphe d'états
            if (file.getAbsolutePath().endsWith(".net")) {
                NetConverter netConverter = new NetConverter(file, errorStream);
                file = netConverter.start();
                // mise à jour des messages
                addMessage(baos.toString()); baos.reset();
                if (file == null) {
                    addMessage("** graph translation failed" + newline);
                    addMessage("---------------------" + newline);
                    return;
                }
            }

            boolean ok = checkDirectories(file);
            if (ok) {
                board = SHDLBoard.getBoard(boardName) ;

                // perform VHDL translation
                File shdlDir = file.getParentFile();
                File vhdlDir = new File(shdlDir.getParentFile(), "vhdl");
                design = processVHDLTranslation(board, file, vhdlDir);

                if (design == null) return;

                topModule = design.getTopModule();
                if (topModule == null) {
                    addMessage("** could not find top module" + newline);
                    return;
                }

                // on classe le design: 0=board only, 1=hybrid, 2=distant only
                int boardIOStatus = board.getModuleIOStatus(topModule) ;
                switch (boardIOStatus) {
                    case 0: addMessage("*** BOARD ONLY I/O DESIGN***\n"); break;
                    case 1: addMessage("*** BOARD & DISTANT I/O DESIGN***\n"); break;
                    case 2: addMessage("*** DISTANT ONLY I/O DESIGN***\n"); break;
                }

                if (boardIOStatus > 0) {
                    // hybrid or distant I/O: needs commUSB communication

                    // create commUSB.vhd
                    addMessage("-- creating 'commUSB.vhd'" + newline);
                    SHDLPredefinedCommUSB commUSB = new SHDLPredefinedCommUSB(new SHDLModuleOccurence("commUSB", 0, null), null);
                    PrintWriter pw = new PrintWriter(new FileOutputStream(new File(vhdlDir, "commUSB.vhd")));
                    pw.println(commUSB.getVHDLDefinition());
                    pw.flush();
                    pw.close();

                    // create <top>_comm.vhd file
                    addMessage("-- creating '" + topModule.getName() + "_comm.vhd'" + newline);
                    boolean overflow = design.generateDistantIOModule(topModule, board, vhdlDir);

                    if (overflow) {
                        addMessage("** WARNING: too many inputs/outputs" + newline);
                        // create <top>_comm.shd file
                        addMessage("-- creating '" + topModule.getName() + "_comm.shd'" + newline);
                        design.generateCommShdModule(topModule, vhdlDir);
                    }

                    // create comm.ini
                    addMessage("-- creating 'comm.ini'" + newline);
                    design.generateCommIni(topModule, board, vhdlDir);
                }

                // bouton I/O visible pour design hybrid ou distant only
                ioButton.setVisible(boardIOStatus > 0);

                if (synthesizeCheck.isSelected()) {
                    File projectDir = new File(vhdlDir.getParentFile(), "ISEproject");
                    ArrayList moduleNames = design.getListModuleNames();
                    String topModuleName = design.getTopModule().getName();
                    if (boardIOStatus > 0) {
                        moduleNames.add("commUSB");
                        topModuleName = topModuleName + "_comm";
                        moduleNames.add(topModuleName);
                    }
                    // on l'exécute dans un thread, sinon aucun message ne s'affiche avant le retour de la méthode
                    Thread thread = new Thread(new SynthesizeThread(projectDir, vhdlDir, design, topModule, moduleNames, board));
                    thread.start();
                }
            } else {
                addMessage("** you have no permission to write in these directories" + newline);
                return;
            }
        } catch (Exception e) {
            // program exception are reported in console
            addMessage("** program exception: '" + e.getMessage() + "'" + newline);
        } finally {
            if (!synthesizeCheck.isSelected()) addMessage("---------------------" + newline);
        }
    }

    ShdlDesign processVHDLTranslation(SHDLBoard board, File file, File destDir) throws Exception {
        // Build a design with the modules defined in these source files
        ShdlDesign design = new ShdlDesign(libManager, false, errorStream);

        // parse them, and collect all modules referenced from main statements
        ArrayList files = new ArrayList();
        files.add(file);
        boolean collectOk = design.collect(files);
        //addMessage("collectOk=" + collectOk);
        addMessage(baos.toString()); baos.reset();
        if (!collectOk) return null;

        // check for loops
        boolean loopsOk = design.checkModuleDependences();
        addMessage(baos.toString()); baos.reset();
        //addMessage("loopsOk=" + loopsOk);
        if (!loopsOk) return null;

        // check all design
        boolean checkOK = design.check();
        addMessage(baos.toString()); baos.reset();
        //addMessage("checkOK=" + checkOK);
        if (!checkOK) return null;

        addMessage("-- parse completed; 0 error" + newline);

        // generate VHDL text
        design.generateVHDL(board, destDir);
        addMessage("-- VHDL generation completed" + newline);

        return design;
    }


    class PathDropListener implements FileDrop.Listener {
        public void filesDropped(File[] files) {
            for (int i = 0; i < files.length; i++) {
                try {
                    File file = files[i];
                    if (file.isDirectory()) {
                        if (shdlPath.getText().length() == 0)
                            shdlPath.setText(file.getCanonicalPath());
                        else
                            shdlPath.setText(shdlPath.getText() + pathSeparator + file.getCanonicalPath());
                    } else
                        addMessage("** '" + file.getCanonicalPath() + "' is not a directory" + newline);
                } catch(Exception e) {}
            }
        }
    }

    // on exécute synthesize dans un thread car sinon les messages ne s'affichent pas au fur et à mesure
    class SynthesizeThread implements Runnable {
        File projectDir;
        File vhdlDir;
        ShdlDesign design;
        SHDLModule topModule;
        ArrayList moduleNames;
        SHDLBoard board;
        public SynthesizeThread(File projectDir, File vhdlDir, ShdlDesign design, SHDLModule topModule, ArrayList moduleNames, SHDLBoard board) {
            this.projectDir = projectDir ;
            this.vhdlDir = vhdlDir;
            this.design = design;
            this.topModule = topModule;
            this.moduleNames = moduleNames;
            this.board = board;
        }
        public void run() {
            try {
                synthesize(projectDir, vhdlDir, design, topModule, moduleNames, board);
                addMessage("---------------------" + newline);
            } catch (Exception e) {
                addMessage("*** error found: " + e.getMessage() + newline);
                e.printStackTrace();
            }
        }
    }

    // produce .bit and .mcs files by calling Xilinx command-line tool
    void synthesize(File projectDir, File vhdlDir, ShdlDesign design, SHDLModule topModule, ArrayList moduleNames, SHDLBoard board) throws Exception {
        boolean hasCommModule = (board.getModuleIOStatus(topModule) > 0); // hybrid ou distant only I/O
        String topModuleName = topModule.getName();
        if (hasCommModule) topModuleName = topModuleName + "_comm";

        // détruire le répertoire projet précédent, s'il existe
        if (projectDir.exists()) deleteDir(projectDir);

        // créer le répertoire projet
        projectDir.mkdir();

        //- créer un fichier myproj.lso qui contient le seul mot: work
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File(projectDir, topModuleName + ".lso")));
        pw.println("work");
        pw.flush();
        pw.close();

        //- créer un répertoire tmp
        File tmpDir = new File(projectDir, "tmp");
        tmpDir.mkdir();

        //- mettre dans myproj.prj la liste des fichiers VHDL à synthétiser
        pw = new PrintWriter(new FileOutputStream(new File(projectDir, topModuleName + ".prj")));
        for (int i = 0; i < moduleNames.size(); i++) {
            pw.println("vhdl work \"../vhdl/" + moduleNames.get(i) + ".vhd\"");
        }
        pw.flush();
        pw.close();

        //- mettre dans myproj.xst les options pour le synthétiseur XST
        //(customiser les options -ifn, -ofn et -top)
        pw = new PrintWriter(new FileOutputStream(new File(projectDir, topModuleName + ".xst")));
        pw.println("set -tmpdir \"./tmp\"");
        pw.println("set -xsthdpdir \"./xst\"");
        pw.println("run");
        pw.println("-ifn " + topModuleName + ".prj");
        pw.println("-ifmt mixed");
        pw.println("-ofn " + topModuleName);
        pw.println("-ofmt NGC");
        if (boardName.equals("Nexys-1000")) {
            pw.println("-p xc3s1000-5-ft256");
        } else if (boardName.equals("Nexys II-1200")) {
            pw.println("-p xc3s1200e-5-fg320");
        }
        pw.println("-top " + topModuleName);
        pw.println("-opt_mode Speed");
        pw.println("-opt_level 1");
        pw.println("-iuc NO");
        pw.println("-lso " + topModuleName + ".lso");
        pw.println("-keep_hierarchy NO");
        pw.println("-rtlview Yes");
        pw.println("-glob_opt AllClockNets");
        pw.println("-read_cores YES");
        pw.println("-write_timing_constraints NO");
        pw.println("-cross_clock_analysis NO");
        pw.println("-hierarchy_separator /");
        pw.println("-bus_delimiter <>");
        pw.println("-case maintain");
        pw.println("-slice_utilization_ratio 100");
        pw.println("-verilog2001 YES");
        pw.println("-fsm_extract YES -fsm_encoding Auto");
        pw.println("-safe_implementation No");
        pw.println("-fsm_style lut");
        pw.println("-ram_extract Yes");
        pw.println("-ram_style Auto");
        pw.println("-rom_extract Yes");
        pw.println("-mux_style Auto");
        pw.println("-decoder_extract YES");
        pw.println("-priority_extract YES");
        pw.println("-shreg_extract YES");
        pw.println("-shift_extract YES");
        pw.println("-xor_collapse YES");
        pw.println("-rom_style Auto");
        pw.println("-mux_extract YES");
        pw.println("-resource_sharing YES");
        pw.println("-mult_style auto");
        pw.println("-iobuf YES");
        pw.println("-max_fanout 500");
        pw.println("-bufg 8");
        pw.println("-register_duplication YES");
        pw.println("-register_balancing No");
        pw.println("-slice_packing YES");
        pw.println("-optimize_primitives NO");
        pw.println("-use_clock_enable Yes");
        pw.println("-use_sync_set Yes");
        pw.println("-use_sync_reset Yes");
        pw.println("-iob auto");
        pw.println("-equivalent_register_removal YES");
        pw.println("-slice_utilization_ratio_maxmargin 5");
        pw.flush();
        pw.close();

        //- exécuter (synthèse):
        //xst -ise myproj.ise -intstyle ise -ifn myproj.xst -ofn myproj.syr
        String[] command = new String[] { System.getenv("XILINX") + "\\bin\\nt\\"+"xst.exe",
            "-ise", topModuleName + ".ise",
            "-intstyle", "ise",
            "-ifn", topModuleName + ".xst",
            "-ofn", topModuleName + ".syr"
        };
        if (verboseCheck.isSelected()) addMessage(StringArray2String(command) + "\n");
        addMessage("--- synthesize . . .      ");
        int err = executeWin(command, projectDir);
        if (err == 0) addMessage("done\n"); else addMessage("** errors found\n");
        if (err != 0) return;

        //créer un fichier Nexys.ucf
        pw = new PrintWriter(new FileOutputStream(new File(projectDir, "Nexys.ucf")));
        ArrayList interfaceSignals = design.getTopModule().getInterfaceSignals();
        if (boardName.equals("Nexys-1000")) {
            if (hasCommModule || new SHDLSignal("mclk", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"mclk\"    LOC = \"A8\"  ;");
            if (hasCommModule || new SHDLSignal("astb", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"astb\"    LOC = \"N8\";");
            if (hasCommModule || new SHDLSignal("dstb", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"dstb\"    LOC = \"P7\";");
            if (hasCommModule || new SHDLSignal("pwr", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pwr\"     LOC = \"N7\";");
            if (hasCommModule || new SHDLSignal("pwait", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pwait\"   LOC = \"N5\";");
            if (hasCommModule || new SHDLSignal("pdb", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<0>\"  LOC = \"N12\";");
            if (hasCommModule || new SHDLSignal("pdb", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<1>\"  LOC = \"P12\";");
            if (hasCommModule || new SHDLSignal("pdb", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<2>\"  LOC = \"N11\";");
            if (hasCommModule || new SHDLSignal("pdb", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<3>\"  LOC = \"P11\";");
            if (hasCommModule || new SHDLSignal("pdb", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<4>\"  LOC = \"N10\";");
            if (hasCommModule || new SHDLSignal("pdb", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<5>\"  LOC = \"P10\";");
            if (hasCommModule || new SHDLSignal("pdb", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<6>\"  LOC = \"M10\";");
            if (hasCommModule || new SHDLSignal("pdb", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<7>\"  LOC = \"R10\";");

            if (new SHDLSignal("btn", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<0>\"  LOC = \"J13\"  ;");
            if (new SHDLSignal("btn", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<1>\"  LOC = \"K14\"  ;");
            if (new SHDLSignal("btn", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<2>\"  LOC = \"K13\"  ;");
            if (new SHDLSignal("btn", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<3>\"  LOC = \"K12\"  ;");
            if (new SHDLSignal("sw", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<0>\"   LOC = \"N15\"  ;");
            if (new SHDLSignal("sw", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<1>\"   LOC = \"J16\"  ;");
            if (new SHDLSignal("sw", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<2>\"   LOC = \"K16\"  ;");
            if (new SHDLSignal("sw", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<3>\"   LOC = \"K15\"  ;");
            if (new SHDLSignal("sw", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<4>\"   LOC = \"L15\"  ;");
            if (new SHDLSignal("sw", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<5>\"   LOC = \"M16\"  ;");
            if (new SHDLSignal("sw", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<6>\"   LOC = \"M15\"  ;");
            if (new SHDLSignal("sw", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<7>\"   LOC = \"N16\"  ;");
            if (new SHDLSignal("ld", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<0>\"   LOC = \"L14\"  ;");
            if (new SHDLSignal("ld", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<1>\"   LOC = \"L13\"  ;");
            if (new SHDLSignal("ld", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<2>\"   LOC = \"M14\"  ;");
            if (new SHDLSignal("ld", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<3>\"   LOC = \"L12\"  ;");
            if (new SHDLSignal("ld", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<4>\"   LOC = \"N14\"  ;");
            if (new SHDLSignal("ld", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<5>\"   LOC = \"M13\"  ;");
            if (new SHDLSignal("ld", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<6>\"   LOC = \"P14\"  ;");
            if (new SHDLSignal("ld", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<7>\"   LOC = \"R16\"  ;");
            if (new SHDLSignal("an", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<3>\"   LOC = \"F12\"  ;");
            if (new SHDLSignal("an", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<2>\"   LOC = \"G13\"  ;");
            if (new SHDLSignal("an", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<1>\"   LOC = \"G12\"  ;");
            if (new SHDLSignal("an", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<0>\"   LOC = \"G14\"  ;");
            if (new SHDLSignal("ssg", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<0>\"  LOC = \"F13\"  ;");
            if (new SHDLSignal("ssg", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<1>\"  LOC = \"E13\"  ;");
            if (new SHDLSignal("ssg", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<2>\"  LOC = \"G15\"  ;");
            if (new SHDLSignal("ssg", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<3>\"  LOC = \"H13\"  ;");
            if (new SHDLSignal("ssg", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<4>\"  LOC = \"J14\"  ;");
            if (new SHDLSignal("ssg", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<5>\"  LOC = \"E14\"  ;");
            if (new SHDLSignal("ssg", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<6>\"  LOC = \"G16\"  ;");
            if (new SHDLSignal("ssg", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<7>\"  LOC = \"H14\"  ;");

            if (new SHDLSignal("ja1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja1\"  LOC = \"T14\"  ;");
            if (new SHDLSignal("ja2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja2\"  LOC = \"R13\"  ;");
            if (new SHDLSignal("ja3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja3\"  LOC = \"T13\"  ;");
            if (new SHDLSignal("ja4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja4\"  LOC = \"R12\"  ;");

            if (new SHDLSignal("jb1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb1\"  LOC = \"T12\"  ;");
            if (new SHDLSignal("jb2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb2\"  LOC = \"R11\"  ;");
            if (new SHDLSignal("jb3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb3\"  LOC = \"P8\"  ;");
            if (new SHDLSignal("jb4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb4\"  LOC = \"T10\"  ;");

            if (new SHDLSignal("jc1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc1\"  LOC = \"D5\"  ;");
            if (new SHDLSignal("jc2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc2\"  LOC = \"P9\"  ;");
            if (new SHDLSignal("jc3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc3\"  LOC = \"A5\"  ;");
            if (new SHDLSignal("jc4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc4\"  LOC = \"A7\"  ;");

            if (new SHDLSignal("jd1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd1\"  LOC = \"A9\"  ;");
            if (new SHDLSignal("jd2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd2\"  LOC = \"A12\"  ;");
            if (new SHDLSignal("jd3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd3\"  LOC = \"C10\"  ;");
            if (new SHDLSignal("jd4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd4\"  LOC = \"D12\"  ;");

        } else if (boardName.equals("Nexys II-1200")) {
            //if (hasCommModule || new SHDLSignal("mclk", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"mclk\"    LOC = \"B8\"  ;");
            if (hasCommModule || new SHDLSignal("mclk", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"mclk\"    LOC = \"B8\"  ;");
            if (hasCommModule || new SHDLSignal("astb", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"astb\"   LOC = \"V14\";");
            if (hasCommModule || new SHDLSignal("dstb", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"dstb\"   LOC = \"U14\";");
            if (hasCommModule || new SHDLSignal("pwr", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pwr\"     LOC = \"V16\";");  // appelé USBFlag dans les .ucf Digilent!!!!!!
            if (hasCommModule || new SHDLSignal("pwait", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pwait\"   LOC = \"N9\";");
            if (hasCommModule || new SHDLSignal("pdb", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<0>\"  LOC = \"R14\";");
            if (hasCommModule || new SHDLSignal("pdb", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<1>\"  LOC = \"R13\";");
            if (hasCommModule || new SHDLSignal("pdb", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<2>\"  LOC = \"P13\";");
            if (hasCommModule || new SHDLSignal("pdb", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<3>\"  LOC = \"T12\";");
            if (hasCommModule || new SHDLSignal("pdb", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<4>\"  LOC = \"N11\";");
            if (hasCommModule || new SHDLSignal("pdb", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<5>\"  LOC = \"R11\";");
            if (hasCommModule || new SHDLSignal("pdb", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<6>\"  LOC = \"P10\";");
            if (hasCommModule || new SHDLSignal("pdb", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"pdb<7>\"  LOC = \"R10\";");

            if (new SHDLSignal("btn", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<0>\"  LOC = \"B18\"  ;");
            if (new SHDLSignal("btn", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<1>\"  LOC = \"D18\"  ;");
            if (new SHDLSignal("btn", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<2>\"  LOC = \"E18\"  ;");
            if (new SHDLSignal("btn", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"btn<3>\"  LOC = \"H13\"  ;");
            if (new SHDLSignal("sw", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<0>\"  LOC = \"G18\"  ;");
            if (new SHDLSignal("sw", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<1>\"  LOC = \"H18\"  ;");
            if (new SHDLSignal("sw", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<2>\"  LOC = \"K18\"  ;");
            if (new SHDLSignal("sw", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<3>\"  LOC = \"K17\"  ;");
            if (new SHDLSignal("sw", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<4>\"  LOC = \"L14\"  ;");
            if (new SHDLSignal("sw", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<5>\"  LOC = \"L13\"  ;");
            if (new SHDLSignal("sw", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<6>\"  LOC = \"N17\"  ;");
            if (new SHDLSignal("sw", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"sw<7>\"  LOC = \"R17\"  ;");
            if (new SHDLSignal("ld", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<0>\"  LOC = \"J14\"  ;");
            if (new SHDLSignal("ld", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<1>\"  LOC = \"J15\"  ;");
            if (new SHDLSignal("ld", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<2>\"  LOC = \"K15\"  ;");
            if (new SHDLSignal("ld", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<3>\"  LOC = \"K14\"  ;");
            if (new SHDLSignal("ld", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<4>\"  LOC = \"E16\"  ;");
            if (new SHDLSignal("ld", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<5>\"  LOC = \"P16\"  ;");
            if (new SHDLSignal("ld", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<6>\"  LOC = \"E4\"  ;");
            if (new SHDLSignal("ld", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ld<7>\"  LOC = \"P4\"  ;");
            if (new SHDLSignal("an", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<0>\"   LOC = \"F17\"  ;");
            if (new SHDLSignal("an", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<1>\"   LOC = \"H17\"  ;");
            if (new SHDLSignal("an", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<2>\"   LOC = \"C18\"  ;");
            if (new SHDLSignal("an", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"an<3>\"   LOC = \"F15\"  ;");
            if (new SHDLSignal("ssg", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<0>\"  LOC = \"L18\"  ;");
            if (new SHDLSignal("ssg", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<1>\"  LOC = \"F18\"  ;");
            if (new SHDLSignal("ssg", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<2>\"  LOC = \"D17\"  ;");
            if (new SHDLSignal("ssg", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<3>\"  LOC = \"D16\"  ;");
            if (new SHDLSignal("ssg", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<4>\"  LOC = \"G14\"  ;");
            if (new SHDLSignal("ssg", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<5>\"  LOC = \"J17\"  ;");
            if (new SHDLSignal("ssg", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<6>\"  LOC = \"H14\"  ;");
            if (new SHDLSignal("ssg", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ssg<7>\"  LOC = \"C17\"  ;");

            if (new SHDLSignal("red", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"red<0>\"  LOC = \"R9\"  ;");
            if (new SHDLSignal("red", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"red<1>\"  LOC = \"T8\"  ;");
            if (new SHDLSignal("red", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"red<2>\"  LOC = \"R8\"  ;");
            if (new SHDLSignal("grn", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"grn<0>\"  LOC = \"N8\"  ;");
            if (new SHDLSignal("grn", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"grn<1>\"  LOC = \"P8\"  ;");
            if (new SHDLSignal("grn", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"grn<2>\"  LOC = \"P6\"  ;");
            if (new SHDLSignal("blue", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"blue<0>\"  LOC = \"U5\"  ;");
            if (new SHDLSignal("blue", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"blue<1>\"  LOC = \"U4\"  ;");
            if (new SHDLSignal("hs", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"hs\"    LOC = \"T4\"  ;");
            if (new SHDLSignal("vs", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"vs\"    LOC = \"U3\"  ;");

            if (new SHDLSignal("rxd", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"rxd\"    LOC = \"U6\"  ;");
            if (new SHDLSignal("txd", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"txd\"    LOC = \"P9\"  ;");

            if (new SHDLSignal("ja1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja1\"    LOC = \"L15\"  ;");
            if (new SHDLSignal("ja2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja2\"  LOC = \"K12\"  ;");
            if (new SHDLSignal("ja3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja3\"  LOC = \"L17\"  ;");
            if (new SHDLSignal("ja4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja4\"  LOC = \"M15\"  ;");
            if (new SHDLSignal("ja7", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja7\"  LOC = \"K13\"  ;");
            if (new SHDLSignal("ja8", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja8\"  LOC = \"L16\"  ;");
            if (new SHDLSignal("ja9", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja9\"  LOC = \"M14\"  ;");
            if (new SHDLSignal("ja10", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ja10\"  LOC = \"M16\"  ;");

            if (new SHDLSignal("jb1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb1\"    LOC = \"M13\"  ;");
            if (new SHDLSignal("jb2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb2\"  LOC = \"R18\"  ;");
            if (new SHDLSignal("jb3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb3\"  LOC = \"R15\"  ;");
            if (new SHDLSignal("jb4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb4\"  LOC = \"T17\"  ;");
            if (new SHDLSignal("jb7", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb7\"  LOC = \"P17\"  ;");
            if (new SHDLSignal("jb8", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb8\"  LOC = \"R16\"  ;");
            if (new SHDLSignal("jb9", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb9\"  LOC = \"T18\"  ;");
            if (new SHDLSignal("jb10", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jb10\"  LOC = \"U18\"  ;");

            if (new SHDLSignal("jc1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc1\"  LOC = \"G15\"  ;");
            if (new SHDLSignal("jc2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc2\"  LOC = \"J16\"  ;");
            if (new SHDLSignal("jc3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc3\"  LOC = \"G13\"  ;");
            if (new SHDLSignal("jc4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc4\"  LOC = \"H16\"  ;");
            if (new SHDLSignal("jc7", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc7\"  LOC = \"H15\"  ;");
            if (new SHDLSignal("jc8", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc8\"  LOC = \"F14\"  ;");
            if (new SHDLSignal("jc9", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc9\"  LOC = \"G16\"  ;");
            if (new SHDLSignal("jc10", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jc10\"  LOC = \"J12\"  ;");

            if (new SHDLSignal("jd1", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd1\"  LOC = \"J13\"  ;");
            if (new SHDLSignal("jd2", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd2\"  LOC = \"M18\"  ;");
            if (new SHDLSignal("jd3", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd3\"  LOC = \"N18\"  ;");
            if (new SHDLSignal("jd4", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd4\"  LOC = \"P18\"  ;");
            if (new SHDLSignal("jd7", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd7\"  LOC = \"K14\"  ;");
            if (new SHDLSignal("jd8", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd8\"  LOC = \"K15\"  ;");
            if (new SHDLSignal("jd9", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd9\"  LOC = \"J15\"  ;");
            if (new SHDLSignal("jd10", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"jd10\"  LOC = \"J14\"  ;");

            if (new SHDLSignal("memoe", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memoe\"  LOC = \"T2\" ;");
            if (new SHDLSignal("memwr", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memwr\"  LOC = \"N7\" ;");
            if (new SHDLSignal("ramadv", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ramadv\"  LOC = \"J4\" ;");
            if (new SHDLSignal("ramcs", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ramcs\"  LOC = \"R6\" ;");
            if (new SHDLSignal("ramclk", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ramclk\"  LOC = \"H5\" ;");
            if (new SHDLSignal("ramcre", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ramcre\"  LOC = \"P7\" ;");
            if (new SHDLSignal("ramlb", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ramlb\"  LOC = \"K5\" ;");
            if (new SHDLSignal("ramub", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ramub\"  LOC = \"K4\" ;");
            if (new SHDLSignal("ramwait", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"ramwait\"  LOC = \"F5\" ;");
            if (new SHDLSignal("flashrp", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"flashrp\"  LOC = \"T5\" ;");
            if (new SHDLSignal("flashcs", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"flashcs\"  LOC = \"R5\" ;");
            if (new SHDLSignal("flashststs", true, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"flashststs\"  LOC = \"D3\" ;");

            if (new SHDLSignal("memadr", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<1>\"  LOC = \"J1\" ;");
            if (new SHDLSignal("memadr", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<2>\"  LOC = \"J2\" ;");
            if (new SHDLSignal("memadr", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<3>\"  LOC = \"H4\" ;");
            if (new SHDLSignal("memadr", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<4>\"  LOC = \"H1\" ;");
            if (new SHDLSignal("memadr", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<5>\"  LOC = \"H2\" ;");
            if (new SHDLSignal("memadr", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<6>\"  LOC = \"J5\" ;");
            if (new SHDLSignal("memadr", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<7>\"  LOC = \"H3\" ;");
            if (new SHDLSignal("memadr", 8, 8, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<8>\"  LOC = \"H6\" ;");
            if (new SHDLSignal("memadr", 9, 9, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<9>\"  LOC = \"F1\" ;");
            if (new SHDLSignal("memadr", 10, 10, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<10>\"  LOC = \"G3\" ;");
            if (new SHDLSignal("memadr", 11, 11, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<11>\"  LOC = \"G6\" ;");
            if (new SHDLSignal("memadr", 12, 12, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<12>\"  LOC = \"G5\" ;");
            if (new SHDLSignal("memadr", 13, 13, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<13>\"  LOC = \"G4\" ;");
            if (new SHDLSignal("memadr", 14, 14, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<14>\"  LOC = \"F2\" ;");
            if (new SHDLSignal("memadr", 15, 15, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<15>\"  LOC = \"E1\" ;");
            if (new SHDLSignal("memadr", 16, 16, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<16>\"  LOC = \"M5\" ;");
            if (new SHDLSignal("memadr", 17, 17, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<17>\"  LOC = \"E2\" ;");
            if (new SHDLSignal("memadr", 18, 18, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<18>\"  LOC = \"C2\" ;");
            if (new SHDLSignal("memadr", 19, 19, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<19>\"  LOC = \"C1\" ;");
            if (new SHDLSignal("memadr", 20, 20, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<20>\"  LOC = \"D2\" ;");
            if (new SHDLSignal("memadr", 21, 21, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<21>\"  LOC = \"K3\" ;");
            if (new SHDLSignal("memadr", 22, 22, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<22>\"  LOC = \"D1\" ;");
            if (new SHDLSignal("memadr", 23, 23, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memadr<23>\"  LOC = \"K6\" ;");

            if (new SHDLSignal("memdb", 0, 0, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"L1\" ;");
            if (new SHDLSignal("memdb", 1, 1, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"L4\" ;");
            if (new SHDLSignal("memdb", 2, 2, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"L6\" ;");
            if (new SHDLSignal("memdb", 3, 3, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"M4\" ;");
            if (new SHDLSignal("memdb", 4, 4, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"N5\" ;");
            if (new SHDLSignal("memdb", 5, 5, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"P1\" ;");
            if (new SHDLSignal("memdb", 6, 6, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"P2\" ;");
            if (new SHDLSignal("memdb", 7, 7, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"R2\" ;");
            if (new SHDLSignal("memdb", 8, 8, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"L3\" ;");
            if (new SHDLSignal("memdb", 9, 9, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"L5\" ;");
            if (new SHDLSignal("memdb", 10, 10, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"M3\" ;");
            if (new SHDLSignal("memdb", 11, 11, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"M6\" ;");
            if (new SHDLSignal("memdb", 12, 12, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"L2\" ;");
            if (new SHDLSignal("memdb", 13, 13, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"N4\" ;");
            if (new SHDLSignal("memdb", 14, 14, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"R3\" ;");
            if (new SHDLSignal("memdb", 15, 15, design.getTopModule()).containedIn(interfaceSignals)) pw.println("NET \"memdb\"  LOC = \"T1\" ;");
        }
        pw.flush();
        pw.close();

        //- exécuter (translation):
        //ngdbuild -ise myproj.ise -intstyle ise -dd _ngo -nt timestamp -uc Nexys.ucf -p xc3s1000-ft256-5 myproj.ngc myproj.ngd
        String fpga = "";
        if (boardName.equals("Nexys-1000")) {
            fpga = "xc3s1000-ft256-5";
        } else if (boardName.equals("Nexys II-1200")) {
            fpga = "xc3s1200e-fg320-5";
        }
        command = new String[] { System.getenv("XILINX") + "\\bin\\nt\\"+"ngdbuild.exe",
            "-ise", topModuleName + ".ise",
            "-intstyle", "ise",
            "-dd", "_ngo",
            "-nt", "timestamp",
            "-uc", "Nexys.ucf",
            "-p", fpga,
            topModuleName + ".ngc",
            topModuleName + ".ngd",
        };
        if (verboseCheck.isSelected()) addMessage(StringArray2String(command) + "\n");
        addMessage("--- translate . . .       ");
        err = executeWin(command, projectDir);
        if (err == 0) addMessage("done\n"); else addMessage("** errors found\n");
        if (err != 0) return;

        //- exécuter (map):
        //map -ise myproj.ise -intstyle ise -p xc3s1000-ft256-5 -cm area -pr b -k 4 -c 100 -o myproj.ncd myproj.ngd myproj.pcf
        command = new String[] { System.getenv("XILINX") + "\\bin\\nt\\"+"map.exe",
            "-ise", topModuleName + ".ise",
            "-intstyle", "ise",
            "-p", fpga,
            "-cm", "area",
            "-pr", "b",
            //"-k", "4",
            "-c", "100",
            "-o", topModuleName + ".ncd",
            topModuleName + ".ngd",
            topModuleName + ".pcf",
        };
        if (verboseCheck.isSelected()) addMessage(StringArray2String(command) + "\n");
        addMessage("--- map . . .             ");
        err = executeWin(command, projectDir);
        if (err == 0) addMessage("done\n"); else addMessage("** errors found\n");
        if (err != 0) return;

        //- exécuter (place and route, par):
        //par -ise myproj.ise -w -intstyle ise -ol std -t 1 myproj.ncd myproj.ncd myproj.pcf
        command = new String[] { System.getenv("XILINX") + "\\bin\\nt\\"+"par.exe",
            "-ise", topModuleName + ".ise",
            "-w",
            "-intstyle", "ise",
            "-ol", "std",
            "-t", "1",
            topModuleName + ".ncd",
            topModuleName + ".ncd",
            topModuleName + ".pcf",
        };
        if (verboseCheck.isSelected()) addMessage(StringArray2String(command) + "\n");
        addMessage("--- place and route . . . ");
        err = executeWin(command, projectDir);
        if (err == 0) addMessage("done\n"); else addMessage("** errors found\n");
        if (err != 0) return;

        //- exécuter:
        //trce -ise myproj.ise -intstyle ise -e 3 -l 3 -s 5 -xml myproj myproj.ncd -o myproj.twr myproj.pcf -ucf Nexys.ucf
        command = new String[] { System.getenv("XILINX") + "\\bin\\nt\\"+"trce.exe",
            "-ise", topModuleName + ".ise",
            "-intstyle", "ise",
            "-l", "3",
            "-s", "5",
            "-xml", topModuleName,
            topModuleName + ".ncd",
            "-o", topModuleName + ".twr",
            topModuleName + ".pcf",
            "-ucf", "Nexys.ucf",
        };
        if (verboseCheck.isSelected()) addMessage(StringArray2String(command) + "\n");
        addMessage("--- trace . . .           ");
        err = executeWin(command, projectDir);
        if (err == 0) addMessage("done\n"); else addMessage("** errors found\n");
        if (err != 0) return;

        //- créer un fichier myproj.ut qui contient les options pour la bitgen
        pw = new PrintWriter(new FileOutputStream(new File(projectDir, topModuleName + ".ut")));
        pw.println("-w");
        pw.println("-g DebugBitstream:No");
        pw.println("-g Binary:no");
        pw.println("-g CRC:Enable");
        pw.println("-g ConfigRate:6");
        pw.println("-g CclkPin:PullUp");
        pw.println("-g M0Pin:PullUp");
        pw.println("-g M1Pin:PullUp");
        pw.println("-g M2Pin:PullUp");
        pw.println("-g ProgPin:PullUp");
        pw.println("-g DonePin:PullUp");
        pw.println("-g TckPin:PullUp");
        pw.println("-g TdiPin:PullUp");
        pw.println("-g TdoPin:PullUp");
        pw.println("-g TmsPin:PullUp");
        pw.println("-g UnusedPin:PullDown");
        pw.println("-g UserID:0xFFFFFFFF");
        pw.println("-g DCMShutdown:Disable");
        pw.println("-g DCIUpdateMode:AsRequired");
        pw.println("-g StartUpClk:JtagClk");
        pw.println("-g DONE_cycle:4");
        pw.println("-g GTS_cycle:5");
        pw.println("-g GWE_cycle:6");
        pw.println("-g LCK_cycle:NoWait");
        pw.println("-g Match_cycle:Auto");
        pw.println("-g Security:None");
        pw.println("-g DonePipe:No");
        pw.println("-g DriveDone:No");
        pw.flush();
        pw.close();

        //- exécuter (bitgen):
        //bitgen -ise myproj.ise -intstyle ise -f myproj.ut myproj.ncd
        command = new String[] { System.getenv("XILINX") + "\\bin\\nt\\" + "bitgen.exe",
            //"-ise", topModuleName + ".ise",
            //"-f", topModuleName + ".ut",
            "-intstyle", "ise",
            "-w",
            "-g", "DebugBitstream:No",
            "-g", "Binary:no",
            "-g", "CRC:Enable",
            "-g", "ProgPin:PullUp",
            "-g", "DonePin:PullUp",
            "-g", "TckPin:PullUp",
            "-g", "TdiPin:PullUp",
            "-g", "TdoPin:PullUp",
            "-g", "TmsPin:PullUp",
            "-g", "UnusedPin:PullDown",
            "-g", "UserID:0xFFFFFFFF",
            "-g", "DCMShutdown:Disable",
            "-g", "StartupClk:JtagClk",
            "-g", "DONE_cycle:4",
            "-g", "GTS_cycle:5",
            "-g", "GWE_cycle:6",
            "-g", "LCK_cycle:NoWait",
            "-g", "Security:None",
            "-g", "DonePipe:No",
            "-g", "DriveDone:No",
            topModuleName + ".ncd",
        };
        if (verboseCheck.isSelected()) addMessage(StringArray2String(command) + "\n");
        addMessage("--- bitgen. . .           ");
        err = executeWin(command, projectDir);
        if (err == 0) addMessage("done\n"); else addMessage("** errors found\n");
        if (err != 0) return;

        // création du fichiers .mcs à partir du fichier .bit
        // génération d'un fichier impact.cmd
        pw = new PrintWriter(new FileOutputStream(new File(projectDir, "impact.cmd")));
        pw.println("setMode -pff");
        pw.println("setSubmode -pffserial");
        pw.println("addPromDevice -p 1 -name xcf04s");
        pw.println("addDesign -version 0 -name 0");
        pw.println("addDeviceChain -index 0");
        pw.println("addDevice -p 1 -file " + topModuleName + ".bit");
        pw.println("generate -format mcs -fillvalue FF -output " + topModuleName);
        pw.println("quit");
        pw.flush();
        pw.close();
        command = new String[] { System.getenv("XILINX") + "\\bin\\nt\\"+"impact.exe",
            "-batch", "impact.cmd",
        };
        if (verboseCheck.isSelected()) addMessage(StringArray2String(command) + "\n");
        addMessage("--- .mcs file creation. . . ");
        err = executeWin(command, projectDir);
        if (err == 0) addMessage("done\n"); else addMessage("** errors found\n");
        if (err != 0) return;

        synthesizeCheck.setSelected(false);
        verboseCheck.setSelected(false);

    }

    // exécute <command> dans le répertoire de travail <dir>, et affiche les messages normaux et d'erreur
    // renvoie la valeur de retour
    int executeWin(String[] command, File dir) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(command);
        Map<String, String> environ = builder.environment();
        builder.directory(dir);

        final Process process = builder.start();

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            if (verboseCheck.isSelected()) addMessage(line + "\n");
            System.out.println(line);
        }

        InputStream eis = process.getErrorStream();
        InputStreamReader eisr = new InputStreamReader(eis);
        BufferedReader ebr = new BufferedReader(eisr);
        String eline;
        while ((eline = ebr.readLine()) != null) {
            if (verboseCheck.isSelected()) addMessage("*** " + eline + "\n");
            System.out.println(line);
        }

        return process.exitValue();
    }


    // détruit le répertoire <dir> et tout son contenu
    boolean deleteDir(File dir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
                deleteDir(files[i]);
            else
                files[i].delete();
        }
        return dir.delete();
    }



    //////////////////////////   DISTANT I/O    //////////////////////////


    class IOListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // génère comm.ini dans le répertoire courant
            File currDir = new File(".");
            design.generateCommIni(topModule, board, currDir);

            try {
                // creation du thread de comm
                commThread = new CommThread();
                if (CommThread.init() < 0) {
                    // impossible de capturer une erreur possible (??)
                    throw new Exception("Unkown error");
                }
                if (CommThread.openData() < 0) {
                    JOptionPane.showMessageDialog(null, "Unable to establish USB connection. Possible causes are:\n  - cable unplugged or board not powered\n  - ExPort software is running\n  - board not listed in Device Table (run 'ExPort->Configure')", "Connection error", JOptionPane.ERROR_MESSAGE);
                    throw new Exception("Unable to establish USB connection");
                }

                // ouvre 'comm.ini' ou le créé vide
                iniFile = new File("comm.ini");
                iniFile.createNewFile();

                // lit comm.ini et traduit ses données dans dataModel
                parseCommIni();

                // creation fichier de log
                logPR = new PrintWriter(new FileWriter("comm.log"));

                // lancement de l'interface
                commFrame = new CommFrame("Distant I/O");

                commFrame.setSize(dataModel.getFrameSize());
                commFrame.setLocation(dataModel.getFrameLocation());
                commFrame.setVisible(true);

                // lancement du thread de lecture
                commThread.start();

            } catch(Exception ex) {
                ex.printStackTrace();
                CommThread.closeData();
                logPR.close();
            }

        }
    }


    class DataPad {
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

    class Scalar {
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
            try {
                br.close();
            } catch (IOException e) {
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
            CommThread.closeData();
            //System.exit(0);
        }
        public void windowDeactivated(WindowEvent ev) {}
        public void windowDeiconified(WindowEvent ev) {}
        public void windowIconified(WindowEvent ev) {}
        public void windowOpened(WindowEvent ev) {}
    }


}
