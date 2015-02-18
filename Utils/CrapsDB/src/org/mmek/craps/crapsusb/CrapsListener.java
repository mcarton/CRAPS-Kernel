package org.mmek.craps.crapsusb;

import java.util.*;

public abstract interface CrapsListener extends EventListener {
    public abstract void reset();
    public abstract void breakpoint();
}
