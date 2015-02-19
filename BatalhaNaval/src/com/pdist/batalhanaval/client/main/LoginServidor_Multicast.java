package com.pdist.batalhanaval.client.main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.pdist.batalhanaval.server.macros.Macros;

public class LoginServidor_Multicast implements Runnable {    
        
	
	protected InetAddress group = null;
	protected InetAddress ip = null;
	protected String ip_serv =null;
	protected int ip_port;
	protected String nome;
	protected String msg="";
	protected MulticastSocket s;
    protected DatagramPacket pkt;
    protected DatagramPacket send;
    protected int port; //mesma
    protected boolean running;
    protected byte[] barray;
    
	
	private final JPanel contentPanel = new JPanel();                                
        
    //Na verdade não faz login, simplesmente aproveita para saber o ip do servidor e chama a class LoginServidor_IP caso tenha sucesso!
	//acho que não vale a pena ter duas a fazer o mesmo depois...visto que a ligaçao tem ke ser via TCP (conforme imagem do enunciado)
	
	public LoginServidor_Multicast(String IP, String Port, String nome) throws IOException{
		
		try {
                        
                    this.group = InetAddress.getByName(IP);
                    this.ip = InetAddress.getLocalHost();
                    this.port = Integer.parseInt(Port);
                    this.nome = nome;
            
                    s = new MulticastSocket(port);
                    s.joinGroup(group);
                    
                    running = true;
                    barray = new byte[Macros.MAX_SIZE];
        	        pkt = new DatagramPacket(barray,Macros.MAX_SIZE);      		
                        
            
	} catch (IOException e) {
		//tratar
		e.printStackTrace();
		return;}
	}
        

                
        @Override
        public void run() {
        	
        	if(s == null || running == false)
        		return;
        	
        	while(running){
        		try {
        			
//=====================Pedido de IP do servidor
        			   String msgToSend = "MSG_SERVIP_REQUEST"; //nova, basta so para saber os dados                       
        			   DatagramPacket dgram = new DatagramPacket(msgToSend.getBytes(), msgToSend.length(),group, port);
                       s.send(dgram);
                       s.setSoTimeout(1500); //caso o servidor nao esteja on..
                       
                       while(!msg.equals("IPSERVIDOR")){ // enquanto n receber esta mensagem...                 	   
                    	   try{
//=============================Resposta do servidor            			
            				   s.receive(pkt);   
                    	   	} catch (Exception e) {            			
                    	   		JOptionPane.showMessageDialog(contentPanel,"(Timeout) Não foi possivel obter uma resposta, tente novamente!");
                    	   	  return;
                    	   	}
                    	   
    				
       				   msg =  new String(pkt.getData(), 0, pkt.getLength());       				
       			       System.out.println("Resposta via multicast: (" + pkt.getAddress().getHostAddress() + ":" + pkt.getPort() + ")MSG: " + msg );	
       			        
       			       //recebe ip e port do servidor
       			       ip_serv = pkt.getAddress().getHostAddress();
       			       ip_port = pkt.getPort();   
       			       
//Como ja sabe o ip e porta do servidor, faz a ligaçao via tcp (so falta controlar se o nome ta em uso))       			       
       			       	if(msg.equals("IPSERVIDOR"))
       			       	{      			      
       			       		Thread t = new Thread(new LoginServidor_IP(ip_serv,nome,""+ip_port) );
       			       		t.setDaemon(true);
       			       		t.start();
       			       		VarsGlobais.NovoJogoThreadCreated = true;   
       			       		running=false;
       			//       	JOptionPane.showMessageDialog(contentPanel,"Conectado com sucesso,  e a ligar via TCP agora!");       			
       			       		return; 
       			       	}       			       	
       		
                       }                     
                  
    				
    			} catch (IOException e) {
    				e.printStackTrace();
    				 JOptionPane.showMessageDialog(contentPanel,"Erro na ligação ao servidor");
    				break;
    			}
        	}
        	return;
        		
                
        }    
        
       
}
