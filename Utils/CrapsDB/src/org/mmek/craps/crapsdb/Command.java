package org.mmek.craps.crapsdb;

import org.mmek.craps.crapsusb.CommException;

public interface Command {
    boolean run(String command) throws CommException;
}
