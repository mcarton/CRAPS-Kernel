package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERM_CRAPS {
  S_TERM_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  NumExpr att_numexpr;
  LEX_CRAPS att_scanner;
  private void regle53() throws EGGException {
    //declaration
    S_FACTOR_CRAPS x_1 = new S_FACTOR_CRAPS(att_scanner) ;
    S_FACTORX_CRAPS x_3 = new S_FACTORX_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_trans_53(x_1, x_3);
    x_3.analyser() ;
    action_create_53(x_1, x_3);
  }
private void action_trans_53(S_FACTOR_CRAPS x_1, S_FACTORX_CRAPS x_3) throws EGGException {
// locales
// instructions
x_3.att_hnumexpr=x_1.att_numexpr;
  }
private void action_create_53(S_FACTOR_CRAPS x_1, S_FACTORX_CRAPS x_3) throws EGGException {
// locales
// instructions
this.att_numexpr=x_3.att_numexpr;
  }
  public void analyser () throws EGGException {    regle53 () ;
  }
  }
