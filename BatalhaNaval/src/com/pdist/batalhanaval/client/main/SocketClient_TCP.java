package com.pdist.batalhanaval.client.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SocketClient_TCP {


	//private static final int TIMEOUT = 1500;

	protected static Socket socket = null; 
	
	protected static InetAddress servAddr = null;
	protected static int servPort;
	
	protected static ObjectOutputStream out=null;
	protected static ObjectInputStream in=null;
	
	protected static JPanel contentPanel;
	
	
	public SocketClient_TCP(InetAddress servAddr, int servPort, JPanel contentPanel )	{
		
		SocketClient_TCP.servAddr = servAddr;
		SocketClient_TCP.servPort = servPort;
		SocketClient_TCP.contentPanel = contentPanel;
		
		try{         
			
            SocketClient_TCP.socket = new Socket(servAddr,servPort);   //cria socket
            
            }catch(Exception e)
            {   
            	JOptionPane.showMessageDialog(contentPanel,"Erro na ligação ao servidor - CRIAR SOCKET");
                VarsGlobais.NovoJogoThreadCreated = false;                 
                return;
            }		
		
		
	    try {
			 
			out = new ObjectOutputStream(SocketClient_TCP.socket.getOutputStream()); //cria OutputStream
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPanel,"Erro na ligação ao servidor - OUT STREAM");
		}
	    
		 
        try {
        	 
			in = new ObjectInputStream(SocketClient_TCP.socket.getInputStream());  //cria InputStream
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPanel,"Erro na ligação ao servidor - INPUT STREAM");
		}
         
		
	}
	
	public static Socket getSocket(){
		return socket;
	}
	
	public static void setSocket(Socket socket){
		SocketClient_TCP.socket = socket;
	}

	public static InetAddress getServAddr() {
		return servAddr;
	}

	public static void setServAddr(InetAddress servAddr) {
		SocketClient_TCP.servAddr = servAddr;
	}

	public static int getServPort() {
		return servPort;
	}

	public static void setServPort(int servPort) {
		SocketClient_TCP.servPort = servPort;
	}

	public static ObjectOutputStream getOut() {
		return out;
	}

	public static void setOut(ObjectOutputStream out) {
		SocketClient_TCP.out = out;
	}

	public static ObjectInputStream getIn() {
		return in;
	}

	public static void setIn(ObjectInputStream in) {
		SocketClient_TCP.in = in;
	}
	
	/*
	public static void setSoCketTimeout(){
		
		 try {
			SocketClient_TCP.socket.setSoTimeout(TIMEOUT);
		} catch (SocketException e) {
        	JOptionPane.showMessageDialog(SocketClient_TCP.contentPanel,"Erro na ligação ao servidor - TIMEOUT (SocketClient_TCP)");

		} 
	}
	*/
	
	
}
