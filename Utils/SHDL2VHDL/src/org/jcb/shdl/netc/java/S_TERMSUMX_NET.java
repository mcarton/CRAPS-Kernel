package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERMSUMX_NET {
  S_TERMSUMX_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETTermsSum att_htermsum;
  LEX_NET att_scanner;
  private void regle23() throws EGGException {
    //declaration
    //appel
  }
  private void regle24() throws EGGException {
    //declaration
    S_TERMSUM_NET x_3 = new S_TERMSUM_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_or ) ;
    action_trans_24(x_3);
    x_3.analyser() ;
  }
private void action_trans_24(S_TERMSUM_NET x_3) throws EGGException {
// locales
// instructions
x_3.att_htermsum=this.att_htermsum;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_outputs : // 30
        regle23 () ;
      break ;
      case LEX_NET.token_pv : // 40
        regle23 () ;
      break ;
      case LEX_NET.token_virg : // 38
        regle23 () ;
      break ;
      case LEX_NET.token_or : // 34
        regle24 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
