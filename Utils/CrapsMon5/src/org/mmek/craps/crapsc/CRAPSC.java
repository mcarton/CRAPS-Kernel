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

    private static SourceContext parseArguments(String[] args) throws CRAPSException {
        String sourceFileName = null;
        String outputFileName = null;

        for (int i = 0; i < args.length; i++) {
            String opt = args[i];
            if(opt.equals("-h") || opt.equals("--help")) { /* -h, --help */
                help();
            }
            else if((opt.equals("-o") || opt.equals("--output"))
                        && i + 1 < args.length) {
                outputFileName = args[i + 1];
                i++;
            }
            else if(sourceFileName == null) { /* FILE.asm */
                if (!opt.endsWith(".asm")) {
                    error(Messages.getString("CRAPS.ext_error"));
                }

                sourceFileName = opt;
            }
            else {
                error(Messages.getString("CRAPS.unknown_option", opt));
            }
        }

        if(sourceFileName == null) {
            error(Messages.getString("CRAPS.file_error"));
        }

        if(outputFileName == null) {
            outputFileName = sourceFileName.substring(0, sourceFileName.length() - 4) + ".obj";
        }

        return new SourceContext(new File(sourceFileName), new File(outputFileName));
    }

    public static void main(String[] args) {
        try {
            SourceContext sc = parseArguments(args);
            Assembler assembler = new Assembler();

            if(assembler.assemble(sc) > 0) {
                for(String message : assembler.getMessages()) {
                    System.err.println("error: " + message);
                }

                System.exit(1);
            }
            else {
                sc.save();
            }
        }
        catch (CRAPSException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
