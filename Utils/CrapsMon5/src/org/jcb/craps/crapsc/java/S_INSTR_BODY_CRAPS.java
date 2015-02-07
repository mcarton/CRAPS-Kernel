package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;
import mg.egg.eggc.libjava.*;
class S_INSTR_BODY_CRAPS {
  S_INSTR_BODY_CRAPS(LEX_CRAPS att_scanner) {
    this.att_scanner = att_scanner;
    }
  SourceLine att_hline;
  LEX_CRAPS att_scanner;
  private void regle28() throws EGGException {
    //declaration
    T_st_stb_CRAPS x_1 = new T_st_stb_CRAPS(att_scanner ) ;
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    S_MEM_REF_CRAPS x_4 = new S_MEM_REF_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_28(x_1, x_2, x_4);
  }
  private void regle27() throws EGGException {
    //declaration
    T_ld_ldub_CRAPS x_1 = new T_ld_ldub_CRAPS(att_scanner ) ;
    S_MEM_REF_CRAPS x_2 = new S_MEM_REF_CRAPS(att_scanner) ;
    S_REGISTER_CRAPS x_4 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_27(x_1, x_2, x_4);
  }
  private void regle26() throws EGGException {
    //declaration
    T_codeop3_CRAPS x_1 = new T_codeop3_CRAPS(att_scanner ) ;
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    S_REGISTER_IMM13_CRAPS x_4 = new S_REGISTER_IMM13_CRAPS(att_scanner) ;
    S_REGISTER_CRAPS x_6 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_6.analyser() ;
    action_create_26(x_1, x_2, x_4, x_6);
  }
  private void regle25() throws EGGException {
    //declaration
    T_branch_CRAPS x_1 = new T_branch_CRAPS(att_scanner ) ;
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    x_1.analyser() ;
    x_2.analyser() ;
    action_create_25(x_1, x_2);
  }
  private void regle20() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_push ) ;
    x_2.analyser() ;
    action_create_20(x_2);
  }
  private void regle24() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_reti ) ;
    action_create_24();
  }
  private void regle23() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_call ) ;
    x_2.analyser() ;
    action_create_23(x_2);
  }
  private void regle40() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    S_LISTNUM_CRAPS x_4 = new S_LISTNUM_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_word ) ;
    x_2.analyser() ;
    action_create_40(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle22() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    S_REGISTER_CRAPS x_4 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_sethi ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_22(x_2, x_4);
  }
  private void regle21() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_pop ) ;
    x_2.analyser() ;
    action_create_21(x_2);
  }
  private void regle15() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_negcc ) ;
    x_2.analyser() ;
    action_create_15(x_2);
  }
  private void regle14() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_tst ) ;
    x_2.analyser() ;
    action_create_14(x_2);
  }
  private void regle17() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_nop ) ;
    action_create_17();
  }
  private void regle16() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    S_REGISTER_CRAPS x_4 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_notcc ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_16(x_2, x_4);
  }
  private void regle37() throws EGGException {
    //declaration
    T_ident_CRAPS x_2 = new T_ident_CRAPS(att_scanner ) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_global_ ) ;
    x_2.analyser() ;
    action_create_37(x_2);
  }
  private void regle19() throws EGGException {
    //declaration
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_ret ) ;
    action_create_19();
  }
  private void regle36() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_equ ) ;
    x_2.analyser() ;
    action_create_36(x_2);
  }
  private void regle18() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_jmp ) ;
    x_2.analyser() ;
    action_create_18(x_2);
  }
  private void regle39() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    S_LISTNUM_CRAPS x_4 = new S_LISTNUM_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_byte ) ;
    x_2.analyser() ;
    action_create_39(x_2, x_4);
    x_4.analyser() ;
  }
  private void regle8() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_inccc ) ;
    x_2.analyser() ;
    action_create_8(x_2);
  }
  private void regle7() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_inc ) ;
    x_2.analyser() ;
    action_create_7(x_2);
  }
  private void regle6() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    S_REGISTER_CRAPS x_4 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_mov ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_6(x_2, x_4);
  }
  private void regle35() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_org ) ;
    x_2.analyser() ;
    action_create_35(x_2);
  }
  private void regle5() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_clr ) ;
    x_2.analyser() ;
    action_create_5(x_2);
  }
  private void regle11() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    S_REGISTER_CRAPS x_4 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_set ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_11(x_2, x_4);
  }
  private void regle10() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_deccc ) ;
    x_2.analyser() ;
    action_create_10(x_2);
  }
  private void regle13() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    S_REGISTER_IMM13_CRAPS x_4 = new S_REGISTER_IMM13_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_cmp ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_13(x_2, x_4);
  }
  private void regle12() throws EGGException {
    //declaration
    S_NUMEXPR_CRAPS x_2 = new S_NUMEXPR_CRAPS(att_scanner) ;
    S_REGISTER_CRAPS x_4 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_setq ) ;
    x_2.analyser() ;
    att_scanner.accepter_sucre(LEX_CRAPS.token_virg ) ;
    x_4.analyser() ;
    action_create_12(x_2, x_4);
  }
  private void regle9() throws EGGException {
    //declaration
    S_REGISTER_CRAPS x_2 = new S_REGISTER_CRAPS(att_scanner) ;
    //appel
    att_scanner.accepter_sucre(LEX_CRAPS.token_dec ) ;
    x_2.analyser() ;
    action_create_9(x_2);
  }
private void action_create_40(S_NUMEXPR_CRAPS x_2, S_LISTNUM_CRAPS x_4) throws EGGException {
// locales
CrapsDirecWord loc_instr;
NumExprList loc_list;
// instructions
loc_list= new NumExprList();
x_4.att_vals=loc_list;
    loc_list.add(x_2.att_numexpr);
loc_instr= new CrapsDirecWord(loc_list);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_11(S_NUMEXPR_CRAPS x_2, S_REGISTER_CRAPS x_4) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthSet(x_2.att_numexpr, x_4.att_val));
  }
private void action_create_10(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthDeccc(x_2.att_val));
  }
private void action_create_15(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthNegcc(x_2.att_val));
  }
private void action_create_14(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthTst(x_2.att_val));
  }
private void action_create_13(S_REGISTER_CRAPS x_2, S_REGISTER_IMM13_CRAPS x_4) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthCmp(x_2.att_val, x_4.att_obj));
  }
private void action_create_39(S_NUMEXPR_CRAPS x_2, S_LISTNUM_CRAPS x_4) throws EGGException {
// locales
CrapsDirecByte loc_instr;
NumExprList loc_list;
// instructions
loc_list= new NumExprList();
x_4.att_vals=loc_list;
    loc_list.add(x_2.att_numexpr);
loc_instr= new CrapsDirecByte(loc_list);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_12(S_NUMEXPR_CRAPS x_2, S_REGISTER_CRAPS x_4) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthSetq(x_2.att_numexpr, x_4.att_val));
  }
private void action_create_36(S_NUMEXPR_CRAPS x_2) throws EGGException {
// locales
CrapsDirecEqu loc_instr;
// instructions
loc_instr= new CrapsDirecEqu(x_2.att_numexpr);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_19() throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthRet());
  }
private void action_create_37(T_ident_CRAPS x_2) throws EGGException {
// locales
CrapsDirecGlobal loc_instr;
// instructions
loc_instr= new CrapsDirecGlobal(x_2.att_txt);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_18(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthJmp(x_2.att_val));
  }
private void action_create_17() throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthNop());
  }
private void action_create_16(S_REGISTER_CRAPS x_2, S_REGISTER_CRAPS x_4) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthNotcc(x_2.att_val, x_2.att_val));
  }
private void action_create_35(S_NUMEXPR_CRAPS x_2) throws EGGException {
// locales
CrapsDirecOrg loc_instr;
// instructions
loc_instr= new CrapsDirecOrg(x_2.att_numexpr);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_20(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthPush(x_2.att_val));
  }
private void action_create_22(S_NUMEXPR_CRAPS x_2, S_REGISTER_CRAPS x_4) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsInstrSetHi(x_2.att_numexpr, x_4.att_val));
  }
private void action_create_21(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthPop(x_2.att_val));
  }
private void action_create_24() throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsInstrReti());
  }
private void action_create_23(S_NUMEXPR_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthCall(x_2.att_numexpr));
  }
private void action_create_26(T_codeop3_CRAPS x_1, S_REGISTER_CRAPS x_2, S_REGISTER_IMM13_CRAPS x_4, S_REGISTER_CRAPS x_6) throws EGGException {
// locales
CrapsInstrArithLog3 loc_instr;
// instructions
loc_instr= new CrapsInstrArithLog3(x_1.att_txt, x_2.att_val, x_4.att_obj, x_6.att_val);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_25(T_branch_CRAPS x_1, S_NUMEXPR_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsInstrBr(x_1.att_txt, x_2.att_numexpr));
  }
private void action_create_8(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthInccc(x_2.att_val));
  }
private void action_create_28(T_st_stb_CRAPS x_1, S_REGISTER_CRAPS x_2, S_MEM_REF_CRAPS x_4) throws EGGException {
// locales
CrapsInstrSt loc_instr;
// instructions
loc_instr= new CrapsInstrSt(x_1.att_txt, x_2.att_val, x_4.att_addrcontent);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_7(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthInc(x_2.att_val));
  }
private void action_create_27(T_ld_ldub_CRAPS x_1, S_MEM_REF_CRAPS x_2, S_REGISTER_CRAPS x_4) throws EGGException {
// locales
CrapsInstrLd loc_instr;
// instructions
loc_instr= new CrapsInstrLd(x_1.att_txt, x_2.att_addrcontent, x_4.att_val);
    this.att_hline.setInstr(loc_instr);
  }
private void action_create_9(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthDec(x_2.att_val));
  }
private void action_create_6(S_REGISTER_CRAPS x_2, S_REGISTER_CRAPS x_4) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthMov(x_2.att_val, x_4.att_val));
  }
private void action_create_5(S_REGISTER_CRAPS x_2) throws EGGException {
// locales
// instructions
    this.att_hline.setInstr( new CrapsSynthClr(x_2.att_val));
  }
  public void analyser () throws EGGException {    att_scanner.lit ( 1 ) ;
    switch ( att_scanner.fenetre[0].code ) {
      case LEX_CRAPS.token_clr : // 17
        regle5 () ;
      break ;
      case LEX_CRAPS.token_mov : // 18
        regle6 () ;
      break ;
      case LEX_CRAPS.token_inc : // 19
        regle7 () ;
      break ;
      case LEX_CRAPS.token_inccc : // 20
        regle8 () ;
      break ;
      case LEX_CRAPS.token_dec : // 21
        regle9 () ;
      break ;
      case LEX_CRAPS.token_deccc : // 22
        regle10 () ;
      break ;
      case LEX_CRAPS.token_set : // 23
        regle11 () ;
      break ;
      case LEX_CRAPS.token_setq : // 24
        regle12 () ;
      break ;
      case LEX_CRAPS.token_cmp : // 25
        regle13 () ;
      break ;
      case LEX_CRAPS.token_tst : // 26
        regle14 () ;
      break ;
      case LEX_CRAPS.token_negcc : // 27
        regle15 () ;
      break ;
      case LEX_CRAPS.token_notcc : // 28
        regle16 () ;
      break ;
      case LEX_CRAPS.token_nop : // 29
        regle17 () ;
      break ;
      case LEX_CRAPS.token_jmp : // 30
        regle18 () ;
      break ;
      case LEX_CRAPS.token_ret : // 31
        regle19 () ;
      break ;
      case LEX_CRAPS.token_push : // 32
        regle20 () ;
      break ;
      case LEX_CRAPS.token_pop : // 33
        regle21 () ;
      break ;
      case LEX_CRAPS.token_sethi : // 35
        regle22 () ;
      break ;
      case LEX_CRAPS.token_call : // 34
        regle23 () ;
      break ;
      case LEX_CRAPS.token_reti : // 36
        regle24 () ;
      break ;
      case LEX_CRAPS.token_branch : // 84
        regle25 () ;
      break ;
      case LEX_CRAPS.token_codeop3 : // 53
        regle26 () ;
      break ;
      case LEX_CRAPS.token_ld_ldub : // 56
        regle27 () ;
      break ;
      case LEX_CRAPS.token_st_stb : // 59
        regle28 () ;
      break ;
      case LEX_CRAPS.token_org : // 85
        regle35 () ;
      break ;
      case LEX_CRAPS.token_equ : // 86
        regle36 () ;
      break ;
      case LEX_CRAPS.token_global_ : // 87
        regle37 () ;
      break ;
      case LEX_CRAPS.token_byte : // 88
        regle39 () ;
      break ;
      case LEX_CRAPS.token_word : // 89
        regle40 () ;
      break ;
      default :
        { String as[]={att_scanner.fenetre[0].getNom()}
;att_scanner._interrompre(att_scanner.messages.S_02,as);
}
    }
  }
  }
