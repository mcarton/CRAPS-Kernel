package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMD111_SHDL {
  S_CMD111_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLCombinatorialSetting att_hcombinSetting;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle31() throws EGGException {
    //declaration
    S_TERMSUM_SHDL x_2 = new S_TERMSUM_SHDL(att_scanner) ;
    S_CMD1111_SHDL x_3 = new S_CMD1111_SHDL(att_scanner) ;
    //appel
    action_trans_31(x_2, x_3);
    x_2.analyser() ;
    x_3.analyser() ;
    att_scanner.accepter_sucre(LEX_SHDL.token_pv ) ;
  }
private void action_trans_31(S_TERMSUM_SHDL x_2, S_CMD1111_SHDL x_3) throws EGGException {
// locales
SHDLTermsSum loc_termsum;
// instructions
x_2.att_hmodule=this.att_hmodule;
x_3.att_hmodule=this.att_hmodule;
x_3.att_hcombinSetting=this.att_hcombinSetting;
loc_termsum= new SHDLTermsSum(this.att_hmodule);
x_2.att_htermsum=loc_termsum;
    this.att_hcombinSetting.setEquation(loc_termsum);
  }
  public void analyser () throws EGGException {    regle31 () ;
  }
  }
