package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERM_SHDL {
  S_TERM_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLTerm att_terme;
  SHDLTerm att_hterm;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle46() throws EGGException {
    //declaration
    S_SIGNAL__SHDL x_2 = new S_SIGNAL__SHDL(att_scanner) ;
    S_TERMX_SHDL x_4 = new S_TERMX_SHDL(att_scanner) ;
    //appel
    action_trans_46(x_2, x_4);
    x_2.analyser() ;
    action_add_46(x_2, x_4);
    x_4.analyser() ;
  }
private void action_add_46(S_SIGNAL__SHDL x_2, S_TERMX_SHDL x_4) throws EGGException {
// locales
// instructions
    this.att_hterm.addSignalOccurence(x_2.att_signalOccurence);
  }
private void action_trans_46(S_SIGNAL__SHDL x_2, S_TERMX_SHDL x_4) throws EGGException {
// locales
// instructions
x_2.att_hmodule=this.att_hmodule;
x_4.att_hmodule=this.att_hmodule;
this.att_terme=this.att_hterm;
x_4.att_hterm=this.att_hterm;
  }
  public void analyser () throws EGGException {    regle46 () ;
  }
  }
