package org.mmek.craps.crapsusb;

import java.util.*;

public abstract interface CommListener extends EventListener {
    public abstract void valueChanged(CommEvent ev);
}
