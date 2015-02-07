package org.jcb.craps.crapsc.java;
import java.io.*;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
public class CRAPS {
  String fileIn ;
  public InputStream source ;
  public CRAPS(Options opts){
    att_options = opts ;
    att_arguments = opts.getArgs() ;
    fileIn = opts.getFileIn();
    LEX_CONTEXTE contexte = null;
    try {
        contexte=new LEX_CONTEXTE(fileIn, 512, "UTF-8");
      att_scanner = new LEX_CRAPS(contexte, 2);
      att_scanner.analyseur.fromContext(contexte);
      }
    catch ( FileNotFoundException fnfe ) {
      System.err.println ( "Fichier introuvable : " + fnfe.getMessage() ) ;
      System.exit ( 1 ) ;
      }
   }
  public CRAPS(Options opts, String src){
    att_options = opts ;
    att_arguments = opts.getArgs() ;
    LEX_CONTEXTE contexte = new LEX_CONTEXTE("", src, 512, "UTF-8");
      att_scanner = new LEX_CRAPS(contexte, 2);
      att_scanner.analyseur.fromContext(contexte);
      }
  public void compile() throws EGGException {
    S_PROG_CRAPS axiome = new S_PROG_CRAPS(att_scanner);
    axiome.att_scanner = this.att_scanner ;
    axiome.att_arguments = this.att_arguments ;
    axiome.att_options = this.att_options ;
    axiome.analyser() ;
    this.att_lines = axiome.att_lines ;
    att_scanner.accepter_fds() ;
    }
  String [] att_arguments;
  public void set_arguments(String [] a_arguments){
    att_arguments = a_arguments;
  }
  public String [] get_arguments(){
    return att_arguments;
  }
  SourceLines att_lines;
  public void set_lines(SourceLines a_lines){
    att_lines = a_lines;
  }
  public SourceLines get_lines(){
    return att_lines;
  }
  LEX_CRAPS att_scanner;
  public void set_scanner(LEX_CRAPS a_scanner){
    att_scanner = a_scanner;
  }
  public LEX_CRAPS get_scanner(){
    return att_scanner;
  }
  Options att_options;
  public void set_options(Options a_options){
    att_options = a_options;
  }
  public Options get_options(){
    return att_options;
  }
  }
