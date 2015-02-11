package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERMSUM_SHDL {
  S_TERMSUM_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLTermsSum att_termsum;
  SHDLModule att_hmodule;
  SHDLTermsSum att_htermsum;
  LEX_SHDL att_scanner;
  private void regle43() throws EGGException {
    //declaration
    S_TERM_SHDL x_2 = new S_TERM_SHDL(att_scanner) ;
    S_TERMSUMX_SHDL x_3 = new S_TERMSUMX_SHDL(att_scanner) ;
    //appel
    action_trans_43(x_2, x_3);
    x_2.analyser() ;
    x_3.analyser() ;
  }
private void action_trans_43(S_TERM_SHDL x_2, S_TERMSUMX_SHDL x_3) throws EGGException {
// locales
SHDLTerm loc_term;
// instructions
x_2.att_hmodule=this.att_hmodule;
x_3.att_hmodule=this.att_hmodule;
loc_term= new SHDLTerm(this.att_hmodule);
x_2.att_hterm=loc_term;
this.att_termsum=this.att_htermsum;
x_3.att_htermsum=this.att_htermsum;
    this.att_htermsum.addTerm(loc_term);
  }
  public void analyser () throws EGGException {    regle43 () ;
  }
  }
