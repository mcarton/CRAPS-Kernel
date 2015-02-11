package org.jcb.shdl.netc.java;
import mg.egg.eggc.libjava.lex.*;

public class JLEX_NET implements  LEX_ANALYZER  {
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

	public JLEX_NET (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public JLEX_NET (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	public JLEX_NET () {
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
		/* 22 */ YY_END,
		/* 23 */ YY_END,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NOT_ACCEPT,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_END,
		/* 38 */ YY_END,
		/* 39 */ YY_NOT_ACCEPT,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NOT_ACCEPT,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NOT_ACCEPT,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
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
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,258,
"2:9,7,3,2:2,4,2:18,6,2:7,30,31,26,27,32,28,38,1,41,43,40:8,35,34,2,33,29,2:" +"2,45:6,39:20,36,2,37,2,23,2,22,42,17,10,13,45,39,25,19,39,18,12,8,20,9,21,3" +"9,14,15,16,11,39,24,44,39:2,2:133,0,5")[0];

	private int yy_rmap[] = unpackFromString(1,82,
"0,1,2,1,3,4,5,6,7,1,8,1:8,9,1:4,10,11,12:7,13,14,15,16,17,18,19,20,21,10,22" +",11,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46" +",47,48,49,50,51,52,53,54,55,56,12,57,58")[0];

	private int yy_nxt[][] = unpackFromString(59,46,
"1,2,3,4,5,1,6,7,8,78,79:4,63,79:2,64,79,80,79:2,81,79,56,79,9,10,34,3,11,12" +",13,14,15,16,17,18,40,79,19,36,79,19,79:2,-1:47,33,-1:47,4,5,-1:44,5:2,-1:4" +"7,6,-1:46,7,-1:46,79,65,79:16,-1:13,79,66:2,79,66,79:2,-1:27,39,-1:58,19:2," +"-1,19,-1:43,24,-1,24,-1:12,25,-1:2,25,-1:3,25,-1:4,25,-1:17,25:4,-1,25,-1:8" +",79:18,-1:13,79,66:2,79,66,79:2,-1,33:2,22,37,22,33:40,-1:29,20,-1:10,19:2," +"-1,19,-1:10,79:12,26,79:5,-1:13,79,66:2,79,66,79:2,-1:40,19:2,42,19,44,-1:4" +",22,-1:45,23,-1:43,39:2,23,38,23,39:40,-1:38,21,-1:15,79:8,27,79:9,-1:13,79" +",66:2,79,66,79:2,-1:8,79:10,28,79:7,-1:13,79,66:2,79,66,79:2,-1:8,79:5,29,7" +"9:12,-1:13,79,66:2,79,66,79:2,-1:8,79:7,30,79:10,-1:13,79,66:2,79,66,79:2,-" +"1:8,79:7,31,79:10,-1:13,79,66:2,79,66,79:2,-1:8,79:7,32,79:10,-1:13,79,66:2" +",79,66,79:2,-1:8,79:5,35,79:12,-1:13,79,66:2,79,66,79:2,-1:8,79:5,41,79:12," +"-1:13,79,66:2,79,66,79:2,-1:8,79:9,43,79:8,-1:13,79,66:2,79,66,79:2,-1:8,79" +":4,45,79:13,-1:13,79,66:2,79,66,79:2,-1:8,79:8,46,79:9,-1:13,79,66:2,79,66," +"79:2,-1:8,79:8,47,79:9,-1:13,79,66:2,79,66,79:2,-1:8,79:8,48,79:9,-1:13,79," +"66:2,79,66,79:2,-1:8,79:17,49,-1:13,79,66:2,79,66,79:2,-1:8,79:7,50,79:10,-" +"1:13,79,66:2,79,66,79:2,-1:8,79,51,79:16,-1:13,79,66:2,79,66,79:2,-1:8,79:3" +",52,79:14,-1:13,79,66:2,79,66,79:2,-1:8,79:3,53,79:14,-1:13,79,66:2,79,66,7" +"9:2,-1:8,79:3,54,79:14,-1:13,79,66:2,79,66,79:2,-1:8,79:3,55,79:14,-1:13,79" +",66:2,79,66,79:2,-1:8,79:5,57,79:12,-1:13,79,66:2,79,66,79:2,-1:8,79:4,58,7" +"9:13,-1:13,79,66:2,79,66,79:2,-1:8,79:2,59,79:15,-1:13,79,66:2,79,66,79:2,-" +"1:8,66:18,-1:13,66:7,-1:8,79:8,70,79:9,-1:13,79,66:2,79,66,79:2,-1:8,79:13," +"60,79:4,-1:13,79,66:2,79,66,79:2,-1:8,79:2,71,79:15,-1:13,79,66:2,79,66,79:" +"2,-1:8,79:13,61,79:4,-1:13,79,66:2,79,66,79:2,-1:8,79:5,72,79:12,-1:13,79,6" +"6:2,79,66,79:2,-1:8,79:2,73,79:15,-1:13,79,66:2,79,66,79:2,-1:8,79:15,74,79" +":2,-1:13,79,66:2,79,66,79:2,-1:8,79,75,79:16,-1:13,79,66:2,79,66,79:2,-1:8," +"79:3,76,79:14,-1:13,79,66:2,79,66,79:2,-1:8,79:8,77,79:9,-1:13,79,66:2,79,6" +"6,79:2,-1:8,79:13,62,79:4,-1:13,79,66:2,79,66,79:2,-1:8,79:3,67,79:14,-1:13" +",79,66:2,79,66,79:2,-1:8,79:12,68,79:5,-1:13,79,66:2,79,66,79:2,-1:8,79:2,6" +"9,79:15,-1:13,79,66:2,79,66,79:2");

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

	return new Yytoken(LEX_NET.EOF , "EOF" , yyline , yychar , yychar+1 ) ;
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
					
	{return new Yytoken(LEX_NET.token_slash, yytext(), yyline, yychar, yychar+1);}
					
case -3:
					
	break;
					
case 3:
					
	{return new Yytoken(LEX_NET.token_autre, yytext(), yyline, yychar, yychar+1);}
					
case -4:
					
	break;
					
case 4:
					
	{return new Yytoken(LEX_NET.token_rc, yytext(), yyline, yychar, yychar+1);}
					
case -5:
					
	break;
					
case 5:
					
	{return new Yytoken(LEX_NET.token_lfs, yytext(), yyline, yychar, yychar+1);}
					
case -6:
					
	break;
					
case 6:
					
	{return new Yytoken(LEX_NET.token_blank, yytext(), yyline, yychar, yychar+1);}
					
case -7:
					
	break;
					
case 7:
					
	{return new Yytoken(LEX_NET.token_tabs, yytext(), yyline, yychar, yychar+1);}
					
case -8:
					
	break;
					
case 8:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -9:
					
	break;
					
case 9:
					
	{return new Yytoken(LEX_NET.token_and, yytext(), yyline, yychar, yychar+1);}
					
case -10:
					
	break;
					
case 10:
					
	{return new Yytoken(LEX_NET.token_or, yytext(), yyline, yychar, yychar+1);}
					
case -11:
					
	break;
					
case 11:
					
	{return new Yytoken(LEX_NET.token_lpar, yytext(), yyline, yychar, yychar+1);}
					
case -12:
					
	break;
					
case 12:
					
	{return new Yytoken(LEX_NET.token_rpar, yytext(), yyline, yychar, yychar+1);}
					
case -13:
					
	break;
					
case 13:
					
	{return new Yytoken(LEX_NET.token_virg, yytext(), yyline, yychar, yychar+1);}
					
case -14:
					
	break;
					
case 14:
					
	{return new Yytoken(LEX_NET.token_egal, yytext(), yyline, yychar, yychar+1);}
					
case -15:
					
	break;
					
case 15:
					
	{return new Yytoken(LEX_NET.token_pv, yytext(), yyline, yychar, yychar+1);}
					
case -16:
					
	break;
					
case 16:
					
	{return new Yytoken(LEX_NET.token_semicol, yytext(), yyline, yychar, yychar+1);}
					
case -17:
					
	break;
					
case 17:
					
	{return new Yytoken(LEX_NET.token_crocouv, yytext(), yyline, yychar, yychar+1);}
					
case -18:
					
	break;
					
case 18:
					
	{return new Yytoken(LEX_NET.token_crocfer, yytext(), yyline, yychar, yychar+1);}
					
case -19:
					
	break;
					
case 19:
					
	{return new Yytoken(LEX_NET.token_num10, yytext(), yyline, yychar, yychar+1);}
					
case -20:
					
	break;
					
case 20:
					
	{return new Yytoken(LEX_NET.token_arrow, yytext(), yyline, yychar, yychar+1);}
					
case -21:
					
	break;
					
case 21:
					
	{return new Yytoken(LEX_NET.token_ptpt, yytext(), yyline, yychar, yychar+1);}
					
case -22:
					
	break;
					
case 22:
					
	{return new Yytoken(LEX_NET.token_comm, yytext(), yyline, yychar, yychar+1);}
					
case -23:
					
	break;
					
case 23:
					
	{return new Yytoken(LEX_NET.token_added, yytext(), yyline, yychar, yychar+1);}
					
case -24:
					
	break;
					
case 24:
					
	{return new Yytoken(LEX_NET.token_num2, yytext(), yyline, yychar, yychar+1);}
					
case -25:
					
	break;
					
case 25:
					
	{return new Yytoken(LEX_NET.token_num16, yytext(), yyline, yychar, yychar+1);}
					
case -26:
					
	break;
					
case 26:
					
	{return new Yytoken(LEX_NET.token_when, yytext(), yyline, yychar, yychar+1);}
					
case -27:
					
	break;
					
case 27:
					
	{return new Yytoken(LEX_NET.token_reset, yytext(), yyline, yychar, yychar+1);}
					
case -28:
					
	break;
					
case 28:
					
	{return new Yytoken(LEX_NET.token_clock, yytext(), yyline, yychar, yychar+1);}
					
case -29:
					
	break;
					
case 29:
					
	{return new Yytoken(LEX_NET.token_module, yytext(), yyline, yychar, yychar+1);}
					
case -30:
					
	break;
					
case 30:
					
	{return new Yytoken(LEX_NET.token_inputs, yytext(), yyline, yychar, yychar+1);}
					
case -31:
					
	break;
					
case 31:
					
	{return new Yytoken(LEX_NET.token_outputs, yytext(), yyline, yychar, yychar+1);}
					
case -32:
					
	break;
					
case 32:
					
	{return new Yytoken(LEX_NET.token_added_outputs, yytext(), yyline, yychar, yychar+1);}
					
case -33:
					
	break;
					
case 34:
					
	{return new Yytoken(LEX_NET.token_autre, yytext(), yyline, yychar, yychar+1);}
					
case -34:
					
	break;
					
case 35:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -35:
					
	break;
					
case 36:
					
	{return new Yytoken(LEX_NET.token_num10, yytext(), yyline, yychar, yychar+1);}
					
case -36:
					
	break;
					
case 37:
					
	{return new Yytoken(LEX_NET.token_comm, yytext(), yyline, yychar, yychar+1);}
					
case -37:
					
	break;
					
case 38:
					
	{return new Yytoken(LEX_NET.token_added, yytext(), yyline, yychar, yychar+1);}
					
case -38:
					
	break;
					
case 40:
					
	{return new Yytoken(LEX_NET.token_autre, yytext(), yyline, yychar, yychar+1);}
					
case -39:
					
	break;
					
case 41:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -40:
					
	break;
					
case 43:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -41:
					
	break;
					
case 45:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -42:
					
	break;
					
case 46:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -43:
					
	break;
					
case 47:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -44:
					
	break;
					
case 48:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -45:
					
	break;
					
case 49:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -46:
					
	break;
					
case 50:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -47:
					
	break;
					
case 51:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -48:
					
	break;
					
case 52:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -49:
					
	break;
					
case 53:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -50:
					
	break;
					
case 54:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -51:
					
	break;
					
case 55:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -52:
					
	break;
					
case 56:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -53:
					
	break;
					
case 57:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -54:
					
	break;
					
case 58:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -55:
					
	break;
					
case 59:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -56:
					
	break;
					
case 60:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -57:
					
	break;
					
case 61:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -58:
					
	break;
					
case 62:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -59:
					
	break;
					
case 63:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -60:
					
	break;
					
case 64:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -61:
					
	break;
					
case 65:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -62:
					
	break;
					
case 66:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -63:
					
	break;
					
case 67:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -64:
					
	break;
					
case 68:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -65:
					
	break;
					
case 69:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -66:
					
	break;
					
case 70:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -67:
					
	break;
					
case 71:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -68:
					
	break;
					
case 72:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -69:
					
	break;
					
case 73:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -70:
					
	break;
					
case 74:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -71:
					
	break;
					
case 75:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -72:
					
	break;
					
case 76:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -73:
					
	break;
					
case 77:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -74:
					
	break;
					
case 78:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -75:
					
	break;
					
case 79:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -76:
					
	break;
					
case 80:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -77:
					
	break;
					
case 81:
					
	{return new Yytoken(LEX_NET.token_ident, yytext(), yyline, yychar, yychar+1);}
					
case -78:
					
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
