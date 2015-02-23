package org.jcb.shdl.shdlc.java;

import java.io.PrintStream;
import java.util.regex.Pattern;

public class SHDLPredefinedRamCtrl extends SHDLPredefinedOccurence {
    public SHDLPredefinedRamCtrl(
        SHDLModuleOccurence moduleOccurence, Pattern namePattern
    ) {
        super(moduleOccurence, namePattern);
    }

    public boolean isInput(int index) {
        switch (index) {
            case 0: case 1: case 2: case 3:
            case 6: case 7: case 20: case 22:
                return true;
            default:
                return false;
        }
    }

    public boolean isOutput(int index) {
        switch (index) {
            case 0: case 1: case 2: case 3:
            case 6: case 7: case 9: case 20:
            case 22:
                return false;
            default:
                return true;
        }
    }

    public boolean isInputOutput(int index) {
        return index == 9;
    }

    public int getArity(int index) {
        switch (index) {
            case 2: return 23;
            case 4: return 16;
            case 7: return 16;
            case 10: return 16;
            case 11: return 24;
            default: return 1;
        }
    }

    public boolean check(boolean ok, SHDLModule module, PrintStream errorStream) {
        return ok; // TODO
    }

    public String getVHDLComponentDeclaration() {
        java.io.InputStream stream = getClass().getResourceAsStream(
            "/resources/RamCtrl.comp.vhd"
        );
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");

        return s.next();
    }

    public String getVHDLDefinition() {
        java.io.InputStream stream = getClass().getResourceAsStream(
            "/resources/RamCtrl.arc.vhd"
        );
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");

        return s.next();
    }
}
