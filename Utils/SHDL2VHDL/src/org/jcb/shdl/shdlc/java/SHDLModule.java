
package org.jcb.shdl.shdlc.java;

import org.jcb.tools.*;
import java.util.*;
import java.io.*;

public class SHDLModule {

	private String name;
	private int beginLine;
	private int endLine;
	private File file;
	private ArrayList interfaceSignals; // signals in interface
	private ArrayList moduleSignals; // signals in the module. Includes interfaceSignals
	private ArrayList combinSettings;
	private ArrayList seqSettings;
	private ArrayList seqModifiers;
	private ArrayList moduleOccurences;
	
	protected static final String newline = System.getProperty("line.separator");

	public SHDLModule() {
		interfaceSignals = new ArrayList();
		moduleSignals = new ArrayList();
		combinSettings = new ArrayList();
		seqSettings = new ArrayList();
		seqModifiers = new ArrayList();
		moduleOccurences = new ArrayList();
	}
	public String toString() {
		//return (beginLine + "-" + endLine + ": name=" + name + ", interfaceSignals=" + interfaceSignals + ", moduleSignals=" + moduleSignals + ", combinSettings=" + combinSettings + ", seqSettings=" + seqSettings + ", seqModifiers=" + seqModifiers + ", moduleOccurences=" + moduleOccurences);
		return name;
	}
	public boolean equals(Object obj) {
		SHDLModule mod = (SHDLModule) obj;
		return this.getName().equals(mod.getName());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public boolean isMain() {
		if (name == null) return true;
		if (name.equals("0main")) return true;
		return false;
	}
	public boolean isEmpty() {
		if (interfaceSignals.size() > 0) return false;
		if (combinSettings.size() > 0) return false;
		if (seqSettings.size() > 0) return false;
		if (seqModifiers.size() > 0) return false;
		if (moduleOccurences.size() > 0) return false;
		return true;
	}
	public int getBeginLine() {
		return beginLine;
	}
	public void setBeginLine(int beginLine) {
		this.beginLine = beginLine;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public ArrayList getInterfaceSignals() {
		return interfaceSignals;
	}
	// ensures that there is only one copy for each signal
	public void addInterfaceSignal(SHDLSignal signal) {
		signal = addModuleSignal(signal);
		interfaceSignals.add(signal);
	}

	public ArrayList getModuleSignals() {
		return moduleSignals;
	}
	// ensures that there is only one copy for each signal
	public SHDLSignal addModuleSignal(SHDLSignal signal) {
		int i = moduleSignals.indexOf(signal);
		if (i == -1)
			moduleSignals.add(signal);
		else
			signal = (SHDLSignal) moduleSignals.get(i);
		return signal;
	}

	// checks if a signal of that name exists and returns it; return null if it does not
	public SHDLSignal lookForSignal(String normalizedSignalName) {
		for (Iterator it = moduleSignals.iterator(); it.hasNext(); ) {
			SHDLSignal signal = (SHDLSignal) it.next();
			if (signal.getNormalizedName().equals(normalizedSignalName)) return signal;
		}
		return null;
	}


	public ArrayList getCombinSettings() {
		return combinSettings;
	}
	// called at parse time, when it does not know the OE term yet
	public void addCombinSetting(SHDLCombinatorialSetting combinSetting) {
		combinSettings.add(combinSetting);
		SHDLSignal signal = combinSetting.getSignal();
		signal.addSource(combinSetting);
	}


	public ArrayList getSequentialSettings() {
		return seqSettings;
	}
	public void addSeqSetting(SHDLSequentialSetting seqSetting) {
		seqSettings.add(seqSetting);
		SHDLSignal signal = seqSetting.getSignal();
		signal.addSource(seqSetting);
	}

	public ArrayList getSequentialModifiers() {
		return seqModifiers;
	}
	public void addSeqModifier(SHDLSequentialModifier modifier) {
		seqModifiers.add(modifier);
	}

	public ArrayList getModuleOccurences() {
		return moduleOccurences;
	}
	public void addModuleOccurence(SHDLModuleOccurence moduleOccurence) {
		moduleOccurences.add(moduleOccurence);
	}


	public boolean prefixExist(String prefix) {
		ArrayList localSignals = getModuleSignals();
		for (int i = 0; i < localSignals.size(); i++) {
			SHDLSignal sig = (SHDLSignal) localSignals.get(i);
			if (sig.isConstant()) continue;
			if (sig.getPrefix().toLowerCase().equals(prefix.toLowerCase())) return true;
		}
		return false;
	}

	private HashMap substituteMap = new HashMap();

	public void substitutePrefixName(String old, String subst) {
		substituteMap.put(old, subst);
	}

	public String getSubstPrefix(String prefix, boolean substitute) {
		if (!substitute) return prefix;
		String subst = (String) substituteMap.get(prefix);
		if (subst == null) return prefix; else return subst;
	}

	private HashMap ioStatCache = new HashMap();

	// for bus bits and scalars
	// return [nbS, nbTS, isRead]
	public int[] getIOStatBit(String bitName) {
		int[] ioStat = (int[]) ioStatCache.get(bitName);
		if (ioStat != null) return ioStat;
		boolean isRead = false;
		int nbS = 0; // nb sources
		int nbTS = 0; // nb tristate sources
		ArrayList sources = getSignalSources(bitName);
		//System.out.println("bitName=" + bitName + ", sources=" + sources);
		int ns = (sources == null) ? 0 : sources.size();
		nbS += ns;
		isRead |= getRead(bitName);
		for (int j = 0; j < ns; j++) {
			Object source = sources.get(j);
			if (source instanceof SHDLCombinatorialSetting) {
				SHDLCombinatorialSetting cs = (SHDLCombinatorialSetting) source;
				if (cs.getOE() != null) nbTS += 1;
			}
		}
		ioStat = new int[] { nbS, nbTS, isRead ? 1 : 0 };
		ioStatCache.put(bitName, ioStat);
		return ioStat;
	}

	public void clearIOStatBit(String bitName) {
		ioStatCache.put(bitName, null);
	}


	// return [nbS, nbTS, isRead]
	public int[] getIOStat(SHDLSignal signal) {
		//System.out.println("getIOStat signal=" + signal);
		String signalName = signal.getNormalizedName();
		int[] ioStat = (int[]) ioStatCache.get(signalName);
		if (ioStat != null) return ioStat;
		boolean isRead = false;
		int nbS = 0; // nb sources
		int nbTS = 0; // nb tristate sources
		if (signal.isPartOfBus()) {
			// bus signal: check all bits
			for (int i = signal.getLowestIndex(); i <= signal.getHighestIndex(); i++) {
				String bitName = signal.getPrefix() + "[" + i + "]";
				int[] stat = getIOStatBit(bitName);
				nbS += stat[0]; // nb sources
				nbTS += stat[1]; // nb tristate sources
				isRead |= (stat[2] == 1) ? true : false;
			}
		} else {
			String bitName = signal.getPrefix();
			int[] stat = getIOStatBit(bitName);
			nbS = stat[0]; // nb sources
			nbTS = stat[1]; // nb tristate sources
			isRead = (stat[2] == 1) ? true : false;
		}
		ioStat = new int[] { nbS, nbTS, isRead ? 1 : 0 };
		ioStatCache.put(signalName, ioStat);
		return ioStat;
	}

	public void clearIOStat(SHDLSignal signal) {
		ioStatCache.put(signal.getNormalizedName(), null);
		if (signal.isPartOfBus()) {
			for (int i = signal.getLowestIndex(); i <= signal.getHighestIndex(); i++) {
				String bitName = signal.getPrefix() + i;
				clearIOStatBit(bitName);
			}
		} else {
			String bitName = signal.getPrefix();
			clearIOStatBit(bitName);
		}
	}

	// convert [nbS, nbTS, isRead] to an IO status string
	public String ioStatus(int[] stat) {
		int nbS = stat[0]; // nb sources
		int nbTS = stat[1]; // nb tristate sources
		boolean isRead = (stat[2] == 1) ? true : false;
		if (nbS > 0) {
			if (nbTS > 0)
				return "inout";
			else if (isRead)
				return "buffer";
			else
				return "out";
		} else if (isRead) {
			return "in";
		} else {
			return "unused";
		}
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// After parsing, signal.getRead() and signal.getSource() do not take into account
	// bus signals cut or regrouped, and effects due to module occurences
	// After these two methods have been applied to all scalar or bus signals of a module,
	// all these effects are taken into account. Use module.getRead(signal) and module.getSignalSources(signal)
	// do get the right values
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// read and sources values are not attached to signals, since most scalar bus signals
	// do not have a signal of their own. They are attached to each scalar signal, part of bus or not
	//
	HashMap sourcesMap = new HashMap();
	HashMap readMap = new HashMap();
	HashMap interfaceMap = new HashMap();

	public void completeScalarSignalSources(SHDLSignal signal, HashMap usedModules) {
		if (signal.isConstant()) return;
		SHDLModule module = signal.getModule();
		// first copy sources collected when checking combinatorial and sequential assignements
		setSignalSources(signal.getNormalizedName(), signal.getSources());
		if (signal.getRead()) setRead(signal.getName(), true);
		// check if the signal is among the arguments of the module's moduleOccurences
		// if so, look through occurences hierarchy to complete its 'read' and source attributes
		for (int i = 0; i < module.getModuleOccurences().size(); i++) {
			SHDLModuleOccurence moduleOccurence = (SHDLModuleOccurence) module.getModuleOccurences().get(i);
			ArrayList arguments = moduleOccurence.getArguments();
			for (int j = 0; j < arguments.size(); j++) {
				SHDLSignal argSignal = (SHDLSignal) arguments.get(j);
				if (!argSignal.equals(signal)) continue;
				if (moduleOccurence.isPredefined()) {
					SHDLPredefinedOccurence po = moduleOccurence.getPredefined();
					if (po.isInput(j) || po.isInputOutput(j))
						setRead(signal.getNormalizedName(), true);
					else if (po.isOutput(j))
						addSignalSource(signal.getNormalizedName(), moduleOccurence);
				} else {
					// get corresponding parameter in submodule definition, recursively
					SHDLModule submod = (SHDLModule) usedModules.get(moduleOccurence.getName());
					SHDLSignal paramSignal = (SHDLSignal) submod.getInterfaceSignals().get(j);
					// search recursively
					submod.completeScalarSignalSources(paramSignal, usedModules);
					ArrayList toAdd = submod.getSignalSources(paramSignal.getName());
					boolean r = submod.getRead(paramSignal.getNormalizedName());
					// transfer results from parameter to argument
					if (r) setRead(signal.getName(), true);
					for (int k = 0; k < toAdd.size(); k++) {
						addSignalSource(signal.getNormalizedName(), toAdd.get(k));
					}
				}
			}
		}
	}

	// <index> is the bit index of bus signal <signal>
	// Look for the status of bus signals bit by bit, recursively through module occurences
	//
	public void completeBusSignalSources(SHDLSignal signal, int index, HashMap usedModules) {
		SHDLModule module = signal.getModule();
		// collect local sources from compatible bus signals
		ArrayList localSignals = module.getModuleSignals();
		for (int i = 0; i < localSignals.size(); i++) {
			SHDLSignal sig = (SHDLSignal) localSignals.get(i);
			if (sig.isConstant()) continue;
			if (!sig.getPrefix().equals(signal.getPrefix())) continue;
			if (!sig.containsIndex(index)) continue;
			addSignalSources(signal.getPrefix() + "[" + index + "]", sig.getSources());
			if (sig.getRead()) setRead(signal.getPrefix() + "[" + index + "]", true);
		}
		
		// check if the signal is among the arguments of the module's moduleOccurences
		// if so, look through occurences hierarchy to complete its 'read' and source attributes
		for (int i = 0; i < module.getModuleOccurences().size(); i++) {
			SHDLModuleOccurence moduleOccurence = (SHDLModuleOccurence) module.getModuleOccurences().get(i);
			ArrayList arguments = moduleOccurence.getArguments();
			for (int j = 0; j < arguments.size(); j++) {
				SHDLSignal argSignal = (SHDLSignal) arguments.get(j);
				// check if they have same prefix
				if (argSignal.isConstant()) continue;
				if (!argSignal.getPrefix().equals(signal.getPrefix())) continue;
				// check that index is in its range
				if (!argSignal.containsIndex(index)) continue;
				if (moduleOccurence.isPredefined()) {
					SHDLPredefinedOccurence po = moduleOccurence.getPredefined();
					if (po.isInput(j) || po.isInputOutput(j))
						setRead(signal.getPrefix() + "[" + index + "]", true);
					else if (po.isOutput(j))
						addSignalSource(signal.getPrefix() + "[" + index + "]", moduleOccurence);
				} else {
					// get corresponding parameter in submodule definition, recursively
					SHDLModule submod = (SHDLModule) usedModules.get(moduleOccurence.getName());
					SHDLSignal paramSignal = (SHDLSignal) submod.getInterfaceSignals().get(j);
					// get its associated index in the submodule
					int pindex = getAssociatedIndex(argSignal, index, paramSignal);
					// search recursively in the submodule
					submod.completeBusSignalSources(paramSignal, pindex, usedModules);
					ArrayList toAdd = submod.getSignalSources(paramSignal.getPrefix() + "[" + pindex + "]");
					boolean r = submod.getRead(paramSignal.getPrefix() + "[" + pindex + "]");
					// transfer results from parameter to argument
					if (r) setRead(signal.getPrefix() + "[" + index + "]", true);
					if (toAdd != null) {
						for (int k = 0; k < toAdd.size(); k++) {
							addSignalSource(signal.getPrefix() + "[" + index + "]", toAdd.get(k));
						}
					}
				}
			}
		}
	}

	// ex: (a31..a16, 17, x15..x0) -> 1
	// ex: (a31..a16, 17, x0..x15) -> 14
	// ex: (a16..a31, 17, x15..x0) -> 14
	// ex: (a16..a31, 17, x0..x15) -> 1
	int getAssociatedIndex(SHDLSignal signal, int index, SHDLSignal paramSignal) {
		if (signal.getN1() >= signal.getN2()) {
			if (paramSignal.getN1() >= paramSignal.getN2()) {
				return paramSignal.getN2() - signal.getN2() + index;
			} else {
				return paramSignal.getN1() + signal.getN1() - index;
			}
		} else {
			if (paramSignal.getN1() >= paramSignal.getN2()) {
				return paramSignal.getN1() - index + signal.getN1();
			} else {
				return paramSignal.getN1() + index - signal.getN1();
			}
		}
	}

	public Iterator getSignalIterator() {
		return sourcesMap.keySet().iterator();
	}

	public ArrayList getSignalSources(String signalName) {
		return ((ArrayList) sourcesMap.get(signalName));
	}
	public void setSignalSources(String signalName, ArrayList sources) {
		sourcesMap.put(signalName, sources);
	}
	public void addSignalSource(String signalName, Object source) {
		ArrayList sources = getSignalSources(signalName);
		if (sources == null) sources = new ArrayList();
		if (sources.contains(source)) return;
		sources.add(source);
		//sourcesMap.put(signalName, sources);
	}
	public void addSignalSources(String signalName, ArrayList toAdd) {
		ArrayList sources = getSignalSources(signalName);
		if (sources == null) sources = new ArrayList();
		for (int i = 0; i < toAdd.size(); i++) {
			Object source = toAdd.get(i);
			if (sources.contains(source)) continue;
			sources.add(source);
		}
		sourcesMap.put(signalName, sources);
	}

	public boolean getRead(String signalName) {
		Boolean val = (Boolean) readMap.get(signalName);
		if (val == null) return false;
		return val.booleanValue();
	}
	public void setRead(String signalName, boolean val) {
		readMap.put(signalName, new Boolean(val));
	}
	public void setIsInterface(String signalName, boolean isInterface) {
		interfaceMap.put(signalName, new Boolean(isInterface));
	}
	public boolean isInterface(String signalName) {
		Boolean val = (Boolean) interfaceMap.get(signalName);
		if (val != null) return val.booleanValue(); else return false;
	}


	///////////////////////////////////////////////////////////////////////////////////////////

	private HashMap signalRangeMap = new HashMap();

	// Return the highest index for all signals having prefix <prefix>
	public int getHighestIndex(String prefix) {
		Integer maxIndex = (Integer) signalRangeMap.get("max:" + prefix);
		if (maxIndex != null) return maxIndex.intValue();
		ArrayList localSignals = getModuleSignals();
		int max = -1;
		for (int i = 0; i < localSignals.size(); i++) {
			SHDLSignal signal = (SHDLSignal) localSignals.get(i);
			if (!signal.isPartOfBus()) continue;
			if (!signal.getPrefix().equals(prefix)) continue;
			max = Math.max(max, signal.getHighestIndex());
		}
		signalRangeMap.put("max:" + prefix, new Integer(max));
		return max;
	}

	// Return the lowest index for all signals having prefix <prefix>
	public int getLowestIndex(String prefix) {
		Integer minIndex = (Integer) signalRangeMap.get("min:" + prefix);
		if (minIndex != null) return minIndex.intValue();
		ArrayList localSignals = getModuleSignals();
		int min = -1;
		for (int i = 0; i < localSignals.size(); i++) {
			SHDLSignal signal = (SHDLSignal) localSignals.get(i);
			if (!signal.isPartOfBus()) continue;
			if (!signal.getPrefix().equals(prefix)) continue;
			if (min == -1)
				min = signal.getLowestIndex();
			else
				min = Math.min(min, signal.getLowestIndex());
		}
		signalRangeMap.put("min:" + prefix, new Integer(min));
		return min;
	}

	// Return -1 if <prefix> is only used as scalar(i.e. 's', 's2'),
	// 0 when it is used as a bus, but only with 1-arity indexes, (i.e. 's[3]')
	// 1 when it is used as a bus 'downto', (i.e. 'r2[2..0]'),
	// 2 when it is used as a bus 'to' (i.e. 'r2[0..2]')
	// -2 when it is used both as a 'downto' and a 'to' bus signal,
	// -3 when when prefix is used both as a bus and as a scalar (i.e. s=a*b; s[3..0]=15;)
	public int getIndexDir(String prefix) {
		Integer dir_ = (Integer) signalRangeMap.get("dir:" + prefix);
		if (dir_ != null) return dir_.intValue();
		ArrayList localSignals = getModuleSignals();
		int dir = -1;
		for (int i = 0; i < localSignals.size(); i++) {
			SHDLSignal signal = (SHDLSignal) localSignals.get(i);
			if (signal.isConstant()) continue;
			if (!signal.getPrefix().equals(prefix)) continue;
			if (!signal.isPartOfBus()) {
				if (dir >= 1) { dir = -3; break; }
			} else if (signal.getArity() == 1) {
				if (dir == -1) dir = 0;
			} else if (signal.getN1() > signal.getN2()) {
				if (dir == 2) { dir = -2; break; } else dir = 1;
			} else if (signal.getN1() < signal.getN2()) {
				if (dir == 1) { dir = -2; break; } else dir = 2;
			}
		}
		signalRangeMap.put("dir:" + prefix, new Integer(dir));
		return dir;
	}


	// create and return all signals associated to <prefix>
	public ArrayList getPrefixSignals(String prefix) {
		ArrayList res = new ArrayList();
		int dir = getIndexDir(prefix);
		int lowest = getLowestIndex(prefix);
		int highest = getHighestIndex(prefix);
		boolean isScalar = (lowest == highest);
		if (dir == -1) {
			// -1 : scalar
			res.add(new SHDLSignal(prefix, false, this));
		/*} else if (dir == 0) {
			// 0 : look for bus signals with prefix individually
			ArrayList localSignals = getModuleSignals();
			for (int i = 0; i < localSignals.size(); i++) {
				SHDLSignal signal = (SHDLSignal) localSignals.get(i);
				if (signal.isConstant()) continue;
				if (!signal.getPrefix().equals(prefix)) continue;
				if (signal.isPartOfBus()) {
					//res.add(new SHDLSignal(prefix, signal.getN1(), signal.getN1(), this));

					//SHDLSignal newSig = new SHDLSignal(prefix, signal.getN1(), signal.getN1(), this);
					//if (newSig.equals(signal)) res.add(signal); else res.add(newSig);
					res.add(signal);
				} else {
					//res.add(new SHDLSignal(prefix, false, this));
					res.add(signal);
				}
			}*/
		} else if ((dir == 1) || (dir == 0)) {
			// 1
			//res.add(prefix + "[" + getHighestIndex(prefix) + ".." + getLowestIndex(prefix) + "]");
			res.add(new SHDLSignal(prefix, getHighestIndex(prefix), getLowestIndex(prefix), this));
		} else {
			// 2
			//res.add(prefix + "[" + getLowestIndex(prefix) + ".." + getHighestIndex(prefix) + "]");
			res.add(new SHDLSignal(prefix, getLowestIndex(prefix), getHighestIndex(prefix), this));
		}
		return res;
	}
	
	public String getVHDLIntPrefixDeclaration(String prefix, String header, boolean substitute) {
		int dir = getIndexDir(prefix);
		if (dir == -1)
			// -1 : scalar
			return (header + "signal " + getSubstPrefix(prefix, substitute) + " : std_logic ;" + newline);
		else if (dir == 0) {
			// 0 : declare each bus signal with prefix individually
			StringBuffer sb = new StringBuffer();
			ArrayList localSignals = getModuleSignals();
			for (int i = 0; i < localSignals.size(); i++) {
				SHDLSignal signal = (SHDLSignal) localSignals.get(i);
				if (signal.isConstant()) continue;
				if (!signal.getPrefix().equals(prefix)) continue;
				if (signal.isPartOfBus())
					sb.append(header + "signal " + getSubstPrefix(prefix, substitute) + signal.getN1() + " : std_logic ;" + newline);
				else
					sb.append(header + "signal " + getSubstPrefix(prefix, substitute) + " : std_logic ;" + newline);
			}
			return new String(sb);
		} else if (dir == 1)
			// 1
			return header + "signal " + getSubstPrefix(prefix, substitute) + " : std_logic_vector (" +  getHighestIndex(prefix) + " downto " + getLowestIndex(prefix) + ") ;" + newline;
		else
			// 2
			return header + "signal " + getSubstPrefix(prefix, substitute) + " : std_logic_vector (" +  getLowestIndex(prefix) + " to " + getHighestIndex(prefix) + ") ;" + newline;
	}

	public String getVHDLSignalDeclaration(SHDLSignal signal, String ioStatus, String header, boolean isLast) {
		String prefix = signal.getPrefix();
		int arity = signal.getArity();
		int dir = getIndexDir(prefix);
		// for 'buffer' signals, declare them as 'out' and use later an internal signal
		if (ioStatus.equals("buffer")) ioStatus = "out";
		if (ioStatus.equals("unused")) ioStatus = "in";
		StringBuffer sb = new StringBuffer();
		if (dir == -1)
			// -1 : scalar
			sb.append(header + getVHDLName(signal, arity, false, false) + " : " + ioStatus + " std_logic");
		else if (dir == 0) {
			if (signal.isPartOfBus()) {
				if (arity == 1)
					sb.append(header + prefix + " : " + ioStatus + " std_logic_vector (" +  signal.getN1() + " downto " + signal.getN1() + ")");
				else
					sb.append(header + prefix + signal.getN1() + " : " + ioStatus + " std_logic");
			} else {
				sb.append(header + prefix + " : " + ioStatus + " std_logic");
			}
		} else if (dir == 1)
			// 1
			sb.append(header + prefix + " : " + ioStatus + " std_logic_vector (" +  signal.getHighestIndex() + " downto " + signal.getLowestIndex() + ")");
		else
			// 2
			sb.append(header + prefix + " : " + ioStatus + " std_logic_vector (" +  signal.getLowestIndex() + " to " + signal.getHighestIndex() + ")");
		if (isLast) sb.append(newline); else sb.append(" ;" + newline);
		return new String(sb);
	}

	public String getVHDLIntSignalDeclaration(SHDLSignal signal, String header) {
		String prefix = signal.getPrefix();
		int arity = signal.getArity();
		int dir = getIndexDir(prefix);
		if (dir == -1)
			// -1 : scalar
			return(header + "signal " + getVHDLName(signal, arity, false, false) + " : std_logic ;");
		else if (dir == 0) {
			return(header + "signal " + prefix + " : std_logic_vector (" +  signal.getHighestIndex() + " downto " + signal.getLowestIndex() + ") ;");
			/*if (signal.isPartOfBus())
				//return(header + "signal " + prefix + signal.getN1() + " : std_logic ;");
				return(header + "signal " + signal.getName() + " : std_logic ;");
			else
				return(header + "signal " + prefix + " : std_logic ;");*/
		} else if (dir == 1)
			// 1
			return(header + "signal " + prefix + " : std_logic_vector (" +  signal.getHighestIndex() + " downto " + signal.getLowestIndex() + ") ;");
		else
			// 2
			return(header + "signal " + prefix + " : std_logic_vector (" +  signal.getLowestIndex() + " to " + signal.getHighestIndex() + ") ;");
	}

	public String getVHDLBufferAssignation(String prefix, String header) {
		int dir = getIndexDir(prefix);
		String subst = getSubstPrefix(prefix, true);
		if (dir == -1)
			// -1 : scalar
			return (header + prefix + " <= " + subst + " ;" + newline);
		else if (dir == 0) {
			// 0 : assign each bus signal with prefix individually
			StringBuffer sb = new StringBuffer();
			ArrayList localSignals = getModuleSignals();
			for (int i = 0; i < localSignals.size(); i++) {
				SHDLSignal signal = (SHDLSignal) localSignals.get(i);
				if (signal.isConstant()) continue;
				if (!signal.getPrefix().equals(prefix)) continue;
				if (signal.isPartOfBus())
					sb.append(header + prefix + signal.getN1() + " <= " + subst + signal.getN1() + " ;" + newline);
				else
					sb.append(header + prefix + " <= " + subst + " ;" + newline);
			}
			return new String(sb);
		} else if (dir == 1)
			// 1
			return header + prefix + "(" +  getHighestIndex(prefix) + " downto " + getLowestIndex(prefix) + ") <= " + subst + "(" +  getHighestIndex(prefix) + " downto " + getLowestIndex(prefix) + ") ;" + newline;
		else
			// 2
			return header + prefix + "(" +  getLowestIndex(prefix) + " to " + getHighestIndex(prefix) + ") <= " + subst + "(" + getLowestIndex(prefix) + " to " + getHighestIndex(prefix) + ") ;" + newline;
	}
	
	public String getVHDLNamePrefix(SHDLSignal signal) {
		if (signal.isConstant()) return "";
		String prefix = signal.getPrefix();
		int dir = getIndexDir(prefix);
		if (!signal.isPartOfBus()) {
			return prefix;
		} else if (signal.getArity() == 1) {
			if (dir == 0)
				return prefix + signal.getN1();
			else
				return prefix;
		} else
			return prefix;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	//             VHDL - VHDL - VHDL - VHDL - VHDL - VHDL - VHDL
	//       VHDL text
	//       It is attached to the module, since a module-scope information is often needed
	///////////////////////////////////////////////////////////////////////////////////////
	
	public String getVHDLName(SHDLSignal signal, int constArity, boolean substitute, boolean isPartOfCombinOrSequStatement) {
		int arity = signal.getArity();
		if (signal.isConstant()) {
			if (constArity == 1) {
				return "'" + signal.getConstantValue() + "'";
			} else {
				if (signal.getConstantBase() == 'D') {
				} else if (signal.getConstantBase() == 'H') {
				} else if (signal.getConstantBase() == 'B') {
				}
				StringBuffer sb = new StringBuffer();
				String binStr = signal.getConstantValue().toString(2);
				if (constArity == 1) sb.append("'"); else sb.append("\"");
				for (int i = 0; i < constArity - binStr.length(); i++) sb.append("0");
				sb.append(binStr);
				if (constArity == 1) sb.append("'"); else sb.append("\"");
				return new String(sb);
			}
		}
		String prefix = signal.getPrefix();
		int dir = getIndexDir(prefix);
		if (substitute) {
			// look for a possible name substitution
			String subst = (String) substituteMap.get(prefix);
			if (subst != null) prefix = subst;
		}

		if (!signal.isPartOfBus()) {
			return prefix;
		} else if (signal.getArity() == 1) {
			if (isPartOfCombinOrSequStatement) {
				return prefix + "(" + signal.getN1() + ")";
			} else {
				return prefix + "(" + signal.getN1() + " downto " + signal.getN1() + ")";
			}
			/*
			// is prefix ever used as bus ?
			if (dir == 0) {
				return prefix + "(" + signal.getN1() + " downto " + signal.getN1() + ")";
				//return prefix + "(" + signal.getN1() + ")";
				//return signal.getName(); // la substitution prefix -> subst ne marche pas
			} else {
				return prefix + "(" + signal.getN1() + ")";
			}
			*/
		} else {
			if (signal.getN1() > signal.getN2())
				return prefix + "(" + signal.getN1() + " downto " + signal.getN2() + ")";
			else
				return prefix + "(" + signal.getN1() + " to " + signal.getN2() + ")";
		}
	}

	public String getVHDLText(SHDLSignalOccurence so, int arity) {
		String sigText = getVHDLName(so.getSignal(), arity, true, true);
		if (so.isInverted()) return ("not " + sigText); else return sigText;
	}


	public String getVHDLText(SHDLTerm term, int arity) {
		StringBuffer sb = new StringBuffer();
		ArrayList sos = term.getSignalOccurences();
		for (int i = 0; i < sos.size(); i++) {
			if (i > 0) sb.append(" and ");
			SHDLSignalOccurence so = (SHDLSignalOccurence) sos.get(i);
			if (so.isInverted() && (sos.size() > 1))
				sb.append("(" + getVHDLText(so, arity) + ")");
			else
				sb.append(getVHDLText(so, arity));
		}
		return new String(sb);
	}


	public String getVHDLText(SHDLTermsSum ts, int arity) {
		StringBuffer sb = new StringBuffer();
		ArrayList terms = ts.getTerms();
		for (int i = 0; i < terms.size(); i++) {
			if (i > 0) sb.append(" or ");
			SHDLTerm t = (SHDLTerm) terms.get(i);
			if (terms.size() > 1)
				sb.append("(" + getVHDLText(t, arity) + ")");
			else
				sb.append(getVHDLText(t, arity));
		}
		return new String(sb);
	}


	public String getVHDLText(SHDLCombinatorialSetting cs) {
		SHDLSignal signal = cs.getSignal();
		int arity = signal.getArity();
		if (cs.getOE() == null) {
			if (signal.getArity() == 1) {
				return ("\t" + getVHDLName(signal, arity, true, true) + " <= " + getVHDLText(cs.getEquation(), arity) + " ;");
			} else if (cs.getEquation().containsScalars()) {
				StringBuffer sb = new StringBuffer();
				sb.append("\t" + getVHDLName(signal, arity, true, true) + " <= ");
				ArrayList terms = cs.getEquation().getTerms();
				for (int i = 0; i < terms.size(); i++) {
					SHDLTerm term = (SHDLTerm) terms.get(i);
					SHDLTerm scalars = term.getScalars();
					SHDLTerm buses = term.getBusesAndConsts();
					sb.append(newline + "\t\t(" + getVHDLText(buses, arity));
					ArrayList sigOccs = scalars.getSignalOccurences();
					for (int j = 0; j < sigOccs.size(); j++) {
						SHDLSignalOccurence sigOcc = (SHDLSignalOccurence) sigOccs.get(j);
						sb.append(" and (");
						for (int k = 0; k < arity; k++) {
							if (k > 0) sb.append("&");
							if (sigOcc.isInverted())
								sb.append("(not " + getVHDLName(sigOcc.getSignal(), arity, true, true) + ")");
							else
								sb.append(getVHDLName(sigOcc.getSignal(), arity, true, true));
						}
						sb.append(")");
					}
					sb.append(")");
					if (i < terms.size() - 1) sb.append(" or");
				}
				sb.append(" ;");
				return new String(sb);
			} else {
				return ("\t" + getVHDLName(signal, arity, true, true) + " <= " + getVHDLText(cs.getEquation(), arity) + " ;");
			}
		} else if (cs.getOE().getSignal().getArity() == 1) {
			int val = cs.getOE().isInverted() ? 0 : 1;
			return ("\t" + getVHDLName(signal, arity, true, true) + " <= " + getVHDLText(cs.getEquation(), arity) + " when " + getVHDLName(cs.getOE().getSignal(), arity, true, true) + " = '" + val + "' else " + nthChar(arity, 'Z') + " ;");
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < arity; i++) {
				if (i > 0) sb.append(newline);
				// so = unique signal occurence on the left of ":"
				SHDLTerm t = (SHDLTerm) cs.getEquation().getTerms().get(0);
				SHDLSignalOccurence so = (SHDLSignalOccurence) t.getSignalOccurences().get(0);
				String soname = getSubstPrefix(so.getSignal().getPrefix(), true);
				String oename = getSubstPrefix(cs.getOE().getSignal().getPrefix(), true);
				if (so.isInverted()) {
					if (cs.getOE().isInverted())
						sb.append("\t" + signal.getPrefix() + "(" + i + ") <= not " + soname + "(" + i + ") when " + oename + "(" + i + ") = '0' else 'Z' ;");
					else
						sb.append("\t" + signal.getPrefix() + "(" + i + ") <= not " + soname + "(" + i + ") when " + oename + "(" + i + ") = '1' else 'Z' ;");
				} else {
					if (cs.getOE().isInverted())
						sb.append("\t" + signal.getPrefix() + "(" + i + ") <= " + soname + "(" + i + ") when " + oename + "(" + i + ") = '0' else 'Z' ;");
					else
						sb.append("\t" + signal.getPrefix() + "(" + i + ") <= " + soname + "(" + i + ") when " + oename + "(" + i + ") = '1' else 'Z' ;");
				}
			}
			return new String(sb);
		}
	}


	public String getVHDLText(SHDLSequentialSetting ss) {
		int arity = ss.getSignal().getArity();
		StringBuffer sb = new StringBuffer();
		sb.append("\tprocess (" + getVHDLName(ss.getCLK().getSignalOccurence().getSignal(), arity, true, true));
		if (ss.getRST() != null) sb.append(", " + getVHDLName(ss.getRST().getSignalOccurence().getSignal(), arity, true, true));
		if (ss.getSET() != null) sb.append(", " + getVHDLName(ss.getSET().getSignalOccurence().getSignal(), arity, true, true));
		sb.append(") begin" + newline);
		boolean first = true;
		if (ss.getRST() != null) {
			int rstVal = ss.getRST().getSignalOccurence().isInverted() ? 0 : 1;
			sb.append("\t\tif " + getVHDLName(ss.getRST().getSignalOccurence().getSignal(), arity, true, true) + " = '" + rstVal + "' then" + newline);
			sb.append("\t\t\t" + getVHDLName(ss.getSignal(), arity, true, true) + " <= " + nthChar(arity, '0') + " ;" + newline);
			first = false;
		}
		if (ss.getSET() != null) {
			int setVal = ss.getSET().getSignalOccurence().isInverted() ? 0 : 1;
			sb.append("\t\t" + (first ? "if " : "elsif ") + getVHDLName(ss.getSET().getSignalOccurence().getSignal(), arity, true, true) + " = '" + setVal + "' then" + newline);
			sb.append("\t\t\t" + getVHDLName(ss.getSignal(), arity, true, true) + " <= " + nthChar(arity, '1') + " ;" + newline);
		}
		int clkVal = ss.getCLK().getSignalOccurence().isInverted() ? 0 : 1;
		String iff = ((ss.getRST() == null) && (ss.getSET() == null)) ? "if " : "elsif ";
		String enas = "";
		if (ss.getENA() != null) {
			int enaVal = ss.getENA().getSignalOccurence().isInverted() ? 0 : 1;
			enas = getVHDLName(ss.getENA().getSignalOccurence().getSignal(), arity, true, true) + " = '" + enaVal + "' and ";
		}
		sb.append("\t\t" + iff + enas + getVHDLName(ss.getCLK().getSignalOccurence().getSignal(), arity, true, true) + "'event and " + getVHDLName(ss.getCLK().getSignalOccurence().getSignal(), arity, true, true) + " = '" + clkVal + "' " + "then" + newline);
		sb.append("\t\t\t" + getVHDLName(ss.getSignal(), arity, true, true) + " <= " + getVHDLText(ss.getEvolution(), arity) + " ;" + newline);
		sb.append("\t\tend if ;" + newline);
		sb.append("\tend process ;");
		return new String(sb);
	}

	public String getVHDLComponentDeclaration() {
		StringBuffer sb = new StringBuffer();
		sb.append("\tcomponent " + getName() + newline);
		sb.append("\t\tport (" + newline);
		for (int j = 0; j < getInterfaceSignals().size(); j++) {
			SHDLSignal signal = (SHDLSignal) getInterfaceSignals().get(j);
			int[] stat = getIOStat(signal);
			String ioStatus = ioStatus(stat);
			boolean isLast = (j == getInterfaceSignals().size() - 1);
			sb.append(getVHDLSignalDeclaration(signal, ioStatus, "\t\t\t", isLast));
		}
		sb.append("\t\t) ;" + newline);
		sb.append("\tend component ;");
		return new String(sb);
	}

	String nthChar(int nb, char c) {
		StringBuffer sb = new StringBuffer();
		if (nb > 0) {
			if (nb == 1) sb.append("'"); else sb.append("\"");
			for (int i = 0; i < nb; i++) sb.append(c);
			if (nb == 1) sb.append("'"); else sb.append("\"");
		}
		return new String(sb);
	}
}

