package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
class T_added_NET {
  public T_added_NET(LEX_NET att_scanner ) {
    this.att_scanner = att_scanner ;
    }
  public void analyser() throws EGGException {
    att_scanner.lit ( 1 ) ;
    att_txt = att_scanner.fenetre[0].getNom() ;
    att_scanner.accepter_sucre ( LEX_NET.token_added ) ;
    }
  LEX_NET att_scanner;
  String att_txt;
  }
