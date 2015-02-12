package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNAL__SHDL {
  S_SIGNAL__SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignalOccurence att_signalOccurence;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle41() throws EGGException {
    //declaration
    S_SIGNAL_SHDL x_2 = new S_SIGNAL_SHDL(att_scanner) ;
    //appel
    action_trans_41(x_2);
    x_2.analyser() ;
    action_create_41(x_2);
  }
  private void regle42() throws EGGException {
    //declaration
    S_SIGNAL_SHDL x_3 = new S_SIGNAL_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_slash ) ;
    action_trans_42(x_3);
    x_3.analyser() ;
    action_create_42(x_3);
  }
private void action_trans_42(S_SIGNAL_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
  }
private void action_trans_41(S_SIGNAL_SHDL x_2) throws EGGException {
// locales
// instructions
x_2.att_hmodule=this.att_hmodule;
  }
private void action_create_41(S_SIGNAL_SHDL x_2) throws EGGException {
// locales
SHDLSignalOccurence loc_signalOccurence;
// instructions
loc_signalOccurence= new SHDLSignalOccurence(x_2.att_signal, false, this.att_hmodule);
this.att_signalOccurence=loc_signalOccurence;
    this.att_hmodule.addModuleSignal(x_2.att_signal);
  }
private void action_create_42(S_SIGNAL_SHDL x_3) throws EGGException {
// locales
SHDLSignalOccurence loc_signalOccurence;
// instructions
loc_signalOccurence= new SHDLSignalOccurence(x_3.att_signal, true, this.att_hmodule);
this.att_signalOccurence=loc_signalOccurence;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_ident : // 48
        regle41 () ;
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle41 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle41 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle41 () ;
      break ;
      case LEX_SHDL.token_slash : // 47
        regle42 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
