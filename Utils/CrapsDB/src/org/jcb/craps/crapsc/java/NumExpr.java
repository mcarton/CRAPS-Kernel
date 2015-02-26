package org.jcb.craps.crapsc.java;

public abstract class NumExpr {
    public abstract boolean isInstanciated(ObjModule localSYmbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl);

    public abstract long getValue(ObjModule localSymbols, ObjModule globalSymbols, CrapsInstrDirecSynth cl);

}

