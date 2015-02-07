package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_NUMEXPR_CRAPS {
  S_NUMEXPR_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  NumExpr att_numexpr;
  LEX_CRAPS att_scanner;
  private void regle52() throws EGGException {
    //declaration
    S_TERM_CRAPS x_1 = new S_TERM_CRAPS(att_scanner) ;
    S_TERMX_CRAPS x_3 = new S_TERMX_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_trans_52(x_1, x_3);
    x_3.analyser() ;
    action_create_52(x_1, x_3);
  }
private void action_trans_52(S_TERM_CRAPS x_1, S_TERMX_CRAPS x_3) throws EGGException {
// locales
// instructions
x_3.att_hnumexpr=x_1.att_numexpr;
  }
private void action_create_52(S_TERM_CRAPS x_1, S_TERMX_CRAPS x_3) throws EGGException {
// locales
// instructions
this.att_numexpr=x_3.att_numexpr;
  }
  public void analyser () throws EGGException {    regle52 () ;
  }
  }
