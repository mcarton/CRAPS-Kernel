package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommEvent;
import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CommListener;
import org.mmek.craps.crapsusb.CrapsApi;

public class BreakCommand implements Command {
    CrapsApi api;

    Pattern address = Pattern.compile("break +0x(\\p{XDigit}+)");

    BreakCommand(CrapsApi api, Disassembler dis, StatePrinter sp) {
        this.api = api;
        this.api.addCommListener(new BreakListener(api, dis, sp));
    }

    public String help() {
        return null;
    }

    public String name() {
        return "break";
    }

    public void run(String command) throws CommException {
        try {
            Matcher mAddress = address.matcher(command);
            if (mAddress.matches()) {
                int address = Integer.parseInt(mAddress.group(1), 16);

                api.writeRegister(26 /* %brk */, address);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid register or address");
        }
    }
}

class BreakListener implements CommListener {
    private CrapsApi api;
    private Disassembler dis;
    private StatePrinter sp;
    private int previousBrk = 0;

    BreakListener(CrapsApi api, Disassembler dis, StatePrinter sp) {
        this.api = api;
        this.dis = dis;
        this.sp = sp;
    }

    public void valueChanged(CommEvent ev) {
        int brk = ev.getBitVector()[62];

        if (brk == 1 && previousBrk == 0) {
            try {
                api.stop();

                System.out.print("\rBreakpoint\n");

                sp.printRegisters();
                sp.printAssembly(dis);
                sp.printStack();
                sp.printEndLine();

                System.out.print("> ");
            }
            catch (CommException ce) {}
        }
        previousBrk = brk;
    }
}
