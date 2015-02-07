package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
class T_codeop3_CRAPS {
  public T_codeop3_CRAPS(LEX_CRAPS att_scanner ) {
    this.att_scanner = att_scanner ;
    }
  public void analyser() throws EGGException {
    att_scanner.lit ( 1 ) ;
    att_txt = att_scanner.fenetre[0].getNom() ;
    att_scanner.accepter_sucre ( LEX_CRAPS.token_codeop3 ) ;
    }
  LEX_CRAPS att_scanner;
  String att_txt;
  }
