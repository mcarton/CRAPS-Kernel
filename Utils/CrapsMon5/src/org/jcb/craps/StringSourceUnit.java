package org.jcb.craps;

import java.util.*;
import mg.egg.eggc.runtime.libjava.SourceUnit;


public class StringSourceUnit extends SourceUnit {
    private String source;

    public StringSourceUnit(String source) {
        super(null);
        this.source = source;
    }

    public char[] getContents() throws Exception {
        return source.toCharArray();
    }
}
