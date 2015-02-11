package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_ISIGNALSX_NET {
  S_ISIGNALSX_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_NET att_scanner;
  private void regle6() throws EGGException {
    //declaration
    //appel
  }
  private void regle7() throws EGGException {
    //declaration
    S_ISIGNALS_NET x_2 = new S_ISIGNALS_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_virg ) ;
    x_2.analyser() ;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_semicol : // 19
        regle6 () ;
      break ;
      case LEX_NET.token_rpar : // 15
        regle6 () ;
      break ;
      case LEX_NET.token_virg : // 16
        regle7 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
