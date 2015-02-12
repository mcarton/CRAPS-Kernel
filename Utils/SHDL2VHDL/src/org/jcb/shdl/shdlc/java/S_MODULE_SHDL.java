package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_MODULE_SHDL {
  S_MODULE_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModule att_module;
  SHDLModules att_hmodules;
  SHDLModules att_modules;
  LEX_SHDL att_scanner;
  SHDLModule glob_module;
  private void regle6() throws EGGException {
    //declaration
    S_HEADER_SHDL x_2 = new S_HEADER_SHDL(att_scanner) ;
    S_CMDS_SHDL x_3 = new S_CMDS_SHDL(att_scanner) ;
    S_FOOTER_SHDL x_4 = new S_FOOTER_SHDL(att_scanner) ;
    //appel
    action_create_module_6(x_2, x_3, x_4);
    x_2.analyser() ;
    x_3.analyser() ;
    x_4.analyser() ;
    action_end_module_6(x_2, x_3, x_4);
  }
private void action_end_module_6(S_HEADER_SHDL x_2, S_CMDS_SHDL x_3, S_FOOTER_SHDL x_4) throws EGGException {
// locales
int loc_line;
// instructions
this.att_modules=this.att_hmodules;
loc_line=this.att_scanner.getEndLine();
    glob_module.setEndLine(loc_line);
  }
private void action_create_module_6(S_HEADER_SHDL x_2, S_CMDS_SHDL x_3, S_FOOTER_SHDL x_4) throws EGGException {
// locales
// instructions
glob_module= new SHDLModule();
this.att_module=glob_module;
x_2.att_hmodule=glob_module;
x_3.att_hmodule=glob_module;
  }
  public void analyser () throws EGGException {    regle6 () ;
  }
  }
