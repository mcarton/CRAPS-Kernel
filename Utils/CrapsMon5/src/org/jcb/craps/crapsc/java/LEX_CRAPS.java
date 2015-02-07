package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
import java.io.*;
public class LEX_CRAPS extends LEXICAL4  {
  static final int EOF = 0 ;
  static final int token_push = 1 ;
  static final int token_tst = 2 ;
  static final int token_negcc = 3 ;
  static final int token_deccc = 4 ;
  static final int token_rpar = 5 ;
  static final int token_ident = 6 ;
  static final int token_num2 = 7 ;
  static final int token_branch = 8 ;
  static final int token_pc = 9 ;
  static final int token_inc = 10 ;
  static final int token_equals = 11 ;
  static final int token_set = 12 ;
  static final int token_jmp = 13 ;
  static final int token_inccc = 14 ;
  static final int token_r = 15 ;
  static final int token_byte = 16 ;
  static final int token_ret = 17 ;
  static final int token_setq = 18 ;
  static final int token_reti = 19 ;
  static final int token_lfs = 20 ;
  static final int token_call = 21 ;
  static final int token_org = 22 ;
  static final int token_tabs = 23 ;
  static final int token_codeop3 = 24 ;
  static final int token_ld_ldub = 25 ;
  static final int token_word = 26 ;
  static final int token_cmp = 27 ;
  static final int token_st_stb = 28 ;
  static final int token_mult_div = 29 ;
  static final int token_num16 = 30 ;
  static final int token_num10 = 31 ;
  static final int token_lbra = 32 ;
  static final int token_virg = 33 ;
  static final int token_sp = 34 ;
  static final int token_clr = 35 ;
  static final int token_semicol = 36 ;
  static final int token_comm = 37 ;
  static final int token_fp = 38 ;
  static final int token_equ = 39 ;
  static final int token_pop = 40 ;
  static final int token_blank = 41 ;
  static final int token_rbra = 42 ;
  static final int token_sethi = 43 ;
  static final int token_plus_minus = 44 ;
  static final int token_mov = 45 ;
  static final int token_nop = 46 ;
  static final int token_notcc = 47 ;
  static final int token_dec = 48 ;
  static final int token_lpar = 49 ;
  static final int token_rc = 50 ;
  static final int token_global_ = 51 ;
  static final int token_autre = 52 ;
  String[] tokenImage = {
    "<EOF>" ,    "push" ,
    "tst" ,
    "negcc" ,
    "deccc" ,
    "rpar" ,
    "ident" ,
    "num2" ,
    "branch" ,
    "pc" ,
    "inc" ,
    "equals" ,
    "set" ,
    "jmp" ,
    "inccc" ,
    "r" ,
    "byte" ,
    "ret" ,
    "setq" ,
    "reti" ,
    "lfs" ,
    "call" ,
    "org" ,
    "tabs" ,
    "codeop3" ,
    "ld_ldub" ,
    "word" ,
    "cmp" ,
    "st_stb" ,
    "mult_div" ,
    "num16" ,
    "num10" ,
    "lbra" ,
    "virg" ,
    "sp" ,
    "clr" ,
    "semicol" ,
    "comm" ,
    "fp" ,
    "equ" ,
    "pop" ,
    "blank" ,
    "rbra" ,
    "sethi" ,
    "plus_minus" ,
    "mov" ,
    "nop" ,
    "notcc" ,
    "dec" ,
    "lpar" ,
    "rc" ,
    "global_" ,
  } ;
  int dernier_accepte ;
  private int [] separateurs = { 
token_lfs
, token_tabs
, token_comm
, token_blank
, token_rc
    } ;
  public int[] getSeparateurs(){
    return separateurs;
    }
  private int [] comments = { 
    } ;
  public int[] getComments(){
    return comments;
    }
  private int le_comment = -1;
  public int getComment(){
    return le_comment;
    }
  public CRAPSMessages messages ;
  public Messages getMessages (){ return messages;} 
  public LEX_CRAPS ( LEX_CONTEXTE lc ,int k) {
    super ( lc , k ) ;
    analyseur=new JLEX_CRAPS();
    dernier_accepte = 0 ;
    messages = new CRAPSMessages();
  }
  public void setSource ( LEXICAL4 scanner) throws EGGException{
    scanner.analyseur.toContext(scanner.contexte);
    analyseur.fromContext(scanner.contexte);
  }
  public void setReader ( LEXICAL4 scanner) {
    scanner.analyseur.setReader(scanner.contexte.source);
  }
  public void accepter_sucre ( int t ) throws EGGException {
    lit ( 1 ) ;
    if ( fenetre[0].code == t ) {
      dernier_accepte = fenetre[0].ligne ;
      decaler () ;
    }else {
      String as[] = {fenetre[0].getNom(), tokenImage[t]};
      _interrompre(messages.S_00,as);
    }
  }
  public UL accepter ( int t ) throws EGGException {
    UL retour ;
    lit ( 1 ) ;
    retour = fenetre[0] ;
    if ( fenetre[0].code == t )
      decaler() ;
    else{
      String as[] = {fenetre[0].getNom()};
      _interrompre(messages.S_00, as);
      }
    return retour ;
    }
  public void accepter_fds() throws EGGException {
    lit ( 1 ) ;
    if ( fenetre[0].code != EOF ) {
      String as[] = {fenetre[0].getNom()};
      _interrompre(messages.S_01, as);
      }
     else {
      dernier_accepte = fenetre[0].ligne ;
      }
    }
  public int getBeginLine() {
    if ( fenetre[0] != null ) {
      return fenetre[0].ligne + 1 ;
      } 
    else {
      return dernier_accepte + 1 ;
      }
    }
  public int getEndLine() {
    if ( fenetre[0] != null ) {
      return fenetre[0].ligne + 1 ;
      } 
    else {
      return dernier_accepte + 1 ;
      }
    }
  public int ligneDepart () {
    return ligneDebut + getEndLine() ;
    }
  public void _interrompre ( int c , String [] m ) throws EGGException {
      throw new EGGException(getBeginLine() + ligneDebut , messages.getMessage(c).toString(m));
    }
    public void _signaler ( int c , String [] m ) throws EGGException {
      System.err.println(getBeginLine() + ligneDebut + " : " + messages.getMessage(c).toString(m));
    }
  }
