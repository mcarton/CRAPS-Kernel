package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNALSX_NET {
  S_SIGNALSX_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETSignals att_hsignals;
  LEX_NET att_scanner;
  NETSignals att_signals;
  private void regle4() throws EGGException {
    //declaration
    //appel
    action_trans_4();
  }
  private void regle5() throws EGGException {
    //declaration
    S_SIGNALS_NET x_3 = new S_SIGNALS_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_virg ) ;
    action_trans_5(x_3);
    x_3.analyser() ;
  }
private void action_trans_4() throws EGGException {
// locales
// instructions
this.att_signals=this.att_hsignals;
  }
private void action_trans_5(S_SIGNALS_NET x_3) throws EGGException {
// locales
// instructions
this.att_signals=this.att_hsignals;
x_3.att_hsignals=this.att_hsignals;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_outputs : // 30
        regle4 () ;
      break ;
      case LEX_NET.token_added_outputs : // 31
        regle4 () ;
      break ;
      case LEX_NET.token_ident : // 46
        regle4 () ;
      break ;
      case LEX_NET.token_added : // 47
        regle4 () ;
      break ;
      case LEX_NET.EOF :
        regle4 () ;
      break ;
      case LEX_NET.token_virg : // 38
        regle5 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
