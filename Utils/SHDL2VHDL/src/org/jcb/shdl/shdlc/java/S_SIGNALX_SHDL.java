package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNALX_SHDL {
  S_SIGNALX_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignal att_hsignal;
  LEX_SHDL att_scanner;
  private void regle13() throws EGGException {
    //declaration
    T_num10_SHDL x_2 = new T_num10_SHDL(att_scanner ) ;
    S_SIGNALXX_SHDL x_4 = new S_SIGNALXX_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_crocouv ) ;
    x_2.analyser() ;
    action_set_13(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle12() throws EGGException {
    //declaration
    //appel
  }
private void action_set_13(T_num10_SHDL x_2, S_SIGNALXX_SHDL x_4) throws EGGException {
// locales
// instructions
x_4.att_hsignal=this.att_hsignal;
    this.att_hsignal.setN1(x_2.att_txt);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_virg : // 41
        regle12 () ;
      break ;
      case LEX_SHDL.token_aff : // 43
        regle12 () ;
      break ;
      case LEX_SHDL.token_pt : // 39
        regle12 () ;
      break ;
      case LEX_SHDL.token_affs : // 44
        regle12 () ;
      break ;
      case LEX_SHDL.token_semicol : // 40
        regle12 () ;
      break ;
      case LEX_SHDL.token_parfer : // 35
        regle12 () ;
      break ;
      case LEX_SHDL.token_or : // 32
        regle12 () ;
      break ;
      case LEX_SHDL.token_and : // 33
        regle12 () ;
      break ;
      case LEX_SHDL.token_pv : // 42
        regle12 () ;
      break ;
      case LEX_SHDL.token_crocouv : // 36
        regle13 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
