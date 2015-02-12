package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
class T_num2_SHDL {
  public T_num2_SHDL(LEX_SHDL att_scanner ) {
    this.att_scanner = att_scanner ;
    }
  public void analyser() throws EGGException {
    att_scanner.lit ( 1 ) ;
    att_txt = att_scanner.fenetre[0].getNom() ;
    att_scanner.accepter_sucre ( LEX_SHDL.token_num2 ) ;
    }
  LEX_SHDL att_scanner;
  String att_txt;
  }
