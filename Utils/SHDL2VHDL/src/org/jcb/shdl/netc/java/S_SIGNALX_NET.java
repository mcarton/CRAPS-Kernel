package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNALX_NET {
  S_SIGNALX_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETSignal att_hsignal;
  LEX_NET att_scanner;
  private void regle33() throws EGGException {
    //declaration
    T_num10_NET x_2 = new T_num10_NET(att_scanner ) ;
    S_SIGNALXX_NET x_4 = new S_SIGNALXX_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_crocouv ) ;
    x_2.analyser() ;
    action_set_33(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle32() throws EGGException {
    //declaration
    //appel
  }
private void action_set_33(T_num10_NET x_2, S_SIGNALXX_NET x_4) throws EGGException {
// locales
// instructions
x_4.att_hsignal=this.att_hsignal;
    this.att_hsignal.setN1(x_2.att_txt);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_virg : // 38
        regle32 () ;
      break ;
      case LEX_NET.token_egal : // 39
        regle32 () ;
      break ;
      case LEX_NET.token_clock : // 28
        regle32 () ;
      break ;
      case LEX_NET.token_inputs : // 29
        regle32 () ;
      break ;
      case LEX_NET.token_and : // 33
        regle32 () ;
      break ;
      case LEX_NET.token_or : // 34
        regle32 () ;
      break ;
      case LEX_NET.token_outputs : // 30
        regle32 () ;
      break ;
      case LEX_NET.token_added_outputs : // 31
        regle32 () ;
      break ;
      case LEX_NET.token_ident : // 46
        regle32 () ;
      break ;
      case LEX_NET.token_added : // 47
        regle32 () ;
      break ;
      case LEX_NET.EOF :
        regle32 () ;
      break ;
      case LEX_NET.token_pv : // 40
        regle32 () ;
      break ;
      case LEX_NET.token_crocouv : // 43
        regle33 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
