package org.mmek.craps.crapsc;

import java.io.*;
import java.util.*;
import org.jcb.craps.crapsc.java.*;

import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.SourceUnit;
import mg.egg.eggc.runtime.libjava.problem.IProblem;
import mg.egg.eggc.runtime.libjava.problem.ProblemReporter;
import mg.egg.eggc.runtime.libjava.problem.ProblemRequestor;


public class Assembler {
    private CrapsMachine crapsMachine;
    private List<String> messages;

    public Assembler() {
        crapsMachine = new CrapsMachine();
        messages = new ArrayList<String>();
    }

    public List<String> getMessages() {
        return messages;
    }

    public int assemble(SourceContext sc) {
        int nbErr = 0;

        ObjModule objModule = sc.getObjModule();
        ObjModule symbolTable = objModule;
        ObjModule globalSymbolTable = crapsMachine.getMemImage();

        boolean syntaxError = false;
        ArrayList<SourceLine> sourceLines = null;
        try {
            SourceUnit source = new SourceUnit(sc.getSourceFile().getPath());

            // Error management
            ProblemReporter prp = new ProblemReporter(source);
            ProblemRequestor prq = new ProblemRequestor(true);

            // Start compilation
            CRAPS compilo = new CRAPS(prp);
            prq.beginReporting();

            compilo.set_eval(true);
            compilo.compile(source);
            sourceLines = compilo.get_lines();

            // Handle errors
            for (IProblem problem : prp.getAllProblems())
                prq.acceptProblem(problem);

            prq.endReporting();
            if(prq.getFatal() > 0) {
                System.exit(prq.getFatal());
            }
        }
        catch(Exception e) {
            syntaxError = true;
            nbErr += 1;
            messages.add(e.toString());
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
                SourceLine sl = sourceLines.get(k);
                CrapsInstrDirecSynth cl = sl.instr_or_direc_or_synth;

                // line with a label: what is its value?
                if (sl.label != null) {
                    // value of a label: EQU is a special case
                    if (cl instanceof CrapsDirecEqu) {
                        // .EQU directive
                        CrapsDirecEqu direc = (CrapsDirecEqu) sl.instr_or_direc_or_synth;

                        if (!direc.expr.isInstanciated(symbolTable, globalSymbolTable, cl)) {
                            undefSymOrExpr = true;
                        }
                        else if (!symbolTable.isDefined(sl.label)) {
                            symbolTable.set(sl.label, direc.expr.getValue(symbolTable, globalSymbolTable, cl), sl.lineno);
                            changed = true;
                        }
                        else if (symbolTable.getLineNo(sl.label) != sl.lineno) {
                            messages.add("symbol '" + sl.label + "' already defined, line #" + sl.lineno);
                            nbErr += 1;
                        }
                    }
                    else if (adrKnown && !symbolTable.isDefined(sl.label)) {
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
                            messages.add("program addresses must go in ascending order, line #" + sl.lineno);
                            nbErr += 1;
                        }
                        else {
                            adr = newadr;
                        }
                    }
                    else {
                        adrKnown = false;
                        undefSymOrExpr = true;
                    }
                }
                else if (cl.getLength(symbolTable, globalSymbolTable).isInstanciated(symbolTable, globalSymbolTable, cl)) {
                    adr += cl.getLength(symbolTable, globalSymbolTable).getValue(symbolTable, globalSymbolTable, cl) / 4;
                    // realign after a .BYTE
                    if ((cl instanceof CrapsDirecByte) && (adr % 2 == 1))
                        adr += 1;
                }
                else {
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
                            messages.add("symbol '" + sl.label + "' undefined, line #" + sl.lineno);
                            nbErr += 1;
                            continue;
                        }
                    }
                    if (cl == null) continue;

                    if (cl instanceof CrapsDirecOrg) {
                        CrapsDirecOrg clo = (CrapsDirecOrg) cl;
                        if (! clo.isInstanciated(symbolTable, globalSymbolTable)) {
                            messages.add(".org address undefined, line #" + sl.lineno);
                            nbErr += 1;
                        }

                    }
                    else if (cl instanceof CrapsDirecEqu) {
                        CrapsDirecEqu cle = (CrapsDirecEqu) cl;
                        if (! cle.isInstanciated(symbolTable, globalSymbolTable)) {
                            messages.add(".equ expression undefined, line #" + sl.lineno);
                            nbErr += 1;
                        }

                    }
                    else if (cl instanceof CrapsDirecGlobal) {
                        CrapsDirecGlobal cle = (CrapsDirecGlobal) cl;
                        if (! cle.isInstanciated(symbolTable, globalSymbolTable)) {
                            messages.add(cle.getSymbol() + " undefined, line #" + sl.lineno);
                            nbErr += 1;
                        }

                    }
                    else if (!cl.isInstanciated(symbolTable, globalSymbolTable)) {
                        messages.add("symbol undefined, line #" + sl.lineno);
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
                    messages.add("imm24 value out of range [0,2^24-1], line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsInstrArithLog3) {
                // check range for 13-bit disp
                CrapsInstrArithLog3 instr = (CrapsInstrArithLog3) cl;
                if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("simm13 value out of range [-4096,+4095], line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsInstrLd) {
                // check range for 13-bit disp
                CrapsInstrLd instr = (CrapsInstrLd) cl;
                if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("simm13 value out of range [-4096,+4095], line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsInstrSt) {
                // check range for 13-bit disp
                CrapsInstrSt instr = (CrapsInstrSt) cl;
                if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("simm13 value out of range [-4096,+4095], line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsInstrBr) {
                // check range for 24-bit disp
                CrapsInstrBr instr = (CrapsInstrBr) cl;
                if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("branching address too far, line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsSynthCall) {
                // check range for 22-bit disp
                CrapsSynthCall instr = (CrapsSynthCall) cl;
                if (instr.isInstanciated(symbolTable, globalSymbolTable) && !instr.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("branching address too far, line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsDirecWord) {
                // check range for 16-bit const
                CrapsDirecWord word = (CrapsDirecWord) cl;
                if (word.isInstanciated(symbolTable, globalSymbolTable) && !word.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("word values out of range [-32768,+32767] or [0,65535], line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsDirecByte) {
                // check range for 8-bit const
                CrapsDirecByte byt = (CrapsDirecByte) cl;
                if (byt.isInstanciated(symbolTable, globalSymbolTable) && !byt.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("byte values out of range [-128,+127] or [0,256], line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsSynthSet) {
                // check range for 32-bit const
                CrapsSynthSet synth = (CrapsSynthSet) cl;
                if (synth.isInstanciated(symbolTable, globalSymbolTable) && !synth.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("32-bit value out of range, line #" + sl.lineno);
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsSynthSetq) {
                // check range for 13-bit const
                CrapsSynthSetq synth = (CrapsSynthSetq) cl;
                if (synth.isInstanciated(symbolTable, globalSymbolTable) && !synth.isContentValid(symbolTable, globalSymbolTable)) {
                    messages.add("13-bit constant out of range [-4096, +4095], line #" + sl.lineno + "; use 'set' instead");
                    nbErr += 1;
                }
            }
            else if (cl instanceof CrapsDirecGlobal) {
                // add global symbol
                CrapsDirecGlobal cdg = (CrapsDirecGlobal) cl;
                symbolTable.addGlobalSymbol(cdg.getSymbol());
            }
        }
        if (nbErr > 0) return nbErr;


        // fourth pass: build hex image
        for (int i = 0; i < sourceLines.size(); i++) {
            SourceLine sl = sourceLines.get(i);
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

        return nbErr;
    }
}
