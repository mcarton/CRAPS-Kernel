package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class PrintCommand implements Command {
    CrapsApi api;
    StatePrinter sp;

    Pattern register     = Pattern.compile("print +%r(\\d+)");
    Pattern address      = Pattern.compile("print +0x(\\p{XDigit}+)");
    Pattern addressRange = Pattern.compile(
        "print +0x(\\p{XDigit}+) *\\.* *0x(\\p{XDigit}+)"
    );

    PrintCommand(CrapsApi api, StatePrinter sp) {
        this.api = api;
        this.sp = sp;
    }

    public String help() {
        return
            "prints the value of a register, of an address or a range of addresses\n"
          + "format:\n"
          + "\tprint %reg\n"
          + "\tprint 0xADDR\n"
          + "\tprint 0xBEGIN .. 0xEND"
        ;
    }

    public String name() {
        return "print";
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
        Matcher mRegister = register.matcher(command);
        if (mRegister.matches()) {
            int register = Integer.parseInt(mRegister.group(1));
            long value = api.readRegister(register);

            print("%r", mRegister.group(1), value);

            return true;
        }

        Matcher mAddress = address.matcher(command);
        if (mAddress.matches()) {
            long address = Long.parseLong(mAddress.group(1), 16);
            long value = api.readMemory(address);

            print("0x", mRegister.group(1), value);

            return true;
        }

        Matcher mRange = addressRange.matcher(command);
        if (mRange.matches()) {
            long first = Long.parseLong(mRange.group(1), 16);
            long last  = Long.parseLong(mRange.group(2), 16);

            sp.printStack(first, last);

            return true;
        }

        return false;
    }

    private void print(String prefix, String mem, long value) {
        System.out.print(prefix);
        System.out.print(mem);
        System.out.print(" = ");
        System.out.print(Long.toHexString(value));
        System.out.print(" (");
        System.out.print(value);
        System.out.println(")");
    }
}
