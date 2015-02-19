package com.pdist.batalhanaval.server.rmi;

import java.rmi.*;
import java.util.ArrayList;


public interface BatalhaNavalRMIInterface extends Remote{
	
	//get numero de quadriculas abatidas no jogo X do jogador Y (jogador 1 ou 2 desse jogo)
	public int getNumeroExplosoes(int jogo, int cliente) throws RemoteException;
	
	//get lista jogadores
	public ArrayList<String> getJogadores() throws RemoteException;
	
	//get lista jogos
	public ArrayList<String> getListaJogos() throws RemoteException;
	
	
	public void addObserver(ClientObserverInterface obs) throws Exception;	

	
}
