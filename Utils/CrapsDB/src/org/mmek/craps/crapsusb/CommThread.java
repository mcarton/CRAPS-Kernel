package org.mmek.craps.crapsusb;

import java.io.*;
import java.util.*;


public class CommThread implements Runnable {

    static {
        System.loadLibrary("crapsusb");
    }

    /**
     * Native methods
     */
    public native static int init();
    public native static List<String> getDeviceAliases();
    public native static long openData(String alias);
    public native static int closeData(long handle);
    public synchronized native static int writeByte(long handle, int num, int data);
    public synchronized native static int readByte(long handle, int num);

    /**
     * Return all available devices
     */
    public static List<Device> getAvailableDevices() {
        List<String> aliases = CommThread.getDeviceAliases();
        List<Device> devices = new ArrayList<>();

        for(String alias : aliases) {
            long handle = CommThread.openData(alias);

            if(handle > 0) {
                CommThread.closeData(handle);
                devices.add(new Device(alias));
            }
        }

        return devices;
    }

    /**
     * CommThread
     */
    private static final int N = 128;
    private static final long IDLE_TIME = 10; // 10ms = probe time if no activity

    private Device device;
    private ArrayList<CommListener> listeners;
    private Thread readThread;
    private boolean readThreadAlive;
    private int[] bitVector = new int[N]; // vector of N bits currently read on the board


    public CommThread(Device device) {
        this.device = device;
        listeners = new ArrayList<>();
        readThread = null;
        readThreadAlive = false;
    }

    public Device getDevice() {
        return device;
    }

    public void stop() throws ConnectionFailedException {
        if(readThreadAlive) {
            readThreadAlive = false;
            try {
                readThread.join();
            }
            catch(InterruptedException e) {}
        }

        if(device.isOpened()) {
            device.close();
        }
    }

    public void start() throws IOException {
        if(!device.isOpened()) {
            device.open();
        }

        // sending a null bitVector
        for (int num = 0; num < N/8; num++)
            sendByte(num, 0);

        // launching read thread
        readThreadAlive = true;
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            // on fait varier byteNum circulairement de 0 à 7
            int byteNum = 0;
            int bytes[] = new int[N/8];
            long lastChangeTime = 0;
            long idleTime = 0L;

            while (readThreadAlive) {
                int byteData = readByte(byteNum);

                if (byteData != bytes[byteNum]) {
                    bytes[byteNum] = byteData;
                    lastChangeTime = System.currentTimeMillis();
                    idleTime = 0L;

                    // update bitVector
                    int index = byteNum * 8;
                    bitVector[index++] = ((byteData & 1) != 0) ? 1 : 0;
                    bitVector[index++] = ((byteData & 2) != 0) ? 1 : 0;
                    bitVector[index++] = ((byteData & 4) != 0) ? 1 : 0;
                    bitVector[index++] = ((byteData & 8) != 0) ? 1 : 0;
                    bitVector[index++] = ((byteData & 16) != 0) ? 1 : 0;
                    bitVector[index++] = ((byteData & 32) != 0) ? 1 : 0;
                    bitVector[index++] = ((byteData & 64) != 0) ? 1 : 0;
                    bitVector[index++] = ((byteData & 128) != 0) ? 1 : 0;

                    // create and send CommEvent
                    CommEvent ev = new CommEvent(byteNum, byteData, bitVector);
                    for(CommListener listener : listeners) {
                        listener.valueChanged(ev);
                    }
                }
                else {
                    if ((System.currentTimeMillis() - lastChangeTime) > 1000) {
                        idleTime = IDLE_TIME; // no update since 1s, slow read
                    }
                }

                byteNum = (byteNum + 1) % (N/8);

                try {
                    Thread.sleep(idleTime);
                }
                catch(InterruptedException e) {}
            }
        }
        catch(CommException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendByte(int num, int data) throws CommException {
        int attempt = 0;
        int err;

        do {
            err = device.writeByte(num, data);
            attempt++;
        }
        while(err < 0 && attempt < 10);

        if(err < 0) {
            throw new CommException("sendByte failed after 10 attempts");
        }
    }

    public synchronized int readByte(int num) throws CommException {
        int attempt = 0;
        int byteData;

        do {
            byteData = device.readByte(num);
            attempt++;
        }
        while(byteData < 0 && attempt < 10);

        if(byteData < 0) {
            throw new CommException("readByte failed after 10 attempts");
        }

        return byteData;
    }

    public int[] getBitVector() {
        return bitVector;
    }

    public long getLongValue(int endIndex, int startIndex) {
        long res = 0;
        long pow2 = 1;

        for (int i = startIndex; i <= endIndex; i++) {
            if (bitVector[i] == 1) res += pow2;
            pow2 = pow2 * 2;
        }

        return res;
    }

    public void addCommListener(CommListener listener) {
        listeners.add(listener);
    }

    public void removeCommListener(CommListener listener) {
        listeners.remove(listener);
    }
}
