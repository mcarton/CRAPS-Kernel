
package org.jcb.craps;

import org.jcb.shdl.*;


public class CrapsTest {

	private CommThread commThread;
	private int[] outs = new int[64];


	public CrapsTest() {

		try {		
			commThread = new CommThread();
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		commThread.addCommListener(new CrapsCommListener());
		CommThread.init();
		CommThread.openData();
				
		// lancement de la communication
		try {
			commThread.start();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
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
		// confirmation que mon_ack=1?
		//System.out.println("wr mem, devrait etre 1:" + (CommThread.readdByte(7) & 128));

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
		// confirmation que mon_ack=0?
		//System.out.println("wr mem, devrait etre 0:" + (CommThread.readdByte(7) & 128));
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
		do { } while ((CommThread.readdByte(7) & 128) == 0);
		// confirmation que mon_ack=1?
		//System.out.println("rd mem, devrait etre 1:" + (CommThread.readdByte(7) & 128));

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

	void waitN(long ms) {
		try {
			Thread.sleep(ms);
		} catch(Exception ex) {
		}
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
		for (int i = first; i <= last; i++) {
			outs[i] = (int) (val % 2);
			val = val / 2;
		}
	}
	
	class CrapsCommListener implements CommListener {
		public void valueChanged(CommEvent ev) {
			/*
			int[] bitVector = ev.getBitVector();
			int brk = bitVector[62];
			if ((brk == 1) && runButton.getActionCommand().equals("stop")) {
				System.out.println("****************** break!!!");
				runButton.setText("run");
				runButton.setActionCommand("run");
				stepButton.setEnabled(true);
				updateViews();
			}
			*/
		}
	}

	
	public long readRegister(int numreg) {
		//if (numreg == numreg) return 0;
		// read procedure
		
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
		do { }
		while ((CommThread.readdByte(7) & 128) == 0);
		// confirmation que mon_ack=1?
		//System.out.println("readreg, devrait etre 1:" + (CommThread.readdByte(7) & 128));

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
		// write procedure
		
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
		// confirmation que mon_ack=1?
		//System.out.println("wrreg, devrait etre 1:" + (CommThread.readdByte(7) & 128));

		// mon_req <- 0
		outs[59] = 0;
		CommThread.sendByte(7, getByte(7));
	}	


	public static void main(String[] args) {
		CrapsTest test = new CrapsTest();

		/*
		for (int i = 0; i < 1000; i++) {
			int n = 1 + i%9;
			test.writeRegister(n, (long) i);
			long r1 = test.readRegister(n);
			if (r1 != i) System.out.println("n=" + n + ", r1=" + r1);
		}
		*/
		
		test.writeRegister(1, 0x1111);
		test.writeRegister(2, 0x2222);
		test.writeRegister(3, 0x3333);
		test.writeRegister(4, 0x4444);
		test.writeRegister(5, 0x5555);
		test.writeMemory(20, 0x2222);
		test.writeMemory(24, 0x4444);
		System.out.println("readRegister(1)=" + Long.toHexString(test.readRegister(1)));
		System.out.println("readRegister(2)=" + Long.toHexString(test.readRegister(2)));
		System.out.println("readRegister(3)=" + Long.toHexString(test.readRegister(3)));
		System.out.println("readRegister(4)=" + Long.toHexString(test.readRegister(4)));
		System.out.println("readRegister(5)=" + Long.toHexString(test.readRegister(5)));
		System.out.println("readRegister(35)=" + test.readRegister(35));
		System.out.println("readMemory(20)=" + Long.toHexString(test.readMemory(20)));
		System.out.println("readMemory(24)=" + Long.toHexString(test.readMemory(24)));
		
		System.exit(0);
	}
}

