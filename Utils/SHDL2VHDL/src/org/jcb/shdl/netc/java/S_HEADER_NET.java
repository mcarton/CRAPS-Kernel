package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_HEADER_NET {
  S_HEADER_NET(LEX_NET att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_NET att_scanner;
  private void regle1() throws EGGException {
    //declaration
    T_ident_NET x_2 = new T_ident_NET(att_scanner ) ;
    S_ISIGNAL_NET x_4 = new S_ISIGNAL_NET(att_scanner) ;
    S_ISIGNAL_NET x_6 = new S_ISIGNAL_NET(att_scanner) ;
    S_ISIGNALS_NET x_8 = new S_ISIGNALS_NET(att_scanner) ;
    S_ISIGNALS_NET x_10 = new S_ISIGNALS_NET(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_NET.token_module ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_lpar ) ;
    x_4.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_virg ) ;
    x_6.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_virg ) ;
    x_8.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_semicol ) ;
    x_10.analyser() ;
    att_scanner.accepter_sucre(LEX_NET.token_rpar ) ;
  }
  public void analyser () throws EGGException {    regle1 () ;
  }
  }
