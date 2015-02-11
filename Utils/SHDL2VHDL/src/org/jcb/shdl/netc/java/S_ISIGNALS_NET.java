package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_ISIGNALS_NET {
  S_ISIGNALS_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_NET att_scanner;
  private void regle4() throws EGGException {
    //declaration
    //appel
  }
  private void regle5() throws EGGException {
    //declaration
    S_ISIGNAL_NET x_1 = new S_ISIGNAL_NET(att_scanner) ;
    S_ISIGNALSX_NET x_2 = new S_ISIGNALSX_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_semicol : // 19
        regle4 () ;
      break ;
      case LEX_NET.token_rpar : // 15
        regle4 () ;
      break ;
      case LEX_NET.token_ident : // 24
        regle5 () ;
      break ;
      case LEX_NET.token_slash : // 20
        regle5 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
