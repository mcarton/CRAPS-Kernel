package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_MEM_REF_CRAPS {
  S_MEM_REF_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  AddrContent att_addrcontent;
  LEX_CRAPS att_scanner;
  private void regle29() throws EGGException {
    //declaration
    S_MEM_REFX_CRAPS x_3 = new S_MEM_REFX_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_lbra ) ;
    action_create_and_trans_29(x_3);
    x_3.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_rbra ) ;
  }
private void action_create_and_trans_29(S_MEM_REFX_CRAPS x_3) throws EGGException {
// locales
AddrContent loc_addr_content;
// instructions
loc_addr_content= new AddrContent();
this.att_addrcontent=loc_addr_content;
x_3.att_haddrcontent=loc_addr_content;
  }
  public void analyser () throws EGGException {    regle29 () ;
  }
  }
