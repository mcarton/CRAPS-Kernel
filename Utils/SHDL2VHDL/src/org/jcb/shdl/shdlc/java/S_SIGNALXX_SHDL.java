package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SIGNALXX_SHDL {
  S_SIGNALXX_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignal att_hsignal;
  LEX_SHDL att_scanner;
  private void regle14() throws EGGException {
    //declaration
    T_num10_SHDL x_2 = new T_num10_SHDL(att_scanner ) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_ptpt ) ;
    x_2.analyser() ;
    action_set_14(x_2);
    att_scanner.accepter_sucre(LEX_SHDL.token_crocfer ) ;
  }
  private void regle15() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_crocfer ) ;
  }
private void action_set_14(T_num10_SHDL x_2) throws EGGException {
// locales
// instructions
    this.att_hsignal.setN2(x_2.att_txt);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_ptpt : // 38
        regle14 () ;
      break ;
      case LEX_SHDL.token_crocfer : // 37
        regle15 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
