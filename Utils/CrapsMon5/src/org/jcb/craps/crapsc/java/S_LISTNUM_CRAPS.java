package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_LISTNUM_CRAPS {
  S_LISTNUM_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  NumExprList att_vals;
  LEX_CRAPS att_scanner;
  private void regle41() throws EGGException {
    //declaration
    //appel
  }
  private void regle42() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    S_LISTNUM_CRAPS x_4 = new S_LISTNUM_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_2.analyser() ;
    action_add_42(x_2, x_4);
    x_4.analyser() ;
  }
private void action_add_42(S_NUMEXPR_CRAPS x_2, S_LISTNUM_CRAPS x_4) throws EGGException {
// locales
// instructions
x_4.att_vals=this.att_vals;
    this.att_vals.add(x_2.att_numexpr);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_clr : // 17
        regle41 () ;
      break ;
      case LEX_CRAPS.token_mov : // 18
        regle41 () ;
      break ;
      case LEX_CRAPS.token_inc : // 19
        regle41 () ;
      break ;
      case LEX_CRAPS.token_inccc : // 20
        regle41 () ;
      break ;
      case LEX_CRAPS.token_dec : // 21
        regle41 () ;
      break ;
      case LEX_CRAPS.token_deccc : // 22
        regle41 () ;
      break ;
      case LEX_CRAPS.token_set : // 23
        regle41 () ;
      break ;
      case LEX_CRAPS.token_setq : // 24
        regle41 () ;
      break ;
      case LEX_CRAPS.token_cmp : // 25
        regle41 () ;
      break ;
      case LEX_CRAPS.token_tst : // 26
        regle41 () ;
      break ;
      case LEX_CRAPS.token_negcc : // 27
        regle41 () ;
      break ;
      case LEX_CRAPS.token_notcc : // 28
        regle41 () ;
      break ;
      case LEX_CRAPS.token_nop : // 29
        regle41 () ;
      break ;
      case LEX_CRAPS.token_jmp : // 30
        regle41 () ;
      break ;
      case LEX_CRAPS.token_ret : // 31
        regle41 () ;
      break ;
      case LEX_CRAPS.token_push : // 32
        regle41 () ;
      break ;
      case LEX_CRAPS.token_pop : // 33
        regle41 () ;
      break ;
      case LEX_CRAPS.token_sethi : // 35
        regle41 () ;
      break ;
      case LEX_CRAPS.token_call : // 34
        regle41 () ;
      break ;
      case LEX_CRAPS.token_reti : // 36
        regle41 () ;
      break ;
      case LEX_CRAPS.token_branch : // 84
        regle41 () ;
      break ;
      case LEX_CRAPS.token_codeop3 : // 53
        regle41 () ;
      break ;
      case LEX_CRAPS.token_ld_ldub : // 56
        regle41 () ;
      break ;
      case LEX_CRAPS.token_st_stb : // 59
        regle41 () ;
      break ;
      case LEX_CRAPS.token_org : // 85
        regle41 () ;
      break ;
      case LEX_CRAPS.token_equ : // 86
        regle41 () ;
      break ;
      case LEX_CRAPS.token_global_ : // 87
        regle41 () ;
      break ;
      case LEX_CRAPS.token_byte : // 88
        regle41 () ;
      break ;
      case LEX_CRAPS.token_word : // 89
        regle41 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle41 () ;
      break ;
      case LEX_CRAPS.EOF :
        regle41 () ;
      break ;
      case LEX_CRAPS.token_virg : // 96
        regle42 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
