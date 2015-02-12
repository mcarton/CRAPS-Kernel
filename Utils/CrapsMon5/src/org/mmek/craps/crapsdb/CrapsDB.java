package org.mmek.craps.crapsdb;

import java.io.*;

import org.jcb.craps.crapsc.java.ObjModule;


public class CrapsDB {
    public static void main(String[] args) {
        CrapsApi api = new CrapsApi();

        if(args.length == 1) {
            // upload a .obj and run it
            System.out.println("running " + args[0]);
            File file = new File(args[0]);

            try {
                ObjModule objModule = ObjModule.load(file);
                api.loadObj(objModule);
                api.step();
                api.run();
            }
            catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        else {
            // nothing to do.. display some stuff
            for(int i = 0; i <= 31; i++) {
                System.out.println("%r" + i + " = " + api.readRegister(i));
            }

            for(long i = 0; i <= 30; i++) {
                System.out.println("mem[" + i + "] = " + api.readMemory(i));
            }
        }
    }
}
