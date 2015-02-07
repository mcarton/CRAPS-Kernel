package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_FACTORX_CRAPS {
  S_FACTORX_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  NumExpr att_numexpr;
  LEX_CRAPS att_scanner;
  NumExpr att_hnumexpr;
  private void regle60() throws EGGException {
    //declaration
    T_mult_div_CRAPS x_1 = new T_mult_div_CRAPS(att_scanner ) ;
    S_FACTOR_CRAPS x_2 = new S_FACTOR_CRAPS(att_scanner) ;
    S_FACTORX_CRAPS x_4 = new S_FACTORX_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    action_trans_60(x_1, x_2, x_4);
    x_4.analyser() ;
    action_create_60(x_1, x_2, x_4);
  }
  private void regle61() throws EGGException {
    //declaration
    //appel
    action_create_61();
  }
private void action_create_61() throws EGGException {
// locales
// instructions
this.att_numexpr=this.att_hnumexpr;
  }
private void action_create_60(T_mult_div_CRAPS x_1, S_FACTOR_CRAPS x_2, S_FACTORX_CRAPS x_4) throws EGGException {
// locales
// instructions
this.att_numexpr=x_4.att_numexpr;
  }
private void action_trans_60(T_mult_div_CRAPS x_1, S_FACTOR_CRAPS x_2, S_FACTORX_CRAPS x_4) throws EGGException {
// locales
// instructions
x_4.att_hnumexpr= new NumExprOp2(x_1.att_txt, this.att_hnumexpr, x_2.att_numexpr);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_mult_div : // 106
        regle60 () ;
      break ;
      case LEX_CRAPS.token_plus_minus : // 105
        regle61 () ;
      break ;
      case LEX_CRAPS.token_virg : // 96
        regle61 () ;
      break ;
      case LEX_CRAPS.token_rpar : // 100
        regle61 () ;
      break ;
      case LEX_CRAPS.token_clr : // 17
        regle61 () ;
      break ;
      case LEX_CRAPS.token_mov : // 18
        regle61 () ;
      break ;
      case LEX_CRAPS.token_inc : // 19
        regle61 () ;
      break ;
      case LEX_CRAPS.token_inccc : // 20
        regle61 () ;
      break ;
      case LEX_CRAPS.token_dec : // 21
        regle61 () ;
      break ;
      case LEX_CRAPS.token_deccc : // 22
        regle61 () ;
      break ;
      case LEX_CRAPS.token_set : // 23
        regle61 () ;
      break ;
      case LEX_CRAPS.token_setq : // 24
        regle61 () ;
      break ;
      case LEX_CRAPS.token_cmp : // 25
        regle61 () ;
      break ;
      case LEX_CRAPS.token_tst : // 26
        regle61 () ;
      break ;
      case LEX_CRAPS.token_negcc : // 27
        regle61 () ;
      break ;
      case LEX_CRAPS.token_notcc : // 28
        regle61 () ;
      break ;
      case LEX_CRAPS.token_nop : // 29
        regle61 () ;
      break ;
      case LEX_CRAPS.token_jmp : // 30
        regle61 () ;
      break ;
      case LEX_CRAPS.token_ret : // 31
        regle61 () ;
      break ;
      case LEX_CRAPS.token_push : // 32
        regle61 () ;
      break ;
      case LEX_CRAPS.token_pop : // 33
        regle61 () ;
      break ;
      case LEX_CRAPS.token_sethi : // 35
        regle61 () ;
      break ;
      case LEX_CRAPS.token_call : // 34
        regle61 () ;
      break ;
      case LEX_CRAPS.token_reti : // 36
        regle61 () ;
      break ;
      case LEX_CRAPS.token_branch : // 84
        regle61 () ;
      break ;
      case LEX_CRAPS.token_codeop3 : // 53
        regle61 () ;
      break ;
      case LEX_CRAPS.token_ld_ldub : // 56
        regle61 () ;
      break ;
      case LEX_CRAPS.token_st_stb : // 59
        regle61 () ;
      break ;
      case LEX_CRAPS.token_org : // 85
        regle61 () ;
      break ;
      case LEX_CRAPS.token_equ : // 86
        regle61 () ;
      break ;
      case LEX_CRAPS.token_global_ : // 87
        regle61 () ;
      break ;
      case LEX_CRAPS.token_byte : // 88
        regle61 () ;
      break ;
      case LEX_CRAPS.token_word : // 89
        regle61 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle61 () ;
      break ;
      case LEX_CRAPS.token_rbra : // 98
        regle61 () ;
      break ;
      case LEX_CRAPS.EOF :
        regle61 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
