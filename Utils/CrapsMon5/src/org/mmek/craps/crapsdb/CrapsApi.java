package org.mmek.craps.crapsdb;

import java.util.*;

import org.jcb.shdl.CommThread;
import org.jcb.shdl.CommListener;
import org.jcb.shdl.CommEvent;

import org.jcb.craps.crapsc.java.ObjModule;
import org.jcb.craps.crapsc.java.ObjEntry;


public class CrapsApi {
    // communication thread
    private CommThread commThread;

    // message sent to CRAPS
    private int[] outs = new int[64];

    public CrapsApi() {
        try {
            commThread = new CommThread();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        // add listeners
        commThread.addCommListener(new CrapsCommListener());

        // initialize communication
        CommThread.init();
        int error = CommThread.openData();
        if (error < 0) {
            System.err.println("USB connection failed!");
            System.exit(1);
        }

        // start read thread
        try {
            commThread.start();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void close() {
        CommThread.closeData();
    }

    // API
    public synchronized void step() {
        // mon_cmd <- "0100" (step)
        setBits(60, 63, 4);
        // mon_req <- 1
        outs[59] = 1;
        CommThread.sendByte(7, getByte(7));
        sleep(5);
        // mon_req <- 0
        outs[59] = 0;
        CommThread.sendByte(7, getByte(7));
    }

    public synchronized void run() {
        // run <- 1
        outs[55] = 1;
        commThread.sendByte(6, getByte(6));
    }

    public synchronized void stop() {
        // run <- 0
        outs[55] = 0;
        commThread.sendByte(6, getByte(6));
    }

    public synchronized long readRegister(int numreg) {
        // mon_cmd <- "0001" (read register)
        setBits(60, 63, 1);
        CommThread.sendByte(7, getByte(7));
        // reg # on pc2board[4..0]
        setBits(0, 4, numreg);
        CommThread.sendByte(0, getByte(0));

        // mon_req <- 1
        outs[59] = 1;
        CommThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do { }
        while ((CommThread.readdByte(7) & 128) == 0);

        // get read data
        long value = 0;
        long pow8 = 1;
        for (int i = 0; i < 4; i++) {
            int b = CommThread.readdByte(i);
            value += pow8 * b;
            pow8 *= 256;
        }

        // mon_req <- 0
        outs[59] = 0;
        CommThread.sendByte(7, getByte(7));
        return value;
    }

    public synchronized void writeRegister(int numreg, long val) {
        // mon_cmd <- "0011" (write register)
        setBits(60, 63, 3);
        CommThread.sendByte(7, getByte(7));
        // reg # on pc2board[36..32]
        setBits(32, 36, numreg);
        CommThread.sendByte(4, getByte(4));
        // value to write on pc2board[31..0]
        setBits(0, 31, val);
        for (int i = 0; i < 4; i++) {
            CommThread.sendByte(i, getByte(i));
        }

        // mon_req <- 1
        outs[59] = 1;
        CommThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do { } while ((CommThread.readdByte(7) & 128) == 0);

        // mon_req <- 0
        outs[59] = 0;
        CommThread.sendByte(7, getByte(7));
    }

    public synchronized long readMemory(long addr) {
        // mon_cmd <- "0000" (read memory)
        setBits(60, 63, 0);
        // address on pc2board[31..0]
        setBits(0, 31, addr);

        for (int i = 0; i < 4; i++) {
            CommThread.sendByte(i, getByte(i));
        }

        // mon_req <- 1
        outs[59] = 1;
        CommThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do { } while ((CommThread.readdByte(7) & 128) == 0);

        // get read data
        long value = 0;
        long pow8 = 1;
        for (int i = 0; i < 4; i++) {
            int b = CommThread.readdByte(i);
            value += pow8 * b;
            pow8 *= 256;
        }

        // mon_req <- 0
        outs[59] = 0;
        CommThread.sendByte(7, getByte(7));
        return value;
    }

    public synchronized void writeMemory(long addr, long val) {
        // mon_cmd <- "0010" (write memory)
        setBits(60, 63, 2);
        // address on pc2board[31..0]
        setBits(0, 31, addr);
        for (int i = 0; i < 4; i++) {
            CommThread.sendByte(i, getByte(i));
        }
        // mon_req <- 1
        outs[59] = 1;
        CommThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do { } while ((CommThread.readdByte(7) & 128) == 0);

        // value to write on pc2board[31..0]
        setBits(0, 31, val);
        for (int i = 0; i < 4; i++) {
            CommThread.sendByte(i, getByte(i));
        }
        // mon_req <- 0
        outs[59] = 0;
        CommThread.sendByte(7, getByte(7));

        // wait for mon_ack = 0
        do { } while ((CommThread.readdByte(7) & 128) != 0);
    }

    public void loadObj(ObjModule objModule) {
        Long[] newKeys = (Long[]) objModule.getKeySet().toArray(new Long[0]);
        Map.Entry[] newEntries = (Map.Entry[]) objModule.getEntrySet().toArray(new Map.Entry[0]);

        for (int i = 0; i < newEntries.length; i++) {
            int addr = newKeys[i].intValue();
            ObjEntry oe = objModule.get(addr);
            //System.out.println("addr=" + addr + ", word=" + oe.word);
            long val = Long.parseLong(oe.word, 2);
            writeMemory((long) addr, val);
        }
    }

    // Communication methods
    private int getByte(int i) {
        int res = 0;
        if (outs[8*i] == 1) res |= 1;
        if (outs[8*i+1] == 1) res |= 2;
        if (outs[8*i+2] == 1) res |= 4;
        if (outs[8*i+3] == 1) res |= 8;
        if (outs[8*i+4] == 1) res |= 16;
        if (outs[8*i+5] == 1) res |= 32;
        if (outs[8*i+6] == 1) res |= 64;
        if (outs[8*i+7] == 1) res |= 128;
        return res;
    }

    private void setBits(int first, int last, long val) {
        for (int i = first; i <= last; i++) {
            outs[i] = (int) (val % 2);
            val = val / 2;
        }
    }

    private void sleep(long n) {
        try {
            Thread.sleep(n);
        }
        catch(InterruptedException ex) {}
    }

    class CrapsCommListener implements CommListener {
        public void valueChanged(CommEvent ev) {
            int[] bitVector = ev.getBitVector();

            // debug
            System.out.print("valueChanged: ");
            for(int i = 0; i < bitVector.length; i++) {
                System.out.print(bitVector[i]);
            }
            System.out.println();

            int brk = bitVector[62];
            int rst = bitVector[61];

            /*
            if ((brk == 1) && runButton.getActionCommand().equals("stop")) {
                System.out.println("****************** break!!!");
                // run <- 0
                outs[55] = 0;
                commThread.sendByte(6, getByte(6));
                runButton.setText("run");
                runButton.setActionCommand("run");
                stepButton.setEnabled(true);
                updateViews();
            }

            if (rst == 1) {
                System.out.println("****************** reset!!!");
                // run <- 0
                outs[55] = 0;
                commThread.sendByte(6, getByte(6));
                runButton.setText("run");
                runButton.setActionCommand("run");
                stepButton.setEnabled(true);
                updateViews();
            }
            */
        }
    }
}
