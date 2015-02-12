package org.mmek.craps.crapsusb;


public class Device {
    private String alias;
    private Long handle;

    public Device(String alias) {
        this.alias = alias;
        handle = null;
    }

    public String getAlias() {
        return alias;
    }

    public synchronized void open() throws ConnectionFailedException {
        assert(handle == null);
        handle = CommThread.openData(alias);

        if(handle == -1) {
            throw new ConnectionFailedException();
        }
    }

    public synchronized void close() throws ConnectionFailedException {
        assert(handle != null);

        if(CommThread.closeData(handle) == -1) {
            throw new ConnectionFailedException();
        }

        handle = null;
    }

    public synchronized int writeByte(int num, int data) {
        assert(handle != null);
        return CommThread.writeByte(handle, num, data);
    }

    public synchronized int readByte(int num) {
        assert(handle != null);
        return CommThread.readByte(handle, num);
    }

    public boolean isOpened() {
        return handle != null;
    }
}
