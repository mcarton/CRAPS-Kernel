package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_HEADER_SHDL {
  S_HEADER_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle7() throws EGGException {
    //declaration
    T_ident_SHDL x_2 = new T_ident_SHDL(att_scanner ) ;
    S_INTERF_SIGNALS_SHDL x_6 = new S_INTERF_SIGNALS_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_module ) ;
    x_2.analyser() ;
    action_add_name_7(x_2, x_6);
    att_scanner.accepter_sucre(LEX_SHDL.token_parouv ) ;
    action_trans1_7(x_2, x_6);
    x_6.analyser() ;
    att_scanner.accepter_sucre(LEX_SHDL.token_parfer ) ;
  }
private void action_trans1_7(T_ident_SHDL x_2, S_INTERF_SIGNALS_SHDL x_6) throws EGGException {
// locales
// instructions
x_6.att_hmodule=this.att_hmodule;
  }
private void action_add_name_7(T_ident_SHDL x_2, S_INTERF_SIGNALS_SHDL x_6) throws EGGException {
// locales
// instructions
    this.att_hmodule.setName(x_2.att_txt);
  }
  public void analyser () throws EGGException {    regle7 () ;
  }
  }
