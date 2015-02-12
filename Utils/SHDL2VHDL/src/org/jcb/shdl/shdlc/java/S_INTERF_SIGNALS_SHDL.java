package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_INTERF_SIGNALS_SHDL {
  S_INTERF_SIGNALS_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle17() throws EGGException {
    //declaration
    S_SIGNAL_SHDL x_2 = new S_SIGNAL_SHDL(att_scanner) ;
    S_INTERF_SIGNALSX_SHDL x_4 = new S_INTERF_SIGNALSX_SHDL(att_scanner) ;
    //appel
    action_trans_17(x_2, x_4);
    x_2.analyser() ;
    action_add_17(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle16() throws EGGException {
    //declaration
    //appel
  }
private void action_add_17(S_SIGNAL_SHDL x_2, S_INTERF_SIGNALSX_SHDL x_4) throws EGGException {
// locales
// instructions
    x_2.att_signal.setIsInterface(true);
    this.att_hmodule.addInterfaceSignal(x_2.att_signal);
  }
private void action_trans_17(S_SIGNAL_SHDL x_2, S_INTERF_SIGNALSX_SHDL x_4) throws EGGException {
// locales
// instructions
x_2.att_hmodule=this.att_hmodule;
x_4.att_hmodule=this.att_hmodule;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_parfer : // 35
        regle16 () ;
      break ;
      case LEX_SHDL.token_ident : // 48
        regle17 () ;
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle17 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle17 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle17 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
