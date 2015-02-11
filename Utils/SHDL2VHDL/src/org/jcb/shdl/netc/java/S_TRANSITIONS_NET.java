package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TRANSITIONS_NET {
  S_TRANSITIONS_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_NET att_scanner;
  private void regle5() throws EGGException {
    //declaration
    S_TRANSITION_NET x_1 = new S_TRANSITION_NET(att_scanner) ;
    S_TRANSITIONS_NET x_2 = new S_TRANSITIONS_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
  }
  public void analyser () throws EGGException {    regle5 () ;
  }
  }
