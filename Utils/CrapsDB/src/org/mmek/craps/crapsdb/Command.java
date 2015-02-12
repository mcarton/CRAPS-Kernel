package org.mmek.craps.crapsdb;

import org.mmek.craps.crapsusb.CommException;

public interface Command {
    void run(String command) throws CommException;
}
