package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_PROG_SHDL {
  S_PROG_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  String [] att_arguments;
  SHDLModules att_modules;
  LEX_SHDL att_scanner;
  Options att_options;
  private void regle0() throws EGGException {
    //declaration
    S_CMDS_OR_MODULES_SHDL x_2 = new S_CMDS_OR_MODULES_SHDL(att_scanner) ;
    //appel
    action_init_0(x_2);
    x_2.analyser() ;
  }
private void action_init_0(S_CMDS_OR_MODULES_SHDL x_2) throws EGGException {
// locales
SHDLModules loc_modules;
SHDLModule loc_mainModule;
// instructions
loc_modules= new SHDLModules();
x_2.att_hmodules=loc_modules;
this.att_modules=loc_modules;
loc_mainModule= new SHDLModule();
    loc_modules.addModule(loc_mainModule);
x_2.att_hmodule=loc_mainModule;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_ident : // 48
        regle0 () ;
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle0 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle0 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle0 () ;
      break ;
      case LEX_SHDL.token_module : // 46
        regle0 () ;
      break ;
      case LEX_SHDL.EOF :
        regle0 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
