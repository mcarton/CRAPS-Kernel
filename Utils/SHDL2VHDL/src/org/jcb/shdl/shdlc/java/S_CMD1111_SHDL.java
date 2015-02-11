package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMD1111_SHDL {
  S_CMD1111_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLCombinatorialSetting att_hcombinSetting;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle33() throws EGGException {
    //declaration
    S_SIGNAL__SHDL x_3 = new S_SIGNAL__SHDL(att_scanner) ;
    //appel
    action_trans_33(x_3);
    att_scanner.accepter_sucre(LEX_SHDL.token_semicol ) ;
    x_3.analyser() ;
    action_set_33(x_3);
  }
  private void regle32() throws EGGException {
    //declaration
    //appel
  }
private void action_set_33(S_SIGNAL__SHDL x_3) throws EGGException {
// locales
// instructions
    this.att_hcombinSetting.setOE(x_3.att_signalOccurence);
  }
private void action_trans_33(S_SIGNAL__SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_pv : // 42
        regle32 () ;
      break ;
      case LEX_SHDL.token_semicol : // 40
        regle33 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
