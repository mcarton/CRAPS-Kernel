package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_FACTOR_CRAPS {
  S_FACTOR_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  NumExpr att_numexpr;
  LEX_CRAPS att_scanner;
  private void regle56() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_lpar ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_rpar ) ;
    action_create_56(x_2);
  }
  private void regle57() throws EGGException {
    //declaration
    T_ident_CRAPS x_1 = new T_ident_CRAPS(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_create_57(x_1);
  }
  private void regle58() throws EGGException {
    //declaration
    S_NUM_CRAPS x_1 = new S_NUM_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_create_58(x_1);
  }
  private void regle59() throws EGGException {
    //declaration
    T_plus_minus_CRAPS x_1 = new T_plus_minus_CRAPS(att_scanner ) ;
    S_NUM_CRAPS x_2 = new S_NUM_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    action_create_59(x_1, x_2);
  }
private void action_create_58(S_NUM_CRAPS x_1) throws EGGException {
// locales
// instructions
this.att_numexpr=x_1.att_numexpr;
  }
private void action_create_59(T_plus_minus_CRAPS x_1, S_NUM_CRAPS x_2) throws EGGException {
// locales
// instructions
this.att_numexpr= new NumExprOp1(x_1.att_txt, x_2.att_numexpr);
  }
private void action_create_56(S_NUMEXPR_CRAPS x_2) throws EGGException {
// locales
// instructions
this.att_numexpr=x_2.att_numexpr;
  }
private void action_create_57(T_ident_CRAPS x_1) throws EGGException {
// locales
// instructions
this.att_numexpr= new NumExprVar(x_1.att_txt);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_lpar : // 99
        regle56 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle57 () ;
      break ;
      case LEX_CRAPS.token_num10 : // 116
        regle58 () ;
      break ;
      case LEX_CRAPS.token_num2 : // 114
        regle58 () ;
      break ;
      case LEX_CRAPS.token_num16 : // 118
        regle58 () ;
      break ;
      case LEX_CRAPS.token_plus_minus : // 105
        regle59 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
