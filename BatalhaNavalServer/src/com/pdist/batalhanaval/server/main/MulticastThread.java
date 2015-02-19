package com.pdist.batalhanaval.server.main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.pdist.batalhanaval.server.macros.Macros;

public class MulticastThread extends Thread{
	protected MulticastSocket s;
    protected DatagramPacket pkt;
    protected DatagramPacket send;
 //===========PORT e IP fixo do servidor MULICAST==============
    protected static int port = 5001;
    protected static String MulticastIP = "225.1.1.1";
//===========PORT e IP fixo do servidor MULICAST==============
    protected InetAddress ip;//Nosso ip
    protected InetAddress group;//ip de broadcast
    protected InetAddress ip_client;//ip do cliente que pede
    protected boolean running;
    protected byte[] barray;
    
    protected String msg=null;
    
    
    public MulticastThread(){
    	try {
//=========== GROUP IP MULICAST==============
			group = InetAddress.getByName(MulticastIP);
//=========== GROUP IP MULICAST==============
			
			ip = InetAddress.getLocalHost();
			
			s = new MulticastSocket(port);
			s.joinGroup(group);
			running = true;			
			barray = new byte[Macros.MAX_SIZE];
	        pkt = new DatagramPacket(barray,Macros.MAX_SIZE);
	        
			this.setDaemon(true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }
    
    public void run(){
    	
    	if(s == null || running == false)
    		return;
    	
    	System.out.println("==== Thread Multicast (IP: "+MulticastIP+" | Port:"+port+") ====");
    	
    	
    	while(running){
    		try {
    			
    			msg="";
    			
    			//recebe
				s.receive(pkt);				
				msg =  new String(pkt.getData(), 0, pkt.getLength());				
				System.out.println("Recebido via Multicast: (" + pkt.getAddress().getHostAddress() + ":" + pkt.getPort() + ")MSG: " + msg );		  
			
				
				if(msg.equals("MSG_SERVIP_REQUEST")){ //se foi um request de ip envia de volta
				
				//resposta - envia o ip do servidor via multicast/broadcast
				System.out.println("MSG_SERVIP_REQUEST - Enviado dados");					
			    String msgToSend = "IPSERVIDOR"; //so para ter a certeza que é do servidor, depois o ip vai no pkt..                      
      			DatagramPacket dgram = new DatagramPacket(msgToSend.getBytes(), msgToSend.length(), 
                             group, port);
                s.send(dgram);	
				}				
				
				
			} catch (IOException e) {
				e.printStackTrace();				
				break;
			}
    		
    	}
    	
    }

    public void terminate(){
    	running = false;
    }
    
    
}
