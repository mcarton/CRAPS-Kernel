package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_DESCR_NET {
  S_DESCR_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  String [] att_arguments;
  LEX_NET att_scanner;
  Options att_options;
  private void regle0() throws EGGException {
    //declaration
    S_HEADER_NET x_1 = new S_HEADER_NET(att_scanner) ;
    S_STATEMENTS_NET x_2 = new S_STATEMENTS_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_module : // 8
        regle0 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
