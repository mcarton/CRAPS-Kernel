package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class PrintCommand implements Command {
    CrapsApi api;
    Disassembler dis;
    StatePrinter sp;

    String labelPattern  = "\\p{Alpha}[\\p{Alnum}\\-_]+";
    String hexPattern    = "0x\\p{XDigit}+";
    String addrPattern   = "(" + hexPattern + ")|(" + labelPattern + ")";

    Pattern register     = Pattern.compile("print +%r(\\d+)");
    Pattern address      = Pattern.compile("print +(" + addrPattern + ")");
    Pattern addressRange = Pattern.compile(
        "print +(" + addrPattern + ") *\\.* *(" + addrPattern + ")"
    );
    Pattern addressIncr  = Pattern.compile(
        "print +(" + addrPattern + ") *\\+ *(\\p{Digit}+)"
    );

    PrintCommand(CrapsApi api, Disassembler dis, StatePrinter sp) {
        this.api = api;
        this.dis = dis;
        this.sp = sp;
    }

    public String help() {
        return
            "prints the value of a register, of an address or a range of addresses\n"
          + "format:\n"
          + "\tprint\n"
          + "\tprint %reg\n"
          + "\tprint [0xADDR|LABEL]\n"
          + "\tprint [0xBEGIN|LABEL] .. [0xEND|LABEL]\n"
          + "\tprint [0xBEGIN|LABEL] + INCR"
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

    public Long parseAddress(String txt) {
        if(txt.startsWith("0x")) {
            return Long.parseLong(txt.substring(2, txt.length()), 16);
        }
        else {
            Long addr = dis.getAddress(txt);

            if(addr == null) {
                System.out.println("Cannot find label " + txt);
            }

            return addr;
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
            Long address = parseAddress(mAddress.group(1));

            if(address != null) {
                long value = api.readMemory(address);
                print("0x", address + "", value);
            }

            return true;
        }

        Matcher mRange = addressRange.matcher(command);
        if (mRange.matches()) {
            Long first = parseAddress(mRange.group(1));
            Long last = parseAddress(mRange.group(4));

            if(first != null && last != null) {
                sp.printStack(first, last);
            }

            return true;
        }

        Matcher mIncr = addressIncr.matcher(command);
        if (mIncr.matches()) {
            Long address = parseAddress(mIncr.group(1));
            long incr = Long.parseLong(mIncr.group(4));

            if(address != null) {
                sp.printStack(address, address + incr);
            }

            return true;
        }

        if(command.equals(name())) {
            sp.printAll();
            return true;
        }

        return false;
    }

    private void print(String prefix, String mem, long value) {
        System.out.print(prefix);
        System.out.print(mem);
        System.out.print(" = ");
        System.out.print("0x" + Long.toHexString(value));
        System.out.print(" (");
        System.out.print(value);
        System.out.println(")");
    }
}
