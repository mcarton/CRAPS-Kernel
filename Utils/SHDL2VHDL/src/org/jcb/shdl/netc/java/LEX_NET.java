package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
import java.io.*;
public class LEX_NET extends LEXICAL4  {
  static final int EOF = 0 ;
  static final int token_slash = 1 ;
  static final int token_crocouv = 2 ;
  static final int token_comm = 3 ;
  static final int token_module = 4 ;
  static final int token_tabs = 5 ;
  static final int token_pv = 6 ;
  static final int token_blank = 7 ;
  static final int token_outputs = 8 ;
  static final int token_crocfer = 9 ;
  static final int token_inputs = 10 ;
  static final int token_ident = 11 ;
  static final int token_num2 = 12 ;
  static final int token_lpar = 13 ;
  static final int token_or = 14 ;
  static final int token_arrow = 15 ;
  static final int token_when = 16 ;
  static final int token_num16 = 17 ;
  static final int token_egal = 18 ;
  static final int token_rc = 19 ;
  static final int token_reset = 20 ;
  static final int token_ptpt = 21 ;
  static final int token_num10 = 22 ;
  static final int token_and = 23 ;
  static final int token_added_outputs = 24 ;
  static final int token_semicol = 25 ;
  static final int token_added = 26 ;
  static final int token_clock = 27 ;
  static final int token_rpar = 28 ;
  static final int token_virg = 29 ;
  static final int token_lfs = 30 ;
  static final int token_autre = 31 ;
  String[] tokenImage = {
    "<EOF>" ,    "slash" ,
    "crocouv" ,
    "comm" ,
    "module" ,
    "tabs" ,
    "pv" ,
    "blank" ,
    "outputs" ,
    "crocfer" ,
    "inputs" ,
    "ident" ,
    "num2" ,
    "lpar" ,
    "or" ,
    "arrow" ,
    "when" ,
    "num16" ,
    "egal" ,
    "rc" ,
    "reset" ,
    "ptpt" ,
    "num10" ,
    "and" ,
    "added_outputs" ,
    "semicol" ,
    "added" ,
    "clock" ,
    "rpar" ,
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
  public NETMessages messages ;
  public Messages getMessages (){ return messages;} 
  public LEX_NET ( LEX_CONTEXTE lc ,int k) {
    super ( lc , k ) ;
    analyseur=new JLEX_NET();
    dernier_accepte = 0 ;
    messages = new NETMessages();
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
