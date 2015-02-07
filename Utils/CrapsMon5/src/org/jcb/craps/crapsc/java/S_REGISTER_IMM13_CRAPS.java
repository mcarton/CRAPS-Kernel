package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_REGISTER_IMM13_CRAPS {
  S_REGISTER_IMM13_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  Object att_obj;
  LEX_CRAPS att_scanner;
  private void regle47() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_1 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_create_47(x_1);
  }
  private void regle48() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_1 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_create_48(x_1);
  }
private void action_create_47(S_REGISTER_CRAPS x_1) throws EGGException {
// locales
// instructions
this.att_obj=x_1.att_val;
  }
private void action_create_48(S_NUMEXPR_CRAPS x_1) throws EGGException {
// locales
// instructions
this.att_obj=x_1.att_numexpr;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_r : // 90
        regle47 () ;
      break ;
      case LEX_CRAPS.token_fp : // 91
        regle47 () ;
      break ;
      case LEX_CRAPS.token_sp : // 92
        regle47 () ;
      break ;
      case LEX_CRAPS.token_pc : // 93
        regle47 () ;
      break ;
      case LEX_CRAPS.token_lpar : // 99
        regle48 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle48 () ;
      break ;
      case LEX_CRAPS.token_num10 : // 116
        regle48 () ;
      break ;
      case LEX_CRAPS.token_num2 : // 114
        regle48 () ;
      break ;
      case LEX_CRAPS.token_num16 : // 118
        regle48 () ;
      break ;
      case LEX_CRAPS.token_plus_minus : // 105
        regle48 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
