package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_STATEMENTS_NET {
  S_STATEMENTS_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETStatements att_statements;
  LEX_NET att_scanner;
  private void regle6() throws EGGException {
    //declaration
    //appel
    action_init_6();
  }
  private void regle7() throws EGGException {
    //declaration
    S_STATEMENT_NET x_1 = new S_STATEMENT_NET(att_scanner) ;
    S_STATEMENTS_NET x_2 = new S_STATEMENTS_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    action_add_7(x_1, x_2);
  }
private void action_init_6() throws EGGException {
// locales
NETStatements loc_statements;
// instructions
loc_statements= new NETStatements();
this.att_statements=loc_statements;
  }
private void action_add_7(S_STATEMENT_NET x_1, S_STATEMENTS_NET x_2) throws EGGException {
// locales
// instructions
    x_2.att_statements.addStatement(x_1.att_statement);
this.att_statements=x_2.att_statements;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.EOF :
        regle6 () ;
      break ;
      case LEX_NET.token_ident : // 46
        regle7 () ;
      break ;
      case LEX_NET.token_outputs : // 30
        regle7 () ;
      break ;
      case LEX_NET.token_added : // 47
        regle7 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
