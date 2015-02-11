package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_INTERFACE_NET {
  S_INTERFACE_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  NETInterface att_interface;
  LEX_NET att_scanner;
  private void regle1() throws EGGException {
    //declaration
    T_ident_NET x_3 = new T_ident_NET(att_scanner ) ;
    S_SIGNAL__NET x_6 = new S_SIGNAL__NET(att_scanner) ;
    S_SIGNAL__NET x_9 = new S_SIGNAL__NET(att_scanner) ;
    S_SIGNALS_NET x_13 = new S_SIGNALS_NET(att_scanner) ;
    S_SIGNALS_NET x_17 = new S_SIGNALS_NET(att_scanner) ;
    S_SIGNALS_NET x_21 = new S_SIGNALS_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_module ) ;
    att_scanner.accepter_sucre(LEX_NET.token_semicol ) ;
    x_3.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_reset ) ;
    att_scanner.accepter_sucre(LEX_NET.token_semicol ) ;
    x_6.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_clock ) ;
    att_scanner.accepter_sucre(LEX_NET.token_semicol ) ;
    x_9.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_inputs ) ;
    att_scanner.accepter_sucre(LEX_NET.token_semicol ) ;
    action_inputs_1(x_3, x_6, x_9, x_13, x_17, x_21);
    x_13.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_outputs ) ;
    att_scanner.accepter_sucre(LEX_NET.token_semicol ) ;
    action_outputs_1(x_3, x_6, x_9, x_13, x_17, x_21);
    x_17.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_added_outputs ) ;
    att_scanner.accepter_sucre(LEX_NET.token_semicol ) ;
    action_added_outputs_1(x_3, x_6, x_9, x_13, x_17, x_21);
    x_21.analyser() ;
    action_create_1(x_3, x_6, x_9, x_13, x_17, x_21);
  }
private void action_create_1(T_ident_NET x_3, S_SIGNAL__NET x_6, S_SIGNAL__NET x_9, S_SIGNALS_NET x_13, S_SIGNALS_NET x_17, S_SIGNALS_NET x_21) throws EGGException {
// locales
NETInterface loc_interface;
// instructions
loc_interface= new NETInterface(x_3.att_txt, x_6.att_signalOccurence, x_9.att_signalOccurence, x_13.att_signals, x_17.att_signals, x_21.att_signals);
this.att_interface=loc_interface;
  }
private void action_outputs_1(T_ident_NET x_3, S_SIGNAL__NET x_6, S_SIGNAL__NET x_9, S_SIGNALS_NET x_13, S_SIGNALS_NET x_17, S_SIGNALS_NET x_21) throws EGGException {
// locales
NETSignals loc_outputs;
// instructions
loc_outputs= new NETSignals();
x_17.att_hsignals=loc_outputs;
  }
private void action_inputs_1(T_ident_NET x_3, S_SIGNAL__NET x_6, S_SIGNAL__NET x_9, S_SIGNALS_NET x_13, S_SIGNALS_NET x_17, S_SIGNALS_NET x_21) throws EGGException {
// locales
NETSignals loc_inputs;
// instructions
loc_inputs= new NETSignals();
x_13.att_hsignals=loc_inputs;
  }
private void action_added_outputs_1(T_ident_NET x_3, S_SIGNAL__NET x_6, S_SIGNAL__NET x_9, S_SIGNALS_NET x_13, S_SIGNALS_NET x_17, S_SIGNALS_NET x_21) throws EGGException {
// locales
NETSignals loc_added_outputs;
// instructions
loc_added_outputs= new NETSignals();
x_21.att_hsignals=loc_added_outputs;
  }
  public void analyser () throws EGGException {    regle1 () ;
  }
  }
