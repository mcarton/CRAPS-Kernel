package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERMX_NET {
  S_TERMX_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETTerm att_hterm;
  LEX_NET att_scanner;
  private void regle27() throws EGGException {
    //declaration
    S_TERM_NET x_3 = new S_TERM_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_and ) ;
    action_trans_27(x_3);
    x_3.analyser() ;
  }
  private void regle26() throws EGGException {
    //declaration
    //appel
  }
private void action_trans_27(S_TERM_NET x_3) throws EGGException {
// locales
// instructions
x_3.att_hterm=this.att_hterm;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_or : // 34
        regle26 () ;
      break ;
      case LEX_NET.token_outputs : // 30
        regle26 () ;
      break ;
      case LEX_NET.token_pv : // 40
        regle26 () ;
      break ;
      case LEX_NET.token_virg : // 38
        regle26 () ;
      break ;
      case LEX_NET.token_and : // 33
        regle27 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
