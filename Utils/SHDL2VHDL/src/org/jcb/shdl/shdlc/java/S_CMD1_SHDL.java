package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMD1_SHDL {
  S_CMD1_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLSignal att_hsignal;
  SHDLCombinatorialSetting att_combinSetting;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  private void regle29() throws EGGException {
    //declaration
    S_SEQEQ_SHDL x_3 = new S_SEQEQ_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_affs ) ;
    action_init_29(x_3);
    x_3.analyser() ;
    att_scanner.accepter_sucre(LEX_SHDL.token_pv ) ;
  }
  private void regle27() throws EGGException {
    //declaration
    S_CMD111_SHDL x_3 = new S_CMD111_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_aff ) ;
    action_trans_27(x_3);
    x_3.analyser() ;
  }
  private void regle28() throws EGGException {
    //declaration
    S_CMD11_SHDL x_3 = new S_CMD11_SHDL(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_SHDL.token_pt ) ;
    action_trans_28(x_3);
    x_3.analyser() ;
  }
private void action_init_29(S_SEQEQ_SHDL x_3) throws EGGException {
// locales
SHDLSequentialSetting loc_seqSetting;
// instructions
loc_seqSetting= new SHDLSequentialSetting(this.att_scanner.getBeginLine(), this.att_hmodule);
x_3.att_hmodule=this.att_hmodule;
x_3.att_hseqSetting=loc_seqSetting;
this.att_combinSetting=null;
    loc_seqSetting.setSignal(this.att_hsignal);
    this.att_hmodule.addSeqSetting(loc_seqSetting);
  }
private void action_trans_28(S_CMD11_SHDL x_3) throws EGGException {
// locales
// instructions
x_3.att_hmodule=this.att_hmodule;
x_3.att_hsignal=this.att_hsignal;
this.att_combinSetting=null;
x_3.att_hcombinSetting=null;
  }
private void action_trans_27(S_CMD111_SHDL x_3) throws EGGException {
// locales
SHDLCombinatorialSetting loc_combinSetting;
// instructions
loc_combinSetting= new SHDLCombinatorialSetting(this.att_scanner.getBeginLine(), this.att_hmodule);
this.att_combinSetting=loc_combinSetting;
x_3.att_hmodule=this.att_hmodule;
x_3.att_hcombinSetting=loc_combinSetting;
    loc_combinSetting.setSignal(this.att_hsignal);
    this.att_hmodule.addCombinSetting(loc_combinSetting);
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_SHDL.token_aff : // 43
        regle27 () ;
      break ;
      case LEX_SHDL.token_pt : // 39
        regle28 () ;
      break ;
      case LEX_SHDL.token_affs : // 44
        regle29 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
