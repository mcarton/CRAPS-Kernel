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
    private HashMap<String, Command> commands = new HashMap<>();

    public UserInterface(CrapsApi api) {
        this.api = api;

        this.commands.put("exit", new ExitCommand());
        this.commands.put("print", new PrintCommand(api));
        this.commands.put("run", new RunCommand(api));
        this.commands.put("step", new StepCommand(api));
    }

    public void loop() throws CommException {
        boolean alive = true;
        Scanner sc = new Scanner(System.in);
        String cmd;

        while(alive) {
            System.out.print("> ");

            try {
                cmd = sc.nextLine();
            }
            catch(NoSuchElementException e) {
                cmd = "exit";
            }


            if (!cmd.isEmpty()) {
                String[] splitCmd = cmd.split(" ");
                Command command = commands.get(splitCmd[0]);
                if (command != null) {
                    alive = command.run(cmd);
                }
                else {
                    System.out.println("Unknown command: " + splitCmd[0]);
                }
            }
        }
    }

    class ExitCommand implements Command {
        public boolean run(String command) {
            return false;
        }
    }

    class RunCommand implements Command {
        CrapsApi api;

        RunCommand(CrapsApi api) { this.api = api; }

        public boolean run(String command) throws CommException {
            api.run();
            return true;
        }
    }

    class StepCommand implements Command {
        CrapsApi api;

        StepCommand(CrapsApi api) { this.api = api; }

        public boolean run(String command) throws CommException {
            api.step();
            return true;
        }
    }
}
