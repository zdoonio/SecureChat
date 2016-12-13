package com.intf;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerIntf extends Remote {

	public String getMessage1() throws RemoteException;

	public String getMessage2() throws RemoteException;
	
	public void setMessage1(String message) throws RemoteException;
	
	public void setMessage2(String message) throws RemoteException;

	public char Login(String login, String haslo) throws RemoteException;

	public void setZalogowano(boolean zalogowano) throws RemoteException;

	public boolean isLogedIn() throws RemoteException;

}