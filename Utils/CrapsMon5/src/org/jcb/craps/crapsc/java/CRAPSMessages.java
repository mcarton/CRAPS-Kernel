package org.jcb.craps.crapsc.java;
import java.util.*;
import mg.egg.eggc.libjava.*;

public class CRAPSMessages extends Vector<Message> implements Messages{
  private static final long serialVersionUID = 1L;
  public Message getMessage(int id) {
    return elementAt(id); 
    }

  public CRAPSMessages(){
    super();
    init();
    }
  public static int S_02 = 0;
  public static int S_01 = 1;
  public static int S_00 = 2;
  public static int P_00 = 3;
  private void init(){
    add(new Message(S_02, "end expected near ^1.", 1));
    add(new Message(S_01, "end expected near ^1.", 1));
    add(new Message(S_00, "unexpected symbol ^1 instead of ^2.", 2));
    add(new Message(P_00, "'+' expected instead of '-'.", 0));
    }
  }
