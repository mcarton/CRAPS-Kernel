package org.jcb.craps.crapsc.java;

import org.jcb.craps.*;
import org.jcb.craps.crapsc.java.*;
import java.util.*;

public class ObjEntry {
    public String word;
    public SourceLine sl;
    public Boolean breakp = false;

    public ObjEntry(String word, SourceLine sl) {
        this.word = word;
        this.sl = sl;
    }

    public String toString() {
        return ("word=" + word + ", sl=" + sl + ", breakp=" + breakp);
    }
}

