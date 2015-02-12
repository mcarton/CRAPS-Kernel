package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_ISIGNAL_NET {
  S_ISIGNAL_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_NET att_scanner;
  private void regle3() throws EGGException {
    //declaration
    T_ident_NET x_2 = new T_ident_NET(att_scanner ) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_slash ) ;
    x_2.analyser() ;
  }
  private void regle2() throws EGGException {
    //declaration
    T_ident_NET x_1 = new T_ident_NET(att_scanner ) ;
    //appel
    x_1.analyser() ;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_ident : // 24
        regle2 () ;
      break ;
      case LEX_NET.token_slash : // 20
        regle3 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
