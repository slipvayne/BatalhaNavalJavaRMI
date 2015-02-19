package com.pdist.batalhanaval.server.controlo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

import com.pdist.batalhanaval.server.main.AtendeCliente;

//nota: tem de ser serializable para poder ser enviado na mensagem

public class Cliente implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 830193890020653451L;
	private InetAddress ip;
	private String nome;
	private boolean onGame;
	private Tabuleiro tabuleiro;
	private Socket mySocket;
	private AtendeCliente myThread;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	//Constructores
	public Cliente(InetAddress ip){
		this.ip = ip;
		nome = " ";
		onGame = false;
		tabuleiro = null;
		mySocket = null;
		myThread = null;
		in = null;
		out = null;
	}	
	public Cliente(InetAddress ip, String nome){
		this.ip = ip;
		this.nome = nome;
		onGame = false;
		tabuleiro = null;
		mySocket = null;
		myThread = null;
		in = null;
		out = null;
	}
	
	//Getters
	public InetAddress getIp() {return ip;}
	public String getNome() {return nome;}
	public boolean isOnGame() {return onGame;}
	public Tabuleiro getTabuleiro(){return tabuleiro;}
	public Socket getMySocket(){return mySocket;}
	public AtendeCliente getMyThread(){return myThread;}
	
	//Setters
	public void setIp(InetAddress ip) {this.ip = ip;}
	public void setNome(String nome) {this.nome = nome;}
	public void setOnGame(boolean onGame) {this.onGame = onGame;}
	public void setTabuleiro(Tabuleiro tabuleiro){this.tabuleiro = tabuleiro;}
	public void setMySocket(Socket socket){this.mySocket = socket;}
	public void setMyThread(AtendeCliente myThread){this.myThread = myThread;}
	
	
	//Alterado.. necessario
	public ObjectInputStream getIn() {
		return in;
	}
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	public ObjectOutputStream getOut() {
		return out;
	}
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	
	
	
}
