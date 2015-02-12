package org.mmek.craps.crapsusb;


public class ConnectionFailedException extends CommException {
    private static final long serialVersionUID = 1L;

    public ConnectionFailedException() {
        super("USB connection failed.");
    }
}
