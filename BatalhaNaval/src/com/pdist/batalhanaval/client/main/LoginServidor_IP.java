package com.pdist.batalhanaval.client.main;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.pdist.batalhanaval.server.macros.Macros;
import com.pdist.batalhanaval.server.mensagens.Mensagem;
import com.pdist.batalhanaval.client.dialogs.ListaJogosEJogadores;
import com.pdist.batalhanaval.client.jogo.Jogo;
import com.pdist.batalhanaval.client.main.BatalhaNaval_Client;
import com.pdist.batalhanaval.client.main.SocketClient_TCP;


public class LoginServidor_IP implements Runnable {
        
        
	protected boolean logIn;
	protected InetAddress servAddr = null;
	protected int servPort;
	protected String nome;
	protected Mensagem msg = null;
	protected int TIMEOUT=1500;
                
	private final JPanel contentPanel = new JPanel();  

                
	public LoginServidor_IP(String IP, String nome, String porto) throws IOException{
                        
                    this.servAddr = InetAddress.getByName(IP);
                    this.servPort = Integer.parseInt(porto);
                    this.nome = nome;
                    this.logIn = false;                       
                                        
                    new SocketClient_TCP(servAddr,servPort,contentPanel);  //cria class/socket/ligacao TCP

	}                       
                        
        @Override
        public void run() {
                
                 try{
                       	             			
                	 SocketClient_TCP.getSocket().setSoTimeout(TIMEOUT); //timeout para o socket (deve estar fora da classe tcp para fzr return aqui)
             			                	 
             	     }catch(Exception e)
                            { 
                            JOptionPane.showMessageDialog(contentPanel,"Erro na ligação ao servidor - TIMEOUT");
                            VarsGlobais.NovoJogoThreadCreated = false;  
                            return;
                            }
                             
                 
                
                while(!logIn){                    
                        try {                                           
                                if(!logIn){
                                        sendLoginRequest();                                     
                                }
                                
                                msg = (Mensagem) SocketClient_TCP.getIn().readObject();
                                switch(msg.getType()){                                  
                                        case Macros.MSG_LOGIN_FAIL:
                                                sendLoginResponse();
                                                break;
                                        case Macros.MSG_LOGIN_LOGGED:
                                                noteAlreadyLogged();
                                                break;
                                        case Macros.MSG_LOGIN_VALIDATED:
                                                logIn = true;
                                                
                                                
                                                //Atencao ao tirar algumas MSGBOX, ke o socket n é bloqueante e se nao obtem logo resposta.. da erro no servidor
                                                //(melhorar essa parte.. e ter muita atençao que é importante)
                                                JOptionPane.showMessageDialog(contentPanel,"Estás ligado ao servidor!\n"+SocketClient_TCP.getSocket().toString());
                                                
                                                Jogo.setNomeJogador1(nome);
                                                Jogo.setNomeJogador2("A aguardar..");
                                                BatalhaNaval_Client.setEstado("A aguardar por um novo jogo...");                                               
                                                                                       
                                              
                        						
                                                ListaJogosEJogadores dialog = new ListaJogosEJogadores(nome);
                        						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        						dialog.setVisible(true);
                        						
                        					                        						
                        						//========= NOVO JOGO =========
                        							new Jogo(dialog);
                        						//========= NOVO JOGO =========
                                                
                        						
                        						
                                                //========                                                
                                                break;
                                }                                       
                                
                        } catch (IOException  e) {
                                
                                 JOptionPane.showMessageDialog(contentPanel,"Erro na ligação ao servidor");
                                 VarsGlobais.NovoJogoThreadCreated = false;  
                                 return;
                        } catch(ClassNotFoundException e){
                                 JOptionPane.showMessageDialog(contentPanel,"Erro ao receber a mensagem");
                                 VarsGlobais.NovoJogoThreadCreated = false;  
                                 return;                                
                        }
                }               
        }       
        
        public void sendLoginResponse() throws IOException{
                //nome = pede novo nome;
                sendLoginRequest();
        }
        
     
        
        public void sendLoginRequest() throws IOException{
                Mensagem msg = new Mensagem(Macros.MSG_LOGIN_REQUEST);
                msg.setMsgText(nome);
                SocketClient_TCP.getOut().flush();
                SocketClient_TCP.getOut().writeObject(msg);
                SocketClient_TCP.getOut().flush();
        }
        
        public void noteAlreadyLogged(){
                //avisa o utilizador que já está logado
        }
}
