package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class DisasmCommand implements Command {
    CrapsApi api;
    Disassembler dis;
    StatePrinter sp;

    String labelPattern  = "\\p{Alpha}[\\p{Alnum}\\-_]+";
    String hexPattern    = "0x\\p{XDigit}+";
    String addrPattern   = "(" + hexPattern + ")|(" + labelPattern + ")";
    Pattern address      = Pattern.compile(
        "disam +(" + addrPattern + ")"
    );
    Pattern addressRange = Pattern.compile(
        "disam +(" + addrPattern + ") *\\.* *(" + addrPattern + ")"
    );
    Pattern addressIncr  = Pattern.compile(
        "disam +(" + addrPattern + ") *\\+ *(\\p{Digit}+)"
    );

    DisasmCommand(CrapsApi api, Disassembler dis, StatePrinter sp) {
        this.api = api;
        this.dis = dis;
        this.sp = sp;
    }

    public String help() {
        return
            "disasemble the value at an address or a range of addresses\n"
          + "format:\n"
          + "\tdisam [0xADDR|LABEL]\n"
          + "\tdisam [0xBEGIN|LABEL] .. [0xEND|LABEL]\n"
          + "\tdisam [0xBEGIN|LABEL] + INCR"
        ;
    }

    public String name() {
        return "disam";
    }

    public void run(String command) throws CommException {
        try {
            if (!impl(command)) {
                System.out.println(help());
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid address");
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
        Matcher mAddress = address.matcher(command);
        if (mAddress.matches()) {
            Long address = parseAddress(mAddress.group(1));

            if(address != null) {
                long value = api.readMemory(address);
                System.out.println(dis.disassemble(address, value));
            }

            return true;
        }

        Matcher mRange = addressRange.matcher(command);
        if (mRange.matches()) {
            Long first = parseAddress(mRange.group(1));
            Long last = parseAddress(mRange.group(4));
            long pc = api.readRegister(30);

            if(first != null && last != null) {
                sp.printAssembly(first, last, pc);
            }

            return true;
        }

        Matcher mIncr = addressIncr.matcher(command);
        if (mIncr.matches()) {
            Long address = parseAddress(mIncr.group(1));
            long incr = Long.parseLong(mIncr.group(4));
            long pc = api.readRegister(30);

            if(address != null) {
                sp.printAssembly(address, address + incr, pc);
            }

            return true;
        }

        return false;
    }
}
