package org.jcb.craps.crapsc.java;
import mg.egg.eggc.libjava.lex.*;

public class JLEX_CRAPS implements  LEX_ANALYZER  {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 256;
	private final int YY_EOF = 257;

	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public JLEX_CRAPS (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public JLEX_CRAPS (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	public JLEX_CRAPS () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	public void fromContext(LEX_CONTEXTE cont) {
		yy_reader = cont.source;
		yy_buffer = cont.buffer;
		yy_buffer_read = cont.b_read;
		yy_buffer_index = cont.b_index;
		yy_buffer_start = cont.b_start;
		yy_buffer_end = cont.b_end;
		yychar = 0;
		yyline = cont.ligne;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}
	public void toContext(LEX_CONTEXTE cont) {		cont.source = yy_reader;
		cont.buffer = yy_buffer;
		cont.b_read = yy_buffer_read;
		cont.b_index = yy_buffer_index;
		cont.b_start = yy_buffer_start;
		cont.b_end = yy_buffer_end;
		cont.ligne = yyline;
	}
	public void setReader(java.io.BufferedReader r) {
		yy_reader = r;
	}
	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_END,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_END,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NOT_ACCEPT,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,258,
"23:9,42,38,23:2,39,23:18,41,23:4,26,23:2,33,34,36,35,30,35,23,37,45,46,44:8" +",29,23:2,28,23:3,47:6,43:20,31,23,32,23,43,23,19,20,1,9,10,27,15,18,7,16,43" +",2,4,8,5,14,13,3,11,12,17,6,25,21,24,22,23:133,0,40")[0];

	private int yy_rmap[] = unpackFromString(1,168,
"0,1,2,3:10,4,5,6,7,8,9,10,11,12,1,13:2,14,13,15,13,16,17,13:2,18,1:4,19,20," +"13:14,21,22,23,24,25,26,27,13:3,28,29,1:5,30,31,32,33,34,35,36,37,38,39,13," +"40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,20,55,56,57,58,59,60,61,62,63," +"64,65,66,67,68,69,70,18,71,72,73,74,75,76,77,78,79,80,81,82,48,83,84,85,86," +"87,88,89,90,91,92,93,94,95,96,97,98,99,100,10,101,102,103,104,105,106,107,1" +"08,109,110,111,112,113,114,115,116,117,118,119,120,13")[0];

	private int yy_nxt[][] = unpackFromString(121,48,
"1,2,55,149,154,72,156,158,159,160,156,77,161,156,162,156,163,164,156,165,80" +",166,156,3,156:2,56,156,4,5,6,7,8,9,10,11,12,57,13,14,1,15,16,156,17,60,17," +"156,-1:49,167,83,167,86,89,167:4,92,167:4,94,167:3,151,96,167:2,-1,167,98,-" +"1,167,-1:15,167,100:3,167,-1:5,54,-1:4,71,-1:4,76,-1:4,79,-1:4,82,-1:60,13," +"14,-1:46,14:2,-1:13,54,-1:4,71,-1:4,76,-1:4,79,-1:4,82,-1:15,58,-1:11,54,-1" +":4,71,-1:4,76,-1:4,79,-1:4,82,-1:16,59,-1:10,54,-1:4,71,-1:4,76,-1:4,79,-1:" +"4,82,-1:18,73:3,-1:2,167:16,131,167:5,-1,167:2,-1,167,-1:15,167,100:3,167,-" +"1,132,167:21,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:19,63,167:2,-1,167:" +"2,-1,167,-1:15,167,100:3,167,-1,167:9,84,167,81,167:4,81,167:5,-1,167:2,-1," +"167,-1:15,167,100:3,167,-1,167:22,-1,167:2,-1,167,-1:15,167,100:3,167,-1,16" +"7:6,43,167:15,-1,167:2,-1,167,-1:15,167,100:3,167,-1,141,167:21,-1,167:2,-1" +",167,-1:15,167,100:3,167,-1,144,167:21,-1,167:2,-1,167,-1:15,167,100:3,167," +"-1,167:12,44,167:4,145,167:4,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167,62," +"167:20,-1,167:2,-1,167,-1:15,167,100:3,167,-1:45,38:2,-1:2,39,-1:7,39:2,-1:" +"8,39:2,-1:6,39,-1:16,39:4,-1:3,99,-1:45,167:4,89,167:3,18,92,167:4,94,167:4" +",96,167:2,-1,167,98,-1,167,-1:15,167,100:3,167,-1:3,22,-1,54,-1:4,71,85,-1:" +"2,88,76,-1:4,79,-1:4,82,-1,91,-1:25,54,-1:4,71,-1:4,76,-1:4,79,-1:4,82,-1:1" +"1,93,-1:51,58,-1:48,59,-1:10,54,-1:4,71,-1:4,76,-1:4,95,97,-1:3,82,-1:18,73" +":3,-1:2,167:7,81,167,87,167:11,81,-1,167:2,-1,167,-1:15,167,100:3,167,-1:38" +",37,-1:22,101,-1:35,167:2,19,167,89,167:4,92,167:4,94,167:4,96,167:2,-1,167" +",98,-1,167,-1:15,167,100:3,167,-1:44,73:3,-1:2,132,167:13,40,167:7,-1,167:2" +",-1,167,-1:15,167,100:3,167,-1,167:12,90,167:9,-1,167:2,-1,167,-1:15,167,10" +"0:3,167,-1:2,150,-1:46,167,114:2,167,89,167:4,116,167,20,167:2,94,167,117,1" +"67:2,96,167:2,-1,167,98,-1,167,-1:15,167,100:3,167,-1,167,157,167:7,84,167," +"81,167:4,81,167:5,-1,167:2,-1,167,-1:15,167,100:3,167,-1:24,103,-1:24,124,2" +"1,167:2,89,124,167,64,167,75,167:3,152,78,167:3,81,96,167,81,-1,167,98,-1,1" +"67,-1:15,167,100:3,167,-1:5,105,-1:43,167:2,23,167:19,-1,167:2,-1,167,-1:15" +",167,100:3,167,-1,167:16,81,167:5,-1,167:2,-1,167,-1:15,167,100:3,167,-1:14" +",34,-1:34,167:13,24,167:8,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:14,81," +"167:7,-1,167:2,-1,167,-1:15,167,100:3,167,-1,35,-1:47,167:2,126,167:19,-1,1" +"67:2,-1,167,-1:15,167,100:3,167,-1,167:16,41,167:5,-1,167:2,-1,167,-1:15,16" +"7,100:3,167,-1:14,36,-1:34,167:12,127,167:9,-1,167:2,-1,167,-1:15,167,100:3" +",167,-1,93:37,37,65,37,93:7,-1,167,157,167:20,-1,167:2,-1,167,-1:15,167,100" +":3,167,-1:24,103,-1:20,38:2,-1:2,167:22,-1,129,167,-1,167,-1:15,167,100:3,1" +"67,-1,167:4,130,167:17,-1,167:2,-1,167,-1:15,167,100:3,167,-1:15,66,-1:33,1" +"00:22,-1,100:2,-1,100,-1:15,100:5,-1:17,67,-1:31,167:11,25,127,167:9,-1,167" +":2,-1,167,-1:15,167,100:3,167,-1:12,109,-1:36,167:2,126,167:2,26,167:16,-1," +"167:2,-1,167,-1:15,167,100:3,167,-1:3,111,-1:45,27,167:21,-1,167:2,-1,167,-" +"1:15,167,100:3,167,-1:20,113,-1:28,167:2,126,167:8,133,167,28,167:8,-1,167:" +"2,-1,167,-1:15,167,100:3,167,-1:10,68,-1:38,167:12,127,167,153,167:7,-1,167" +":2,-1,167,-1:15,167,100:3,167,-1:9,69,-1:39,29,167:11,127,167:9,-1,167:2,-1" +",167,-1:15,167,100:3,167,-1:19,115,-1:30,70,-1:46,167:11,30,127,167:9,-1,16" +"7:2,-1,167,-1:15,167,100:3,167,-1,167:19,19,167:2,-1,167:2,-1,167,-1:15,167" +",100:3,167,-1,167:11,31,167:10,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:2" +",126,167:10,32,167:8,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:10,134,167:" +"11,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:13,33,167:8,-1,167:2,-1,167,-" +"1:15,167,100:3,167,-1,167:16,135,167:5,-1,167:2,-1,167,-1:15,167,100:3,167," +"-1,167:8,19,167:13,-1,167:2,-1,167,-1:15,167,100:3,167,-1,81,167:9,81,167:1" +"1,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:2,74,167:19,-1,167:2,-1,167,-1" +":15,167,100:3,167,-1,167:14,40,167:7,-1,167:2,-1,167,-1:15,167,100:3,167,-1" +",167,42,167:20,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:11,139,167:10,-1," +"167:2,-1,167,-1:15,167,100:3,167,-1,167:2,140,167:19,-1,167:2,-1,167,-1:15," +"167,100:3,167,-1,167:19,61,167:2,-1,167:2,-1,167,-1:15,167,100:3,167,-1,62," +"167:21,-1,167:2,-1,167,-1:15,167,100:3,167,-1,142,167:21,-1,167:2,-1,167,-1" +":15,167,100:3,167,-1,167:17,45,167:4,-1,167:2,-1,167,-1:15,167,100:3,167,-1" +",167,146,167:20,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:10,81,167:11,-1," +"167:2,-1,167,-1:15,167,100:3,167,-1,167:2,19,167:19,-1,167:2,-1,167,-1:15,1" +"67,100:3,167,-1,167:19,147,167:2,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167" +":9,46,167:12,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:8,47,167:13,-1,167:" +"2,-1,167,-1:15,167,100:3,167,-1,48,167:21,-1,167:2,-1,167,-1:15,167,100:3,1" +"67,-1,49,167:21,-1,167:2,-1,167,-1:15,167,100:3,167,-1,50,167:21,-1,167:2,-" +"1,167,-1:15,167,100:3,167,-1,51,167:21,-1,167:2,-1,167,-1:15,167,100:3,167," +"-1,167:6,52,167:15,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:18,148,167:3," +"-1,167:2,-1,167,-1:15,167,100:3,167,-1,167,53,167:20,-1,167:2,-1,167,-1:15," +"167,100:3,167,-1,167:4,89,167:4,102,167:4,94,167:4,96,167:2,-1,167,98,-1,16" +"7,-1:15,167,100:3,167,-1:5,107,-1:43,167,128,167:20,-1,167:2,-1,167,-1:15,1" +"67,100:3,167,-1,167:4,136,167:17,-1,167:2,-1,167,-1:15,167,100:3,167,-1,143" +",167:21,-1,167:2,-1,167,-1:15,167,100:3,167,-1,167:4,104,167:4,92,167:4,94," +"167:4,96,167:2,-1,167,98,-1,167,-1:15,167,100:3,167,-1,167:4,137,167:17,-1," +"167:2,-1,167,-1:15,167,100:3,167,-1,167:4,89,167:4,92,167:4,94,167:4,96,167" +":2,-1,167,98,-1,167,-1:15,167,100:3,167,-1,167:4,138,167:17,-1,167:2,-1,167" +",-1:15,167,100:3,167,-1,167:4,89,167:2,106,167,92,167:4,94,167:4,96,167:2,-" +"1,167,98,-1,167,-1:15,167,100:3,167,-1,167:4,108,167:4,110,167:4,94,167:4,9" +"6,167:2,-1,167,98,-1,167,-1:15,167,100:3,167,-1,167:4,89,167:4,112,167:4,94" +",167:4,96,167:2,-1,167,98,-1,167,-1:15,167,100:3,167,-1,167:4,89,167:4,92,1" +"18,167:3,94,167:4,96,167:2,-1,167,98,-1,167,-1:15,167,100:3,167,-1,167:4,11" +"9,167:4,92,167:4,94,167,120,167:2,96,167:2,-1,167,98,-1,167,-1:15,167,100:3" +",167,-1,167:3,121,89,167:4,92,167:4,94,167:4,96,167:2,-1,167,98,-1,167,-1:1" +"5,167,100:3,167,-1,167:3,122,89,167:4,92,167:4,94,167:4,96,167:2,-1,167,98," +"-1,167,-1:15,167,100:3,167,-1,167:4,89,167:2,123:2,92,167:4,94,167:4,96,167" +":2,-1,167,98,-1,167,-1:15,167,100:3,167,-1,167:4,125,167:2,155,167,92,167:4" +",94,167:4,96,167:2,-1,167,98,-1,167,-1:15,167,100:3,167");

	public Yytoken yylex ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

	return new Yytoken(LEX_CRAPS.EOF , "EOF" , yyline , yychar , yychar+1 ) ;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					
case 1:
					
	
					
case -2:
					
	break;
					
case 2:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -3:
					
	break;
					
case 3:
					
	{return new Yytoken(LEX_CRAPS.token_autre, yytext(), yyline, yychar, yychar+1);}
					
case -4:
					
	break;
					
case 4:
					
	{return new Yytoken(LEX_CRAPS.token_equals, yytext(), yyline, yychar, yychar+1);}
					
case -5:
					
	break;
					
case 5:
					
	{return new Yytoken(LEX_CRAPS.token_semicol, yytext(), yyline, yychar, yychar+1);}
					
case -6:
					
	break;
					
case 6:
					
	{return new Yytoken(LEX_CRAPS.token_virg, yytext(), yyline, yychar, yychar+1);}
					
case -7:
					
	break;
					
case 7:
					
	{return new Yytoken(LEX_CRAPS.token_lbra, yytext(), yyline, yychar, yychar+1);}
					
case -8:
					
	break;
					
case 8:
					
	{return new Yytoken(LEX_CRAPS.token_rbra, yytext(), yyline, yychar, yychar+1);}
					
case -9:
					
	break;
					
case 9:
					
	{return new Yytoken(LEX_CRAPS.token_lpar, yytext(), yyline, yychar, yychar+1);}
					
case -10:
					
	break;
					
case 10:
					
	{return new Yytoken(LEX_CRAPS.token_rpar, yytext(), yyline, yychar, yychar+1);}
					
case -11:
					
	break;
					
case 11:
					
	{return new Yytoken(LEX_CRAPS.token_plus_minus, yytext(), yyline, yychar, yychar+1);}
					
case -12:
					
	break;
					
case 12:
					
	{return new Yytoken(LEX_CRAPS.token_mult_div, yytext(), yyline, yychar, yychar+1);}
					
case -13:
					
	break;
					
case 13:
					
	{return new Yytoken(LEX_CRAPS.token_rc, yytext(), yyline, yychar, yychar+1);}
					
case -14:
					
	break;
					
case 14:
					
	{return new Yytoken(LEX_CRAPS.token_lfs, yytext(), yyline, yychar, yychar+1);}
					
case -15:
					
	break;
					
case 15:
					
	{return new Yytoken(LEX_CRAPS.token_blank, yytext(), yyline, yychar, yychar+1);}
					
case -16:
					
	break;
					
case 16:
					
	{return new Yytoken(LEX_CRAPS.token_tabs, yytext(), yyline, yychar, yychar+1);}
					
case -17:
					
	break;
					
case 17:
					
	{return new Yytoken(LEX_CRAPS.token_num10, yytext(), yyline, yychar, yychar+1);}
					
case -18:
					
	break;
					
case 18:
					
	{return new Yytoken(LEX_CRAPS.token_ld_ldub, yytext(), yyline, yychar, yychar+1);}
					
case -19:
					
	break;
					
case 19:
					
	{return new Yytoken(LEX_CRAPS.token_codeop3, yytext(), yyline, yychar, yychar+1);}
					
case -20:
					
	break;
					
case 20:
					
	{return new Yytoken(LEX_CRAPS.token_st_stb, yytext(), yyline, yychar, yychar+1);}
					
case -21:
					
	break;
					
case 21:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -22:
					
	break;
					
case 22:
					
	{return new Yytoken(LEX_CRAPS.token_r, yytext(), yyline, yychar, yychar+1);}
					
case -23:
					
	break;
					
case 23:
					
	{return new Yytoken(LEX_CRAPS.token_clr, yytext(), yyline, yychar, yychar+1);}
					
case -24:
					
	break;
					
case 24:
					
	{return new Yytoken(LEX_CRAPS.token_cmp, yytext(), yyline, yychar, yychar+1);}
					
case -25:
					
	break;
					
case 25:
					
	{return new Yytoken(LEX_CRAPS.token_ret, yytext(), yyline, yychar, yychar+1);}
					
case -26:
					
	break;
					
case 26:
					
	{return new Yytoken(LEX_CRAPS.token_mov, yytext(), yyline, yychar, yychar+1);}
					
case -27:
					
	break;
					
case 27:
					
	{return new Yytoken(LEX_CRAPS.token_inc, yytext(), yyline, yychar, yychar+1);}
					
case -28:
					
	break;
					
case 28:
					
	{return new Yytoken(LEX_CRAPS.token_nop, yytext(), yyline, yychar, yychar+1);}
					
case -29:
					
	break;
					
case 29:
					
	{return new Yytoken(LEX_CRAPS.token_dec, yytext(), yyline, yychar, yychar+1);}
					
case -30:
					
	break;
					
case 30:
					
	{return new Yytoken(LEX_CRAPS.token_set, yytext(), yyline, yychar, yychar+1);}
					
case -31:
					
	break;
					
case 31:
					
	{return new Yytoken(LEX_CRAPS.token_tst, yytext(), yyline, yychar, yychar+1);}
					
case -32:
					
	break;
					
case 32:
					
	{return new Yytoken(LEX_CRAPS.token_pop, yytext(), yyline, yychar, yychar+1);}
					
case -33:
					
	break;
					
case 33:
					
	{return new Yytoken(LEX_CRAPS.token_jmp, yytext(), yyline, yychar, yychar+1);}
					
case -34:
					
	break;
					
case 34:
					
	{return new Yytoken(LEX_CRAPS.token_sp, yytext(), yyline, yychar, yychar+1);}
					
case -35:
					
	break;
					
case 35:
					
	{return new Yytoken(LEX_CRAPS.token_pc, yytext(), yyline, yychar, yychar+1);}
					
case -36:
					
	break;
					
case 36:
					
	{return new Yytoken(LEX_CRAPS.token_fp, yytext(), yyline, yychar, yychar+1);}
					
case -37:
					
	break;
					
case 37:
					
	{return new Yytoken(LEX_CRAPS.token_comm, yytext(), yyline, yychar, yychar+1);}
					
case -38:
					
	break;
					
case 38:
					
	{return new Yytoken(LEX_CRAPS.token_num2, yytext(), yyline, yychar, yychar+1);}
					
case -39:
					
	break;
					
case 39:
					
	{return new Yytoken(LEX_CRAPS.token_num16, yytext(), yyline, yychar, yychar+1);}
					
case -40:
					
	break;
					
case 40:
					
	{return new Yytoken(LEX_CRAPS.token_org, yytext(), yyline, yychar, yychar+1);}
					
case -41:
					
	break;
					
case 41:
					
	{return new Yytoken(LEX_CRAPS.token_equ, yytext(), yyline, yychar, yychar+1);}
					
case -42:
					
	break;
					
case 42:
					
	{return new Yytoken(LEX_CRAPS.token_call, yytext(), yyline, yychar, yychar+1);}
					
case -43:
					
	break;
					
case 43:
					
	{return new Yytoken(LEX_CRAPS.token_reti, yytext(), yyline, yychar, yychar+1);}
					
case -44:
					
	break;
					
case 44:
					
	{return new Yytoken(LEX_CRAPS.token_setq, yytext(), yyline, yychar, yychar+1);}
					
case -45:
					
	break;
					
case 45:
					
	{return new Yytoken(LEX_CRAPS.token_push, yytext(), yyline, yychar, yychar+1);}
					
case -46:
					
	break;
					
case 46:
					
	{return new Yytoken(LEX_CRAPS.token_byte, yytext(), yyline, yychar, yychar+1);}
					
case -47:
					
	break;
					
case 47:
					
	{return new Yytoken(LEX_CRAPS.token_word, yytext(), yyline, yychar, yychar+1);}
					
case -48:
					
	break;
					
case 48:
					
	{return new Yytoken(LEX_CRAPS.token_inccc, yytext(), yyline, yychar, yychar+1);}
					
case -49:
					
	break;
					
case 49:
					
	{return new Yytoken(LEX_CRAPS.token_notcc, yytext(), yyline, yychar, yychar+1);}
					
case -50:
					
	break;
					
case 50:
					
	{return new Yytoken(LEX_CRAPS.token_negcc, yytext(), yyline, yychar, yychar+1);}
					
case -51:
					
	break;
					
case 51:
					
	{return new Yytoken(LEX_CRAPS.token_deccc, yytext(), yyline, yychar, yychar+1);}
					
case -52:
					
	break;
					
case 52:
					
	{return new Yytoken(LEX_CRAPS.token_sethi, yytext(), yyline, yychar, yychar+1);}
					
case -53:
					
	break;
					
case 53:
					
	{return new Yytoken(LEX_CRAPS.token_global_, yytext(), yyline, yychar, yychar+1);}
					
case -54:
					
	break;
					
case 55:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -55:
					
	break;
					
case 56:
					
	{return new Yytoken(LEX_CRAPS.token_autre, yytext(), yyline, yychar, yychar+1);}
					
case -56:
					
	break;
					
case 57:
					
	{return new Yytoken(LEX_CRAPS.token_mult_div, yytext(), yyline, yychar, yychar+1);}
					
case -57:
					
	break;
					
case 58:
					
	{return new Yytoken(LEX_CRAPS.token_blank, yytext(), yyline, yychar, yychar+1);}
					
case -58:
					
	break;
					
case 59:
					
	{return new Yytoken(LEX_CRAPS.token_tabs, yytext(), yyline, yychar, yychar+1);}
					
case -59:
					
	break;
					
case 60:
					
	{return new Yytoken(LEX_CRAPS.token_num10, yytext(), yyline, yychar, yychar+1);}
					
case -60:
					
	break;
					
case 61:
					
	{return new Yytoken(LEX_CRAPS.token_ld_ldub, yytext(), yyline, yychar, yychar+1);}
					
case -61:
					
	break;
					
case 62:
					
	{return new Yytoken(LEX_CRAPS.token_codeop3, yytext(), yyline, yychar, yychar+1);}
					
case -62:
					
	break;
					
case 63:
					
	{return new Yytoken(LEX_CRAPS.token_st_stb, yytext(), yyline, yychar, yychar+1);}
					
case -63:
					
	break;
					
case 64:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -64:
					
	break;
					
case 65:
					
	{return new Yytoken(LEX_CRAPS.token_comm, yytext(), yyline, yychar, yychar+1);}
					
case -65:
					
	break;
					
case 66:
					
	{return new Yytoken(LEX_CRAPS.token_org, yytext(), yyline, yychar, yychar+1);}
					
case -66:
					
	break;
					
case 67:
					
	{return new Yytoken(LEX_CRAPS.token_equ, yytext(), yyline, yychar, yychar+1);}
					
case -67:
					
	break;
					
case 68:
					
	{return new Yytoken(LEX_CRAPS.token_byte, yytext(), yyline, yychar, yychar+1);}
					
case -68:
					
	break;
					
case 69:
					
	{return new Yytoken(LEX_CRAPS.token_word, yytext(), yyline, yychar, yychar+1);}
					
case -69:
					
	break;
					
case 70:
					
	{return new Yytoken(LEX_CRAPS.token_global_, yytext(), yyline, yychar, yychar+1);}
					
case -70:
					
	break;
					
case 72:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -71:
					
	break;
					
case 73:
					
	{return new Yytoken(LEX_CRAPS.token_num10, yytext(), yyline, yychar, yychar+1);}
					
case -72:
					
	break;
					
case 74:
					
	{return new Yytoken(LEX_CRAPS.token_codeop3, yytext(), yyline, yychar, yychar+1);}
					
case -73:
					
	break;
					
case 75:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -74:
					
	break;
					
case 77:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -75:
					
	break;
					
case 78:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -76:
					
	break;
					
case 80:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -77:
					
	break;
					
case 81:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -78:
					
	break;
					
case 83:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -79:
					
	break;
					
case 84:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -80:
					
	break;
					
case 86:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -81:
					
	break;
					
case 87:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -82:
					
	break;
					
case 89:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -83:
					
	break;
					
case 90:
					
	{return new Yytoken(LEX_CRAPS.token_branch, yytext(), yyline, yychar, yychar+1);}
					
case -84:
					
	break;
					
case 92:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -85:
					
	break;
					
case 94:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -86:
					
	break;
					
case 96:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -87:
					
	break;
					
case 98:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -88:
					
	break;
					
case 100:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -89:
					
	break;
					
case 102:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -90:
					
	break;
					
case 104:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -91:
					
	break;
					
case 106:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -92:
					
	break;
					
case 108:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -93:
					
	break;
					
case 110:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -94:
					
	break;
					
case 112:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -95:
					
	break;
					
case 114:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -96:
					
	break;
					
case 116:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -97:
					
	break;
					
case 117:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -98:
					
	break;
					
case 118:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -99:
					
	break;
					
case 119:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -100:
					
	break;
					
case 120:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -101:
					
	break;
					
case 121:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -102:
					
	break;
					
case 122:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -103:
					
	break;
					
case 123:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -104:
					
	break;
					
case 124:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -105:
					
	break;
					
case 125:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -106:
					
	break;
					
case 126:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -107:
					
	break;
					
case 127:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -108:
					
	break;
					
case 128:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -109:
					
	break;
					
case 129:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -110:
					
	break;
					
case 130:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -111:
					
	break;
					
case 131:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -112:
					
	break;
					
case 132:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -113:
					
	break;
					
case 133:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -114:
					
	break;
					
case 134:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -115:
					
	break;
					
case 135:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -116:
					
	break;
					
case 136:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -117:
					
	break;
					
case 137:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -118:
					
	break;
					
case 138:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -119:
					
	break;
					
case 139:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -120:
					
	break;
					
case 140:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -121:
					
	break;
					
case 141:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -122:
					
	break;
					
case 142:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -123:
					
	break;
					
case 143:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -124:
					
	break;
					
case 144:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -125:
					
	break;
					
case 145:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -126:
					
	break;
					
case 146:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -127:
					
	break;
					
case 147:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -128:
					
	break;
					
case 148:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -129:
					
	break;
					
case 149:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -130:
					
	break;
					
case 151:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -131:
					
	break;
					
case 152:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -132:
					
	break;
					
case 153:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -133:
					
	break;
					
case 154:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -134:
					
	break;
					
case 155:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -135:
					
	break;
					
case 156:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -136:
					
	break;
					
case 157:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -137:
					
	break;
					
case 158:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -138:
					
	break;
					
case 159:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -139:
					
	break;
					
case 160:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -140:
					
	break;
					
case 161:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -141:
					
	break;
					
case 162:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -142:
					
	break;
					
case 163:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -143:
					
	break;
					
case 164:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -144:
					
	break;
					
case 165:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -145:
					
	break;
					
case 166:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -146:
					
	break;
					
case 167:
					
	{return new Yytoken(LEX_CRAPS.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -147:
					
	break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
