package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNAL_SHDL {
  S_SIGNAL_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignal att_signal;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle10() throws EGGException {
    //declaration
    T_num10_SHDL x_1 = new T_num10_SHDL(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_set_10(x_1);
  }
  private void regle11() throws EGGException {
    //declaration
    T_num16_SHDL x_1 = new T_num16_SHDL(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_set_11(x_1);
  }
  private void regle9() throws EGGException {
    //declaration
    T_num2_SHDL x_1 = new T_num2_SHDL(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_set_9(x_1);
  }
  private void regle8() throws EGGException {
    //declaration
    T_ident_SHDL x_1 = new T_ident_SHDL(att_scanner ) ;
    S_SIGNALX_SHDL x_3 = new S_SIGNALX_SHDL(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_init_8(x_1, x_3);
    x_3.analyser() ;
  }
private void action_set_11(T_num16_SHDL x_1) throws EGGException {
// locales
SHDLSignal loc_signal;
// instructions
loc_signal= new SHDLSignal(x_1.att_txt, this.att_hmodule);
this.att_signal=loc_signal;
  }
private void action_set_10(T_num10_SHDL x_1) throws EGGException {
// locales
SHDLSignal loc_signal;
// instructions
loc_signal= new SHDLSignal(x_1.att_txt, this.att_hmodule);
this.att_signal=loc_signal;
  }
private void action_set_9(T_num2_SHDL x_1) throws EGGException {
// locales
SHDLSignal loc_signal;
// instructions
loc_signal= new SHDLSignal(x_1.att_txt, this.att_hmodule);
this.att_signal=loc_signal;
  }
private void action_init_8(T_ident_SHDL x_1, S_SIGNALX_SHDL x_3) throws EGGException {
// locales
SHDLSignal loc_signal;
// instructions
loc_signal= new SHDLSignal(x_1.att_txt, false, this.att_hmodule);
this.att_signal=loc_signal;
x_3.att_hsignal=loc_signal;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_ident : // 48
        regle8 () ;
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle9 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle10 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle11 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
