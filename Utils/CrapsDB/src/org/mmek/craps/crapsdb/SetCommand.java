package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class SetCommand implements Command {
    CrapsApi api;

    Pattern register = Pattern.compile("set +%r(\\d+) *= *(.*)");
    Pattern address  = Pattern.compile("set +0x(\\p{XDigit}+) *= *(.*)");

    SetCommand(CrapsApi api) { this.api = api; }

    public void run(String command) throws CommException {
        try {
            if (!impl(command)) {
                System.out.println(
                    "format:\n"
                  + "\tset %reg = value\n"
                  + "\tset 0xADDR = value"
                );
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid register or address");
        }
    }

    public boolean impl(String command) throws CommException {
        Matcher mRegister = register.matcher(command);
        if (mRegister.matches()) {
            int register = Integer.parseInt(mRegister.group(1));
            long value = Long.parseLong(mRegister.group(2));

            api.writeRegister(register, value);

            return true;
        }

        Matcher mAddress = address.matcher(command);
        if (mAddress.matches()) {
            long address = Long.parseLong(mAddress.group(1), 16);
            long value = Long.parseLong(mAddress.group(2));

            api.writeMemory(address, value);

            return true;
        }

        return false;
    }
}
