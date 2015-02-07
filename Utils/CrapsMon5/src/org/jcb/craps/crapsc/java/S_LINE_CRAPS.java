package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_LINE_CRAPS {
  S_LINE_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  SourceLine att_line;
  LEX_CRAPS att_scanner;
  private void regle3() throws EGGException {
    //declaration
    S_INSTR_BODY_CRAPS x_2 = new S_INSTR_BODY_CRAPS(att_scanner) ;
    //appel
    action_create_3(x_2);
    x_2.analyser() ;
  }
  private void regle38() throws EGGException {
    //declaration
    T_ident_CRAPS x_1 = new T_ident_CRAPS(att_scanner ) ;
    S_NUMEXPR_CRAPS x_3 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_equals ) ;
    x_3.analyser() ;
    action_create_38(x_1, x_3);
  }
  private void regle4() throws EGGException {
    //declaration
    T_ident_CRAPS x_1 = new T_ident_CRAPS(att_scanner ) ;
    S_INSTR_BODY_CRAPS x_4 = new S_INSTR_BODY_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_semicol ) ;
    action_create_4(x_1, x_4);
    x_4.analyser() ;
  }
private void action_create_38(T_ident_CRAPS x_1, S_NUMEXPR_CRAPS x_3) throws EGGException {
// locales
SourceLine loc_line;
CrapsDirecEqu loc_instr;
// instructions
loc_line= new SourceLine();
this.att_line=loc_line;
    loc_line.setLineno(this.att_scanner.getBeginLine());
    loc_line.setLabel(x_1.att_txt);
loc_instr= new CrapsDirecEqu(x_3.att_numexpr);
    loc_line.setInstr(loc_instr);
  }
private void action_create_4(T_ident_CRAPS x_1, S_INSTR_BODY_CRAPS x_4) throws EGGException {
// locales
SourceLine loc_line;
// instructions
loc_line= new SourceLine();
this.att_line=loc_line;
    loc_line.setLineno(this.att_scanner.getBeginLine());
    loc_line.setLabel(x_1.att_txt);
x_4.att_hline=loc_line;
  }
private void action_create_3(S_INSTR_BODY_CRAPS x_2) throws EGGException {
// locales
SourceLine loc_line;
// instructions
loc_line= new SourceLine();
this.att_line=loc_line;
    loc_line.setLineno(this.att_scanner.getBeginLine());
x_2.att_hline=loc_line;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_clr : // 17
        regle3 () ;
      break ;
      case LEX_CRAPS.token_mov : // 18
        regle3 () ;
      break ;
      case LEX_CRAPS.token_inc : // 19
        regle3 () ;
      break ;
      case LEX_CRAPS.token_inccc : // 20
        regle3 () ;
      break ;
      case LEX_CRAPS.token_dec : // 21
        regle3 () ;
      break ;
      case LEX_CRAPS.token_deccc : // 22
        regle3 () ;
      break ;
      case LEX_CRAPS.token_set : // 23
        regle3 () ;
      break ;
      case LEX_CRAPS.token_setq : // 24
        regle3 () ;
      break ;
      case LEX_CRAPS.token_cmp : // 25
        regle3 () ;
      break ;
      case LEX_CRAPS.token_tst : // 26
        regle3 () ;
      break ;
      case LEX_CRAPS.token_negcc : // 27
        regle3 () ;
      break ;
      case LEX_CRAPS.token_notcc : // 28
        regle3 () ;
      break ;
      case LEX_CRAPS.token_nop : // 29
        regle3 () ;
      break ;
      case LEX_CRAPS.token_jmp : // 30
        regle3 () ;
      break ;
      case LEX_CRAPS.token_ret : // 31
        regle3 () ;
      break ;
      case LEX_CRAPS.token_push : // 32
        regle3 () ;
      break ;
      case LEX_CRAPS.token_pop : // 33
        regle3 () ;
      break ;
      case LEX_CRAPS.token_sethi : // 35
        regle3 () ;
      break ;
      case LEX_CRAPS.token_call : // 34
        regle3 () ;
      break ;
      case LEX_CRAPS.token_reti : // 36
        regle3 () ;
      break ;
      case LEX_CRAPS.token_branch : // 84
        regle3 () ;
      break ;
      case LEX_CRAPS.token_codeop3 : // 53
        regle3 () ;
      break ;
      case LEX_CRAPS.token_ld_ldub : // 56
        regle3 () ;
      break ;
      case LEX_CRAPS.token_st_stb : // 59
        regle3 () ;
      break ;
      case LEX_CRAPS.token_org : // 85
        regle3 () ;
      break ;
      case LEX_CRAPS.token_equ : // 86
        regle3 () ;
      break ;
      case LEX_CRAPS.token_global_ : // 87
        regle3 () ;
      break ;
      case LEX_CRAPS.token_byte : // 88
        regle3 () ;
      break ;
      case LEX_CRAPS.token_word : // 89
        regle3 () ;
      break ;
      case LEX_CRAPS.token_ident : // 112
        att_scanner.lit ( 2 ) ;
        switch ( att_scanner.fenetre[1].code ) {
          case LEX_CRAPS.token_semicol : // 95
            regle4 () ;
          break ;
          case LEX_CRAPS.token_equals : // 94
            regle38 () ;
          break ;
          default :
            { String as[]={att_scanner.fenetre[1].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
        }
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
