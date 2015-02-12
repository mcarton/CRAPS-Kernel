
package org.jcb.shdl;

import java.util.*;

public abstract interface CommListener extends EventListener {

	public abstract void valueChanged(CommEvent ev);
	
}