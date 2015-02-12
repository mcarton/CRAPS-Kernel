package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_OPT_OUTPUTS_NET {
  S_OPT_OUTPUTS_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETAffectations att_haffectations;
  NETAffectations att_affectations;
  LEX_NET att_scanner;
  private void regle13() throws EGGException {
    //declaration
    S_AFFECTATIONS_NET x_3 = new S_AFFECTATIONS_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_outputs ) ;
    action_trans_13(x_3);
    x_3.analyser() ;
    action_set_13(x_3);
  }
  private void regle12() throws EGGException {
    //declaration
    //appel
    action_trans_12();
  }
private void action_set_13(S_AFFECTATIONS_NET x_3) throws EGGException {
// locales
// instructions
this.att_affectations=x_3.att_affectations;
  }
private void action_trans_13(S_AFFECTATIONS_NET x_3) throws EGGException {
// locales
// instructions
x_3.att_haffectations=this.att_haffectations;
  }
private void action_trans_12() throws EGGException {
// locales
// instructions
this.att_affectations=null;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_pv : // 40
        regle12 () ;
      break ;
      case LEX_NET.token_outputs : // 30
        regle13 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
