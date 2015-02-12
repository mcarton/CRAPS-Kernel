package org.mmek.craps.crapsc;

import java.io.PrintStream;
import java.text.ParseException;

public class BinNum {

    public BinNum() {}

    public static int parseUnsigned(String str) throws ParseException {
        int val = 0;
        for(int i = 0; i < str.length(); i++) {
            int nc = "01".indexOf(str.substring(i, i + 1));
            if(nc == -1)
                throw new ParseException((new StringBuilder("bad bin number: ")).append(str).toString(), i);
            val = val * 2 + nc;
        }

        return val;
    }

    public static int parseSigned16(String str) throws ParseException {
        if(str.length() != 16)
            throw new ParseException((new StringBuilder("length must be 16: ")).append(str).toString(), 0);
        boolean isNeg = str.charAt(0) == '1';
        int val = 0;

        for(int i = 1; i < 16; i++) {
            int nc = "01".indexOf(str.substring(i, i + 1));
            if(nc == -1)
                throw new ParseException((new StringBuilder("bad bin number: ")).append(str).toString(), i);
            val = val * 2 + nc;
        }

        if(isNeg)
            return val - 32768;
        else
            return val;
    }

    public static String formatUnsigned16(int n) {
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < 16; i++) {
            int r = n % 2;
            res.insert(0, (new StringBuilder(String.valueOf(r))).toString());
            n /= 2;
        }

        return res.toString();
    }

    public static String formatUnsigned32(long n) {
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < 32; i++) {
            long r = n % 2L;
            if(r < 0L)
                r += 2L;
            res.insert(0, (new StringBuilder(String.valueOf(r))).toString());
            n /= 2L;
        }

        return res.toString();
    }

    public static String formatUnsigned(int n, int nbDigit) {
        StringBuffer res = new StringBuffer();
        for(int i = 0; i < nbDigit; i++) {
            int r = n % 2;
            res.insert(0, (new StringBuilder(String.valueOf(r))).toString());
            n /= 2;
        }

        return res.toString();
    }

    public static String formatUnsigned(int n) {
        StringBuffer res = new StringBuffer();
        do {
            int r = n % 2;
            res.insert(0, (new StringBuilder(String.valueOf(r))).toString());
            n /= 2;
        } while(n > 0);

        return res.toString();
    }

    public static int nbBinDigits(int n) {
        if(n == 0)
            return 1;
        int nb;
        for(nb = 0; n > 0; nb++)
            n /= 2;

        return nb;
    }

    public static String formatSigned16(int n) {
        StringBuffer res = new StringBuffer();

        if(n < 0)
            n += 0x10000;

        for(int i = 0; i < 16; i++) {
            int r = n % 2;
            res.insert(0, (new StringBuilder(String.valueOf(r))).toString());
            n /= 2;
        }

        return res.toString();
    }

    public static void main(String args[]) {
        try {
            int n = 12;
            String sn = formatSigned16(n);
            int nc = parseUnsigned(sn);
            int sc = parseSigned16(sn);
            System.out.println((new StringBuilder("n=")).append(n).append(", sn=").append(sn).append(", nc=").append(nc).append(", sc=").append(sc).toString());
            n = -1;
            sn = formatSigned16(n);
            nc = parseUnsigned(sn);
            sc = parseSigned16(sn);
            System.out.println((new StringBuilder("n=")).append(n).append(", sn=").append(sn).append(", nc=").append(nc).append(", sc=").append(sc).toString());
            n = 32767;
            sn = formatSigned16(n);
            nc = parseUnsigned(sn);
            sc = parseSigned16(sn);
            System.out.println((new StringBuilder("n=")).append(n).append(", sn=").append(sn).append(", nc=").append(nc).append(", sc=").append(sc).toString());
            n = 32768;
            sn = formatSigned16(n);
            nc = parseUnsigned(sn);
            sc = parseSigned16(sn);
            System.out.println((new StringBuilder("n=")).append(n).append(", sn=").append(sn).append(", nc=").append(nc).append(", sc=").append(sc).toString());
            n = 32769;
            sn = formatSigned16(n);
            nc = parseUnsigned(sn);
            sc = parseSigned16(sn);
            System.out.println((new StringBuilder("n=")).append(n).append(", sn=").append(sn).append(", nc=").append(nc).append(", sc=").append(sc).toString());
            System.out.println((new StringBuilder("nbBinDigits(0)=")).append(nbBinDigits(0)).toString());
            System.out.println((new StringBuilder("nbBinDigits(1)=")).append(nbBinDigits(1)).toString());
            System.out.println((new StringBuilder("nbBinDigits(2)=")).append(nbBinDigits(2)).toString());
            System.out.println((new StringBuilder("nbBinDigits(7)=")).append(nbBinDigits(7)).toString());
            System.out.println((new StringBuilder("nbBinDigits(8)=")).append(nbBinDigits(8)).toString());
            System.out.println((new StringBuilder("nbBinDigits(123)=")).append(nbBinDigits(123)).toString());
            System.out.println((new StringBuilder("nbBinDigits(128)=")).append(nbBinDigits(128)).toString());
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
