
package org.jcb.shdl;

import java.io.*;
import org.jcb.shdl.*;



public class TestComm {
	
	private CommThread commThread;
	private int mon_ack;
	private int[] outs = new int[64];


	public TestComm() throws IOException {
		commThread = new CommThread();
	}	
	
	
	public static void main(String[] args) {
		try {
			TestComm test = new TestComm();
			test.go();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public void go() throws Exception {

		if (CommThread.init() < 0) return;
		
		if (CommThread.openData() < 0) return;
		
		java.util.Random rand = new java.util.Random();
		
		
		// test d'écriture/lecture des registres
		long[] values = new long[15];
		for (int j = 1; j < 10; j++) {
			values[j] = readRegister(j);
		}
		for (int i = 0; i < 100; i++) {
			long val = (long) rand.nextInt();
			if (val < 0) val = -val;
			int num = rand.nextInt(9) + 1;
			
			writeRegister(num, val);
			long v = readRegister(num);
			if (v == val) {
				System.out.println("******************** succes i=" + i + ", num=" + num + ", val=" + val);
			} else {
				System.out.println("******************** echec i=" + i + ", num=" + num + ", val=" + val + ", v=" + v);
			}
			values[num] = v;
			// verifie si un changement a eu lieu sur les autres
			for (int j = 1; j < 10; j++) {
				long y = readRegister(j);
				if (values[j] != y) {
					System.out.println("******************** modif j=" + j + ", old=" + values[j] +", new=" + y);
					values[j] = y;
				}
			}
		}
		
		
		//writeMemory(1L, 31L);
		//long u = readMemory(1L);
		//System.out.println("u=" + u);
		
		
		final int MAXADDR = 512;
		for (int i = 0; i < 100; i++) {
			long val = (long) rand.nextInt();
			if (val < 0) val = -val;
			int iaddr = rand.nextInt(MAXADDR);
			long addr = (long) iaddr;
			addr = addr * 4; // multiple de 4
			
			writeMemory(addr, val);

			long v = readMemory(addr);
			if (v == val) {
				System.out.println("******************** succes i=" + i + ", addr=" + addr + ", val=" + val);
			} else {
				System.out.println("******************** echec i=" + i + ", addr=" + addr + ", val=" + val + ", v=" + v);
				boolean ok = false;
				for (int j = 0; j < 20; j++) {
					long vv = readMemory(addr);
					if (vv == val) { ok = true; break; }
				}
				System.out.println("******************** relecture ok=" + ok);
			}
		}
		
		CommThread.closeData();
		System.exit(0);
	}
	
	
	int getByte(int i) {
		int res = 0;
		if (outs[8*i] == 1) res |= 1;
		if (outs[8*i+1] == 1) res |= 2;
		if (outs[8*i+2] == 1) res |= 4;
		if (outs[8*i+3] == 1) res |= 8;
		if (outs[8*i+4] == 1) res |= 16;
		if (outs[8*i+5] == 1) res |= 32;
		if (outs[8*i+6] == 1) res |= 64;
		if (outs[8*i+7] == 1) res |= 128;
		return res;
	}
	
	void setBits(int first, int last, long val) {
		//System.out.println("first=" + first + ", last=" + last + ", val=" + val);
		for (int i = first; i <= last; i++) {
			outs[i] = (int) (val % 2);
			val = val / 2;
			//System.out.println("i=" + i + ", val=" + val + ", outs[i]=" + outs[i]);
		}
		//System.out.println("getByte(4)=" + getByte(4));
	}

	
	public long readRegister(int numreg) {
		//if (numreg == numreg) return 0;
		
		// mon_cmd <- "0001" (read register)
		setBits(60, 63, 1);
		CommThread.sendByte(7, getByte(7));
		// reg # on pc2board[5..0]
		setBits(0, 5, numreg);
		CommThread.sendByte(0, getByte(0));
		
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
		do { } while ((CommThread.readdByte(7) & 128) == 0);

		// get read data
		long value = 0;
		long pow8 = 1;
		for (int i = 0; i < 4; i++) {
			int b = CommThread.readdByte(i);
			value += pow8 * b;
			pow8 *= 256;
		}

		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
					
		return value;
	}
	
	
	public void writeRegister(int numreg, long val) {
		
		// mon_cmd <- "0011" (write register)
		setBits(60, 63, 3);
		CommThread.sendByte(7, getByte(7));
		// reg # on pc2board[37..32]
		setBits(32, 37, numreg);
		CommThread.sendByte(4, getByte(4));
		// value to write on pc2board[31..0]
		setBits(0, 31, val);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
		do { } while ((CommThread.readdByte(7) & 128) == 0);

		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
	}	
	
	
	public void writeMemory(long addr, long val) {
		
		// mon_cmd <- "0010" (write memory)
		setBits(60, 63, 2);
		// address on pc2board[31..0]
		setBits(0, 31, addr);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
		do { } while ((CommThread.readdByte(7) & 128) == 0);

		// value to write on pc2board[31..0]
		setBits(0, 31, val);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 0
		do { } while ((CommThread.readdByte(7) & 128) != 0);
	}	
	
	
	public long readMemory(long addr) {

		// mon_cmd <- "0000" (read memory)
		setBits(60, 63, 0);
		// address on pc2board[31..0]
		setBits(0, 31, addr);
		for (int i = 0; i < 4; i++) {
			CommThread.sendByte(i, getByte(i));
		}
		// mon_req <- 1
		outs[59] = 1;
		CommThread.sendByte(7, getByte(7));
		
		// wait for mon_ack = 1
System.out.println("memread #1 wait for mon_ack = 1");
		do { } while ((CommThread.readdByte(7) & 128) == 0);
System.out.println("ok");

		// get read data
		long value = 0;
		long pow8 = 1;
		for (int i = 0; i < 4; i++) {
			int b = CommThread.readdByte(i);
			value += pow8 * b;
			pow8 *= 256;
		}

		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
		return value;
	}	
	
}

		
//try { Thread.sleep(10); } catch(Exception ex) {}
