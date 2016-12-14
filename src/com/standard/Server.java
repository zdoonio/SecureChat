package com.standard;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import com.db.DbCheckUser;
import com.intf.ServerIntf;
import com.security.PBKDF;

public class Server extends UnicastRemoteObject implements ServerIntf {
	/**
	 * 
	 */
	/*-----------------------------------------------------*/
	//													   //
	//													   //
	//				CREATED BY DOMINIK ZEDD				   //
	//													   //
	/*-----------------------------------------------------*/
	private static final long serialVersionUID = 1L;
	public String MESSAGE1 = "";
	public String MESSAGE2 = "";
	public static boolean isLogedIn;
	public String name = "";
	public ArrayList<String> connectedUsers = new ArrayList<String>();
	public String targetName ="";
	public int flagState = 0;
	
	
	private int iterator=0;
	
	
	

	static final Scanner input = new Scanner(System.in);

	public Server() throws RemoteException {
		super(0); // required to avoid the 'rmic' step, see below
		//connectedUsers = new ArrayList<String>();
	}
	
	public void sendMessage1(String message) {
		this.MESSAGE1 = message;
	}
	
	public void sendMessage2(String message) {
		this.MESSAGE2 = message;
	}
	
	public void sendFlagState(int flag){
		this.flagState = flag;
	}
	
	public void sendTargetName(String name){
		this.targetName = name;
	}
	
	public void sendClientName(String name){
		this.name = name;
		/*for (int i = 0; i < connectedUsers.length; ++i){
			if(connectedUsers[i].equals(name)) {
				connectedUsers[i] ="";
			}
		}*/
		
		if(!connectedUsers.contains(name))
			connectedUsers.add(iterator,name);
		//connectedUsers[iterator]=name;
		//iterator++;
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
		Server.isLogedIn = zalogowano;
	}
	
	public ArrayList<String> getConnectedUser(){
		return connectedUsers;
	}
	
	public String getTargetName(){
		return targetName;
	}
	
	public int getFlagState(){
		return flagState;
	}

	public char Login(String name, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		DbCheckUser usr = new DbCheckUser();
		String[] pass = new String[2];
		System.out.println(pass[0]+":"+pass[1]);
		
		pass = usr.check(name);
		String g = PBKDF.PBKDF2_ITERATIONS+":"+pass[0]+":"+pass[1];
		System.out.println(g);
		boolean validated = PBKDF.validatePassword(password, g, PBKDF.SHA1_ALGORITHM);
		System.out.println(validated);
		if (validated == true) {
			setZalogowano(true);
		}

		else {
			setZalogowano(false);
		}

		return 0;
	}
	
	//public void 
	

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
		Naming.rebind("//localhost/ServerSecure", obj);
		System.out.println("PeerServer bound in registry");
	}


}
