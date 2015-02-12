package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERMSUMX_SHDL {
  S_TERMSUMX_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModule att_hmodule;
  SHDLTermsSum att_htermsum;
  LEX_SHDL att_scanner;
  private void regle45() throws EGGException {
    //declaration
    S_TERMSUM_SHDL x_3 = new S_TERMSUM_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_or ) ;
    action_trans_45(x_3);
    x_3.analyser() ;
  }
  private void regle44() throws EGGException {
    //declaration
    //appel
  }
private void action_trans_45(S_TERMSUM_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
x_3.att_htermsum=this.att_htermsum;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_semicol : // 40
        regle44 () ;
      break ;
      case LEX_SHDL.token_pv : // 42
        regle44 () ;
      break ;
      case LEX_SHDL.token_or : // 32
        regle45 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
