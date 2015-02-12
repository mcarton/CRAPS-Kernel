package org.jcb.shdl.shdlc.java;

import java.io.PrintStream;
import java.util.regex.Pattern;

public class SHDLPredefinedRs232RefComp extends SHDLPredefinedOccurence {
    public SHDLPredefinedRs232RefComp(
        SHDLModuleOccurence moduleOccurence, Pattern namePattern
    ) {
        super(moduleOccurence, namePattern);
    }

    public boolean isInput(int index) {
        switch (index) {
            case 1: case 2: case 3:
            case 7: case 8: case 12:
                return true;
            case 0: case 4: case 5:
            case 6: case 9: case 10:
            case 11:
                return false;
            default:
                throw new IllegalArgumentException(
                    "SHDLPredefinedRs232RefComp has 13 parameters"
                );
        }
    }

    public boolean isOutput(int index) {
        switch (index) {
            case 0: case 4: case 9:
            case 10: case 11:
                return true;
            case 1: case 2: case 3:
            case 5: case 6: case 7:
            case 8: case 12:
                return false;
            default:
                throw new IllegalArgumentException(
                    "SHDLPredefinedRs232RefComp has 13 parameters"
                );
        }
    }

    public boolean isInputOutput(int index) {
        if (index == 5 || index == 6) {
            return true;
        }
        else if (index < 0 || index > 12) {
            throw new IllegalArgumentException(
                "SHDLPredefinedRs232RefComp has 13 parameters"
            );
        }
        else {
            return false;
        }
    }

    public int getArity(int index) {
        if (index == 3 || index == 4) {
            return 8;
        }
        else if (index <= 12) {
            return 1;
        }
        else {
            return -1;
        }
    }

    public boolean check(boolean ok, SHDLModule module, PrintStream errorStream) {
        return ok; // TODO: 232
    }

    public String getVHDLComponentDeclaration() {
        java.io.InputStream stream = getClass().getResourceAsStream(
            "/resources/RS232RefComp.comp.vhd"
        );
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");

        return s.next();
    }

    public String getVHDLDefinition() {
        java.io.InputStream stream = getClass().getResourceAsStream(
            "/resources/RS232RefComp.arc.vhd"
        );
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");

        return s.next();
    }
}
