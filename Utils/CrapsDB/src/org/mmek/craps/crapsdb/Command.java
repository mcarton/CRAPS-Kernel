package org.mmek.craps.crapsdb;

import org.mmek.craps.crapsusb.CommException;

public interface Command {
    String help();
    String name();
    void run(String command) throws CommException;
}
