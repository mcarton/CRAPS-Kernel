package org.mmek.craps.crapsusb;

import java.io.*;
import java.util.*;

import org.jcb.craps.crapsc.java.ObjModule;
import org.jcb.craps.crapsc.java.ObjEntry;


public class CrapsApi {
    // communication thread
    private CommThread commThread;

    // message sent to CRAPS
    private int[] outs = new int[64];

    // list of event listeners
    private List<CrapsListener> listeners;

    boolean running;

    public CrapsApi(Device device) throws IOException {
        commThread = new CommThread(device);
        commThread.addCommListener(new CrapsCommListener());
        commThread.start();

        listeners = new ArrayList<>();

        // ensure it's not running
        running = false;
        stop();
    }

    public Device getDevice() {
        return commThread.getDevice();
    }

    public boolean isRunning() {
        return running;
    }

    public synchronized void close() throws CommException {
        commThread.stop();
    }

    public synchronized void step() throws CommException {
        assert(!running);

        setBits(60, 63, 4); // mon_cmd = "0100" (step)
        outs[59] = 1; // mon_req = 1
        commThread.sendByte(7, getByte(7));

        sleep(5);

        outs[59] = 0; // mon_req = 0
        commThread.sendByte(7, getByte(7));
    }

    public synchronized void run() throws CommException {
        assert(!running);
        running = true;

        outs[55] = 1; // run = 1
        commThread.sendByte(6, getByte(6));
    }

    public synchronized void stop() throws CommException {
        running = false;

        outs[55] = 0; // run = 0
        commThread.sendByte(6, getByte(6));
    }

    public synchronized long readRegister(int numreg) throws CommException {
        setBits(60, 63, 1); // mon_cmd = "0001" (read register)
        commThread.sendByte(7, getByte(7));

        setBits(0, 4, numreg);
        commThread.sendByte(0, getByte(0));

        outs[59] = 1; // mon_req = 1
        commThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do {} while ((commThread.readByte(7) & 128) == 0);

        // get read data
        long value = 0;
        long pow8 = 1;
        for (int i = 0; i < 4; i++) {
            int b = commThread.readByte(i);
            value += pow8 * b;
            pow8 *= 256;
        }

        outs[59] = 0; // mon_req = 0
        commThread.sendByte(7, getByte(7));

        return value;
    }

    public synchronized void writeRegister(int numreg, long val) throws CommException {
        setBits(60, 63, 3); // mon_cmd = "0011" (write register)
        commThread.sendByte(7, getByte(7));

        setBits(32, 36, numreg);
        commThread.sendByte(4, getByte(4));

        // value to write on pc2board[31..0]
        setBits(0, 31, val);
        for (int i = 0; i < 4; i++) {
            commThread.sendByte(i, getByte(i));
        }

        outs[59] = 1; // mon_req = 1
        commThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do {} while ((commThread.readByte(7) & 128) == 0);

        outs[59] = 0; // mon_req = 0
        commThread.sendByte(7, getByte(7));
    }

    public synchronized long readMemory(long addr) throws CommException {
        setBits(60, 63, 0); // mon_cmd = "0000" (read memory)
        // address on pc2board[31..0]
        setBits(0, 31, addr);

        for (int i = 0; i < 4; i++) {
            commThread.sendByte(i, getByte(i));
        }

        outs[59] = 1; // mon_req = 1
        commThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do {} while ((commThread.readByte(7) & 128) == 0);

        // get read data
        long value = 0;
        long pow8 = 1;
        for (int i = 0; i < 4; i++) {
            int b = commThread.readByte(i);
            value += pow8 * b;
            pow8 *= 256;
        }

        outs[59] = 0; // mon_req = 0
        commThread.sendByte(7, getByte(7));

        return value;
    }

    public synchronized void writeMemory(long addr, long val) throws CommException {
        setBits(60, 63, 2); // mon_cmd = "0010" (write memory)
        // address on pc2board[31..0]
        setBits(0, 31, addr);
        for (int i = 0; i < 4; i++) {
            commThread.sendByte(i, getByte(i));
        }

        outs[59] = 1; // mon_req = 1
        commThread.sendByte(7, getByte(7));

        // wait for mon_ack = 1
        do {} while ((commThread.readByte(7) & 128) == 0);

        // value to write on pc2board[31..0]
        setBits(0, 31, val);
        for (int i = 0; i < 4; i++) {
            commThread.sendByte(i, getByte(i));
        }

        outs[59] = 0; // mon_req = 0
        commThread.sendByte(7, getByte(7));

        // wait for mon_ack = 0
        do {} while ((commThread.readByte(7) & 128) != 0);
    }

    public void loadObj(ObjModule objModule) throws CommException {
        Long[] newKeys = objModule.getKeySet().toArray(new Long[0]);
        Map.Entry<Long, ObjEntry>[] newEntries = objModule.getEntrySet().toArray(new Map.Entry[0]);

        for (int i = 0; i < newEntries.length; i++) {
            int addr = newKeys[i].intValue();
            ObjEntry oe = objModule.get(addr);
            long val = Long.parseLong(oe.word, 2);
            writeMemory((long) addr, val);
        }
    }

    public void addListener(CrapsListener cl) {
        listeners.add(cl);
    }

    // Communication methods
    private int getByte(int i) {
        int res = 0;
        if (outs[8*i  ] == 1) res |= 1;
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
            int brk = bitVector[62];
            int rst = bitVector[61];

            if(brk == 1 && running) {
                try {
                    stop();
                }
                catch(CommException e) {}

                for(CrapsListener listener : listeners) {
                    listener.breakpoint();
                }
            }

            if(rst == 1) {
                try {
                    stop();
                }
                catch(CommException e) {}

                for(CrapsListener listener : listeners) {
                    listener.reset();
                }
            }
        }
    }
}
