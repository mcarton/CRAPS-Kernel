package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMDS_OR_MODULES_SHDL {
  S_CMDS_OR_MODULES_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModules att_hmodules;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  int glob_beginLine;
  private void regle2() throws EGGException {
    //declaration
    S_MODULE_SHDL x_2 = new S_MODULE_SHDL(att_scanner) ;
    S_CMDS_OR_MODULES_SHDL x_4 = new S_CMDS_OR_MODULES_SHDL(att_scanner) ;
    //appel
    action_trans_2(x_2, x_4);
    x_2.analyser() ;
    action_add_2(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle1() throws EGGException {
    //declaration
    S_CMD_SHDL x_2 = new S_CMD_SHDL(att_scanner) ;
    S_CMDS_OR_MODULES_SHDL x_3 = new S_CMDS_OR_MODULES_SHDL(att_scanner) ;
    //appel
    action_trans_1(x_2, x_3);
    x_2.analyser() ;
    x_3.analyser() ;
  }
  private void regle3() throws EGGException {
    //declaration
    //appel
    action_print_3();
  }
private void action_add_2(S_MODULE_SHDL x_2, S_CMDS_OR_MODULES_SHDL x_4) throws EGGException {
// locales
// instructions
    x_2.att_modules.addModule(x_2.att_module);
    x_2.att_module.setBeginLine(glob_beginLine);
  }
private void action_print_3() throws EGGException {
// locales
// instructions
  }
private void action_trans_2(S_MODULE_SHDL x_2, S_CMDS_OR_MODULES_SHDL x_4) throws EGGException {
// locales
// instructions
x_4.att_hmodules=this.att_hmodules;
x_2.att_hmodules=this.att_hmodules;
x_4.att_hmodule=this.att_hmodule;
glob_beginLine=this.att_scanner.getBeginLine();
  }
private void action_trans_1(S_CMD_SHDL x_2, S_CMDS_OR_MODULES_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodules=this.att_hmodules;
x_2.att_hmodule=this.att_hmodule;
x_2.att_hsignal=null;
x_3.att_hmodule=this.att_hmodule;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_ident : // 48
        regle1 () ;
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle1 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle1 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle1 () ;
      break ;
      case LEX_SHDL.token_module : // 46
        regle2 () ;
      break ;
      case LEX_SHDL.EOF :
        regle3 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
