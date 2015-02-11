package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_FOOTER_SHDL {
  S_FOOTER_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  LEX_SHDL att_scanner;
  private void regle25() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_end_ ) ;
    att_scanner.accepter_sucre(LEX_SHDL.token_module ) ;
  }
  public void analyser () throws EGGException {    regle25 () ;
  }
  }
