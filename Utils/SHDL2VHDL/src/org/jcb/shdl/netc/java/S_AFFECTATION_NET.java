package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_AFFECTATION_NET {
  S_AFFECTATION_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETAffectation att_affectation;
  LEX_NET att_scanner;
  private void regle19() throws EGGException {
    //declaration
    S_SIGNAL_NET x_1 = new S_SIGNAL_NET(att_scanner) ;
    S_TERMSUM_NET x_4 = new S_TERMSUM_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_egal ) ;
    action_trans_19(x_1, x_4);
    x_4.analyser() ;
    action_create_19(x_1, x_4);
  }
private void action_create_19(S_SIGNAL_NET x_1, S_TERMSUM_NET x_4) throws EGGException {
// locales
NETAffectation loc_aff;
// instructions
loc_aff= new NETAffectation(x_1.att_signal, x_4.att_termsum);
this.att_affectation=loc_aff;
  }
private void action_trans_19(S_SIGNAL_NET x_1, S_TERMSUM_NET x_4) throws EGGException {
// locales
NETTermsSum loc_termsum;
// instructions
loc_termsum= new NETTermsSum();
x_4.att_htermsum=loc_termsum;
  }
  public void analyser () throws EGGException {    regle19 () ;
  }
  }
