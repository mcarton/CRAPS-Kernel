package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SUBM_SIGNALS_SHDL {
  S_SUBM_SIGNALS_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModuleOccurence att_hmodOccurence;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle21() throws EGGException {
    //declaration
    //appel
  }
  private void regle22() throws EGGException {
    //declaration
    S_SIGNAL_SHDL x_2 = new S_SIGNAL_SHDL(att_scanner) ;
    S_SUBM_SIGNALSX_SHDL x_4 = new S_SUBM_SIGNALSX_SHDL(att_scanner) ;
    //appel
    action_trans_22(x_2, x_4);
    x_2.analyser() ;
    action_set_22(x_2, x_4);
    x_4.analyser() ;
  }
private void action_trans_22(S_SIGNAL_SHDL x_2, S_SUBM_SIGNALSX_SHDL x_4) throws EGGException {
// locales
// instructions
x_2.att_hmodule=this.att_hmodule;
x_4.att_hmodule=this.att_hmodule;
x_4.att_hmodOccurence=this.att_hmodOccurence;
  }
private void action_set_22(S_SIGNAL_SHDL x_2, S_SUBM_SIGNALSX_SHDL x_4) throws EGGException {
// locales
// instructions
    this.att_hmodOccurence.addArgument(x_2.att_signal);
    this.att_hmodule.addModuleSignal(x_2.att_signal);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_parfer : // 35
        regle21 () ;
      break ;
      case LEX_SHDL.token_semicol : // 40
        regle21 () ;
      break ;
      case LEX_SHDL.token_ident : // 48
        regle22 () ;
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle22 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle22 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle22 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
