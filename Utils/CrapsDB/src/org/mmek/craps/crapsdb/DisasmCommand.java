package org.mmek.craps.crapsdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class DisasmCommand implements Command {
    CrapsApi api;
    Disassembler dis;
    StatePrinter sp;

    Pattern address      = Pattern.compile("disam +0x(\\p{XDigit}+)");
    Pattern addressRange = Pattern.compile(
        "disam +0x(\\p{XDigit}+) *\\.* *0x(\\p{XDigit}+)"
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
          + "\tdisam 0xADDR\n"
          + "\tdisam 0xBEGIN .. 0xEND"
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

    public boolean impl(String command) throws CommException {
        Matcher mAddress = address.matcher(command);
        if (mAddress.matches()) {
            long address = Long.parseLong(mAddress.group(1), 16);
            long value = api.readMemory(address);

            System.out.println(dis.disassemble(address, value));

            return true;
        }

        Matcher mRange = addressRange.matcher(command);
        if (mRange.matches()) {
            long first = Long.parseLong(mRange.group(1), 16);
            long last  = Long.parseLong(mRange.group(2), 16);
            long pc = api.readRegister(30);

            sp.printAssembly(first, last, pc);

            return true;
        }

        return false;
    }
}
