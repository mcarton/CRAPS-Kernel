package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class PrintCommand implements Command {
    CrapsApi api;

    Pattern register = Pattern.compile("print +%r(\\d+)");
    Pattern address  = Pattern.compile("print +0x(\\p{XDigit}+)");

    PrintCommand(CrapsApi api) { this.api = api; }

    public boolean run(String command) throws CommException {
        try {
            if (!impl(command)) {
                System.out.println("format:\n\tprint %reg\n\tprint 0xADDR");
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid register or address");
        }

        return true;
    }

    public boolean impl(String command) throws CommException {
        Matcher mRegister = register.matcher(command);
        if (mRegister.matches()) {
            int register = Integer.parseInt(mRegister.group(1));
            long value = api.readRegister(register);

            System.out.println("%r" + mRegister.group(1) + " = " + value);

            return true;
        }

        Matcher mAddress = address.matcher(command);
        if (mAddress.matches()) {
            long address = Long.parseLong(mAddress.group(1), 16);
            long value = api.readMemory(address);

            System.out.println("0x" + mAddress.group(1) + " = " + value);

            return true;
        }

        return false;
    }
}
