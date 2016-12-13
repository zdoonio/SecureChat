package com.standard;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.*;

import com.intf.ServerIntf;

public class Server extends UnicastRemoteObject implements ServerIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String MESSAGE1 = "";
	public String MESSAGE2 = "";
	public static boolean isLogedIn;
	

	static final Scanner input = new Scanner(System.in);

	public Server() throws RemoteException {
		super(0); // required to avoid the 'rmic' step, see below
	}
	
	public void setMessage1(String message) {
		this.MESSAGE1 = message;
	}
	
	public void setMessage2(String message) {
		this.MESSAGE2 = message;
	}

	public String getMessage1() {
		return MESSAGE1;
	}

	public String getMessage2() {
		return MESSAGE2;
	}

	public boolean isLogedIn() {
		return isLogedIn;
	}

	public void setZalogowano(boolean zalogowano) {
		//BankServer.zalogowano = zalogowano;
	}

	public char Login(String name, String password) {

		if ((name.equals("ewa")) && (password.equals("1234"))) {
			setZalogowano(true);
		}

		else {
			setZalogowano(false);
		}

		return 0;
	}

	public static void main(String args[]) throws Exception {
		System.out.println("RMI server started");

		try { // special exception handler for registry creation
			LocateRegistry.createRegistry(1099);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			// do nothing, error means registry already exists
			System.out.println("java RMI registry already exists.");
		}

		// Instantiate RmiServer

		Server obj = new Server();

		// Bind this object instance to the name "RmiServer"
		Naming.rebind("//localhost/BankServer", obj);
		System.out.println("PeerServer bound in registry");
	}


}
