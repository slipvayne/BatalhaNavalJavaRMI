package com.pdist.batalhanaval.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface ClientObserverInterface extends Remote{
	
	public void actualizarInfo() throws RemoteException;
	

	
}
