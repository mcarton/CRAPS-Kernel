
package org.jcb.shdl;

import java.util.*;

// pour une équipotentielle :
// -3 = niveau flottant, cad est n'est reliée à aucune source de courant
// -1 = source de courant présente, de valeur indéterminée
// >=0 = reliée à une source de courant connue

// pour un événement sur une patte de propagateur :
// -2 = état haute impédance
// -1 = source de courant, de valeur indéterminée
// >= 0 = source de courant de valeur connue

public class Matrix {

	private String[] equiNames;		// equipotential names
	private int[] widths;			// bus widths of equipotentials

	private TreeMap rows;
	private ArrayList[] connectedProps;
	private ArrayList[] connectedPins;
	private String[] undefValues;
	private String[] unknownValues;
	private String[] zeroValues;
	private String[] highZValues;
	private final Long MIN_LONG = new Long(Long.MIN_VALUE);

	public Matrix(String[] equiNames, int[] widths) {
		this.equiNames = equiNames;
		this.widths = widths;
		rows = new TreeMap();
		connectedProps = new ArrayList[getNbEqui()];
		connectedPins = new ArrayList[getNbEqui()];
		for (int i = 0; i < getNbEqui(); i++) {
			connectedProps[i] = new ArrayList();
			connectedPins[i] = new ArrayList();
		}
		// predefined values for each equipotential
		undefValues = new String[getNbEqui()];
		unknownValues = new String[getNbEqui()];
		zeroValues = new String[getNbEqui()];
		highZValues = new String[getNbEqui()];
		for (int i = 0; i < getNbEqui(); i++) {
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < getWidth(i); j++)
				sb.append("~");
			undefValues[i] = new String(sb);
			sb = new StringBuffer();
			for (int j = 0; j < getWidth(i); j++)
				sb.append("?");
			unknownValues[i] = new String(sb);
			sb = new StringBuffer();
			for (int j = 0; j < getWidth(i); j++)
				sb.append("0");
			zeroValues[i] = new String(sb);
			sb = new StringBuffer();
			for (int j = 0; j < getWidth(i); j++)
				sb.append("Z");
			highZValues[i] = new String(sb);
		}
	}

	public String getEquiName(int equiIndex) {
		return equiNames[equiIndex];
	}

	public int getWidth(int equiIndex) {
		return widths[equiIndex];
	}

	public int getNbEqui() {
		return equiNames.length;
	}

	public String getUndefValue(int equiIndex) {
		return undefValues[equiIndex];
	}

	public String getUnknownValue(int equiIndex) {
		return unknownValues[equiIndex];
	}

	public String getZeroValue(int equiIndex) {
		return zeroValues[equiIndex];
	}

	public String getHighZValue(int equiIndex) {
		return highZValues[equiIndex];
	}

	public int getNbRows() {
		return rows.size();
	}

	public MatrixRow getRow(int rowIndex) {
		Object[] values = rows.values().toArray();
		return ((MatrixRow) values[rowIndex]);
	}

	public MatrixRow getRow(long time) {
		return ((MatrixRow) rows.get(new Long(time)));
	}

	public MatrixRow lastRow() {
		if (rows.size() == 0) return null;
		return ((MatrixRow) rows.get(rows.lastKey()));
	}

	public String getValue(long time, int equiIndex) {
		String val = getUndefValue(equiIndex);
		MatrixRow row = getRow(time);
		if ((row != null) && (row.vals[equiIndex] != null)
				// a High-Z event does not give a value
				&& (!row.vals[equiIndex].val.equals(getHighZValue(equiIndex))))
			val = row.vals[equiIndex].val;
		else {
			SortedMap sub = rows.subMap(MIN_LONG, new Long(time));
			if ((sub.size() > 0) && !val.equals(getHighZValue(equiIndex))) {
				val = getValue(((Long) sub.lastKey()).longValue(), equiIndex);
			}
		}
		return val;
	}

	public String getValueBefore(long time, int equiIndex) {
		String val = getUndefValue(equiIndex);
		SortedMap sub = rows.subMap(MIN_LONG, new Long(time));
		if (sub.size() > 0) {
			val = getValue(((Long) sub.lastKey()).longValue(), equiIndex);
		}
		return val;
	}

	public Ev getEventBefore(long time, int equiIndex) {
		Ev ev = null;
		SortedMap sub = rows.subMap(MIN_LONG, new Long(time));
		if (sub.size() > 0) {
			Long Time = (Long) sub.lastKey();
			MatrixRow row = (MatrixRow) rows.get(Time);
			ev = row.vals[equiIndex];
		}
		return ev;
	}

	public MatrixRow createRow(long time) {
		MatrixRow row = new MatrixRow(this);
		row.time = time;
		row.vals = new Ev[getNbEqui()];
		for (int i = 0; i < getNbEqui(); i++) {
			row.vals[i] = null;
		}
		rows.put(new Long(time), row);
		return row;
	}

	public MatrixRow createOrGetRow(long time) {
		MatrixRow row = getRow(time);
		if (row != null) return row;
		return createRow(time);
	}

	public void connectPropagatorToEqui(int equiIndex, Propagator prop, int propIndex) {
		connectedProps[equiIndex].add(prop);
		connectedPins[equiIndex].add(new Integer(propIndex));
		prop.setEquiIndex(propIndex, equiIndex);
	}

	public ArrayList getConnectedPropagators(int equiIndex) {
		return connectedProps[equiIndex];
	}

	public ArrayList getConnectedPins(int equiIndex) {
		return connectedPins[equiIndex];
	}
}

