package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
class T_ident_NET {
  public T_ident_NET(LEX_NET att_scanner ) {
    this.att_scanner = att_scanner ;
    }
  public void analyser() throws EGGException {
    att_scanner.lit ( 1 ) ;
    att_txt = att_scanner.fenetre[0].getNom() ;
    att_scanner.accepter_sucre ( LEX_NET.token_ident ) ;
    }
  LEX_NET att_scanner;
  String att_txt;
  }
