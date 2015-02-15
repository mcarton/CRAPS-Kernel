package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class BreakCommand implements Command {
    CrapsApi api;

    Pattern address = Pattern.compile("break +0x(\\p{XDigit}+)");

    BreakCommand(CrapsApi api) {
        this.api = api;
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
