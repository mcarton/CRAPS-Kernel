package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMD2_SHDL {
  S_CMD2_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignal att_hsignal;
  SHDLModuleOccurence att_hmodOccurence;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle35() throws EGGException {
    //declaration
    S_SUBM_SIGNALS_SHDL x_3 = new S_SUBM_SIGNALS_SHDL(att_scanner) ;
    S_CMD21_SHDL x_4 = new S_CMD21_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_parouv ) ;
    action_trans_35(x_3, x_4);
    x_3.analyser() ;
    x_4.analyser() ;
  }
private void action_trans_35(S_SUBM_SIGNALS_SHDL x_3, S_CMD21_SHDL x_4) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
x_4.att_hmodule=this.att_hmodule;
x_4.att_hsignal=this.att_hsignal;
x_3.att_hmodOccurence=this.att_hmodOccurence;
x_4.att_hmodOccurence=this.att_hmodOccurence;
  }
  public void analyser () throws EGGException {    regle35 () ;
  }
  }
