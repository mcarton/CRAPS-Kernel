package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMD21_SHDL {
  S_CMD21_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignal att_hsignal;
  SHDLModuleOccurence att_hmodOccurence;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle36() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_parfer ) ;
    action_update_36();
    att_scanner.accepter_sucre(LEX_SHDL.token_pv ) ;
  }
  private void regle37() throws EGGException {
    //declaration
    S_SUBM_SIGNALS_SHDL x_3 = new S_SUBM_SIGNALS_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_semicol ) ;
    action_trans_37(x_3);
    x_3.analyser() ;
    att_scanner.accepter_sucre(LEX_SHDL.token_parfer ) ;
    action_update_37(x_3);
    att_scanner.accepter_sucre(LEX_SHDL.token_pv ) ;
  }
private void action_update_37(S_SUBM_SIGNALS_SHDL x_3) throws EGGException {
// locales
// instructions
    this.att_hmodule.addModuleOccurence(this.att_hmodOccurence);
  }
private void action_update_36() throws EGGException {
// locales
// instructions
    this.att_hmodule.addModuleOccurence(this.att_hmodOccurence);
  }
private void action_trans_37(S_SUBM_SIGNALS_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
x_3.att_hmodOccurence=this.att_hmodOccurence;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_parfer : // 35
        regle36 () ;
      break ;
      case LEX_SHDL.token_semicol : // 40
        regle37 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
