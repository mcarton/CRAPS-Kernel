
package org.jcb.craps.crapsc.java;
 
import java.io.*;
import java.util.*;
import java.text.*;
import org.jcb.craps.*;


public abstract class NumExpr {

	public abstract boolean isInstanciated(ObjModule localSYmbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl);

	public abstract long getValue(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl);

}

