package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNAL__NET {
  S_SIGNAL__NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETSignalOccurence att_signalOccurence;
  LEX_NET att_scanner;
  private void regle20() throws EGGException {
    //declaration
    S_SIGNAL_NET x_1 = new S_SIGNAL_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_create_20(x_1);
  }
  private void regle21() throws EGGException {
    //declaration
    S_SIGNAL_NET x_2 = new S_SIGNAL_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_slash ) ;
    x_2.analyser() ;
    action_create_21(x_2);
  }
private void action_create_20(S_SIGNAL_NET x_1) throws EGGException {
// locales
NETSignalOccurence loc_signalOccurence;
// instructions
loc_signalOccurence= new NETSignalOccurence(x_1.att_signal, false);
this.att_signalOccurence=loc_signalOccurence;
  }
private void action_create_21(S_SIGNAL_NET x_2) throws EGGException {
// locales
NETSignalOccurence loc_signalOccurence;
// instructions
loc_signalOccurence= new NETSignalOccurence(x_2.att_signal, true);
this.att_signalOccurence=loc_signalOccurence;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_ident : // 46
        regle20 () ;
      break ;
      case LEX_NET.token_num2 : // 49
        regle20 () ;
      break ;
      case LEX_NET.token_num10 : // 52
        regle20 () ;
      break ;
      case LEX_NET.token_num16 : // 54
        regle20 () ;
      break ;
      case LEX_NET.token_slash : // 42
        regle21 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
