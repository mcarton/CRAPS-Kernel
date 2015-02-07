package org.jcb.craps;

import org.jcb.craps.*;
import java.util.*;
import java.text.*;

	
public class ConstCraps {

	final static String[] CODEOPS = {
		/* 0 -> 13 */   "addcc", "subc", "andcc", "orcc", "xorcc", "notcc", "sll", "srl", "add", "sub", "and", "or", "xor", "not", "--", "--",
		/* 16 -> 17 */  "sethi", "setlo",
		/* 18 -> 21 */  "ld", "ldub", "st", "stb",
		/* 22 -> 30 */  "ba", "be", "bne", "bcs", "bcc", "bneg", "bpos", "bvs", "bvc"
	};

	final static String[] CODEOP1_1 = { "ba", "be", "bne", "bcs", "bcc", "bneg", "bpos", "bvs", "bvc" };
	final static String[] CODEOP1_2 = { "sll", "srl" };
	final static String[] CODEOP2_1 = { "sethi", "setlo" };
	final static String[] CODEOP2_2 = { "ld", "ldub" };
	final static String[] CODEOP2_3 = { "st", "stb" };
	final static String[] CODEOP2_4 = { "not", "notcc" };
	final static String[] CODEOP3 = { "add", "addcc", "sub", "subcc", "and", "andcc", "or", "orcc", "xor", "xorcc" };

	final static String[] DIREC = { "word", "org" };

	final static String[] REGNAMES = { "%r0", "%r1", "%r2", "%r3", "%r4", "%r5", "%r6", "%r7", "%pc", "%sp" };
	final static int[] REGNUMS = { 0, 1, 2, 3, 4, 5, 6, 7, 14, 7 };
}

