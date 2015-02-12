package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMD_SHDL {
  S_CMD_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignal att_hsignal;
  SHDLModuleOccurence att_modOccurence;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle26() throws EGGException {
    //declaration
    S_SIGNAL_SHDL x_2 = new S_SIGNAL_SHDL(att_scanner) ;
    S_CMD1_SHDL x_4 = new S_CMD1_SHDL(att_scanner) ;
    //appel
    action_trans_26(x_2, x_4);
    x_2.analyser() ;
    action_trans2_26(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle34() throws EGGException {
    //declaration
    T_ident_SHDL x_1 = new T_ident_SHDL(att_scanner ) ;
    S_CMD2_SHDL x_3 = new S_CMD2_SHDL(att_scanner) ;
    //appel
    x_1.analyser() ;
    action_trans_34(x_1, x_3);
    x_3.analyser() ;
  }
private void action_trans_34(T_ident_SHDL x_1, S_CMD2_SHDL x_3) throws EGGException {
// locales
SHDLModuleOccurence loc_moduleOccurence;
int loc_line;
// instructions
x_3.att_hmodule=this.att_hmodule;
x_3.att_hsignal=this.att_hsignal;
loc_line=this.att_scanner.getBeginLine();
loc_moduleOccurence= new SHDLModuleOccurence(x_1.att_txt, loc_line, this.att_hmodule);
this.att_modOccurence=loc_moduleOccurence;
x_3.att_hmodOccurence=loc_moduleOccurence;
  }
private void action_trans2_26(S_SIGNAL_SHDL x_2, S_CMD1_SHDL x_4) throws EGGException {
// locales
// instructions
x_4.att_hsignal=x_2.att_signal;
    this.att_hmodule.addModuleSignal(x_2.att_signal);
  }
private void action_trans_26(S_SIGNAL_SHDL x_2, S_CMD1_SHDL x_4) throws EGGException {
// locales
// instructions
x_2.att_hmodule=this.att_hmodule;
x_4.att_hmodule=this.att_hmodule;
this.att_modOccurence=null;
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_ident : // 48
        att_scanner.lit ( 2 ) ;
        switch ( att_scanner.fenetre[1].code ) {
          case LEX_SHDL.token_crocouv : // 36
            regle26 () ;
          break ;
          case LEX_SHDL.token_aff : // 43
            regle26 () ;
          break ;
          case LEX_SHDL.token_pt : // 39
            regle26 () ;
          break ;
          case LEX_SHDL.token_affs : // 44
            regle26 () ;
          break ;
          case LEX_SHDL.token_parouv : // 34
            regle34 () ;
          break ;
          default :
            { String as[]={att_scanner.fenetre[1].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
        }
      break ;
      case LEX_SHDL.token_num2 : // 50
        regle26 () ;
      break ;
      case LEX_SHDL.token_num10 : // 53
        regle26 () ;
      break ;
      case LEX_SHDL.token_num16 : // 55
        regle26 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
