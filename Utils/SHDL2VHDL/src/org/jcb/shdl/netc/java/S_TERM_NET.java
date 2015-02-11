package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERM_NET {
  S_TERM_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETTerm att_terme;
  NETTerm att_hterm;
  LEX_NET att_scanner;
  private void regle25() throws EGGException {
    //declaration
    S_SIGNAL__NET x_2 = new S_SIGNAL__NET(att_scanner) ;
    S_TERMX_NET x_4 = new S_TERMX_NET(att_scanner) ;
    //appel
    action_trans_25(x_2, x_4);
    x_2.analyser() ;
    action_add_25(x_2, x_4);
    x_4.analyser() ;
  }
private void action_add_25(S_SIGNAL__NET x_2, S_TERMX_NET x_4) throws EGGException {
// locales
// instructions
    this.att_hterm.addSignalOccurence(x_2.att_signalOccurence);
  }
private void action_trans_25(S_SIGNAL__NET x_2, S_TERMX_NET x_4) throws EGGException {
// locales
// instructions
this.att_terme=this.att_hterm;
x_4.att_hterm=this.att_hterm;
  }
  public void analyser () throws EGGException {    regle25 () ;
  }
  }
