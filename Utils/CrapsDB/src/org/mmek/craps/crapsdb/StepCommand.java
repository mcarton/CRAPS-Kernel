package org.mmek.craps.crapsdb;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

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
        return
            "format:\n"
          + "\tstep         make one step and print new state\n"
          + "\tstep !       make one step, but shut up\n"
          + "\tstep silent  same as \"step !\", but more explicit, also sooo much longer…\n"
        ;
    }

    public String name() {
        return "step";
    }

    public void run(String command) throws CommException {
        api.step();

        if (!command.endsWith("silent") && !command.endsWith("!")) {
            sp.printRegisters();
            sp.printAssembly(dis);
            sp.printStack();
            sp.printEndLine();
        }
    }
}
