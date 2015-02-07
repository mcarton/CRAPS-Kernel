package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_MEM_REFX_CRAPS {
  S_MEM_REFX_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_CRAPS att_scanner;
  AddrContent att_haddrcontent;
  private void regle31() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_1 = new S_REGISTER_CRAPS(att_scanner) ;
    S_MEM_REFXX_CRAPS x_3 = new S_MEM_REFXX_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_trans_and_update_31(x_1, x_3);
    x_3.analyser() ;
  }
  private void regle30() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_1 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_create_30(x_1);
  }
private void action_trans_and_update_31(S_REGISTER_CRAPS x_1, S_MEM_REFXX_CRAPS x_3) throws EGGException {
// locales
// instructions
x_3.att_haddrcontent=this.att_haddrcontent;
    this.att_haddrcontent.setRs1(x_1.att_val);
  }
private void action_create_30(S_NUMEXPR_CRAPS x_1) throws EGGException {
// locales
// instructions
    this.att_haddrcontent.setRs2_or_disp(x_1.att_numexpr);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_lpar : // 99
        regle30 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle30 () ;
      break ;
      case LEX_CRAPS.token_num10 : // 116
        regle30 () ;
      break ;
      case LEX_CRAPS.token_num2 : // 114
        regle30 () ;
      break ;
      case LEX_CRAPS.token_num16 : // 118
        regle30 () ;
      break ;
      case LEX_CRAPS.token_plus_minus : // 105
        regle30 () ;
      break ;
      case LEX_CRAPS.token_r : // 90
        regle31 () ;
      break ;
      case LEX_CRAPS.token_fp : // 91
        regle31 () ;
      break ;
      case LEX_CRAPS.token_sp : // 92
        regle31 () ;
      break ;
      case LEX_CRAPS.token_pc : // 93
        regle31 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
