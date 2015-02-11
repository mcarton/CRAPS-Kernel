package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNAL_NET {
  S_SIGNAL_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETSignal att_signal;
  LEX_NET att_scanner;
  private void regle30() throws EGGException {
    //declaration
    T_num10_NET x_1 = new T_num10_NET(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_set_30(x_1);
  }
  private void regle31() throws EGGException {
    //declaration
    T_num16_NET x_1 = new T_num16_NET(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_set_31(x_1);
  }
  private void regle29() throws EGGException {
    //declaration
    T_num2_NET x_1 = new T_num2_NET(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_set_29(x_1);
  }
  private void regle28() throws EGGException {
    //declaration
    T_ident_NET x_1 = new T_ident_NET(att_scanner ) ;
    S_SIGNALX_NET x_3 = new S_SIGNALX_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_init_28(x_1, x_3);
    x_3.analyser() ;
  }
private void action_init_28(T_ident_NET x_1, S_SIGNALX_NET x_3) throws EGGException {
// locales
NETSignal loc_signal;
// instructions
loc_signal= new NETSignal(x_1.att_txt, false);
this.att_signal=loc_signal;
x_3.att_hsignal=loc_signal;
  }
private void action_set_31(T_num16_NET x_1) throws EGGException {
// locales
NETSignal loc_signal;
// instructions
loc_signal= new NETSignal(x_1.att_txt);
this.att_signal=loc_signal;
  }
private void action_set_30(T_num10_NET x_1) throws EGGException {
// locales
NETSignal loc_signal;
// instructions
loc_signal= new NETSignal(x_1.att_txt);
this.att_signal=loc_signal;
  }
private void action_set_29(T_num2_NET x_1) throws EGGException {
// locales
NETSignal loc_signal;
// instructions
loc_signal= new NETSignal(x_1.att_txt);
this.att_signal=loc_signal;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_ident : // 46
        regle28 () ;
      break ;
      case LEX_NET.token_num2 : // 49
        regle29 () ;
      break ;
      case LEX_NET.token_num10 : // 52
        regle30 () ;
      break ;
      case LEX_NET.token_num16 : // 54
        regle31 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
