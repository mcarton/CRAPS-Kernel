package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_PROG_CRAPS {
  S_PROG_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  String [] att_arguments;
  SourceLines att_lines;
  LEX_CRAPS att_scanner;
  Options att_options;
  private void regle0() throws EGGException {
    //declaration
    S_LINES_CRAPS x_2 = new S_LINES_CRAPS(att_scanner) ;
    //appel
    action_init_0(x_2);
    x_2.analyser() ;
  }
private void action_init_0(S_LINES_CRAPS x_2) throws EGGException {
// locales
SourceLines loc_lines;
// instructions
loc_lines= new SourceLines();
x_2.att_hlines=loc_lines;
this.att_lines=loc_lines;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_clr : // 17
        regle0 () ;
      break ;
      case LEX_CRAPS.token_mov : // 18
        regle0 () ;
      break ;
      case LEX_CRAPS.token_inc : // 19
        regle0 () ;
      break ;
      case LEX_CRAPS.token_inccc : // 20
        regle0 () ;
      break ;
      case LEX_CRAPS.token_dec : // 21
        regle0 () ;
      break ;
      case LEX_CRAPS.token_deccc : // 22
        regle0 () ;
      break ;
      case LEX_CRAPS.token_set : // 23
        regle0 () ;
      break ;
      case LEX_CRAPS.token_setq : // 24
        regle0 () ;
      break ;
      case LEX_CRAPS.token_cmp : // 25
        regle0 () ;
      break ;
      case LEX_CRAPS.token_tst : // 26
        regle0 () ;
      break ;
      case LEX_CRAPS.token_negcc : // 27
        regle0 () ;
      break ;
      case LEX_CRAPS.token_notcc : // 28
        regle0 () ;
      break ;
      case LEX_CRAPS.token_nop : // 29
        regle0 () ;
      break ;
      case LEX_CRAPS.token_jmp : // 30
        regle0 () ;
      break ;
      case LEX_CRAPS.token_ret : // 31
        regle0 () ;
      break ;
      case LEX_CRAPS.token_push : // 32
        regle0 () ;
      break ;
      case LEX_CRAPS.token_pop : // 33
        regle0 () ;
      break ;
      case LEX_CRAPS.token_sethi : // 35
        regle0 () ;
      break ;
      case LEX_CRAPS.token_call : // 34
        regle0 () ;
      break ;
      case LEX_CRAPS.token_reti : // 36
        regle0 () ;
      break ;
      case LEX_CRAPS.token_branch : // 84
        regle0 () ;
      break ;
      case LEX_CRAPS.token_codeop3 : // 53
        regle0 () ;
      break ;
      case LEX_CRAPS.token_ld_ldub : // 56
        regle0 () ;
      break ;
      case LEX_CRAPS.token_st_stb : // 59
        regle0 () ;
      break ;
      case LEX_CRAPS.token_org : // 85
        regle0 () ;
      break ;
      case LEX_CRAPS.token_equ : // 86
        regle0 () ;
      break ;
      case LEX_CRAPS.token_global_ : // 87
        regle0 () ;
      break ;
      case LEX_CRAPS.token_byte : // 88
        regle0 () ;
      break ;
      case LEX_CRAPS.token_word : // 89
        regle0 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        regle0 () ;
      break ;
      case LEX_CRAPS.EOF :
        regle0 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
