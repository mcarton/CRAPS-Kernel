package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class BreakCommand implements Command {
    CrapsApi api;
    Disassembler dis;

    Pattern address = Pattern.compile("break +0x(\\p{XDigit}+)");
    Pattern label = Pattern.compile("break +(\\p{Graph}+)");

    BreakCommand(CrapsApi api, Disassembler dis) {
        this.api = api;
        this.dis = dis;
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
