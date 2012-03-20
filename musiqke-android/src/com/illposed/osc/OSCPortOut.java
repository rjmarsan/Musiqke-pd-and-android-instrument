/**
 * @author cramakrishnan
 *
 * Copyright (C) 2004, C. Ramakrishnan / Illposed Software
 * All rights reserved.
 * 
 * See license.txt (or license.rtf) for license information.
 * 
 * 
 * OSCPortOut is the class that sends OSC messages.
 *
 * To send OSC, in your code you should instantiate and hold onto an OSCPort
 * pointing at the address and port number for the receiver.
 *
 * When you want to send an OSC message, call send().
 *
 * An example based on com.illposed.osc.test.OSCPortTest::testMessageWithArgs() :
		OSCPort sender = new OSCPort();
		Object args[] = new Object[2];
		args[0] = new Integer(3);
		args[1] = "hello";
		OSCMessage msg = new OSCMessage("/sayhello", args);
		 try {
			sender.send(msg);
		 } catch (Exception e) {
			 showError("Couldn't send");
		 }
 */

package com.illposed.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class OSCPortOut extends OSCPort {

	protected InetAddress address;
	
	public LinkedList<OSCPacket> queue = new LinkedList<OSCPacket>();
	
	protected class OffThreadDispatcher extends Thread {
		public boolean  keepRunning = true;
		public void run() {
			while (keepRunning) {
				while (!queue.isEmpty()) {
					try {
						OSCPacket packet = queue.removeFirst();
						send(packet);
					} catch (Exception e) {
						queue.clear(); // WTF LETS GET OUT OF HERE
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
				}
			}
		}
			
	}
	OffThreadDispatcher dispatch = new OffThreadDispatcher();
	
	/**
	 * Create an OSCPort that sends to newAddress, newPort
	 * @param newAddress InetAddress
	 * @param newPort int
	 */
	public OSCPortOut(InetAddress newAddress, int newPort) throws SocketException {
		socket = new DatagramSocket();
		address = newAddress;
		port = newPort;
		dispatch.start();
	}

	/**
	 * Create an OSCPort that sends to newAddress, on the standard SuperCollider port
	 * @param newAddress InetAddress
	 *
	 * Default the port to the standard one for SuperCollider
	 */
	public OSCPortOut(InetAddress newAddress) throws SocketException {
		this(newAddress, defaultSCOSCPort);
	}

	/**
	 * Create an OSCPort that sends to localhost, on the standard SuperCollider port
	 * Default the address to localhost
	 * Default the port to the standard one for SuperCollider
	 */
	public OSCPortOut() throws UnknownHostException, SocketException {
		this(InetAddress.getLocalHost(), defaultSCOSCPort);
	}
	
	public void sendAsync(OSCPacket packet) {
		queue.add(packet);
		dispatch.interrupt();
	}
	
	/**
	 * @param aPacket OSCPacket
	 */
	public void send(OSCPacket aPacket) throws IOException {
		byte[] byteArray = aPacket.getByteArray();
		DatagramPacket packet = 
			new DatagramPacket(byteArray, byteArray.length,address,port);
		socket.send(packet);
	}
}
