package org.mmek.craps.crapsdb;

import java.io.*;
import java.util.*;

import org.mmek.craps.crapsusb.Device;
import org.mmek.craps.crapsusb.CommThread;
import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

import org.jcb.craps.crapsc.java.ObjModule;


public class CrapsDB {

    private static void help() {
        System.out.println("usage: crapsdb [-h] [FILE.obj]\n\n"
                         + "Debug a craps processor.\n\n"
                         + "optional arguments:\n"
                         + "  -h, --help     show this help message and exit\n"
                         + "  FILE.obj       load a .obj file");
        System.exit(0);
    }

    private static void error(String err) {
        System.err.println("usage: crapsdb [-h] [FILE.obj]\n"
                         + "error: " + err);
        System.exit(1);
    }

    private static File parseArguments(String[] args) {
        File objFile = null;

        for (int i = 0; i < args.length; i++) {
            String opt = args[i];

            if(opt.equals("-h") || opt.equals("--help")) { /* -h, --help */
                help();
            }
            else if(objFile == null) { /* FILE.obj */
                if (!opt.endsWith(".obj")) {
                    error("The file extension must be .obj");
                }

                objFile = new File(opt);
            }
            else {
                error("Unknown option " + opt);
            }
        }

        return objFile;
    }

    public static Device chooseDevice() {
        List<Device> devices = CommThread.getAvailableDevices();

        if(devices.size() == 0) {
            System.err.println("No device available.");
            System.exit(1);
            return null; // unreachable
        }
        else if(devices.size() == 1) {
            return devices.get(0);
        }
        else {
            int deviceIndex;
            Scanner sc = new Scanner(System.in);
            System.out.println("Availables devices:");

            for(int i = 0; i < devices.size(); i++) {
                System.out.println("  " + (i+1) + " - " + devices.get(i).getAlias());
            }

            do {
                System.out.print("\nSelect the device to use: ");
                deviceIndex = sc.nextInt() - 1;
            }
            while(deviceIndex < 0 || deviceIndex >= devices.size());

            return devices.get(deviceIndex);
        }
    }

    public static void main(String[] args) {
        File objFile = parseArguments(args);

        CommThread.init();

        Device device = chooseDevice();
        CrapsApi api = null;
        ObjModule objModule = null;

        try {
            api = new CrapsApi(device);

            // fix segfault with Ctrl-C
            Runtime.getRuntime().addShutdownHook(new ExitThread(api));

            // load object file
            if(objFile != null) {
                System.out.print("Loading file " + objFile.getPath() + " ... ");
                objModule = ObjModule.load(objFile);
                api.loadObj(objModule);
                System.out.println("done.");
            }

            UserInterface ui = new UserInterface(api, objModule);
            ui.loop();
        }
        catch(IOException e) {
            System.err.print(e);
        }
        finally {
            if(api != null) {
                try {
                    api.close();
                }
                catch(CommException e) {
                    System.err.println(e);
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Thread called when typing Ctrl-C
     */
    static class ExitThread extends Thread {
        private CrapsApi api;

        public ExitThread(CrapsApi api) {
            this.api = api;
        }

        public void run() {
            try {
                api.close();
            }
            catch(CommException e) {
                System.err.println(e);
            }
        }
    }
}
