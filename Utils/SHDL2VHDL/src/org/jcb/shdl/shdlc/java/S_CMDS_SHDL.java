package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMDS_SHDL {
  S_CMDS_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle5() throws EGGException {
    //declaration
    S_CMD_SHDL x_2 = new S_CMD_SHDL(att_scanner) ;
    S_CMDS_SHDL x_3 = new S_CMDS_SHDL(att_scanner) ;
    //appel
    action_trans_5(x_2, x_3);
    x_2.analyser() ;
    x_3.analyser() ;
  }
  private void regle4() throws EGGException {
    //declaration
    //appel
  }
private void action_trans_5(S_CMD_SHDL x_2, S_CMDS_SHDL x_3) throws EGGException {
// locales
// instructions
x_2.att_hmodule=this.att_hmodule;
x_2.att_hsignal=null;
x_3.att_hmodule=this.att_hmodule;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_end_ : // 45
        regle4 () ;
      break ;
      case LEX_SHDL.token_ident : // 48
        regle5 () ;
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle5 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle5 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle5 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
