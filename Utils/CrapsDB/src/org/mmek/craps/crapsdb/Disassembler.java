package org.mmek.craps.crapsdb;

import org.jcb.craps.crapsc.java.ObjModule;


public class Disassembler {
    private ObjModule objModule;

    public Disassembler(ObjModule objModule) {
        this.objModule = objModule;
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
                        case 8:  codeop = "ba    "; break;
                        case 1:  codeop = "be    "; break;
                        case 9:  codeop = "bne   "; break;
                        case 5:  codeop = "bcs   "; break;
                        case 13: codeop = "bcc   "; break;
                        case 14: codeop = "bpos  "; break;
                        case 6:  codeop = "bneg  "; break;
                        case 7:  codeop = "bvs   "; break;
                        case 15: codeop = "bvc   "; break;
                        case 10: codeop = "bg    "; break;
                        case 2:  codeop = "ble   "; break;
                        case 11: codeop = "bge   "; break;
                        case 3:  codeop = "bl    "; break;
                        case 12: codeop = "bgu   "; break;
                        case 4:  codeop = "bleu  "; break;
                    }
                    String relDisp = disp24 + "";
                    if (disp24 >= 0) relDisp = "+" + relDisp;

                    if (objModule == null) {
                        return codeop + relDisp;
                    }
                    else {
                        long brAddr = addr + disp24;
                        String sym = objModule.getSym(brAddr);
                        if (sym == null) return codeop + relDisp;
                        return codeop + sym + " (" + relDisp + ")";
                    }
                }
                else {
                    // sethi
                    long imm24 = instr % 16777216L; // 2^24
                    long rd = (instr / 16777216L) % 32; // 2^24
                    return "sethi  0x" + Long.toHexString(imm24) + ", %r" + rd;
                }
            case 1: // reti
                return "reti";
            case 2: // arithmetique & logique
            case 3: // acces memoire
                long op3 = (instr / 524288L) % 64; // 2^19
                long rs1 = (instr / 16384L) % 32; // 2^14
                long rs2 = instr % 32;
                long rd = (instr / 33554432L) % 32; // 2^25
                long ir13 = (instr / 8192L) % 2; // 2^13
                long simm13 = instr % 8192L; // 2^13
                if (simm13 >= 4096L) simm13 -= 8192L;

                String codeop = "";
                switch ((int) op3) {
                    case 0:  codeop = "add   "; break;
                    case 16: codeop = "addcc "; break;
                    case 4:  codeop = "sub   "; break;
                    case 20: codeop = "subcc "; break;
                    case 26: codeop = "umulcc "; break;
                    case 1:  codeop = "and "; break;
                    case 2:  codeop = "or  "; break;
                    case 3:  codeop = "xor  "; break;
                    case 7:  codeop = "xnor  "; break;
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
}
