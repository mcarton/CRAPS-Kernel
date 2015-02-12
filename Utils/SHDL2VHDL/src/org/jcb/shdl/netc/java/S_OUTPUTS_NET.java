package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_OUTPUTS_NET {
  S_OUTPUTS_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETMooreOutputs att_outputs;
  LEX_NET att_scanner;
  private void regle14() throws EGGException {
    //declaration
    T_ident_NET x_2 = new T_ident_NET(att_scanner ) ;
    S_AFFECTATIONS_NET x_4 = new S_AFFECTATIONS_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_outputs ) ;
    x_2.analyser() ;
    action_trans_14(x_2, x_4);
    x_4.analyser() ;
    action_create_14(x_2, x_4);
    att_scanner.accepter_sucre(LEX_NET.token_pv ) ;
  }
private void action_create_14(T_ident_NET x_2, S_AFFECTATIONS_NET x_4) throws EGGException {
// locales
NETMooreOutputs loc_mooreOutputs;
// instructions
loc_mooreOutputs= new NETMooreOutputs(x_2.att_txt, x_4.att_affectations);
this.att_outputs=loc_mooreOutputs;
  }
private void action_trans_14(T_ident_NET x_2, S_AFFECTATIONS_NET x_4) throws EGGException {
// locales
NETAffectations loc_affectations;
// instructions
loc_affectations= new NETAffectations();
x_4.att_haffectations=loc_affectations;
  }
  public void analyser () throws EGGException {    regle14 () ;
  }
  }
