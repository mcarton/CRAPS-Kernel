package org.mmek.craps.crapsdb;

import java.io.*;
import java.util.*;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;
import org.mmek.craps.crapsusb.CrapsApi;

import org.jcb.craps.crapsc.java.ObjModule;

public class UserInterface {
    private CrapsApi api;
    private ObjModule objModule;
    private Disassembler dis = new Disassembler(objModule);
    private StatePrinter sp;
    private ArrayList<Command> commands = new ArrayList<>();

    public UserInterface(CrapsApi api, ObjModule objModule) {
        this.api = api;
        this.objModule = objModule;
        this.dis = new Disassembler(objModule);
        this.sp = new StatePrinter(api, objModule);

        this.commands.add(new DisasmCommand(api, dis, sp));
        this.commands.add(new HelpCommand(commands));
        this.commands.add(new PrintCommand(api, sp));
        this.commands.add(new RunCommand(api));
        this.commands.add(new SetCommand(api));
        this.commands.add(new StepCommand(api, dis, sp));
    }

    public void loop() throws CommException {
        Scanner sc = new Scanner(System.in);
        String cmd;

        sp.printRegisters();
        sp.printAssembly(dis);
        sp.printStack();
        sp.printEndLine();

        while (true) {
            System.out.print("> ");

            try {
                cmd = sc.nextLine();
            }
            catch(NoSuchElementException e) {
                break;
            }

            if (cmd.equals("exit") || cmd.equals(":q")) {
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

        public String help() {
            return null;
        }

        public String name() {
            return "run";
        }

        public void run(String command) throws CommException {
            api.run();
        }
    }

    class StepCommand implements Command {
        CrapsApi api;
        Disassembler dis;
        StatePrinter sp;

        StepCommand(CrapsApi api, Disassembler dis, StatePrinter sp) {
            this.api = api;
            this.dis = dis;
            this.sp = sp;
        }

        public String help() {
            return null;
        }

        public String name() {
            return "step";
        }

        public void run(String command) throws CommException {
            api.step();

            sp.printRegisters();
            sp.printAssembly(dis);
            sp.printStack();
            sp.printEndLine();
        }
    }
}
