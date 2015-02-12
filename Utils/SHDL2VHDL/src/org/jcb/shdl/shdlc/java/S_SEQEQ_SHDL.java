package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_SEQEQ_SHDL {
  S_SEQEQ_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSequentialSetting att_hseqSetting;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle38() throws EGGException {
    //declaration
    S_SIGNAL__SHDL x_2 = new S_SIGNAL__SHDL(att_scanner) ;
    S_SEQEQX_SHDL x_4 = new S_SEQEQX_SHDL(att_scanner) ;
    //appel
    action_trans_38(x_2, x_4);
    x_2.analyser() ;
    action_trans2_38(x_2, x_4);
    x_4.analyser() ;
  }
private void action_trans2_38(S_SIGNAL__SHDL x_2, S_SEQEQX_SHDL x_4) throws EGGException {
// locales
// instructions
x_4.att_hmodule=this.att_hmodule;
    this.att_hseqSetting.setSig1(x_2.att_signalOccurence);
  }
private void action_trans_38(S_SIGNAL__SHDL x_2, S_SEQEQX_SHDL x_4) throws EGGException {
// locales
// instructions
x_2.att_hmodule=this.att_hmodule;
x_4.att_hseqSetting=this.att_hseqSetting;
  }
  public void analyser () throws EGGException {    regle38 () ;
  }
  }
