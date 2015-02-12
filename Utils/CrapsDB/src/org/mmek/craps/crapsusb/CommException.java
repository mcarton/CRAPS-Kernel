package org.mmek.craps.crapsusb;

import java.io.IOException;

public class CommException extends IOException {
    private static final long serialVersionUID = 1L;

    public CommException(String message) {
        super(message);
    }
}
