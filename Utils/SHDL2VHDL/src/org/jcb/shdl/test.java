
package org.jcb.shdl;

import org.jcb.shdl.shdlc.java.*;
import mg.egg.eggc.libjava.*;
import mg.egg.eggc.libegg.base.*;
import java.io.*;

public class test {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		try {
			// opts non utilisable temporairement, mais obligatoire 
			Options opts = new Options(new String[0]) ;
			// Ce nouveau constructeur prend en deuxieme argument la chaine source
			SHDL compilo = new SHDL(opts, "x = y;\nz = x * y;") ;
			compilo.compile() ;
			// on récupère un attribut synthétisé sur l'axiome PROG
			System.out.println("modules=" + compilo.get_modules());

			// pas forcement
			System.exit(0);

		} catch(EGGException e){
			// affichage des messages d'erreur
			// a adapter egalement
			if (e.getLine() == -1)
				System.err.println(e.getMsg());
			else
				System.err.println(e.getLine() + " : " + e.getMsg());
			System.exit(1);
		}
	}
}

