
package org.jcb.shdl;

import java.io.*;
import java.util.*;
import javax.swing.*;



public class CommThread implements Runnable {
	
	// impossible de capturer une erreur possible de chargement de la DLL (??)
	static { System.loadLibrary("usbComm1.3"); }
	
	public native static int init();
	public native static int openData();
	public native static int closeData();
	synchronized public native static int writeByte(int num, int val);
	synchronized public native static int readByte(int num);

	private final int N = 128;

	private Thread readThread;
	private ArrayList listeners = new ArrayList();
	private final long IDLETIME = 10; // 10ms = durée de probe si pas d'activité
	private long idleTime = 0L; // durée d'attente entre chaque lecture de Byte : IDLETIME si peu occupé, 0 sinon
	private long lastChangeTime = 0;
	private int bytes[] = new int[N/8]; // table des Bytes
	private int[] bitVector = new int[N]; // vecteur des N bits courants lus sur la carte, 0 ou 1

	
	// a faire tourner avec un design en I/O à distance:
	// module echo(a[63..0] : s[63..0])
    //    a[63..0] = s[63..0] ;
    // end module
	// on peut aller jusqu'à 127 in + 127 out (pourquoi pas 128, je ne me rappelle plus)
	public static void main(String args[]) {
		CommThread.init();

		int error = CommThread.openData();
		if (error < 0) {
			System.out.println("*** init impossible");
			return;
		}
		java.util.Random rand = new java.util.Random();
		for (int i = 0; i < 10; i++) {
			//int num = rand.nextInt(8);
			int num = rand.nextInt(15);
			int data = rand.nextInt(256);
			CommThread.sendByte(num, data);
			//CommThread.writeByte(num, data); // si on veut tester la fonction native
			int val = CommThread.readdByte(num);
			//int val = CommThread.readByte(num); // si on veut tester la fonction native
			System.out.println("num=" + num + ", data=" + data + ", val=" + val);
			if (val != data)
				System.out.println("error, num=" + num + ", data=" + data + ", val=" + val);
		}
		CommThread.closeData();
	}
	
	
	public void start() throws IOException {
		
		// projection du bitVector nul
		try {
			for (int num = 0; num < N/8; num++) sendByte(num, 0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		// lancement du thread de lecture
		readThread = new Thread(this);
		readThread.start();
	}


	public void run() {
		try {
			// on fait varier byteNum circulairement de 0 à 7
			int byteNum = 0;
			while (true) {
				int byteData = CommThread.readByte(byteNum);
				if (byteData < 0) {
					int nbRetry = 0;
					while (nbRetry < 10) {
						byteData = CommThread.readByte(byteNum);
						if (byteData >= 0) break;
						nbRetry += 1;
					}
					if (nbRetry < 10) {
						System.out.println("CommThread.readByte problem, fixed after " + nbRetry + " retries");
					} else {
						throw new InterruptedException();
					}
				}
				if (byteData != bytes[byteNum]) {
					//System.out.println("receivedByte num=" + byteNum + ", data=" + byteData);
					bytes[byteNum] = byteData;
					lastChangeTime = System.currentTimeMillis();
					if (idleTime > 0L) System.out.println("lecture rapide...");
					idleTime = 0L; // lecture en continu pendant au moins 1s
			
					// update bitVector
					int index = byteNum * 8;
					bitVector[index++] = ((byteData & 1) != 0) ? 1 : 0;
					bitVector[index++] = ((byteData & 2) != 0) ? 1 : 0;
					bitVector[index++] = ((byteData & 4) != 0) ? 1 : 0;
					bitVector[index++] = ((byteData & 8) != 0) ? 1 : 0;
					bitVector[index++] = ((byteData & 16) != 0) ? 1 : 0;
					bitVector[index++] = ((byteData & 32) != 0) ? 1 : 0;
					bitVector[index++] = ((byteData & 64) != 0) ? 1 : 0;
					bitVector[index++] = ((byteData & 128) != 0) ? 1 : 0;
					
					// create and send CommEvent
					CommEvent ev = new CommEvent(byteNum, byteData, bitVector);
					for (int j = 0; j < listeners.size(); j++) {
						CommListener listener = (CommListener) listeners.get(j);
						listener.valueChanged(ev);
					}
				} else {
					if ((System.currentTimeMillis() - lastChangeTime) > 1000) {
						if (idleTime == 0L) System.out.println("lecture lente...");
						idleTime = IDLETIME; // pas de changement depuis 1s -> lecture lente
					}
				}
				byteNum += 1; if (byteNum == N/8) byteNum = 0;
				Thread.sleep(idleTime);
			}
			
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "USB connection failed!", "Connection error", JOptionPane.ERROR_MESSAGE);
			CommThread.closeData();
			//System.exit(0);
		}
	}

	
	// tous les arguments sur 8 bits (entre 0 et 255)
	synchronized public static void sendByte(int num, int data) {
//		System.out.println("sendByte num=" + num + ", data=" + data);
		int err = CommThread.writeByte(num, data);
		if (err < 0) {
			int nbRetry = 0;
			while (nbRetry < 10) {
				err = CommThread.writeByte(num, data);
				if (err >= 0) break;
				nbRetry += 1;
			}
			if (nbRetry < 10) {
				System.out.println("CommThread.writeByte problem, fixed after " + nbRetry + " retries");
			} else {
				JOptionPane.showMessageDialog(null, "USB connection failed!", "Connection error", JOptionPane.ERROR_MESSAGE);
				CommThread.closeData();
				//System.exit(1);
			}
		}
	}
	
	synchronized public static int readdByte(int num) {
		int data = CommThread.readByte(num);
//		System.out.println("readByte num=" + num + ", data=" + data);
		return data;
	}
	
	
	public int[] getBitVector() {
		return bitVector;
	}
	
	
	public long getLongValue(int endIndex, int startIndex) {
		long res = 0;
		long pow2 = 1;
		for (int i = startIndex; i <= endIndex; i++) {
			if (bitVector[i] == 1) res += pow2;
			pow2 = pow2 * 2;
		}
		return res;
	}


	public void addCommListener(CommListener listener) {
		listeners.add(listener);
	}
	public void removeCommListener(CommListener listener) {
		listeners.remove(listener);
	}
}

