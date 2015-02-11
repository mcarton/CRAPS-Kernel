package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_INTERF_SIGNALSX_SHDL {
  S_INTERF_SIGNALSX_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle20() throws EGGException {
    //declaration
    S_INTERF_SIGNALS_SHDL x_3 = new S_INTERF_SIGNALS_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_semicol ) ;
    action_trans_20(x_3);
    x_3.analyser() ;
  }
  private void regle18() throws EGGException {
    //declaration
    //appel
  }
  private void regle19() throws EGGException {
    //declaration
    S_INTERF_SIGNALS_SHDL x_3 = new S_INTERF_SIGNALS_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_virg ) ;
    action_trans_19(x_3);
    x_3.analyser() ;
  }
private void action_trans_20(S_INTERF_SIGNALS_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
  }
private void action_trans_19(S_INTERF_SIGNALS_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_parfer : // 35
        regle18 () ;
      break ;
      case LEX_SHDL.token_virg : // 41
        regle19 () ;
      break ;
      case LEX_SHDL.token_semicol : // 40
        regle20 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
