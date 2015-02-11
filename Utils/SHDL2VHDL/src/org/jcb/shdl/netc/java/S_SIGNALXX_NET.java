package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNALXX_NET {
  S_SIGNALXX_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETSignal att_hsignal;
  LEX_NET att_scanner;
  private void regle34() throws EGGException {
    //declaration
    T_num10_NET x_2 = new T_num10_NET(att_scanner ) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_ptpt ) ;
    x_2.analyser() ;
    action_set_34(x_2);
    att_scanner.accepter_sucre(LEX_NET.token_crocfer ) ;
  }
  private void regle35() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_crocfer ) ;
  }
private void action_set_34(T_num10_NET x_2) throws EGGException {
// locales
// instructions
    this.att_hsignal.setN2(x_2.att_txt);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_ptpt : // 45
        regle34 () ;
      break ;
      case LEX_NET.token_crocfer : // 44
        regle35 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
