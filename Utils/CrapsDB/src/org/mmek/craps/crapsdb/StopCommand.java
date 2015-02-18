package org.mmek.craps.crapsdb;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

class StopCommand implements Command {
    CrapsApi api;
    StatePrinter sp;

    StopCommand(CrapsApi api, StatePrinter sp) {
        this.api = api;
        this.sp = sp;
    }

    public String help() {
        return
            "format:\n"
          + "\tstop         stop and print new state\n"
          + "\tstop !       stop, but shut up\n"
          + "\tstop silent  same as \"stop !\", but more explicit, also sooo much longer…\n"
        ;
    }

    public String name() {
        return "stop";
    }

    public void run(String command) throws CommException {
        api.stop();

        if (!command.endsWith("silent") && !command.endsWith("!")) {
            sp.printAll();
        }
    }
}
