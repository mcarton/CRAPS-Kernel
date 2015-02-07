package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_LINES_CRAPS {
  S_LINES_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  SourceLines att_hlines;
  LEX_CRAPS att_scanner;
  private void regle2() throws EGGException {
    //declaration
    S_LINE_CRAPS x_2 = new S_LINE_CRAPS(att_scanner) ;
    S_LINES_CRAPS x_4 = new S_LINES_CRAPS(att_scanner) ;
    //appel
    action_trans_2(x_2, x_4);
    x_2.analyser() ;
    action_add_2(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle1() throws EGGException {
    //declaration
    //appel
    action_print_1();
  }
private void action_add_2(S_LINE_CRAPS x_2, S_LINES_CRAPS x_4) throws EGGException {
// locales
// instructions
    this.att_hlines.add(x_2.att_line);
  }
private void action_print_1() throws EGGException {
// locales
// instructions
System.out.print(""+this.att_hlines+"\n");
  }
private void action_trans_2(S_LINE_CRAPS x_2, S_LINES_CRAPS x_4) throws EGGException {
// locales
// instructions
x_4.att_hlines=this.att_hlines;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.EOF :
        regle1 () ;
      break ;
      case LEX_CRAPS.token_clr : // 17
        regle2 () ;
      break ;
      case LEX_CRAPS.token_mov : // 18
        regle2 () ;
      break ;
      case LEX_CRAPS.token_inc : // 19
        regle2 () ;
      break ;
      case LEX_CRAPS.token_inccc : // 20
        regle2 () ;
      break ;
      case LEX_CRAPS.token_dec : // 21
        regle2 () ;
      break ;
      case LEX_CRAPS.token_deccc : // 22
        regle2 () ;
      break ;
      case LEX_CRAPS.token_set : // 23
        regle2 () ;
      break ;
      case LEX_CRAPS.token_setq : // 24
        regle2 () ;
      break ;
      case LEX_CRAPS.token_cmp : // 25
        regle2 () ;
      break ;
      case LEX_CRAPS.token_tst : // 26
        regle2 () ;
      break ;
      case LEX_CRAPS.token_negcc : // 27
        regle2 () ;
      break ;
      case LEX_CRAPS.token_notcc : // 28
        regle2 () ;
      break ;
      case LEX_CRAPS.token_nop : // 29
        regle2 () ;
      break ;
      case LEX_CRAPS.token_jmp : // 30
        regle2 () ;
      break ;
      case LEX_CRAPS.token_ret : // 31
        regle2 () ;
      break ;
      case LEX_CRAPS.token_push : // 32
        regle2 () ;
      break ;
      case LEX_CRAPS.token_pop : // 33
        regle2 () ;
      break ;
      case LEX_CRAPS.token_sethi : // 35
        regle2 () ;
      break ;
      case LEX_CRAPS.token_call : // 34
        regle2 () ;
      break ;
      case LEX_CRAPS.token_reti : // 36
        regle2 () ;
      break ;
      case LEX_CRAPS.token_branch : // 84
        regle2 () ;
      break ;
      case LEX_CRAPS.token_codeop3 : // 53
        regle2 () ;
      break ;
      case LEX_CRAPS.token_ld_ldub : // 56
        regle2 () ;
      break ;
      case LEX_CRAPS.token_st_stb : // 59
        regle2 () ;
      break ;
      case LEX_CRAPS.token_org : // 85
        regle2 () ;
      break ;
      case LEX_CRAPS.token_equ : // 86
        regle2 () ;
      break ;
      case LEX_CRAPS.token_global_ : // 87
        regle2 () ;
      break ;
      case LEX_CRAPS.token_byte : // 88
        regle2 () ;
      break ;
      case LEX_CRAPS.token_word : // 89
        regle2 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle2 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
