package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_MEM_REFXX_CRAPS {
  S_MEM_REFXX_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_CRAPS att_scanner;
  AddrContent att_haddrcontent;
  private void regle33() throws EGGException {
    //declaration
    T_plus_minus_CRAPS x_1 = new T_plus_minus_CRAPS(att_scanner ) ;
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    action_update_33(x_1, x_2);
  }
  private void regle32() throws EGGException {
    //declaration
    //appel
  }
  private void regle34() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_1 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_update_34(x_1);
  }
private void action_update_34(S_NUMEXPR_CRAPS x_1) throws EGGException {
// locales
// instructions
    this.att_haddrcontent.setRs2_or_disp(x_1.att_numexpr);
  }
private void action_update_33(T_plus_minus_CRAPS x_1, S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_haddrcontent.setRs2_or_disp(x_2.att_val);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_rbra : // 98
        regle32 () ;
      break ;
      case LEX_CRAPS.token_plus_minus : // 105
        att_scanner.lit ( 2 ) ;
        switch ( att_scanner.fenetre[1].code ) {
          case LEX_CRAPS.token_r : // 90
            regle33 () ;
          break ;
          case LEX_CRAPS.token_fp : // 91
            regle33 () ;
          break ;
          case LEX_CRAPS.token_sp : // 92
            regle33 () ;
          break ;
          case LEX_CRAPS.token_pc : // 93
            regle33 () ;
          break ;
          case LEX_CRAPS.token_num10 : // 116
            regle34 () ;
          break ;
          case LEX_CRAPS.token_num2 : // 114
            regle34 () ;
          break ;
          case LEX_CRAPS.token_num16 : // 118
            regle34 () ;
          break ;
          default :
            { String as[]={att_scanner.fenetre[1].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
        }
      break ;
      case LEX_CRAPS.token_lpar : // 99
        regle34 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle34 () ;
      break ;
      case LEX_CRAPS.token_num10 : // 116
        regle34 () ;
      break ;
      case LEX_CRAPS.token_num2 : // 114
        regle34 () ;
      break ;
      case LEX_CRAPS.token_num16 : // 118
        regle34 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
