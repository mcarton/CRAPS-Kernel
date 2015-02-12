package org.mmek.craps.crapsdb;

import java.io.*;
import java.util.*;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;


public class UserInterface {
    // manage colors
    private final String ALL_OFF = "\033[1;0m";
    private final String BOLD = "\033[1;1m";
    private final String BLUE = "\033[1;34m";
    private final String GREEN = "\033[1;32m";
    private final String RED = "\033[1;31m";
    private final String YELLOW = "\033[1;33m";

    private CrapsApi api;

    public UserInterface(CrapsApi api) {
        this.api = api;
    }

    public void loop() throws CommException {
        boolean alive = true;
        Scanner sc = new Scanner(System.in);

        while(alive) {
            System.out.print("> ");
            String cmd = sc.nextLine();

            if(cmd.equals("exit")) {
                alive = false;
            }
            else if(cmd.equals("toto")) {
                System.out.println(BOLD + RED + "coucou" + ALL_OFF);
            }
            else if(cmd.equals("tata")) {
                System.out.println(api.readRegister(20));
            }
            else if(cmd.equals("step")) {
                api.step();
            }
            else if(cmd.equals("run")) {
                api.run();
            }
        }
    }
}
