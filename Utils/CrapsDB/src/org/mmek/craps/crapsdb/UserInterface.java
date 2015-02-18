package org.mmek.craps.crapsdb;

import java.io.*;
import java.util.*;

import org.mmek.craps.crapsusb.CommEvent;
import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;
import org.mmek.craps.crapsusb.CommListener;

import org.jcb.craps.crapsc.java.ObjModule;

public class UserInterface {
    private CrapsApi api;
    private ObjModule objModule;
    private Disassembler dis;
    private StatePrinter sp;
    private ArrayList<Command> commands = new ArrayList<>();

    public UserInterface(CrapsApi api, ObjModule objModule) {
        this.api = api;
        this.objModule = objModule;
        this.dis = new Disassembler(objModule);
        this.sp = new StatePrinter(api, dis);

        this.commands.add(new BreakCommand(api, dis, sp));
        this.commands.add(new DisasmCommand(api, dis, sp));
        this.commands.add(new HelpCommand(commands));
        this.commands.add(new PrintCommand(api, dis, sp));
        this.commands.add(new RunCommand(api));
        this.commands.add(new SetCommand(api));
        this.commands.add(new StepCommand(api, sp));
        this.commands.add(new StopCommand(api, sp));

        this.api.addCommListener(new ResetListener(api, sp));
    }

    public void loop() throws CommException {
        Scanner sc = new Scanner(System.in);
        String cmd;

        sp.printAll();

        String lastCmd = "";
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

            if (cmd.isEmpty()) {
                cmd = lastCmd;
            }

            if (!cmd.isEmpty()) {
                boolean found = false;
                for (Command command : commands) {
                    if (cmd.startsWith(command.name())) {
                        command.run(cmd);
                        lastCmd = cmd;
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
}

class ResetListener implements CommListener {
    private CrapsApi api;
    private StatePrinter sp;

    ResetListener(CrapsApi api, StatePrinter sp) {
        this.api = api;
        this.sp = sp;
    }

    public void valueChanged(CommEvent ev) {
        int rst = ev.getBitVector()[61];

        if (rst == 1) {
            try {
                api.stop();

                System.out.print(Colors.BOLD + "\rReset\n" + Colors.ALL_OFF);
                sp.printAll();
                System.out.print("> ");
            }
            catch (CommException ce) {}
        }
    }
}
