package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_AFFECTATIONS_NET {
  S_AFFECTATIONS_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETAffectations att_haffectations;
  NETAffectations att_affectations;
  LEX_NET att_scanner;
  private void regle16() throws EGGException {
    //declaration
    S_AFFECTATION_NET x_1 = new S_AFFECTATION_NET(att_scanner) ;
    S_AFFECTATIONSX_NET x_3 = new S_AFFECTATIONSX_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_trans_16(x_1, x_3);
    x_3.analyser() ;
    action_add_16(x_1, x_3);
  }
  private void regle15() throws EGGException {
    //declaration
    //appel
    action_trans_15();
  }
private void action_add_16(S_AFFECTATION_NET x_1, S_AFFECTATIONSX_NET x_3) throws EGGException {
// locales
// instructions
    this.att_haffectations.addAffectation(x_1.att_affectation);
  }
private void action_trans_16(S_AFFECTATION_NET x_1, S_AFFECTATIONSX_NET x_3) throws EGGException {
// locales
// instructions
this.att_affectations=this.att_haffectations;
x_3.att_haffectations=this.att_haffectations;
  }
private void action_trans_15() throws EGGException {
// locales
// instructions
this.att_affectations=this.att_haffectations;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_pv : // 40
        regle15 () ;
      break ;
      case LEX_NET.token_ident : // 46
        regle16 () ;
      break ;
      case LEX_NET.token_num2 : // 49
        regle16 () ;
      break ;
      case LEX_NET.token_num10 : // 52
        regle16 () ;
      break ;
      case LEX_NET.token_num16 : // 54
        regle16 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
