package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_TRANSITION_NET {
  S_TRANSITION_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETTransition att_transition;
  LEX_NET att_scanner;
  private void regle11() throws EGGException {
    //declaration
    T_ident_NET x_1 = new T_ident_NET(att_scanner ) ;
    T_ident_NET x_3 = new T_ident_NET(att_scanner ) ;
    S_TERMSUM_NET x_6 = new S_TERMSUM_NET(att_scanner) ;
    S_OPT_OUTPUTS_NET x_7 = new S_OPT_OUTPUTS_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_arrow ) ;
    x_3.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_when ) ;
    action_trans_11(x_1, x_3, x_6, x_7);
    x_6.analyser() ;
    x_7.analyser() ;
    action_create_11(x_1, x_3, x_6, x_7);
    att_scanner.accepter_sucre(LEX_NET.token_pv ) ;
  }
private void action_create_11(T_ident_NET x_1, T_ident_NET x_3, S_TERMSUM_NET x_6, S_OPT_OUTPUTS_NET x_7) throws EGGException {
// locales
NETTransition loc_transition;
// instructions
loc_transition= new NETTransition(x_1.att_txt, x_3.att_txt, x_6.att_termsum, x_7.att_affectations);
this.att_transition=loc_transition;
  }
private void action_trans_11(T_ident_NET x_1, T_ident_NET x_3, S_TERMSUM_NET x_6, S_OPT_OUTPUTS_NET x_7) throws EGGException {
// locales
NETTermsSum loc_termsum;
NETAffectations loc_affectations;
// instructions
loc_termsum= new NETTermsSum();
x_6.att_htermsum=loc_termsum;
loc_affectations= new NETAffectations();
x_7.att_haffectations=loc_affectations;
  }
  public void analyser () throws EGGException {    regle11 () ;
  }
  }
