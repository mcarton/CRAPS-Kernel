package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
class T_ident_SHDL {
  public T_ident_SHDL(LEX_SHDL att_scanner ) {
    this.att_scanner = att_scanner ;
    }
  public void analyser() throws EGGException {
    att_scanner.lit ( 1 ) ;
    att_txt = att_scanner.fenetre[0].getNom() ;
    att_scanner.accepter_sucre ( LEX_SHDL.token_ident ) ;
    }
  LEX_SHDL att_scanner;
  String att_txt;
  }
