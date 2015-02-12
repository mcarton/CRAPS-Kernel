package org.jcb.shdl.shdlc.java;
import java.util.*;
import mg.egg.eggc.libjava.*;

public class SHDLMessages extends Vector<Message> implements Messages{
  private static final long serialVersionUID = 1L;
  public Message getMessage(int id) {
    return elementAt(id); 
    }

  public SHDLMessages(){
    super();
    init();
    }
  public static int S_02 = 0;
  public static int S_01 = 1;
  public static int S_00 = 2;
  public static int P_02 = 3;
  private void init(){
    add(new Message(S_02, "end expected near ^1.", 1));
    add(new Message(S_01, "end expected near ^1.", 1));
    add(new Message(S_00, "unexpected symbol ^1 instead of ^2.", 2));
    add(new Message(P_02, ".clk, .rst, .set or .ena expected, instead of ^1.", 1));
    }
  }
