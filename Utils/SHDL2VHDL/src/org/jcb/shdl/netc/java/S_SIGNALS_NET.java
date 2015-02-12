package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNALS_NET {
  S_SIGNALS_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETSignals att_hsignals;
  LEX_NET att_scanner;
  NETSignals att_signals;
  private void regle3() throws EGGException {
    //declaration
    S_SIGNAL_NET x_1 = new S_SIGNAL_NET(att_scanner) ;
    S_SIGNALSX_NET x_3 = new S_SIGNALSX_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_trans_3(x_1, x_3);
    x_3.analyser() ;
    action_add_3(x_1, x_3);
  }
  private void regle2() throws EGGException {
    //declaration
    //appel
    action_trans_2();
  }
private void action_add_3(S_SIGNAL_NET x_1, S_SIGNALSX_NET x_3) throws EGGException {
// locales
// instructions
    this.att_hsignals.addSignal(x_1.att_signal);
  }
private void action_trans_3(S_SIGNAL_NET x_1, S_SIGNALSX_NET x_3) throws EGGException {
// locales
// instructions
this.att_signals=this.att_hsignals;
x_3.att_hsignals=this.att_hsignals;
  }
private void action_trans_2() throws EGGException {
// locales
// instructions
this.att_signals=this.att_hsignals;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_outputs : // 30
        regle2 () ;
      break ;
      case LEX_NET.token_added_outputs : // 31
        regle2 () ;
      break ;
      case LEX_NET.token_ident : // 46
        att_scanner.lit ( 2 ) ;
        switch ( att_scanner.fenetre[1].code ) {
          case LEX_NET.token_arrow : // 35
            regle2 () ;
          break ;
          case LEX_NET.token_crocouv : // 43
            regle3 () ;
          break ;
          case LEX_NET.token_virg : // 38
            regle3 () ;
          break ;
          case LEX_NET.token_outputs : // 30
            regle3 () ;
          break ;
          case LEX_NET.token_added_outputs : // 31
            regle3 () ;
          break ;
          case LEX_NET.token_ident : // 46
            regle3 () ;
          break ;
          case LEX_NET.token_added : // 47
            regle3 () ;
          break ;
          case LEX_NET.EOF :
            regle3 () ;
          break ;
          default :
            { String as[]={att_scanner.fenetre[1].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
        }
      break ;
      case LEX_NET.token_added : // 47
        regle2 () ;
      break ;
      case LEX_NET.EOF :
        regle2 () ;
      break ;
      case LEX_NET.token_num2 : // 49
        regle3 () ;
      break ;
      case LEX_NET.token_num10 : // 52
        regle3 () ;
      break ;
      case LEX_NET.token_num16 : // 54
        regle3 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
