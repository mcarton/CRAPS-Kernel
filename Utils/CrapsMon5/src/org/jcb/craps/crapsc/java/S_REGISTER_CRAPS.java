package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_REGISTER_CRAPS {
  S_REGISTER_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  String att_val;
  LEX_CRAPS att_scanner;
  private void regle43() throws EGGException {
    //declaration
    T_num10_CRAPS x_2 = new T_num10_CRAPS(att_scanner ) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_r ) ;
    x_2.analyser() ;
    action_create_43(x_2);
  }
  private void regle44() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_fp ) ;
    action_create_44();
  }
  private void regle45() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_sp ) ;
    action_create_45();
  }
  private void regle46() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_pc ) ;
    action_create_46();
  }
private void action_create_45() throws EGGException {
// locales
// instructions
this.att_val="%r29";
  }
private void action_create_46() throws EGGException {
// locales
// instructions
this.att_val="%r30";
  }
private void action_create_43(T_num10_CRAPS x_2) throws EGGException {
// locales
// instructions
this.att_val="%r"+x_2.att_txt;
  }
private void action_create_44() throws EGGException {
// locales
// instructions
this.att_val="%r27";
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_r : // 90
        regle43 () ;
      break ;
      case LEX_CRAPS.token_fp : // 91
        regle44 () ;
      break ;
      case LEX_CRAPS.token_sp : // 92
        regle45 () ;
      break ;
      case LEX_CRAPS.token_pc : // 93
        regle46 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
