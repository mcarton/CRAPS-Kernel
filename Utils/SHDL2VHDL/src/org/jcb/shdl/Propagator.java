
package org.jcb.shdl;

import java.util.*;
import org.jcb.shdl.*;


public abstract class Propagator {

	public abstract String getName();

	public abstract int nbPins();
	public abstract void setEquiIndex(int propIndex, int equiIndex);
	public abstract int getEquiIndex(int propIndex);

	public abstract void propagateChanges(long time, Ev[] changes);
}

