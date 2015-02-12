package org.mmek.craps.crapsdb;

import org.jcb.craps.crapsc.java.ObjModule;

import org.mmek.craps.crapsusb.CommException;
import org.mmek.craps.crapsusb.CrapsApi;

public class StatePrinter {
    private CrapsApi api;
    private ObjModule objModule;

    public StatePrinter(CrapsApi api, ObjModule module) {
        this.api = api;
        this.objModule = objModule;
    }

    public void printRegisters() throws CommException {
        System.out.println(Colors.BLUE + "[----------------------------------registers-----------------------------------]" + Colors.ALL_OFF);

        // first line : %r1..%r5
        for(int i = 1; i <= 5; i++) {
            System.out.print(" " + Colors.GREEN + "%r" + i + ":  " + Colors.ALL_OFF + formatHexString(api.readRegister(i)));
        }
        System.out.println();

        // second line : %r6..%r9 %psr
        for(int i = 6; i <= 9; i++) {
            System.out.print(" " + Colors.GREEN + "%r" + i + ":  " + Colors.ALL_OFF + formatHexString(api.readRegister(i)));
        }

        int psr = (int) api.readRegister(25);
        System.out.print(" " + Colors.GREEN + "%psr: " + Colors.ALL_OFF);

        // flags
        if((psr & 128) > 0) System.out.print(Colors.GREEN);
        else System.out.print(Colors.RED);
        System.out.print("N " + Colors.ALL_OFF);

        if((psr & 64) > 0) System.out.print(Colors.GREEN);
        else System.out.print(Colors.RED);
        System.out.print("Z " + Colors.ALL_OFF);

        if((psr & 32) > 0) System.out.print(Colors.GREEN);
        else System.out.print(Colors.RED);
        System.out.print("V " + Colors.ALL_OFF);

        if((psr & 16) > 0) System.out.print(Colors.GREEN);
        else System.out.print(Colors.RED);
        System.out.print("C " + Colors.ALL_OFF);

        // int
        System.out.println(psr & 0xf);

        // last line : %brk %fp %ret %sp %pc
        System.out.print(" " + Colors.GREEN + "%brk: " + Colors.ALL_OFF + formatHexString(api.readRegister(26)));
        System.out.print(" " + Colors.GREEN + "%fp:  " + Colors.ALL_OFF + formatHexString(api.readRegister(27)));
        System.out.print(" " + Colors.GREEN + "%ret: " + Colors.ALL_OFF + formatHexString(api.readRegister(28)));
        System.out.print(" " + Colors.GREEN + "%sp:  " + Colors.ALL_OFF + formatHexString(api.readRegister(29)));
        System.out.print(" " + Colors.GREEN + "%pc:  " + Colors.ALL_OFF + formatHexString(api.readRegister(30)));
        System.out.println();
    }

    public void printAssembly() throws CommException {
        System.out.println(Colors.BLUE + "[-------------------------------------code-------------------------------------]" + Colors.ALL_OFF);

        Disassembler disas = new Disassembler(objModule);
        long pc = api.readRegister(30);

        for(long addr = Math.max(0, pc - 3); addr <= pc + 5; addr++) {
            System.out.print(" 0x" + formatHexString(addr) + " | ");

            if (objModule != null && objModule.getSym(addr) != null
                    && !objModule.getSym(addr).isEmpty()) {
                String label = objModule.getSym(addr) + ":";
                if(label.length() > 15) label = label.substring(0, 13) + ":";
                System.out.print(padRight(label, 15, ' ') + "    ");
            }
            else {
                System.out.print("                   ");
            }

            if(addr == pc) System.out.print(Colors.GREEN);
            System.out.print(disas.disassemble(addr, api.readMemory(addr)));
            System.out.println(Colors.ALL_OFF);
        }
    }

    public void printStack() throws CommException {
        System.out.println(Colors.BLUE + "[------------------------------------stack-------------------------------------]" + Colors.ALL_OFF);

        long sp = api.readRegister(29);

        for(long addr = sp; addr <= Math.min(sp + 6, 8192); addr++) {
            System.out.println(Colors.BLUE + " 0x" + formatHexString(addr) + Colors.ALL_OFF
                             + " | 0x" + formatHexString(api.readMemory(addr)));
        }
    }

    public void printEndLine() {
        System.out.println(Colors.BLUE + "[------------------------------------------------------------------------------]" + Colors.ALL_OFF);
    }

    public String formatHexString(long val) {
        return padLeft(Long.toHexString(val), 8, '0');
    }

    public String padLeft(String s, int length, char c) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        while(sb.length() < length) sb.insert(0, c);
        return sb.toString();
    }

    public String padRight(String s, int length, char c) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        while(sb.length() < length) sb.append(c);
        return sb.toString();
    }
}
