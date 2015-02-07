
package org.jcb.craps;

import java.util.*;

import org.jcb.craps.crapsc.java.ObjEntry;
import org.jcb.craps.crapsc.java.ObjModule;
import org.jcb.tools.*;
import javax.swing.table.*;
import javax.swing.event.*;



public class CrapsMachine {

	private ArrayList objModules;
	private ObjModule memImage; // fusion of all loaded obj module. Its symbol table contains all global symbols
	private HashMap addr2mod;


	public CrapsMachine() {
		objModules = new ArrayList();
		memImage = new ObjModule();
		addr2mod = new HashMap();
	}

	// throws an error when there are conflicts with other already loaded images
	public void addObjModule(ObjModule objModule) throws Exception {
		objModules.add(objModule);
		// include new entries
		Long[] newKeys = (Long[]) objModule.getKeySet().toArray(new Long[0]);
		Map.Entry[] newEntries = (Map.Entry[]) objModule.getEntrySet().toArray(new Map.Entry[0]);
		for (int i = 0; i < newEntries.length; i++) {
			long addr = newKeys[i].intValue();
			ObjEntry oe = memImage.get(addr);
			/*
			if ((oe != null) && (oe.word != "XXXXXXXXXXXXXXXX")) {
System.out.println("conflit avec oe=" + oe + ", addr=" + addr);
				throw new ObjConflictException(addr, oe.word, oe.sl);
			}
			*/
			oe = (ObjEntry) newEntries[i].getValue();
			memImage.add(objModule, addr, oe.word, oe.sl);
			setModule(addr, objModule);
		}
		// include global symbols
		String[] syms = (String[]) objModule.getSymSet().toArray(new String[0]);
		for (int i = 0; i < syms.length; i++) {
			if (! objModule.isGlobalSymbol(syms[i])) continue;
			long addr = objModule.getIntVal(syms[i]);
			memImage.set(syms[i], addr, -1);
		}
	}

	public ObjModule getMemImage() {
		return memImage;
	}

	public void removeObjModule(ObjModule objModule) throws Exception {
		objModules.remove(objModule);
		Map.Entry[] entries = (Map.Entry[]) memImage.getEntrySet().toArray(new Map.Entry[0]);
		for (int i = 0; i < entries.length; i++) {
			ObjEntry oe = (ObjEntry) entries[i].getValue();
			if (oe.om == objModule) memImage.remove(((Long) entries[i].getKey()).intValue());
		}
		// remove global symbols
		String[] syms = (String[]) objModule.getSymSet().toArray(new String[0]);
		for (int i = 0; i < syms.length; i++) {
			if (! objModule.isGlobalSymbol(syms[i])) continue;
			memImage.removeSymbol(syms[i]);
		}
	}

	public boolean probeObjModule(ObjModule objModule) {
		return objModules.contains(objModule);
	}

	public void clearObjModules() throws Exception {
		objModules.clear();
	}
	
	public void setModule(long addr, ObjModule objModule) {
		addr2mod.put(new Long(addr), objModule);
	}
	
	public ObjModule getModule(long addr) {
		return ((ObjModule) addr2mod.get(new Long(addr)));
	}

}

