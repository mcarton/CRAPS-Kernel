package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommEvent;
import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CommListener;
import org.mmek.craps.crapsusb.CrapsApi;

public class BreakCommand implements Command {
    CrapsApi api;
    Disassembler dis;

    Pattern address = Pattern.compile("break +0x(\\p{XDigit}+)");
    Pattern label = Pattern.compile("break +(\\p{Graph}+)");

    BreakCommand(CrapsApi api, Disassembler dis, StatePrinter sp) {
        this.api = api;
        this.dis = dis;
        this.api.addCommListener(new BreakListener(api, sp));
    }

    public String help() {
        return
            "set a breakpoint\n"
          + "format:\n"
          + "\tbreak 0xADDR\n"
          + "\tbreak LABEL"
        ;
    }

    public String name() {
        return "break";
    }

    public void run(String command) throws CommException {
        try {
            if (!impl(command)) {
                System.out.println(help());
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid register or address");
        }
    }

    public boolean impl(String command) throws CommException {
        Matcher mAddress = address.matcher(command);
        if (mAddress.matches()) {
            int address = Integer.parseInt(mAddress.group(1), 16);
            api.writeRegister(26 /* %brk */, address);
            return true;
        }

        Matcher mLabel = label.matcher(command);
        if (mLabel.matches()) {
            String lbl = mLabel.group(1);
            Long address = dis.getAddress(lbl);

            if(address == null) {
                System.out.println("Cannot find label " + lbl);
            }
            else {
                api.writeRegister(26 /* %brk */, address);
            }

            return true;
        }

        return false;
    }
}

class BreakListener implements CommListener {
    private CrapsApi api;
    private StatePrinter sp;
    private int previousBrk = 1;

    BreakListener(CrapsApi api, StatePrinter sp) {
        this.api = api;
        this.sp = sp;
    }

    public void valueChanged(CommEvent ev) {
        int brk = ev.getBitVector()[62];

        if (brk == 1 && previousBrk == 0) {
            try {
                api.stop();

                System.out.print(Colors.BOLD + "\rBreakpoint reached\n" + Colors.ALL_OFF);
                sp.printAll();
                System.out.print("> ");
            }
            catch (CommException ce) {}
        }
        previousBrk = brk;
    }
}
