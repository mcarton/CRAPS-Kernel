package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERMX_SHDL {
  S_TERMX_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLTerm att_hterm;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle48() throws EGGException {
    //declaration
    S_TERM_SHDL x_3 = new S_TERM_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_and ) ;
    action_trans_48(x_3);
    x_3.analyser() ;
  }
  private void regle47() throws EGGException {
    //declaration
    //appel
  }
private void action_trans_48(S_TERM_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
x_3.att_hterm=this.att_hterm;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_or : // 32
        regle47 () ;
      break ;
      case LEX_SHDL.token_semicol : // 40
        regle47 () ;
      break ;
      case LEX_SHDL.token_pv : // 42
        regle47 () ;
      break ;
      case LEX_SHDL.token_and : // 33
        regle48 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
