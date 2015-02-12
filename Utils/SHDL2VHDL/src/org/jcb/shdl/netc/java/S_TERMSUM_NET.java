package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TERMSUM_NET {
  S_TERMSUM_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETTermsSum att_termsum;
  NETTermsSum att_htermsum;
  LEX_NET att_scanner;
  private void regle22() throws EGGException {
    //declaration
    S_TERM_NET x_2 = new S_TERM_NET(att_scanner) ;
    S_TERMSUMX_NET x_3 = new S_TERMSUMX_NET(att_scanner) ;
    //appel
    action_trans_22(x_2, x_3);
    x_2.analyser() ;
    x_3.analyser() ;
  }
private void action_trans_22(S_TERM_NET x_2, S_TERMSUMX_NET x_3) throws EGGException {
// locales
NETTerm loc_term;
// instructions
loc_term= new NETTerm();
x_2.att_hterm=loc_term;
this.att_termsum=this.att_htermsum;
x_3.att_htermsum=this.att_htermsum;
    this.att_htermsum.addTerm(loc_term);
  }
  public void analyser () throws EGGException {    regle22 () ;
  }
  }
