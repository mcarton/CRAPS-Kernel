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
    private ArrayList<Command> commands = new ArrayList<>();

    public UserInterface(CrapsApi api) {
        this.api = api;

        this.commands.add(new PrintCommand(api));
        this.commands.add(new RunCommand(api));
        this.commands.add(new SetCommand(api));
        this.commands.add(new StepCommand(api));
    }

    public void loop() throws CommException {
        boolean alive = true;
        Scanner sc = new Scanner(System.in);
        String cmd;

        while (true) {
            System.out.print("> ");

            try {
                cmd = sc.nextLine();
            }
            catch(NoSuchElementException e) {
                break;
            }

            if (cmd.equals("exit")) {
                break;
            }

            if (!cmd.isEmpty()) {
                boolean found = false;
                for (Command command : commands) {
                    if (cmd.startsWith(command.name())) {
                        command.run(cmd);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Unknown command");
                }
            }
        }
    }

    class RunCommand implements Command {
        CrapsApi api;

        RunCommand(CrapsApi api) { this.api = api; }

        public String name() {
            return "run";
        }

        public void run(String command) throws CommException {
            api.run();
        }
    }

    class StepCommand implements Command {
        CrapsApi api;

        StepCommand(CrapsApi api) { this.api = api; }

        public String name() {
            return "step";
        }

        public void run(String command) throws CommException {
            api.step();
        }
    }
}
