package org.mmek.craps.crapsc;

import java.io.*;

import mg.egg.eggc.runtime.libjava.SourceUnit;
import mg.egg.eggc.runtime.libjava.problem.IProblem;
import mg.egg.eggc.runtime.libjava.problem.ProblemReporter;
import mg.egg.eggc.runtime.libjava.problem.ProblemRequestor;

import org.jcb.craps.crapsc.java.CRAPS;

public class CRAPSC implements Serializable {
    private static final long serialVersionUID = 1L;

    private static void help() throws CRAPSException {
        throw new CRAPSException(Messages.getString("CRAPS.help"));
    }

    private static void error(String a) throws CRAPSException {
        throw new CRAPSException(Messages.getString("CRAPS.error", a));
    }

    private static SourceUnit parseArguments(String[] args) throws CRAPSException {
        String fileName = null;

        for (int i = 0; i < args.length; i++) {
            String opt = args[i];
            if(opt.equals("-h") || opt.equals("--help")) { /* -h, --help */
                help();
            }
            else if(fileName == null) { /* FILE.asm */
                if (!opt.endsWith(".asm")) {
                    error(Messages.getString("CRAPS.ext_error"));
                }

                fileName = opt;
            }
            else {
                error(Messages.getString("CRAPS.unknown_option", opt));
            }
        }

        if(fileName == null) {
            error(Messages.getString("CRAPS.file_error"));
        }

        return new SourceUnit(fileName);
    }

    public static void main(String[] args) {
        try {
            SourceUnit source = parseArguments(args);

            // Error management
            ProblemReporter prp = new ProblemReporter(source);
            ProblemRequestor prq = new ProblemRequestor(true);

            // Start compilation
            CRAPS compilo = new CRAPS(prp);
            prq.beginReporting();

            compilo.set_eval(true);
            compilo.compile(source);

            // Handle errors
            for (IProblem problem : prp.getAllProblems())
                prq.acceptProblem(problem);

            prq.endReporting();
            System.exit(prq.getFatal());
        }
        catch (CRAPSException e) {
            // Internal errors
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (Exception e) {
            // Other errors
            e.printStackTrace();
            System.exit(1);
        }
    }
}
