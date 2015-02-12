package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SEQEQX_SHDL {
  S_SEQEQX_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSequentialSetting att_hseqSetting;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle39() throws EGGException {
    //declaration
    //appel
  }
  private void regle40() throws EGGException {
    //declaration
    S_SIGNAL__SHDL x_3 = new S_SIGNAL__SHDL(att_scanner) ;
    S_SIGNAL__SHDL x_5 = new S_SIGNAL__SHDL(att_scanner) ;
    S_SIGNAL__SHDL x_7 = new S_SIGNAL__SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_and ) ;
    action_trans_40(x_3, x_5, x_7);
    x_3.analyser() ;
    att_scanner.accepter_sucre(LEX_SHDL.token_or ) ;
    x_5.analyser() ;
    att_scanner.accepter_sucre(LEX_SHDL.token_and ) ;
    x_7.analyser() ;
    action_set_40(x_3, x_5, x_7);
  }
private void action_trans_40(S_SIGNAL__SHDL x_3, S_SIGNAL__SHDL x_5, S_SIGNAL__SHDL x_7) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
x_5.att_hmodule=this.att_hmodule;
x_7.att_hmodule=this.att_hmodule;
  }
private void action_set_40(S_SIGNAL__SHDL x_3, S_SIGNAL__SHDL x_5, S_SIGNAL__SHDL x_7) throws EGGException {
// locales
// instructions
    this.att_hseqSetting.setSig2(x_3.att_signalOccurence);
    this.att_hseqSetting.setSig3(x_5.att_signalOccurence);
    this.att_hseqSetting.setSig4(x_7.att_signalOccurence);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_pv : // 42
        regle39 () ;
      break ;
      case LEX_SHDL.token_and : // 33
        regle40 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
