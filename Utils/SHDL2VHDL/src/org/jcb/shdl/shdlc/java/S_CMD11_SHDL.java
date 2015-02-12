package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_CMD11_SHDL {
  S_CMD11_SHDL(LEX_SHDL att_scanner) {
    this.att_scanner = att_scanner;
    }
  SHDLCombinatorialSetting att_hcombinSetting;
  SHDLSignal att_hsignal;
  SHDLSequentialSetting att_seqSetting;
  SHDLModule att_hmodule;
  LEX_SHDL att_scanner;
  SHDLSequentialModifier glob_seqModifier;
  private void regle30() throws EGGException {
    //declaration
    T_ident_SHDL x_1 = new T_ident_SHDL(att_scanner ) ;
    S_SIGNAL__SHDL x_4 = new S_SIGNAL__SHDL(att_scanner) ;
    //appel
    x_1.analyser() ;
    att_scanner.accepter_sucre(LEX_SHDL.token_aff ) ;
    action_trans_30(x_1, x_4);
    x_4.analyser() ;
    action_set_30(x_1, x_4);
    att_scanner.accepter_sucre(LEX_SHDL.token_pv ) ;
  }
private void action_set_30(T_ident_SHDL x_1, S_SIGNAL__SHDL x_4) throws EGGException {
// locales
// instructions
    glob_seqModifier.setSignalOccurence(x_4.att_signalOccurence);
  }
private void action_trans_30(T_ident_SHDL x_1, S_SIGNAL__SHDL x_4) throws EGGException {
// locales
// instructions
glob_seqModifier= new SHDLSequentialModifier(this.att_scanner.getBeginLine(), this.att_hmodule);
    glob_seqModifier.setSignal(this.att_hsignal);
    glob_seqModifier.setModifier(x_1.att_txt);
    this.att_hmodule.addSeqModifier(glob_seqModifier);
if (x_1.att_txt.equalsIgnoreCase("clk")){
x_4.att_hmodule=this.att_hmodule;
}
else if (x_1.att_txt.equalsIgnoreCase("rst")){
x_4.att_hmodule=this.att_hmodule;
}
else if (x_1.att_txt.equalsIgnoreCase("set")){
x_4.att_hmodule=this.att_hmodule;
}
else if (x_1.att_txt.equalsIgnoreCase("ena")){
x_4.att_hmodule=this.att_hmodule;
}
else {
{ String as[]={
""+x_1.att_txt, ""+x_1.att_txt}
;att_scanner._interrompre(att_scanner.messages.P_02,as);
}

}
this.att_seqSetting=null;
  }
  public void analyser () throws EGGException {    regle30 () ;
  }
  }
