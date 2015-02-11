package org.mmek.craps.crapsc;

import java.io.*;


public class CRAPSC implements Serializable {
    private static final long serialVersionUID = 1L;

    private static void help() throws CRAPSException {
        throw new CRAPSException(Messages.getString("CRAPS.help"));
    }

    private static void error(String a) throws CRAPSException {
        throw new CRAPSException(Messages.getString("CRAPS.error", a));
    }

    private static File parseArguments(String[] args) throws CRAPSException {
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

        return new File(fileName);
    }

    public static void main(String[] args) {
        try {
            SourceContext sc = new SourceContext(parseArguments(args));
            Assembler assembler = new Assembler();

            if(assembler.assemble(sc) > 0) {
                for(String message : assembler.getMessages()) {
                    System.err.println("error: " + message);
                }

                System.exit(1);
            }
            else {
                System.out.println("TODO"); // TODO
            }
        }
        catch (CRAPSException e) {
            // Internal errors
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
