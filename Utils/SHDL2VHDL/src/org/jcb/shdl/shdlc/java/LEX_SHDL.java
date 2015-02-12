package org.jcb.shdl.shdlc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
import java.io.*;
public class LEX_SHDL extends LEXICAL4  {
  static final int EOF = 0 ;
  static final int token_slash = 1 ;
  static final int token_module = 2 ;
  static final int token_crocouv = 3 ;
  static final int token_comm = 4 ;
  static final int token_parfer = 5 ;
  static final int token_end_ = 6 ;
  static final int token_tabs = 7 ;
  static final int token_pv = 8 ;
  static final int token_pt = 9 ;
  static final int token_blank = 10 ;
  static final int token_crocfer = 11 ;
  static final int token_aff = 12 ;
  static final int token_ident = 13 ;
  static final int token_num2 = 14 ;
  static final int token_affs = 15 ;
  static final int token_or = 16 ;
  static final int token_num16 = 17 ;
  static final int token_rc = 18 ;
  static final int token_ptpt = 19 ;
  static final int token_parouv = 20 ;
  static final int token_num10 = 21 ;
  static final int token_and = 22 ;
  static final int token_semicol = 23 ;
  static final int token_virg = 24 ;
  static final int token_lfs = 25 ;
  static final int token_autre = 26 ;
  String[] tokenImage = {
    "<EOF>" ,    "slash" ,
    "module" ,
    "crocouv" ,
    "comm" ,
    "parfer" ,
    "end_" ,
    "tabs" ,
    "pv" ,
    "pt" ,
    "blank" ,
    "crocfer" ,
    "aff" ,
    "ident" ,
    "num2" ,
    "affs" ,
    "or" ,
    "num16" ,
    "rc" ,
    "ptpt" ,
    "parouv" ,
    "num10" ,
    "and" ,
    "semicol" ,
    "virg" ,
    "lfs" ,
  } ;
  int dernier_accepte ;
  private int [] separateurs = { 
token_comm
, token_tabs
, token_blank
, token_rc
, token_lfs
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
  public SHDLMessages messages ;
  public Messages getMessages (){ return messages;} 
  public LEX_SHDL ( LEX_CONTEXTE lc ,int k) {
    super ( lc , k ) ;
    analyseur=new JLEX_SHDL();
    dernier_accepte = 0 ;
    messages = new SHDLMessages();
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
