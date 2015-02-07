
package org.jcb.craps.crapsc.java;

import java.util.*;
import java.io.*;
import org.jcb.tools.*;
import org.jcb.craps.crapsc.java.*;


// encapsulates a memory map and its associated symbol table

public class ObjModule {

	private TreeMap map;
	private HashMap sym2val;
	private HashMap val2sym;
	private HashMap sym2lineno;
	private ArrayList globalSymbols;

	public ObjModule() {
		map = new TreeMap();
		sym2val = new HashMap();
		val2sym = new HashMap();
		sym2lineno = new HashMap();
		globalSymbols = new ArrayList();
	}

	public void reset() {
		map.clear();
		sym2val.clear();
		val2sym.clear();
		sym2lineno.clear();
	}

	// return ordered addresses
	public Set getKeySet() {
		return map.keySet();
	}

	// return entry set of ObjEntry, ordered by addresses
	public Set getEntrySet() {
		return map.entrySet();
	}

	// <addr> must be even. <om> is the module from which comes this entry,
	// in case of obj module fusion
	public void add(ObjModule om, long addr, String word, SourceLine sl) {
		ObjEntry oe = new ObjEntry(om, word, sl);
		map.put(new Long(addr), oe);
	}

	// <addr> must be even
	public ObjEntry get(long addr) {
		return (ObjEntry) map.get(new Long(addr));
	}

	public void remove(long addr) {
		map.remove(new Long(addr));
	}


	public static ObjModule load(File file) throws Exception {
		ObjModule objModule = new ObjModule();
		LineNumberReader reader = new LineNumberReader(new FileReader(file));
		while (true) {
			String line = reader.readLine();
			if (line == null) break;
			int sp1 = line.indexOf(' ');
			int sp2 = line.indexOf(' ', sp1 + 1);
			String type = line.substring(0, sp1);
			if (type.equals("word")) {
				// load word contents
				String saddr = line.substring(sp1 + 1, sp2);
				String word = line.substring(sp2 + 1);
				objModule.add(objModule, Integer.parseInt(saddr), word, null);
			} else if (type.equals("sym")) {
				// load global symbol
				String sym = line.substring(sp1 + 1, sp2);
				String val = line.substring(sp2 + 1);
				objModule.set(sym, Integer.parseInt(val), -1);
				objModule.addGlobalSymbol(sym);
			}
		}
		return objModule;
	}


	public void save(File file) throws Exception {
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		// save word contents
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			Long addr = (Long) it.next();
			ObjEntry oe = (ObjEntry) map.get(addr);
			writer.println("word " + addr + " " + oe.word);
		}
		// save global symbols values
		Iterator its = sym2val.keySet().iterator();
		while (its.hasNext()) {
			String sym = (String) its.next();
			if (! isGlobalSymbol(sym)) continue;
			String val = (String) sym2val.get(sym);
			writer.println("sym " + sym + " " + val);
		}
		writer.flush();
		writer.close();
	}


	////////////       SYMBOLS & LINES MANAGEMENT       //////////////

	public boolean isDefined(String sym) {
		return (sym2val.containsKey(sym));
	}

	public void set(String sym, long val, int lineno) {
		sym2val.put(sym, val + "");
		val2sym.put(val + "", sym);
		sym2lineno.put(sym, lineno + "");
	}

	public void removeSymbol(String sym) {
		String val = (String) sym2val.get(sym);
		sym2val.remove(sym);
		val2sym.remove(val);
		sym2lineno.remove(sym);

		int idx = globalSymbols.indexOf(sym);
		if (idx != -1) globalSymbols.remove(idx);
	}

	public long getIntVal(String sym) {
		return Long.parseLong((String) sym2val.get(sym));
	}

	public String getSym(long val) {
		return ((String) val2sym.get(val + ""));
	}

	public int getLineNo(String sym) {
		return Integer.parseInt((String) sym2lineno.get(sym));
	}

	// return set of all symbols
	public Set getSymSet() {
		return sym2val.keySet();
	}

	public void addGlobalSymbol(String symbol) {
		globalSymbols.add(symbol);
	}

	public boolean isGlobalSymbol(String sym) {
		return globalSymbols.contains(sym);
	}

	public String toString() {
		return sym2val.toString();
	}
	
}


