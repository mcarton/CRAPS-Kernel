
package org.jcb.shdl;

import org.jcb.shdl.shdlc.java.*;

import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libegg.base.*;
import java.util.*;
import java.io.*;

public class ShdlDesign {

	private LibManager libManager;
	private boolean verbose;
	private PrintStream errorStream;

	private HashMap collectedModules;
	//private ArrayList predefinedOccurences;

	private static final String newline = System.getProperty("line.separator");
	private static final long serialVersionUID = 1L;

	public ShdlDesign(LibManager libManager, boolean verbose, PrintStream errorStream) {
		this.libManager = libManager;
		this.verbose = verbose;
		this.errorStream = errorStream;
		collectedModules = new HashMap();
		//predefinedOccurences = new ArrayList();
	}


	// Parse all sourceFiles, and then recursively collect all modules, starting from main statements
	// The first file is supposed to contain the main statements
	// Return true if no error is detected
	//
	public boolean collect(ArrayList sourceFiles) {
		boolean ok = true;
		for (int i = 0; i < sourceFiles.size(); i++) {
			File file = (File) sourceFiles.get(i);
			SHDL compilo = null;
			try {
				String[] args = new String[] { file.getAbsolutePath() };
				if (verbose) errorStream.println("-- parse file " + file.getAbsolutePath());
				Options opts = new Options(args);
				opts.analyse();
				compilo = new SHDL(opts);
				compilo.compile() ;
				// modules = synthetized attribute of axiom PROG
				ArrayList modules = compilo.get_modules().getModules();
				for (int j = 0; j < modules.size(); j++) {
					SHDLModule module = (SHDLModule) modules.get(j);
					if (module.isEmpty()) continue; 
				      	if (module.getName() == null) module.setName("0main");
					module.setFile(file);
					// look for an already present module of that name
					SHDLModule other = (SHDLModule) collectedModules.get(module.getName());
					if (other != null) {
						if (module.isMain()) {
							errorStream.println("** " + file.getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : main statements already defined in '" + other.getFile().getAbsolutePath());
						} else {
							errorStream.println("** " + file.getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : module '" + module.getName() + "' already defined in '" + other.getFile().getCanonicalPath() + ":" + other.getBeginLine() + ":" + other.getEndLine());
						}
						return false;
					} else
						// add module to collectedModules
						collectedModules.put(module.getName(), module);
				}

			} catch(EGGException e){
				// display error messages
				if (e.getLine() == -1)
					errorStream.println("** " + file.getName() + " : parse error, " + e.getMsg());
				else
					errorStream.println("** " + file.getName() + ":" + e.getLine() + " : parse error, " + e.getMsg());
				return false; // to continue would cause too many meaningless errors
			} catch(Exception e){
				e.printStackTrace();
				errorStream.println("** unknown internal error e=" + e);
				return false; // to continue would cause too many meaningless errors
			} finally {
				try {
					compilo.get_scanner().contexte.source.close() ;
				} catch(Exception ex) {
					errorStream.println("** could not close source file: " + file);
				}
			}
		}

		// recursively collect all modules referenced from the initial modules
		ArrayList occurences = new ArrayList();
		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			occurences.addAll(module.getModuleOccurences());
		}
		return collectModules(occurences, ok);
	}

	
	//
	// recursively collect all modules referenced from the list of SHDLModuleOccurence of <toAdd>
	//
	public boolean collectModules(ArrayList toAdd, boolean ok) {
		if (toAdd.size() == 0) {
			// success : all modules referenced from the initial ones are in collectedModules
			//errorStream.println("collectedModules=" + collectedModules);
			return ok;
		}

		ArrayList toBeAdded = new ArrayList();
		for (int i = 0; i < toAdd.size(); i++) {
			SHDLModuleOccurence occurence = (SHDLModuleOccurence) toAdd.get(i);
			// check if it is the name of a predefined module
			SHDLPredefinedOccurence po = SHDLPredefinedOccurence.getPredefined(occurence);
			if (po != null) {
				//predefinedOccurences.add(po);
				occurence.setPredefined(po);
				continue;
			}
			// check if the referenced module has already been loaded
			SHDLModule found = (SHDLModule) collectedModules.get(occurence.getName());
			if (found == null) {
				// referenced module was not already parsed files -> look for it in files
				// with names starting with the module's name
				File file = libManager.lookFor(occurence.getName());
				SHDL compilo = null;
				
				// traduction d'une description de graphe d'états
				if ((file != null) && file.getAbsolutePath().endsWith(".net")) {
					NetConverter netConverter = new NetConverter(file, errorStream);
					file = netConverter.start();
					if (file == null) {
						errorStream.println("** graph translation failed" + newline);
						errorStream.println("---------------------" + newline);
						ok = false;
					}
				}
				
				if (file != null) {
					try {
						String[] args = new String[] { file.getAbsolutePath() };
						if (verbose) errorStream.println("-- parse file " + file.getAbsolutePath());
						Options opts = new Options(args);
						opts.analyse();
						compilo = new SHDL(opts);
						compilo.compile() ;
						ArrayList modules = compilo.get_modules().getModules();
						// examine parsed modules
						for (int j = 0; j < modules.size(); j++) {
							SHDLModule module = (SHDLModule) modules.get(j);
							if (module.isMain()) {
							} else {
								// look if this module has already been collected
								SHDLModule other = (SHDLModule) collectedModules.get(module.getName());
								if (other != null) {
									errorStream.println("** " + file.getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : module '" + module.getName() + "' already defined in " + other.getFile().getName() + ":" + other.getBeginLine() + "-" + other.getEndLine());
									ok = false;
								} else {
									collectedModules.put(module.getName(), module);
									module.setFile(file);
								}
							}
						}
					} catch(EGGException e){
						ok = false;
						// display error messages
						if (e.getLine() == -1)
							errorStream.println("** " + file.getName() + " : " + e.getMsg());
						else
							errorStream.println("** " + file.getName() + ":" + e.getLine() + " : " + e.getMsg());
					} finally {
						try {
							compilo.get_scanner().contexte.source.close() ;
						} catch(Exception ex) {
							errorStream.println("** could not close source file: " + file);
						}
					}
				}
			} 
			
			// check (again) if occurence module is present
			found = (SHDLModule) collectedModules.get(occurence.getName());
			if (found != null) {
				// look for all referenced submodules and add them for next round
				ArrayList occurences = found.getModuleOccurences();
				for (int k = 0; k < occurences.size(); k++) {
					SHDLModuleOccurence occur = (SHDLModuleOccurence) occurences.get(k);
					// add for next round all modules new to usedModules
					//if (collectedModules.get(occur.getName()) == null) {
						toBeAdded.add(occur);
					//}
				}
			} else {
				errorStream.println("** " + occurence.getModule().getFile().getName() + ":" + occurence.getBeginLine() + " : could not find module '" + occurence.getName() + "'");
				ok = false;
			}
		}
		// go on recursively
		return collectModules(toBeAdded, ok);
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// look for loops in the modules hierarchy
	//
	public boolean checkModuleDependences() {
		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			SHDLModule[] dep = checkModuleDependences(module);
			if (dep == null) return true;
			errorStream.println("** modules '" + dep[0].getName() + "' and '" + dep[1].getName() + "' are making (possibly indirectly) mutual references");
		}
		return false;
	}

	private HashMap ancestors = new HashMap();

	// look for loops in the modules hierarchy, starting from father
	// return null if there is none, or a pair of modules which are mutually dependant
	SHDLModule[] checkModuleDependences(SHDLModule father) {
		ArrayList fatherAncestors = (ArrayList) ancestors.get(father);
		if (fatherAncestors == null) fatherAncestors = new ArrayList();
		for (int i = 0; i < father.getModuleOccurences().size(); i++) {
			SHDLModuleOccurence modOcc = (SHDLModuleOccurence) father.getModuleOccurences().get(i);
			if (modOcc.isPredefined()) continue;
			SHDLModule son = (SHDLModule) collectedModules.get(modOcc.getName());
			ArrayList sonAncestors = (ArrayList) ancestors.get(son);
			if (sonAncestors == null) sonAncestors = new ArrayList();
			// add <father>'s ancestors as ancestors of <son>
			for (int j = 0; j < fatherAncestors.size(); j++) {
				Object fatherAncestor = fatherAncestors.get(j);
				if (!sonAncestors.contains(fatherAncestor)) sonAncestors.add(fatherAncestor);
			}
			// add <father> as ancestor of <son>
			if (!sonAncestors.contains(father)) sonAncestors.add(father);
			ancestors.put(son, sonAncestors);
			// stop prematurely if <son> is one of his own ancestors
			if (sonAncestors.contains(son)) {
				return new SHDLModule[] { father, son };
			} else {
				// check from <son>
				SHDLModule[] dep = checkModuleDependences(son);
				if (dep != null) return dep;
			}
		}
		return null;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Check all modules of collectedModules
	//
	public boolean check() {
		boolean ok = true;

		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();

			// check that module name is different than 'main' and VHDL compliant
			if (module.getName().equalsIgnoreCase("main")) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : module name '" + module.getName() + "' cannot be 'main'");
				ok = false;
			}
			if ((module.getName().indexOf("__") != -1) || module.getName().startsWith("_") || module.getName().endsWith("_")) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : module name '" + module.getName() + "' cannot start or end  with '_', nor contain '__' (VHDL compatibility)");
				ok = false;
			}

			// check signals and compute their arities
			for (int i = 0; i < module.getModuleSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
				boolean oks = signal.checkSignal();
				if (!oks) errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : unallowed signal name '" + signal.getName());
				ok &= oks;

			}
			if (! ok) return false; // to continue would cause too many errors

			// check combinatorial settings individually
			for (int i = 0; i < module.getCombinSettings().size(); i++) {
				SHDLCombinatorialSetting combinSetting = (SHDLCombinatorialSetting) module.getCombinSettings().get(i);
				ok = checkCombinSetting(ok, combinSetting);
			}

			// check sequential settings individually
			for (int i = 0; i < module.getSequentialSettings().size(); i++) {
				SHDLSequentialSetting seqSetting = (SHDLSequentialSetting) module.getSequentialSettings().get(i);
				ok = checkSeqSetting(ok, seqSetting);
			}

			// check sequential modifiers and attach them to the corresponding sequential settings
			for (int i = 0; i < module.getSequentialModifiers().size(); i++) {
				SHDLSequentialModifier seqModifier = (SHDLSequentialModifier) module.getSequentialModifiers().get(i);
				ok = checkSeqModifier(ok, seqModifier);
			}

			// check that each sequential setting has at least a clock modifier, and that
			// each T and JK flip-flop has a reset modifier
			for (int i = 0; i < module.getSequentialSettings().size(); i++) {
				SHDLSequentialSetting seqSetting = (SHDLSequentialSetting) module.getSequentialSettings().get(i);
				ok = checkSeqSettingComplete(ok, seqSetting);
			}

			// check module occurences
			for (int i = 0; i < module.getModuleOccurences().size(); i++) {
				SHDLModuleOccurence moduleOccurence = (SHDLModuleOccurence) module.getModuleOccurences().get(i);
				if (moduleOccurence.isPredefined()) {
					ok = moduleOccurence.getPredefined().check(ok, module, errorStream);
				} else {
					ok = checkModuleOccurence(ok, moduleOccurence, module);
				}
			}
		}

		// the next step could crash with 'wrong number of argument' errors on module occurences
		if (!ok) return false;

		// Complete for each scalar signal and for each scalar element of buses its source list
		// and its 'read' attribute, by going through the occurences hierarchy
		// Store results in hashmaps, since most bus scalar signals do not appear as SHDLSignal's
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			for (int i = 0; i < module.getModuleSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
				// complete signal sources, going through occurences hierarchy
				if (signal.isPartOfBus()) {
					for (int j = signal.getLowestIndex(); j <= signal.getHighestIndex(); j++) {
						String signalName = signal.getPrefix() + "[" + j + "]";
						if (module.getSignalSources(signalName) == null) {
							module.completeBusSignalSources(signal, j, collectedModules);
						}
						// set 'isInterface" status
						module.setIsInterface(signalName, signal.isInterface() | module.isInterface(signalName));
					}
				} else {
					module.completeScalarSignalSources(signal, collectedModules);
				}
			}
		}
		
		// Look for source conflicts between signals
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			// all main module signals are supposed to be set/get from the outside
			//if (module.isMain()) continue;
			for (Iterator sit = module.getSignalIterator(); sit.hasNext(); ) {
				String signalName = (String) sit.next();
				ArrayList sources = module.getSignalSources(signalName);
				ok = checkSourcesConflict(ok, signalName, sources, module);
			}
		}
		
		// Look for :
		//   - unused signals: signals which have no source and are not read by anything
		//   - unassigned signals : non-interface signals which have no source
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			
			// all main module signals are supposed to be set/get from the outside
			if (module.isMain()) continue;
			for (int i = 0; i < module.getModuleSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
				if (signal.isConstant()) continue;
				if (signal.isPartOfBus()) {
					ArrayList unassigned = new ArrayList();
					ArrayList unused = new ArrayList();
					for (int j = signal.getLowestIndex(); j <= signal.getHighestIndex(); j++) {
						String signalName = signal.getPrefix() + "[" + j + "]";
						ArrayList sources = module.getSignalSources(signalName);
						boolean isRead = module.getRead(signalName);
						if ((sources == null) || (sources.size() == 0)) {
							if (!module.isInterface(signalName) && isRead) {
								//System.out.println("uass signalName=" + signalName);
								unassigned.add(new Integer(j));
							} else if (!isRead)
								unused.add(new Integer(j));
						}
					}
					if (unassigned.size() == 1) {
						errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : WARNING : signal '" + signal.getPrefix() + "[" + unassigned.get(0) + "]' is not assigned");
					} else if (unassigned.size() > 1) {
						ArrayList segments = findSegments(unassigned);
						for (int k = 0; k < segments.size(); k++) {
							int[] segment = (int[]) segments.get(k);
							errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : WARNING : signals '" + signal.getPrefix() + "[" + segment[0] + ".." + segment[1] + "]' are not assigned");
						}
					}
					if (unused.size() == 1) {
						errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : WARNING : signal '" + signal.getPrefix() + "[" + unused.get(0) + "]' is not used");
					} else if (unused.size() > 1) {
						ArrayList segments = findSegments(unused);
						for (int k = 0; k < segments.size(); k++) {
							int[] segment = (int[]) segments.get(k);
							errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : WARNING : signals '" + signal.getPrefix() + "[" + segment[0] + ".." + segment[1] + "]' are not used");
						}
					}
				} else {
					String signalName = signal.getPrefix();
					ArrayList sources = module.getSignalSources(signalName);
					boolean isRead = module.getRead(signalName);
					if ((sources == null) || (sources.size() == 0)) {
						if (!signal.isInterface() && isRead) {
							errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : WARNING : signal '" + signalName + "' is not assigned");
						} else if (!isRead) {
							errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : WARNING : signal '" + signalName + "' is unused");
						}
					}
				}
			}
		}

//		// -- VHDL --
//		// Make special checks and completions for module names such as 'DigilentS3'
//		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
//			SHDLModule module = (SHDLModule) it.next();
//			SHDLBoard board = SHDLBoard.getBoard(module);
//			if (board != null) {
//				ok = board.check(ok, errorStream);
//			}
//		}

		// Look for signal prefixes used both as buses and as scalars (i.e. s=a*b; s[3..0]=15;)
		// Look also for bus signals which are used both 'dowto' and 'to'
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			ArrayList checkedInError = new ArrayList();
			for (int i = 0; i < module.getModuleSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
				if (signal.isConstant()) continue;
				String prefix = signal.getPrefix();
				if (checkedInError.contains(prefix)) continue;
				int dir = module.getIndexDir(prefix);
				if (dir == -3) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal '" + prefix + "' is used both as a bus and as a scalar");
					checkedInError.add(prefix);
					ok = false;
				} else if (dir == -2) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : bus signal '" + signal.getPrefix() + "' indexes are used both in ascending and descending order");
					checkedInError.add(signal.getPrefix());
					ok = false;
				}
			}
		}
		
		// Check that all signal names are different than module name
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			for (int i = 0; i < module.getModuleSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
				if (signal.isConstant()) continue;
				String prefix = signal.getPrefix();
				if (prefix.equals(module.getName())) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal '" + prefix + "' cannot have the same name than the module in which it is defined");
					ok = false;
					break;
				}
			}
		}
		
		// -- VHDL --
		// Check that modules names are VHDL compatibles, that they are not 'main'
		// nor a VHDL-reserved word, and are all different for a case-insensitive equality,
		ArrayList moduleNames = new ArrayList();
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			if (module.getName().equals("main")) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : module name cannot be 'main'");
				ok = false;
			}
			if (isVHDLReserved(module.getName())) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : module name '" + module.getName() + "' is reserved (VHDL compatibility)");
				ok = false;
			}
			if ((module.getName().indexOf("__") != -1) || module.getName().startsWith("_") || module.getName().endsWith("_")) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal name '" + module.getName() + "' cannot start or end  with '_', nor contain '__' (VHDL compatibility)");
				ok = false;
			}
			for (int i = 0; i < moduleNames.size(); i++) {
				if (module.getName().equalsIgnoreCase((String)moduleNames.get(i))) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : module name '" + module.getName() + "' is used elsewhere with different case-letters");
					ok = false;
					break;
				}
			}
			moduleNames.add(module.getName());
		}
		
		// -- VHDL --
		// Check that signal names are VHDL compatibles, are not VHDL-reserved words,
		// and are all different for a case-insensitive equality
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			ArrayList signalPrefixes = new ArrayList();
			ArrayList toCheckForCase = new ArrayList();
			for (int i = 0; i < module.getModuleSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
				if (signal.isConstant()) continue;
				String prefix = signal.getPrefix();
				if (signalPrefixes.contains(prefix)) continue;
				signalPrefixes.add(prefix);
				ArrayList sigs = module.getPrefixSignals(prefix);
				for (int j = 0; j < sigs.size(); j++) {
					SHDLSignal sig = (SHDLSignal) sigs.get(j);
					String vhdlPrefix = module.getVHDLNamePrefix(sig);
					if (toCheckForCase.contains(vhdlPrefix)) continue;
					toCheckForCase.add(vhdlPrefix);
				}
				if (isVHDLReserved(prefix)) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal name '" + prefix + "' is reserved (VHDL compatibility)");
					ok = false;
				}
				if ((prefix.indexOf("__") != -1) || prefix.startsWith("_") || prefix.endsWith("_")) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal name '" + prefix + "' cannot start or end  with '_', nor contain '__' (VHDL compatibility)");
					ok = false;
				}
			}
			// test that the different vhdl prefixes are all different, with no case-sensitivity
			for (int i = 0; i < toCheckForCase.size(); i++) {
				String vhdlPrefix = (String) toCheckForCase.get(i);
				for (int j = 0; j < toCheckForCase.size(); j++) {
					if (j == i) continue;
					if (toCheckForCase.get(j).equals(vhdlPrefix)) {
						errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal names '" + vhdlPrefix + "' and '" + toCheckForCase.get(j) + "' are equals when ignoring case");
						ok = false;
						break;
					}
				}
			}
		}
		
		// -- VHDL --
		// Check that all bus interface signals have different prefixes and that the range of
		// bus signals go from the lowest to the highest index used
		for (Iterator it = collectedModules.values().iterator(); it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			ArrayList prefixes = new ArrayList();
			for (int i = 0; i < module.getInterfaceSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(i);
				String prefix = signal.getPrefix();
				int dir = module.getIndexDir(prefix);
				if (dir <= 0) continue; // no problem with 1-arity only signals
				if (prefixes.contains(prefix)) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : signal prefix '" + prefix + "' cannot be used several times in the module interface");
					ok = false;
				} else
					prefixes.add(prefix);
				int high = module.getHighestIndex(prefix);
				int low = module.getLowestIndex(prefix);
				if (signal.isPartOfBus()) {
					if ((signal.getLowestIndex() != low) || (signal.getHighestIndex() != high)) {
						errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : interface signal '" + signal.getName() + "' does not cover all the range of indexes " + low + "-" + high + " used in the module for bus signal '" + prefix + "'");
						ok = false;
					}
				} else if (high != low) {
					StringBuffer otherSignals = new StringBuffer();
					ArrayList localSignals = module.getModuleSignals();
					for (int j = 0; j < localSignals.size(); j++) {
						SHDLSignal sig = (SHDLSignal) localSignals.get(j);
						if (sig.isConstant()) continue;
						if (!sig.getPrefix().equals(prefix)) continue;
						if (sig.equals(signal)) continue;
						if (otherSignals.length() > 0) otherSignals.append(", ");
						otherSignals.append(sig.getName());
					}
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : interface signal '" + signal.getName() + "' is declared as scalar, but is used as a bus in the module body with the following signals : " + otherSignals);
					ok = false;
				}
			}
		}
		
		return ok;
	}

	final String[] reservedVHDLWords = { "abs", "access", "after", "alias", "all", "and", "architecture",
		"array", "assert", "attribute", "begin", "block", "body", "buffer", "bus", "case",
		"component", "configuration", "constant", "disconnect", "downto", "else", "elsif", "end",
		"entity", "exit", "file", "for", "function", "generate", "generic", "group", "guarded",
		"if", "impure", "in", "inertial", "inout", "is", "label", "library", "linkage", "literal",
		"loop", "map", "mod", "nand", "new", "next", "nor", "not", "null", "of", "on", "open",
		"or", "others", "out", "package", "port", "postponed", "procedure", "process", "pure",
		"range", "record", "register", "reject", "rem", "report", "return", "rol", "ror", "select",
		"severity", "shared", "signal", "sla", "sll", "sra", "srl", "subtype", "then", "to", "transport",
		"type", "unaffected", "units", "until", "use", "variable", "wait", "when", "while", "with",
		"xnor", "xor", "xor3" };

	boolean isVHDLReserved(String name) {
		for (int i = 0; i < reservedVHDLWords.length; i++) {
			if (name.equalsIgnoreCase(reservedVHDLWords[i])) return true;
		}
		return false;
	}

	ArrayList findSegments(ArrayList nums) {
		ArrayList res = new ArrayList();
		int last = -2;
		int[] segment = null;
		for (int i = 0; i < nums.size(); i++) {
			int num = ((Integer) nums.get(i)).intValue();
			if (num == last + 1) {
				segment[1] = num;
			} else {
				if (segment != null) res.add(segment);
				segment = new int[] { num, num };
			}
			last = num;
		}
		if (segment != null) res.add(segment);
		return res;
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// check combinatorial setting
	// set also the 'read' and 'write' attributes for all included signals
	// 
	boolean checkCombinSetting(boolean ok, SHDLCombinatorialSetting combinSetting) {
		SHDLModule module = combinSetting.getModule();
		int equationArity = 0;
		if (combinSetting.getOE() == null) {
			// check that all terms of the equation have the same arity
			for (int i = 0; i < combinSetting.getEquation().getTerms().size(); i++) {
				SHDLTerm term = (SHDLTerm) combinSetting.getEquation().getTerms().get(i);
				int termArity = 0;
				for (int j = 0; j < term.getSignalOccurences().size(); j++) {
					SHDLSignalOccurence so = (SHDLSignalOccurence) term.getSignalOccurences().get(j);
					// set 'read' attribute
					so.getSignal().setRead(true);
					int a = so.getSignal().getArity();
					//System.out.println("so=" + so + ", arity=" + a);
					if (termArity == 0) {
						termArity = a;
					} else if (termArity == 1) {
						termArity = a;
					} else if (termArity > 1) {
						if (a == 1) {
						} else if (a > 1) {
							if (a != termArity) {
								errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": term '" + term.getWrittenForm() + "' has factors of incompatible arities");
								return false;
							}
						} else {
							if (-a > termArity) {
								errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": term '" + term.getWrittenForm() + "' has factors of incompatible arities");
								return false;
							}
						}
					} else {
						if (a == 1) {
						} else if (a > 1) {
							if (a < -termArity) {
								errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": term '" + term.getWrittenForm() + "' has factors of incompatible arities");
								return false;
							}
							termArity = a;
						} else {
							termArity = Math.min(termArity, a);
						}
					}
				}
				// merge termArity and equationArity
				if (equationArity == 0)
					equationArity = termArity;
				else if (equationArity > 0) {
					if (termArity > 0) {
						if (equationArity != termArity) {
							errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": term '" + term.getWrittenForm() + "' has an arity incompatible with previous terms in the sum");
							return false;
						}
					} else {
						if (equationArity < -termArity) {
							errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": constant term '" + term.getWrittenForm() + "' has an arity higher than previous terms in the sum");
							return false;
						}
					}
				} else {
					if (termArity > 0) {
						if (-equationArity > termArity) {
							errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": term '" + term.getWrittenForm() + "' has an arity smaller than previous constants in the sum");
							return false;
						}
					} else {
						equationArity = Math.min(equationArity, termArity);
					}
				}
			}
		} else {
			// 3-state setting: the equation must be reduced to a single signal occurence
			if (combinSetting.getEquation().getTerms().size() != 1) {
				errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": an output enable can only be applied to a single signal, (plain or inverted)");
				return false;
			} else {
				SHDLTerm term = (SHDLTerm) combinSetting.getEquation().getTerms().get(0);
				if (term.getSignalOccurences().size() != 1) {
					errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": an output enable can only be applied to a single signal, (plain or inverted)");
					return false;
				}
				SHDLSignalOccurence so = (SHDLSignalOccurence) term.getSignalOccurences().get(0);
				SHDLSignal oeSignal = combinSetting.getOE().getSignal();
				// set 'read' attribute
				so.getSignal().setRead(true);
				oeSignal.setRead(true);
				// if the output enable signal is not a scalar,
				// check if its arity is compatible with the output
				equationArity = so.getSignal().getArity();
				int oeArity = oeSignal.getArity();
				if (equationArity > 0) {
					// the left part is not a constant
					if (oeArity > 1) {
						// oeSignal is not a constant nor a scalar
						// check that arities are equals on both sides
						if (oeArity != equationArity) {
							errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": in '" + combinSetting.getEquation().getWrittenForm() + ":" + combinSetting.getOE().getWrittenForm() + "', arities of both sides are not equal");
							return false;
						}
					} else if (oeArity < 0) {
						// oeSignal is a constant (e.g. 'a[7..0]=x[7..0]:0b1010101 ;')
						if (-oeArity > equationArity) {
							errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": in '" + combinSetting.getEquation().getWrittenForm() + ":" + combinSetting.getOE().getWrittenForm() + "', arities on both sides are not compatible");
							return false;
						}
					}
				} else {
					// the left part is a constant
					if (oeArity > 1) {
						// oeSignal is not a constant nor a scalar
						if (oeArity < -equationArity) {
							errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": in '" + combinSetting.getEquation().getWrittenForm() + ":" + combinSetting.getOE().getWrittenForm() + "', arities on both sides are not compatible");
							return false;
						}
					} else if (oeArity < 0) {
						// oeSignal is a constant
						if (-oeArity < -equationArity) {
							errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": in '" + combinSetting.getEquation().getWrittenForm() + ":" + combinSetting.getOE().getWrittenForm() + "', arities on both sides are not compatible");
							return false;
						}
					}
				}
			}
		}
		// check arity compatibility between both sides of the '='
		if (equationArity > 0) {
			if (equationArity != combinSetting.getSignal().getArity()) {
				errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": the signal '" + combinSetting.getSignal().getName() + "' and its equation '" + combinSetting.getEquation().getWrittenForm() + "' have incompatible arities");
				return false;
			}
		} else {
			if (-equationArity > combinSetting.getSignal().getArity()) {
				errorStream.println("** " + module.getFile().getName() + ":" + combinSetting.getLineNo() + ": the signal '" + combinSetting.getSignal().getName() + "' has an arity too small (= " + combinSetting.getSignal().getArity() + ") for the right-hand expression '" + combinSetting.getEquation().getWrittenForm() + "' (>= " + -equationArity + ")");
				return false;
			}
		}
		return ok;
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// check sequential setting
	// set also the 'read' and 'write' attributes for all included signals
	//
	boolean checkSeqSetting(boolean ok, SHDLSequentialSetting seqSetting) {
		SHDLModule module = seqSetting.getModule();
		SHDLSignal sig = seqSetting.getSignal();
		// set'read' attribute for sig1
		seqSetting.getSig1().getSignal().setRead(true);

		int commonArity = sig.getArity();
		int a1 = seqSetting.getSig1().getSignal().getArity();
		if (seqSetting.getSig2() == null) {
			// D-flipflops: check arities
			if (((a1 > 0) && (a1 != commonArity)) || ((a1 < 0) && (-a1 > commonArity))) {
				errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": signal '" + sig.getName() + "' and term '" + seqSetting.getSig1().getWrittenForm() + "' have incompatible arities");
				return false;
			}
			return true;
		}
		// JK or T flipflop; sig1/2/3/4 all not null
		// set'read' attribute for sig2/3/4
		seqSetting.getSig2().getSignal().setRead(true);
		seqSetting.getSig3().getSignal().setRead(true);
		seqSetting.getSig4().getSignal().setRead(true);
		// extract first signal occurence
		SHDLSignalOccurence first = null;
		if (sig.equals(seqSetting.getSig1().getSignal())) {
			first = seqSetting.getSig1();
			// check arities
			int a2 = seqSetting.getSig2().getSignal().getArity();
			if (((a2 > 0) && (a2 != commonArity)) || ((a2 < 0) && (-a2 > commonArity))) {
				errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": signal '" + sig.getName() + "' and term '" + seqSetting.getSig2().getWrittenForm() + "' have incompatible arities");
				return false;
			}
		} else if (sig.equals(seqSetting.getSig2().getSignal())) {
			first = seqSetting.getSig2();
			// check arities
			if (((a1 > 0) && (a1 != commonArity)) || ((a1 < 0) && (-a1 > commonArity))) {
				errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": signal '" + sig.getName() + "' and term '" + seqSetting.getSig1().getWrittenForm() + "' have incompatible arities");
				return false;
			}
		} else {
			errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": incorrect sequential setting");
			return false;
		}
		// check second term, and extract second signal occurence
		SHDLSignalOccurence second= null;
		if (sig.equals(seqSetting.getSig3().getSignal())) {
			second = seqSetting.getSig3();
			// check arities
			int a4 = seqSetting.getSig4().getSignal().getArity();
			if (((a4 > 0) && (a4 != commonArity)) || ((a4 < 0) && (-a4 > commonArity))) {
				errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": signal '" + sig.getName() + "' and term '" + seqSetting.getSig4().getWrittenForm() + "' have incompatible arities");
				return false;
			}
		} else if (sig.equals(seqSetting.getSig4().getSignal())) {
			second = seqSetting.getSig4();
			// check arities
			int a3 = seqSetting.getSig3().getSignal().getArity();
			if (((a3 > 0) && (a3 != commonArity)) || ((a3 < 0) && (-a3 > commonArity))) {
				errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": signal '" + sig.getName() + "' and term '" + seqSetting.getSig3().getWrittenForm() + "' have incompatible arities");
				return false;
			}
		} else {
			errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": incorrect sequential setting");
			return false;
		}
		// check if first and second have opposite polarity (e.g. q and /q)
		if ((first.isInverted() && second.isInverted()) || (!first.isInverted() && !second.isInverted())) {
			errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": incorrect sequential setting");
			return false;
		}
		return ok;
	}
	

	// Check that each sequential setting has at least a clock modifier, and that
	// each T and JK flip-flop has a reset modifier
	boolean checkSeqSettingComplete(boolean ok, SHDLSequentialSetting seqSetting) {
		SHDLModule module = seqSetting.getModule();
		if (seqSetting.getCLK() == null) {
			errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": a flip-flop must have an .clk assignement");
			ok = false;
		}
		if (!seqSetting.isDFlipFlop()) {
			// T or JK flip-flop
			if (seqSetting.getRST() == null) {
				errorStream.println("** " + module.getFile().getName() + ":" + seqSetting.getLineNo() + ": a T or JK flip-flop must have an .rst assignement");
				ok = false;
			}
		}
		return ok;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Check sequential modifier and attach it to its corresponding sequential setting
	// Set also 'read' attribute for modifier signals
	// Check that the modifier has an arity of 1
	//
	boolean checkSeqModifier(boolean ok, SHDLSequentialModifier seqModifier) {
		SHDLModule module = seqModifier.getModule();
		// check arity compatibility
		int signalArity = seqModifier.getSignal().getArity();
		SHDLSignal modSignal = seqModifier.getSignalOccurence().getSignal();
		// set 'read' attribute
		modSignal.setRead(true);
		int modArity = modSignal.getArity();
		if (modArity != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + seqModifier.getLineNo() + ": modifier '" + modSignal.getName() + "' is not a scalar signal");
			ok = false;
		}
		// look for the corresponding sequential setting
		boolean found = false;
		SHDLSequentialSetting seqSetting = null;
		for (int i = 0; i < module.getSequentialSettings().size(); i++) {
			seqSetting = (SHDLSequentialSetting) module.getSequentialSettings().get(i);
			if (!seqSetting.getSignal().equals(seqModifier.getSignal())) continue;
			found = true;
			break;
		}
		if (!found) {
			errorStream.println("** " + module.getFile().getName() + ":" + seqModifier.getLineNo() + ": there is no sequential assignement with a signal name '" + seqModifier.getSignal().getName() + "' to attach this modifier to");
			ok = false;
		} else {
			// attach modifier to seqSetting
			SHDLSequentialModifier pb = null;
			if (seqModifier.getModifier().equals("rst")) {
				if ((pb = seqSetting.getRST()) == null) {
					seqSetting.setRST(seqModifier);
				}
			} else if (seqModifier.getModifier().equals("set")) {
				if ((pb = seqSetting.getSET()) == null) {
					seqSetting.setSET(seqModifier);
				}
			} else if (seqModifier.getModifier().equals("clk")) {
				if ((pb = seqSetting.getCLK()) == null) {
					seqSetting.setCLK(seqModifier);
				}
			} else if (seqModifier.getModifier().equals("ena")) {
				if ((pb = seqSetting.getENA()) == null) {
					seqSetting.setENA(seqModifier);
				}
			}
			if (pb != null) {
				errorStream.println("** " + module.getFile().getName() + ":" + seqModifier.getLineNo() + ": a modifier '." + seqModifier.getModifier() + "' already exists for signal name '" + seqModifier.getSignal().getName() + "' at line " + pb.getLineNo());
				ok = false;
			}
		}

		return ok;
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////yy
	//
	// check module occurence
	// set also the 'moduleOccurenceSource' and 'moduleOccurenceArgumentIndex' attributes
	//
	boolean checkModuleOccurence(boolean ok, SHDLModuleOccurence moduleOccurence, SHDLModule module) {
		// look for the definition of the module the occurence is related to
		SHDLModule occModule = (SHDLModule) collectedModules.get(moduleOccurence.getName());
		// compare their argument's list arities
		if (occModule.getInterfaceSignals().size() != moduleOccurence.getArguments().size()) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": module instanciation " + moduleOccurence.getName() + " : incorrect number of arguments");
			ok = false;
		} else {
			for (int i = 0; i < moduleOccurence.getArguments().size(); i++) {
				SHDLSignal argSignal = (SHDLSignal) moduleOccurence.getArguments().get(i);
				SHDLSignal paramSignal = (SHDLSignal) occModule.getInterfaceSignals().get(i);
				if ((argSignal.getArity() > 0) && (argSignal.getArity() != paramSignal.getArity())) {
					errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": instanciation of '" + moduleOccurence.getName() + "' : argument #" + (i + 1) + " '" + argSignal.getName() + "' has not the same arity than the corresponding term in the module definition '" + paramSignal.getName() + "'");
					ok = false;
				} else if ((argSignal.getArity() < -1) && (paramSignal.getArity() == 1)) {
					errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": instanciation of '" + moduleOccurence.getName() + "' : argument #" + (i + 1) + " '" + argSignal.getName() + "' does not fit in the corresponding boolean term in the module definition '" + paramSignal.getName() + "'");
					ok = false;
				} else if ((argSignal.getArity() < 0) && (-argSignal.getArity() > paramSignal.getArity())) {
					errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": instanciation of '" + moduleOccurence.getName() + "' : argument #" + (i + 1) + " '" + argSignal.getName() + "' is a constant too large to fit in the corresponding term in the module definition '" + paramSignal.getName() + "'");
					ok = false;
				}
			}
		}
		return ok;
	}

	boolean checkRamsModuleOccurence(boolean ok, SHDLModuleOccurence moduleOccurence, SHDLModule module) {
		if (moduleOccurence.getArguments().size() != 6) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined SRAM module '" + moduleOccurence.getName() + "' : it does not have the 6 mandatory arguments <clock>, <write>, <enable>, <address>, <data_in>, <data_out>");
			return false;
		}
		SHDLSignal clkSignal = (SHDLSignal) moduleOccurence.getArguments().get(0);
		if (clkSignal.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined SRAM module '" + moduleOccurence.getName() + "' : first argument (clock) must be a scalar");
			ok = false;
		}
		SHDLSignal weSignal = (SHDLSignal) moduleOccurence.getArguments().get(1);
		if (weSignal.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined SRAM module '" + moduleOccurence.getName() + "' : second argument (write enable) must be a scalar");
			ok = false;
		}
		SHDLSignal enSignal = (SHDLSignal) moduleOccurence.getArguments().get(2);
		if (enSignal.getArity() != 1) {
			errorStream.println("** " + module.getFile().getName() + ":" + moduleOccurence.getBeginLine() + ": predefined SRAM module '" + moduleOccurence.getName() + "' : third argument (enable) must be a scalar");
			ok = false;
		}
		SHDLSignal addrSignal = (SHDLSignal) moduleOccurence.getArguments().get(3);


		SHDLSignal diSignal = (SHDLSignal) moduleOccurence.getArguments().get(4);
		SHDLSignal doSignal = (SHDLSignal) moduleOccurence.getArguments().get(5);

		return ok;
	}

	//
	// look for source conflicts
	//
	boolean checkSourcesConflict(boolean ok, String signalName, ArrayList sources, SHDLModule module) {
		ArrayList combinatorialNTSources = new ArrayList();
		ArrayList combinatorialTSources = new ArrayList();
		ArrayList sequentialSources = new ArrayList();
		ArrayList predefinedSources = new ArrayList();
		for (int i = 0; i < sources.size(); i++) {
			Object source = sources.get(i);
			if (source instanceof SHDLCombinatorialSetting) {
				SHDLCombinatorialSetting cs = (SHDLCombinatorialSetting) source;
				if (cs.getOE() == null)
					combinatorialNTSources.add(source);
				else
					combinatorialTSources.add(source);
			} else if (source instanceof SHDLSequentialSetting) {
				sequentialSources.add(source);
			} else if (source instanceof SHDLModuleOccurence) {
				// predefined module
				predefinedSources.add(source);
			}
		}
		if (combinatorialNTSources.size() + sequentialSources.size() + predefinedSources.size() > 1) {
			ArrayList allSources = (ArrayList) combinatorialNTSources.clone();
			allSources.addAll(sequentialSources);
			errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal '" + signalName + "' is assigned several times (lines " + linesOfSources(sources, module) + ")");
			ok = false;
		}
		if ((combinatorialTSources.size() > 0) && (combinatorialNTSources.size() + sequentialSources.size() + predefinedSources.size() > 0)) {
			ArrayList allSources = (ArrayList) combinatorialTSources.clone();
			allSources.addAll(combinatorialNTSources);
			allSources.addAll(sequentialSources);
			errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + "-" + module.getEndLine() + " : signal '" + signalName + "' is assigned in a mix of direct and tristate assignements (lines " + linesOfSources(allSources, module) + ")");
			ok = false;
		}
		return ok;
	}

	// return a list of lines where appear <sources>. When they belong to another module
	// than <currentModule>, prefix the number with the module's name
	String linesOfSources(ArrayList sources, SHDLModule currentModule) {
		String res = "";
		for (int i = 0; i < sources.size(); i++) {
			if ((i > 0) && (i == sources.size() - 1))
				res += " and ";
			else if (i > 0)
				res += ", ";
			Object source = sources.get(i);
			if (source instanceof SHDLCombinatorialSetting) {
				SHDLCombinatorialSetting combinSetting = (SHDLCombinatorialSetting) source;
				if (!combinSetting.getModule().equals(currentModule))
					res += combinSetting.getModule().getName() + ":";
				res += combinSetting.getLineNo();
			} else if (source instanceof SHDLSequentialSetting) {
				SHDLSequentialSetting seqSetting = (SHDLSequentialSetting) source;
				if (!seqSetting.getModule().equals(currentModule))
					res += seqSetting.getModule().getName() + ":";
				res += seqSetting.getLineNo();
			} else if (source instanceof SHDLModuleOccurence) {
				// predefined module occurence
				SHDLModuleOccurence modOcc = (SHDLModuleOccurence) source;
				if (!modOcc.getModule().equals(currentModule))
					res += modOcc.getModule().getName() + ":";
				res += modOcc.getBeginLine();
			}
		}
		return res;
	}
	
	// return the first found module referenced by no other
	SHDLModule getTopModule() {
		HashMap occurenceOfMap = new HashMap();
		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
//			if (module.isEmpty()) continue; // mainly for empty main
//			if (module.isMain()) return module;
			
			ArrayList occurences = module.getModuleOccurences();
			for (int i = 0; i < occurences.size(); i++) {
				SHDLModuleOccurence occurence = (SHDLModuleOccurence) occurences.get(i);
				ArrayList list = (ArrayList) occurenceOfMap.get(occurence.getName());
				if (list == null) list = new ArrayList();
				list.add(module);
				occurenceOfMap.put(occurence.getName(), list);
			}
		}
		// renvoie le premier module qui a un champ occurenceOf vide
		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			if (occurenceOfMap.get(module.getName()) == null) return module;
		}
		return null;
	}
	
	
	boolean generateCommShdModule(SHDLModule module, File vhdlDir) {
		String modName = module.getName();
		File outFile = new File(vhdlDir, modName + "_comm.shd");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(outFile));
		} catch(IOException ex) {
			errorStream.println("** impossible to open '" + outFile + "' for writing");
		}
		
		pw.println("module Nexys(mclk, pdb[7..0], astb, dstb, pwr, pwait)");
		pw.println();
		pw.println("    commUSB(mclk, pdb[7..0], astb, dstb, pwr, pwait, pc2board[63..0], board2pc[63..0]) ;");
		pw.println();
		
		// combinatorial statements
		int inIdx = 0;
		int outIdx = 0;
		for (int j = 0; j < module.getInterfaceSignals().size(); j++) {
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(j);
			String prefix = signal.getPrefix();
			int dir = module.getIndexDir(prefix);
			int[] stat = module.getIOStat(signal);
			String sigName = module.getVHDLName(signal, 0, false, true);
			String ioStatus = module.ioStatus(stat);
			if (ioStatus.equals("buffer")) ioStatus = "out";
			if (ioStatus.equals("unused")) ioStatus = "in";
			
			if (ioStatus.equals("in")) {
				if (dir <= 0) {
					pw.println("    " + sigName + " = pc2board[" + inIdx + "] ;");
					inIdx += 1;
				} else if (dir == 1) {
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    " + prefix + "[" + signal.getN1() + ".." + signal.getN2() + "] = pc2board[" + (inIdx + high - low) + ".." + inIdx + "] ;");
					inIdx += high - low + 1;
				} else {			
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    " + prefix + "[" + signal.getN1() + ".." + signal.getN2() + "] = pc2board[" + inIdx + ".." + (inIdx + high - low) + "] ;");
					inIdx += high - low + 1;
				}
			} else if (ioStatus.equals("out") || ioStatus.equals("inout")) {
				if (dir <= 0) {
					pw.println("    board2pc[" + outIdx + "] = " + sigName + " ;");
					outIdx += 1;
				} else if (dir == 1) {
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    board2pc[" + (outIdx + high - low) + ".." + outIdx + "] = " + prefix + "[" + signal.getN1() + ".." + signal.getN2() + "] ;");
					outIdx += high - low + 1;
				} else {			
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    board2pc[" + outIdx + ".." + (outIdx + high - low) + "] = " + prefix + "[" + signal.getN1() + ".." + signal.getN2() + "] ;");
					outIdx += high - low + 1;
				}
			}
		}
		pw.println();
		
		// module instanciation
		pw.print("    " + modName + "(");
		for (int j = 0; j < module.getInterfaceSignals().size(); j++) {
			if (j > 0) pw.print(", ");
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(j);
			String prefix = signal.getPrefix();
			int dir = module.getIndexDir(prefix);
			if (dir <= 0)
				pw.print(prefix);
			else
				pw.print(prefix + "[" + signal.getN1() + ".." + signal.getN2() + "]");
		}
		pw.println(") ;");
		pw.println();

		pw.println("end module");

		// flushing and closing
		pw.flush();
		pw.close();
		
		return ((inIdx >= 128) || (outIdx >= 128));
	}
	
	
	boolean generateDistantIOModule(SHDLModule module, SHDLBoard board, File vhdlDir) {
		String modName = module.getName();
		File outFile = new File(vhdlDir, modName + "_comm.vhd");
		PrintWriter pw = null;
		List boardPrefixes = Arrays.asList(board.getBoardPrefixes());
		try {
			pw = new PrintWriter(new FileOutputStream(outFile));
		} catch(IOException ex) {
			errorStream.println("** impossible to open '" + outFile + "' for writing");
		}
		
		pw.println("library IEEE;");
		pw.println("use IEEE.STD_LOGIC_1164.ALL;");
		pw.println("use IEEE.STD_LOGIC_ARITH.ALL;");
		pw.println("use IEEE.STD_LOGIC_UNSIGNED.ALL;");
		pw.println();
		pw.println("entity " + modName + "_comm is");
		pw.println("    port (");
		
		// add signals necessary for commUSB
		pw.println("        mclk : in std_logic ;");
		pw.println("        pdb : inout std_logic_vector (7 downto 0) ;");
		pw.println("        astb : in std_logic ;");
		pw.println("        dstb : in std_logic ;");
		pw.println("        pwr : in std_logic ;");
		pw.print("        pwait : out std_logic");
		
		// add other board signals
		ArrayList others = new ArrayList();
		for (int j = 0; j < module.getInterfaceSignals().size(); j++) {
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(j);
			String prefix = signal.getPrefix();
			if (prefix.equals("mclk") || prefix.equals("pdb") || prefix.equals("astb") || prefix.equals("dstb") || prefix.equals("pwr") || prefix.equals("pwr")) continue;
			if (!boardPrefixes.contains(prefix)) continue;
			others.add(signal);
		}
		pw.println((others.size() > 0) ? ";" : ""); // termine la ligne pwait
		for (int j = 0; j < others.size(); j++) {
			SHDLSignal signal = (SHDLSignal) others.get(j);
			int[] stat = module.getIOStat(signal);
			String ioStatus = module.ioStatus(stat);
			pw.println(module.getVHDLSignalDeclaration(signal, ioStatus, "        ", (j == others.size() - 1)));
		}
		
		pw.println(") ;");
		pw.println("end " + modName + "_comm ;");
		pw.println();
		pw.println("architecture synthesis of " + modName + "_comm is");
		pw.println();
		pw.println("    -- submodules declarations");
		pw.println("    component commUSB");
		pw.println("        port (");
		pw.println("            mclk     : in std_logic;");
		pw.println("            pdb      : inout std_logic_vector(7 downto 0);");
		pw.println("            astb     : in std_logic;");
		pw.println("            dstb     : in std_logic;");
		pw.println("            pwr      : in std_logic;");
		pw.println("            pwait    : out std_logic;");
		pw.println("            pc2board : out std_logic_vector(127 downto 0);");
		pw.println("            board2pc : in std_logic_vector(127 downto 0)");
		pw.println("        ) ;");
		pw.println("    end component ;");

		// component declaration
		pw.println(module.getVHDLComponentDeclaration());
		pw.println();
		pw.println("    -- internal signals declarations");
		pw.println("    signal pc2board : std_logic_vector (127 downto 0) ;");
		pw.println("    signal board2pc : std_logic_vector (127 downto 0) ;");
		for (int j = 0; j < module.getInterfaceSignals().size(); j++) {
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(j);
			// on inclut pas les signaux de la carte
			if (boardPrefixes.contains(signal.getPrefix())) continue;
			pw.println(module.getVHDLIntSignalDeclaration(signal, "    "));
		}
		pw.println();
		
		pw.println("begin");
		pw.println();
		
		// combinatorial statements
		pw.println("    -- combinatorial statements");
		int inIdx = 0;
		int outIdx = 0;
		for (int j = 0; j < module.getInterfaceSignals().size(); j++) {
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(j);
			String prefix = signal.getPrefix();
			// on inclut pas les signaux de la carte
			if (boardPrefixes.contains(prefix)) continue;
			int dir = module.getIndexDir(prefix);
			int[] stat = module.getIOStat(signal);
			String sigName = module.getVHDLName(signal, 0, false, true);
			String ioStatus = module.ioStatus(stat);
			if (ioStatus.equals("buffer")) ioStatus = "out";
			if (ioStatus.equals("unused")) ioStatus = "in";
			
			if (ioStatus.equals("in")) {
				if (dir <= 0) {
					pw.println("    " + sigName + " <= pc2board(" + inIdx + ") ;");
					inIdx += 1;
				} else if (dir == 1) {
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    " + sigName + " <= pc2board(" + (inIdx + high - low) + " downto " + inIdx + ") ;");
					inIdx += high - low + 1;
				} else {			
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    " + sigName + " <= pc2board(" + inIdx + " downto " + (inIdx + high - low) + ") ;");
					inIdx += high - low + 1;
				}
			} else if (ioStatus.equals("out") || ioStatus.equals("inout")) {
				if (dir <= 0) {
					pw.println("    board2pc(" + outIdx + ") <= " + sigName + " ;");
					outIdx += 1;
				} else if (dir == 1) {
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    board2pc(" + (outIdx + high - low) + " downto " + outIdx + ") <= " + sigName + " ;");
					outIdx += high - low + 1;
				} else {			
					int high = signal.getHighestIndex();
					int low = signal.getLowestIndex();
					pw.println("    board2pc(" + outIdx + " upto " + (outIdx + high - low) + ") <= " + sigName + " ;");
					outIdx += high - low + 1;
				}
			}
		}
//		pw.println("    ld(7 downto 0) <= \"00000000\" ;");
//		pw.println("    an(3 downto 0) <= \"1111\" ;");
//		pw.println("    ssg(7 downto 0) <= \"11111111\" ;");
//		pw.println("    txda <= '1' ;");

		pw.println();
		
		// components instanciation
		pw.println("    -- components instanciations");
		pw.println("    commUSB_0 : commUSB port map (mclk, pdb(7 downto 0), astb, dstb, pwr, pwait, pc2board(127 downto 0), board2pc(127 downto 0)) ;");
		pw.print("    " + modName + "_0 : " + modName + " port map (");
		for (int j = 0; j < module.getInterfaceSignals().size(); j++) {
			if (j > 0) pw.print(", ");
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(j);
			if (signal.isPartOfBus()) {
				pw.print(module.getVHDLName(signal, 0, false, false));
			} else {
				pw.print(module.getVHDLName(signal, 0, false, true));
			}
		}
		pw.println(") ;");
		pw.println();
		pw.println();

		pw.println("end synthesis;");

		// flushing and closing
		pw.flush();
		pw.close();		
		
		return ((inIdx >= 128) || (outIdx >= 128));
	}
	
	
	void generateCommIni(SHDLModule module, SHDLBoard board, File vhdlDir) {
		String modName = module.getName();
		File outFile = new File(vhdlDir, "comm.ini");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(outFile));
		} catch(IOException ex) {
			errorStream.println("** impossible to open '" + outFile + "' for writing");
		}
		
		pw.println("100,40,600,400");
		int inIdx = 0;
		int outIdx = 0;
		int x = 20;
		int y = 20;
		List boardPrefixes = Arrays.asList(board.getBoardPrefixes());
		for (int j = 0; j < module.getInterfaceSignals().size(); j++) {
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(j);
			String prefix = signal.getPrefix();
			// on n'inclue pas les signaux de la carte
			if (boardPrefixes.contains(prefix)) continue;
			int dir = module.getIndexDir(prefix);
			int[] stat = module.getIOStat(signal);
			String ioStatus = module.ioStatus(stat);
			if (ioStatus.equals("buffer")) ioStatus = "out";
			if (ioStatus.equals("unused")) ioStatus = "in";
			
			int high = signal.getHighestIndex();
			int low = signal.getLowestIndex();
			int w = Math.max(100, 30*(high-low+1));
			if (ioStatus.equals("in")) {
				if (dir <= 0) {
					pw.println(prefix + ": out[" + inIdx + ".." + inIdx + "] (" + x + "," + y + "," + w + ",70)");
					inIdx += 1;
				} else if (dir == 1) {
					pw.println(prefix + ": out[" + (inIdx + high - low) + ".." + inIdx + "] (" + x + "," + y + "," + w + ",70)");
					inIdx += high - low + 1;
				} else {			
					pw.println(prefix + ": out[" + inIdx + ".." + (inIdx + high - low) + "] (" + x + "," + y + "," + w + ",70)");
					inIdx += high - low + 1;
				}
			} else if (ioStatus.equals("out") || ioStatus.equals("inout")) {
				if (dir <= 0) {
					pw.println(prefix + ": in[" + outIdx + ".." + outIdx + "] (" + x + "," + y + "," + w + ",70)");
					outIdx += 1;
				} else if (dir == 1) {
					pw.println(prefix + ": in[" + (outIdx + high - low) + ".." + outIdx + "] (" + x + "," + y + "," + w + ",70)");
					outIdx += high - low + 1;
				} else {			
					pw.println(prefix + ": in[" + outIdx + ".." + (outIdx + high - low) + "] (" + x + "," + y + "," + w + ",70)");
					outIdx += high - low + 1;
				}
			}
			x += 50;
			y += 30;
		}

		// flushing and closing
		pw.flush();
		pw.close();
	}

	// return the list of all module names
	public ArrayList getListModuleNames() {
		ArrayList list = new ArrayList();
		ArrayList namesDefined = new ArrayList();
		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			if (module.isEmpty()) continue; // mainly for empty main

			String modName = module.getName();
			if (module.isMain()) modName = "main";
			list.add(modName);
			// look for associated predefined modules (mult, ram, etc.)
			for (int i = 0; i < module.getModuleOccurences().size(); i++) {
				SHDLModuleOccurence moduleOccurence = (SHDLModuleOccurence) module.getModuleOccurences().get(i);
				if (!moduleOccurence.isPredefined()) continue;
				SHDLPredefinedOccurence po = moduleOccurence.getPredefined();
				if (po.isInLibrary()) continue;
				if (namesDefined.contains(po.getModuleOccurence().getName().toLowerCase())) continue;
				namesDefined.add(po.getModuleOccurence().getName().toLowerCase());
				modName = po.getModuleOccurence().getName();
				list.add(modName);
			}
		}
		return list;
	}
	
	///////////////////////////////      BOARD I/O        ///////////////////////////////////

	// board only or hybrid designs
	// make additionnal checks and add missing statements to associated module
	public boolean check(SHDLBoard board, boolean ok, PrintStream errorStream) {
		boolean ok_ = true;
		SHDLModule module = getTopModule() ;
		String[] boardPrefixes = board.getBoardPrefixes();
		int[] boardN1 = board.getBoardN1();
		int[] boardN2 = board.getBoardN2();
		String[] boardIOStatus = board.getBoardIOStatus();
		ArrayList interfacePrefixes = new ArrayList();
		for (int i = 0; i < module.getInterfaceSignals().size(); i++) {
			SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(i);
			String prefix = signal.getPrefix();
			if (!interfacePrefixes.contains(prefix)) interfacePrefixes.add(prefix);
			// check interface signal name
			int pi = board.prefixIndex(prefix, boardPrefixes);
			if (pi == -1) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : " + module.getName() + " interface signal '" + prefix + "' is not allowed in a '" + board.getBoardModuleName() + "' module. Possible signal names are: " + board.strPrefixes());
				ok_ = false;
				continue;
			}
			// check indexes range
			int n1 = boardN1[pi];
			int n2 = boardN2[pi];
			if (signal.isPartOfBus()) {
			       if (n1 == n2) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : " + module.getName() + " interface signal '" + prefix + "' must be a scalar");
					ok_ = false;
					continue;
			       } else if ((signal.getN1() != n1) || (signal.getN2() != n2)) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : " + module.getName() + " interface signal '" + prefix + "' does not have the expected index range : " + n1 + ".." + n2);
					ok_ = false;
					continue;
				}
			} else if (n1 != n2) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : " + module.getName() + " interface signal '" + prefix + "' must be a bus signal " + n1 + ".." + n2);
				ok_ = false;
				continue;
			}

			// check IO status
			if (signal.isPartOfBus()) {
				for (int j = signal.getLowestIndex(); j <= signal.getHighestIndex(); j++) {
					int[] stat = module.getIOStatBit(signal.getPrefix() + "[" + j + "]");
					String status = module.ioStatus(stat);
					//System.out.println("signal=" + signal + ", nbsource=" + stat[0] + ", nbtsources=" + stat[1] + ", isRead=" + stat[2] + ", status=" + status + ", boardIOStatus[pi]=" + boardIOStatus[pi]);
					if (status.equals("unused")) continue;
					if (status.equals("buffer")) continue; // pour des cas tordus, pas sûr tres correct
					if (status.equals("in") && boardIOStatus[pi].equals("inout")) continue;
					if (!status.equals(boardIOStatus[pi])) {
						errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : " + module.getName() + " interface signal '" + prefix + "[" + j + "]' does not have the expected '" + boardIOStatus[pi] + "' input/output status.");
						ok_ = false;
						continue;
					}
				}
			} else {
				int[] stat = module.getIOStat(signal);
				String status = module.ioStatus(stat);
				if (status.equals("unused")) continue;
				if (status.equals("buffer")) continue; // pour des cas tordus, pas sûr très correct
				if (!status.equals(boardIOStatus[pi])) {
					errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : " + module.getName() + " interface signal '" + signal.getName() + "' does not have the expected '" + boardIOStatus[pi] + "' input/output status.");
					ok_ = false;
					continue;
				}
			}
		}
		// check that there are no module signals with prefixes among boardPrefixes which are not in the interface
		ArrayList alreadyChecked = new ArrayList();
		for (int i = 0; i < module.getModuleSignals().size(); i++) {
			SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
			if (signal.isConstant()) continue;
			String prefix = signal.getPrefix();
			if (interfacePrefixes.contains(prefix)) continue;
			if (alreadyChecked.contains(prefix)) continue;
			alreadyChecked.add(prefix);
			int pi = board.prefixIndex(prefix, boardPrefixes);
			if (pi != -1) {
				errorStream.println("** " + module.getFile().getName() + ":" + module.getBeginLine() + " : " + module.getName() + " signal '" + prefix + "' has a predefined meaning in module " + board.getBoardModuleName() + ", and must be declared in the interface");
				ok_ = false;
				continue;
			}
		}
		if (!ok_) return false;

		// now all interface signals which are among boardPrefixes have the right IO status and arities
		// some of them may be completely or partly unused -> use them in dummy assignations
		// they may not be all present -> add them, and use them in dummy assignations
		
//		// add missing signals of boardPrefixes in interface
//		for (int i = 0; i < boardPrefixes.length; i++) {
//			String boardPrefix = boardPrefixes[i];
//			if (!module.prefixExist(boardPrefix)) {
//				//System.out.println(boardPrefix + " not present -> add it in interface");
//				SHDLSignal addedSignal = new SHDLSignal(boardPrefix, true, module);
//				if (boardN1[i] != boardN2[i]) {
//					addedSignal.setN1("" + boardN1[i]);
//					addedSignal.setN2("" + boardN2[i]);
//				}
//				module.addInterfaceSignal(addedSignal);
//			}
//		}

		// for completely or partly unused signals, use them in dummy assignations
		for (int i = 0; i < boardPrefixes.length; i++) {
			String boardPrefix = boardPrefixes[i];
			if (boardN1[i] == boardN2[i]) {
				// scalar signal
				int[] stat = module.getIOStatBit(boardPrefix);
				if (stat[0] + stat[1] + stat[2] == 0) {
					if (boardIOStatus[i].equals("out")) {
						//System.out.println("out scalar " + boardPrefix + " unused");
						String d = board.getBoardDefaultValues()[i];
						SHDLSignal signal = module.lookForSignal(boardPrefix);
						// assign it to default value
						createAssignation(board, signal, board.getBoardDefaultValues()[i]);
						// clear IOstat
						module.clearIOStat(signal);
						int[] stats = module.getIOStat(signal);
					} else {
						//System.out.println("in or inout scalar " + boardPrefix + " unused");
						// do nothing
					}
				}
			} else {
				// bus signal; check all its bits
				SHDLSignal signal = module.lookForSignal(boardPrefix + "[" + boardN1[i] + ".." + boardN2[i] + "]");
				int[] stat = module.getIOStat(signal);
				if (stat[0] + stat[1] + stat[2] == 0) {
					// bus signal completely unused
					if (boardIOStatus[i].equals("out")) {
						// assign it to default values
						createAssignation(board, signal, "0b" + board.getBoardDefaultValues()[i]);
						// clear IOstat
						module.clearIOStat(signal);
					} else {
						// do nothing
					}
				} else {
					// bus signal partly unused -> 
					for (int j = signal.getLowestIndex(); j <= signal.getHighestIndex(); j++) {
						stat = module.getIOStatBit(signal.getPrefix() + "[" + j + "]");
						if (stat[0] + stat[1] + stat[2] == 0) {
							if (boardIOStatus[i].equals("out")) {
								// create signal since it cannot exist
								SHDLSignal sig = new SHDLSignal(boardPrefix, j, j, module);
								// assign it to default value
								char d = board.getBoardDefaultValues()[i].charAt(j - signal.getLowestIndex());
								createAssignation(board, sig, new String(new char[] { d }));
								// clear IOstat
								module.clearIOStat(sig);
								int[] stats = module.getIOStat(sig);
							} else {
								// do nothing
							}
						}
					}
				}
			}
		}
		return (ok_ & ok);
	}

	protected void createAssignation(SHDLBoard board, SHDLSignal signal, String value) {
		SHDLModule module = getTopModule() ;
		SHDLCombinatorialSetting cs = new SHDLCombinatorialSetting(0, module);
		SHDLTerm t = new SHDLTerm(module);
		t.addSignalOccurence(new SHDLSignalOccurence(new SHDLSignal(value, module), false, module));
		SHDLTermsSum ts = new SHDLTermsSum(module);
		ts.addTerm(t);
		cs.setSignal(signal);
		cs.setEquation(ts);
		module.addCombinSetting(cs);
		if (signal.isPartOfBus()) {
			for (int j = signal.getLowestIndex(); j <= signal.getHighestIndex(); j++) {
				module.addSignalSource(signal.getPrefix() + j, cs);
			}
		} else {
			module.addSignalSource(signal.getNormalizedName(), cs);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// generate VHDL code in <destdir> directory for all used modules
	// 
	/////////////////////////////////////////////////////////////////////////////////////////////////
	void generateVHDL(SHDLBoard board, File destdir) {
		SHDLModule topModule = getTopModule();
		// create a VHDL entity for each used module
		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
//			if (module.isEmpty()) continue; // mainly for empty main

			String modName = module.getName();
			if (module.isMain()) modName = "main";
			File outFile = new File(destdir, modName + ".vhd");
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream(outFile));
			} catch(IOException ex) {
				errorStream.println("** impossible to open '" + outFile + "' for writing");
			}
			pw.println("library IEEE;");
			pw.println("library UNISIM;");
			pw.println("use IEEE.STD_LOGIC_1164.ALL;");
			pw.println("use IEEE.STD_LOGIC_ARITH.ALL;");
			pw.println("use IEEE.STD_LOGIC_UNSIGNED.ALL;");
			pw.println();

			// collect all signal prefixes
			ArrayList prefixes = new ArrayList();
			for (int i = 0; i < module.getModuleSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getModuleSignals().get(i);
				if (signal.isConstant()) continue;
				String prefix = signal.getPrefix();
				if (prefixes.contains(prefix)) continue;
				prefixes.add(prefix);
			}
			// collect all signals to declare
			ArrayList toDeclare = new ArrayList();
			for (int i = 0; i < prefixes.size(); i++) {
				String prefix = (String) prefixes.get(i);
				ArrayList sigs = module.getPrefixSignals(prefix);
				for (int j = 0; j < sigs.size(); j++) {
					SHDLSignal sig = (SHDLSignal) sigs.get(j);
					if (toDeclare.contains(sig)) continue;
					toDeclare.add(sig);
				}
			}

			// entity declarations
			String moduleName = module.getName();
//			if (moduleName.equals("0main")) moduleName = "main";
			ArrayList bufferSignals = new ArrayList(); // list of internal buffer signals for r/w IO signals
			pw.println("entity " + moduleName + " is");
			pw.println("\tport (");
			for (int i = 0; i < module.getInterfaceSignals().size(); i++) {
				SHDLSignal signal = (SHDLSignal) module.getInterfaceSignals().get(i);
				String prefix = signal.getPrefix();
				int[] stat = module.getIOStat(signal);
				String ioStatus = module.ioStatus(stat);
				// for top module, prefer the board io status for board signals
				if (moduleName.equals(topModule.getName())) {
					for (int j = 0; j < board.getBoardPrefixes().length; j++) {
						if (board.getBoardPrefixes()[j].equals(prefix)) {
							ioStatus = board.getBoardIOStatus()[j];
							break;
						}
					}
				}
				if (ioStatus.equals("buffer") && !bufferSignals.contains(prefix)) bufferSignals.add(prefix);
				boolean isLast = (i == module.getInterfaceSignals().size() - 1);
				pw.print(module.getVHDLSignalDeclaration(signal, ioStatus, "\t\t", isLast));
				toDeclare.remove(signal);
			}
			pw.println("\t);");
			pw.println("end " + moduleName + ";");
			pw.println();

			// architecture section
			pw.println("architecture synthesis of " + moduleName + " is");

			// components declarations
			if (module.getModuleOccurences().size() > 0) pw.println(newline + "\t-- submodules declarations");
			ArrayList modulesDeclared = new ArrayList();
			for (int i = 0; i < module.getModuleOccurences().size(); i++) {
				SHDLModuleOccurence modOcc = (SHDLModuleOccurence) module.getModuleOccurences().get(i);
				if (modulesDeclared.contains(modOcc.getName())) continue; else modulesDeclared.add(modOcc.getName());
				SHDLPredefinedOccurence po = modOcc.getPredefined();
				if (po != null) {
					pw.println(po.getVHDLComponentDeclaration());
				} else {
					SHDLModule mod = (SHDLModule) collectedModules.get(modOcc.getName());
					pw.println(mod.getVHDLComponentDeclaration());
				}
			}

			// declare buffer signals for interface signals converted from 'buffer' to 'out'
			if (bufferSignals.size() > 0) pw.println(newline + "\t-- buffer signals declarations");
			for (int i = 0; i < bufferSignals.size(); i++) {
				String prefix = (String) bufferSignals.get(i);
				String bufferPrefix = prefix + "_int";
				while (module.prefixExist(bufferPrefix)) {
					bufferPrefix += "_int";
				}
				module.substitutePrefixName(prefix, bufferPrefix);
				pw.print(module.getVHDLIntPrefixDeclaration(prefix, "\t", true));
			}
			// internal signals declarations : all other toDeclare signals
			if (toDeclare.size() > 0) pw.println(newline + "\t-- internal signals declarations");
			for (int i = 0; i < toDeclare.size(); i++) {
				SHDLSignal signal = (SHDLSignal) toDeclare.get(i);
				pw.println(module.getVHDLIntSignalDeclaration(signal, "\t"));
			}
			pw.println();

			pw.println("begin");

			// buffer signals assignations
			if (bufferSignals.size() > 0) pw.println(newline + "\t-- buffer signals assignations");
			for (int i = 0; i < bufferSignals.size(); i++) {
				String prefix = (String) bufferSignals.get(i);
				pw.print(module.getVHDLBufferAssignation(prefix, "\t"));
			}

			// combinatorial settings
			if (module.getCombinSettings().size() > 0) pw.println(newline + "\t-- concurrent statements");
			for (int i = 0; i < module.getCombinSettings().size(); i++) {
				SHDLCombinatorialSetting combinSetting = (SHDLCombinatorialSetting) module.getCombinSettings().get(i);
				pw.println(module.getVHDLText(combinSetting));
			}
			
			// sequential settings
			if (module.getSequentialSettings().size() > 0) pw.println(newline + "\t-- sequential statements");
			for (int i = 0; i < module.getSequentialSettings().size(); i++) {
				SHDLSequentialSetting seqSetting = (SHDLSequentialSetting) module.getSequentialSettings().get(i);
				pw.println(module.getVHDLText(seqSetting));
			}

			// components instanciations
			if (module.getModuleOccurences().size() > 0) pw.println(newline + "\t-- components instanciations");
			for (int i = 0; i < module.getModuleOccurences().size(); i++) {
				SHDLModuleOccurence modOcc = (SHDLModuleOccurence) module.getModuleOccurences().get(i);
				pw.print("\t" + modOcc.getName() + "_" + i + " : " + modOcc.getName() + " port map (");
				SHDLPredefinedOccurence po = modOcc.getPredefined();
				ArrayList arguments = modOcc.getArguments();
				for (int j = 0; j < arguments.size(); j++) {
					SHDLSignal sig = (SHDLSignal) arguments.get(j);
					if (sig.isConstant()) {
						if (po != null) {
							pw.print(module.getVHDLName(sig, po.getArity(j), true, true));
						} else {
							SHDLModule submod = (SHDLModule) collectedModules.get(modOcc.getName());
							SHDLSignal interfSig = (SHDLSignal) submod.getInterfaceSignals().get(j);
							pw.print(module.getVHDLName(sig, interfSig.getArity(), true, true));
						}
					} else
						pw.print(module.getVHDLName(sig, 0, true, true));
					if (j < arguments.size() - 1) pw.print(", ");
				}
				pw.println(") ;");
			}

			pw.println(newline + "end synthesis;");

			// flushing and closing
			pw.flush();
			pw.close();
		}
		// create VHDL modules for predefined occurences
		ArrayList namesDefined = new ArrayList();
		for (Iterator it = collectedModules.values().iterator() ; it.hasNext(); ) {
			SHDLModule module = (SHDLModule) it.next();
			for (int i = 0; i < module.getModuleOccurences().size(); i++) {
				SHDLModuleOccurence moduleOccurence = (SHDLModuleOccurence) module.getModuleOccurences().get(i);
				if (!moduleOccurence.isPredefined()) continue;
				SHDLPredefinedOccurence po = moduleOccurence.getPredefined();
				if (po.isInLibrary()) continue;
				if (namesDefined.contains(po.getModuleOccurence().getName().toLowerCase())) continue;
				namesDefined.add(po.getModuleOccurence().getName().toLowerCase());
				String modName = po.getModuleOccurence().getName();
				File outFile = new File(destdir, modName + ".vhd");
				PrintWriter pw = null;
				try {
					pw = new PrintWriter(new FileOutputStream(outFile));
				} catch(IOException ex) {
					errorStream.println("** impossible to open '" + outFile + "' for writing");
				}
				// contents
				pw.print(po.getVHDLDefinition());
				// flush and close
				pw.flush();
				pw.close();
			}
		}
	}

}

