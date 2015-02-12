
package org.jcb.shdl;

import java.util.*;

public class MdleBundle extends ListResourceBundle {

	public Object[][] getContents() {
		return contents;
	}

	private Object[][] contents = {

		{ "copy",			"copy" },
		{ "paste",			"paste" },
		{ "select-all",			"select all" },
		{ "edit",			"edit" },
		{ "delete",			"delete" },
		{ "add-pin",			"add pin" },
		{ "add-fork",			"add bus fork" },
		{ "delete-node",		"delete node" },
		{ "add-line-after",		"add line after" },
		{ "add-quad-after",		"add quadratic curve after" },
		{ "orientation",		"orientation" },
		{ "left",			"left" },
		{ "right",			"right" },
		{ "up",				"up" },
		{ "down",			"down" },
		{ "inverted",			"inverted" },
		{ "clocked",			"clocked" },
		{ "visible-pin-label",		"visible pin label" },
		{ "hide",			"hide" },
		{ "module-name1-visible",	"module name #1 visible" },
		{ "module-name2-visible",	"module name #2 visible" },

		{ "fork", 			"bus fork" },

		{ "buffer", 			"buffer" },
		{ "not", 			"not" },
		{ "buffer-3-state", 		"buffer, 3 state" },
		{ "buffer-3-state-inverted",	"buffer, 3 state, inverted" },
		{ "and2", 			"and, 2-input" },
		{ "nand2", 			"nand, 2-input" },
		{ "or2", 			"or, 2-input" },
		{ "nor2", 			"nor, 2-input" },
		{ "xor2", 			"xor, 2-input" },
		{ "xor2bis", 			"xorbis, 2-input" },
		{ "xor3", 			"xor, 3-input" },
		{ "and3", 			"and, 3-input" },
		{ "nand3", 			"nand, 3-input" },
		{ "or3", 			"or, 3-input" },
		{ "nor3", 			"nor, 3-input" },

		{ "error1",			"One of these interface pins must be connected to an equipotential first" },

	};
}
 
