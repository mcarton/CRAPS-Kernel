
package org.jcb.craps;

import java .util.*;

public class CrapsEventsQueue {

	private ArrayList queue;

	public CrapsEventsQueue() {
		queue = new ArrayList();
	}

	public void putEvent(CrapsEvent event) {
		queue.add(event);
	}

	public CrapsEvent peekEvent() {
		if (queue.size() == 0) return null;
		return ((CrapsEvent) queue.get(0));
	}

	public void removeEvent() {
		if (queue.size() == 0) return;
		queue.remove(0);
	}


}


