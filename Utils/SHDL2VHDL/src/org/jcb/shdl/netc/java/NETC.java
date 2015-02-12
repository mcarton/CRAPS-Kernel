package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libegg.base.*;
import java.io.*;
public class NETC implements Serializable {
 	private static final long serialVersionUID = 1L;
  public static void main(String[] args){
    System.err.println("version " + "12");
    try {
      Options opts = new Options(args) ;
      opts.analyse();
      NET compilo = new NET(opts) ;
      compilo.compile() ;
      System.exit(0);
      }
    catch(EGGException e){
      if (e.getLine() == -1)
        System.err.println(e.getMsg());
      else
        System.err.println(e.getLine() + " : " + e.getMsg());
      System.exit(1);
      }
    }
  }
