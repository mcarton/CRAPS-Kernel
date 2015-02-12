package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_DIAGRAM_NET {
  S_DIAGRAM_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  String [] att_arguments;
  NETStateDiagram att_diagram;
  LEX_NET att_scanner;
  Options att_options;
  private void regle0() throws EGGException {
    //declaration
    S_INTERFACE_NET x_1 = new S_INTERFACE_NET(att_scanner) ;
    S_STATEMENTS_NET x_2 = new S_STATEMENTS_NET(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    action_create_0(x_1, x_2);
  }
private void action_create_0(S_INTERFACE_NET x_1, S_STATEMENTS_NET x_2) throws EGGException {
// locales
NETStateDiagram loc_diagram;
// instructions
loc_diagram= new NETStateDiagram(x_1.att_interface, x_2.att_statements);
this.att_diagram=loc_diagram;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_NET.token_module : // 26
        regle0 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
