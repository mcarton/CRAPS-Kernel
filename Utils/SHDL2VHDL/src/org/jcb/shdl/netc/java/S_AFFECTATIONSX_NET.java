package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_AFFECTATIONSX_NET {
  S_AFFECTATIONSX_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETAffectations att_haffectations;
  NETAffectations att_affectations;
  LEX_NET att_scanner;
  private void regle17() throws EGGException {
    //declaration
    //appel
    action_trans_17();
  }
  private void regle18() throws EGGException {
    //declaration
    S_AFFECTATIONS_NET x_3 = new S_AFFECTATIONS_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_virg ) ;
    action_trans_18(x_3);
    x_3.analyser() ;
  }
private void action_trans_17() throws EGGException {
// locales
// instructions
this.att_affectations=this.att_haffectations;
  }
private void action_trans_18(S_AFFECTATIONS_NET x_3) throws EGGException {
// locales
// instructions
this.att_affectations=this.att_haffectations;
x_3.att_haffectations=this.att_haffectations;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_pv : // 40
        regle17 () ;
      break ;
      case LEX_NET.token_virg : // 38
        regle18 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
