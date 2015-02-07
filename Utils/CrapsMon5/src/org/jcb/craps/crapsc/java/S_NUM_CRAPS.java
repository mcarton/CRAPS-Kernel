package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_NUM_CRAPS {
  S_NUM_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  NumExpr att_numexpr;
  LEX_CRAPS att_scanner;
  private void regle50() throws EGGException {
    //declaration
    T_num2_CRAPS x_1 = new T_num2_CRAPS(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_create_50(x_1);
  }
  private void regle51() throws EGGException {
    //declaration
    T_num16_CRAPS x_1 = new T_num16_CRAPS(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_create_51(x_1);
  }
  private void regle49() throws EGGException {
    //declaration
    T_num10_CRAPS x_1 = new T_num10_CRAPS(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_create_49(x_1);
  }
private void action_create_51(T_num16_CRAPS x_1) throws EGGException {
// locales
// instructions
this.att_numexpr= new NumExprInt(x_1.att_txt, 16);
  }
private void action_create_50(T_num2_CRAPS x_1) throws EGGException {
// locales
// instructions
this.att_numexpr= new NumExprInt(x_1.att_txt, 2);
  }
private void action_create_49(T_num10_CRAPS x_1) throws EGGException {
// locales
// instructions
this.att_numexpr= new NumExprInt(x_1.att_txt, 10);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_num10 : // 116
        regle49 () ;
      break ;
      case LEX_CRAPS.token_num2 : // 114
        regle50 () ;
      break ;
      case LEX_CRAPS.token_num16 : // 118
        regle51 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
