package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_STATEMENT_NET {
  S_STATEMENT_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETStatement att_statement;
  LEX_NET att_scanner;
  private void regle10() throws EGGException {
    //declaration
    T_added_NET x_1 = new T_added_NET(att_scanner ) ;
    //appel
    x_1.analyser() ;
    action_create_10(x_1);
  }
  private void regle8() throws EGGException {
    //declaration
    S_TRANSITION_NET x_1 = new S_TRANSITION_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_create_8(x_1);
  }
  private void regle9() throws EGGException {
    //declaration
    S_OUTPUTS_NET x_1 = new S_OUTPUTS_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_create_9(x_1);
  }
private void action_create_10(T_added_NET x_1) throws EGGException {
// instructions
this.att_statement= new NETStatement(x_1.att_txt);
  }
private void action_create_8(S_TRANSITION_NET x_1) throws EGGException {
// locales
NETStatement loc_statement;
// instructions
loc_statement= new NETStatement(x_1.att_transition);
this.att_statement=loc_statement;
  }
private void action_create_9(S_OUTPUTS_NET x_1) throws EGGException {
// locales
NETStatement loc_statement;
// instructions
loc_statement= new NETStatement(x_1.att_outputs);
this.att_statement=loc_statement;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_ident : // 46
        regle8 () ;
      break ;
      case LEX_NET.token_outputs : // 30
        regle9 () ;
      break ;
      case LEX_NET.token_added : // 47
        regle10 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
