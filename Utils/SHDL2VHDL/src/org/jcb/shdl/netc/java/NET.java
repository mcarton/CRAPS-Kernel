package org.jcb.shdl.netc.java;
import java.io.*;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libjava.lex.*;
public class NET {
  String fileIn ;
  public InputStream source ;
  public NET(Options opts){
    att_options = opts ;
    att_arguments = opts.getArgs() ;
    fileIn = opts.getFileIn();
    LEX_CONTEXTE contexte = null;
    try {
        contexte=new LEX_CONTEXTE(fileIn, 512, "UTF-8");
      att_scanner = new LEX_NET(contexte, 2);
      att_scanner.analyseur.fromContext(contexte);
      }
    catch ( FileNotFoundException fnfe ) {
      System.err.println ( "Fichier introuvable : " + fnfe.getMessage() ) ;
      System.exit ( 1 ) ;
      }
   }
  public NET(Options opts, String src){
    att_options = opts ;
    att_arguments = opts.getArgs() ;
    LEX_CONTEXTE contexte = new LEX_CONTEXTE("", src, 512, "UTF-8");
      att_scanner = new LEX_NET(contexte, 2);
      att_scanner.analyseur.fromContext(contexte);
      }
  public void compile() throws EGGException {
    S_DIAGRAM_NET axiome = new S_DIAGRAM_NET(att_scanner);
    axiome.att_scanner = this.att_scanner ;
    axiome.att_arguments = this.att_arguments ;
    axiome.att_options = this.att_options ;
    axiome.analyser() ;
    this.att_diagram = axiome.att_diagram ;
    att_scanner.accepter_fds() ;
    }
  String [] att_arguments;
  public void set_arguments(String [] a_arguments){
    att_arguments = a_arguments;
  }
  public String [] get_arguments(){
    return att_arguments;
  }
  NETStateDiagram att_diagram;
  public void set_diagram(NETStateDiagram a_diagram){
    att_diagram = a_diagram;
  }
  public NETStateDiagram get_diagram(){
    return att_diagram;
  }
  LEX_NET att_scanner;
  public void set_scanner(LEX_NET a_scanner){
    att_scanner = a_scanner;
  }
  public LEX_NET get_scanner(){
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
