package com.pdist.batalhanaval.client.dialogs;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.pdist.batalhanaval.client.main.SocketClient_TCP;
import com.pdist.batalhanaval.client.main.VarsGlobais;
import com.pdist.batalhanaval.server.macros.Macros;
import com.pdist.batalhanaval.server.mensagens.Mensagem;


public class ListaJogosEJogadores extends JDialog {

	
	private static final long serialVersionUID = 4394138207569213899L;
	private final JPanel contentPanel = new JPanel();
	private ArrayList<String> nomeJogos = null;
	private ArrayList<String> nomeJogadores = null;
	
	private String nomeJogador;
	public static String nomeJogadorConvidado;
	
		
	public ListaJogosEJogadores(String nomeJogador) throws IOException {
		setAlwaysOnTop(false);
		
		setResizable(false);  
		setModal(false);
		setTitle("Novo Jogo - Listar Jogos/Jogadores");
		setBounds(100, 100, 552, 310);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		this.nomeJogador = nomeJogador;
		
		
		//TODO Receber e enviar convites e estar a sincronizar de X em X segundos (criar thread e colocar la os metodos daqui)
		
			
		try {
			getListaJogos(sendListaJogosRequest()); //envia pedido ao servidor e recebe lista de jogos
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPanel,"PEDIR LISTA JOGOS - Erro na ligação ao servidor");
			return;
		}   
		
		
		try {
			getListaJogadores(sendListaJogadoresRequest()); //envia pedido ao servidor e recebe lista de jogadores
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPanel,"PEDIR LISTA JOGADORES - Erro na ligação ao servidor");
			return;
		}   
		
			
		JLabel lblListaDeJogos = new JLabel("Jogos Activos:");
		lblListaDeJogos.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblListaDeJogos.setBounds(77, 13, 158, 26);
		contentPanel.add(lblListaDeJogos);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(276, 0, 2, 285);
		contentPanel.add(separator);
		
		JButton btnEnviarConvite = new JButton("Enviar Convite");
		btnEnviarConvite.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEnviarConvite.setBounds(358, 216, 128, 33);
		contentPanel.add(btnEnviarConvite);
		
		
		JLabel lblConvidarJogadores = new JLabel("Jogadores Online:");
		lblConvidarJogadores.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblConvidarJogadores.setBounds(338, 13, 158, 26);
		contentPanel.add(lblConvidarJogadores);
		
//DEPOIS ALTERAR PARA PACKAGE LISTENERS...	
		btnEnviarConvite.addActionListener(new ActionListener() { 
			   public void actionPerformed(ActionEvent evt) {	
						  
				   
				   //verificar se o user está a convidar-se a si proprio
				   if( getNomeJogador().equals(VarsGlobais.listaJogadores.getSelectedValue()) ){
					   JOptionPane.showMessageDialog(contentPanel, "Não se pode convidar a si mesmo!");
					   return;
				   }
				   
				   
				   //Enviar msg ao servidor para convidar o X jogador
				   //Se o jogador aceitar (ter timeout) o jogo é iniciado
				   try{
					   //enviar convite
					   if(VarsGlobais.sentConvite==false){ //para impedir spam
						   sendConvite();
						   VarsGlobais.sentConvite = true;
					   }
					 //definir timeout para 15 segundos (tem 15 segundos para aceitar/rejeitar o convite)
				     //SocketClient_TCP.getSocket().setSoTimeout(15000);
					   //aguardar resposta (timeout 15s)
					   //receberRespostaConvite();
					   //se tiver sido aceite, fecha janela dos convites e começa o jogo
				   }catch(SocketTimeoutException e){
					   JOptionPane.showMessageDialog(contentPanel, "Não foi obtida resposta do outro jogador");
				   }catch(IOException e){
					   JOptionPane.showMessageDialog(contentPanel, "Erro a enviar convite.");
				   }
				  
				   
				   
			   }
		});
		
			
	}
		
	
	//GET (por causa do actionListener)
	public String getNomeJogador(){
		return nomeJogador;
	}
	
	//enviar um pedido ao servidor para que este envie um pedido (convite) a um certo jogador
	public void sendConvite() throws IOException{
		
		nomeJogadorConvidado = VarsGlobais.listaJogadores.getSelectedValue();
		//dizer ao servidor que se quer iniciar um jogo e passar o nome do jogador a convidar
		Mensagem msg = new Mensagem(Macros.MSG_INICIAR_JOGO, VarsGlobais.listaJogadores.getSelectedValue());
				
		
		//enviar mensagem
		SocketClient_TCP.getOut().flush();
		SocketClient_TCP.getOut().writeObject(msg);
		SocketClient_TCP.getOut().flush();
		
	}
	
	
	
	
	public Mensagem sendListaJogosRequest() throws IOException{   	  
    	  
    	        	
		Mensagem msg = new Mensagem(Macros.MSG_LISTA_JOGOS);
        msg.setMsgText(nomeJogador);      
           
           
        SocketClient_TCP.getOut().flush();
        SocketClient_TCP.getOut().writeObject(msg);
        SocketClient_TCP.getOut().flush();
           
        try{
             	
        	msg = (Mensagem) SocketClient_TCP.getIn().readObject(); 
               
            } catch (Exception  e) {                                                    
            	JOptionPane.showMessageDialog(contentPanel,"Erro ao obter lista de jogos");
                VarsGlobais.NovoJogoThreadCreated = false;                     
            }
           
        return msg;
	}
	
	
	public void getListaJogos(Mensagem listajogos)
	{
		nomeJogos = listajogos.getNomesJogos();			
		
		
		for(int i=0; i<nomeJogos.size();i++)
		{
			VarsGlobais.modelListaJogos.addElement(nomeJogos.get(i)); //carregar info do jogos para a lista
		}
		
		VarsGlobais.listaJogos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		VarsGlobais.listaJogos.setSelectedIndex(0); //selecionar o 1º
		VarsGlobais.listaJogos.setLayoutOrientation(JList.VERTICAL); 		
		VarsGlobais.listaJogos.setFont(new Font("Tahoma", Font.PLAIN, 14));
		VarsGlobais.listaJogos.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));	
		JScrollPane listaScroll = new JScrollPane(VarsGlobais.listaJogos);   //para aparecer o scroll na lista
		listaScroll.setBounds(49, 50, 181, 135);		
		contentPanel.add(listaScroll);
	}
	
	
	   public Mensagem sendListaJogadoresRequest() throws IOException{   	  
	    	  
		   Mensagem msg = new Mensagem(Macros.MSG_LISTA_ONLINE);
           msg.setMsgText(nomeJogador);      
           
           
           SocketClient_TCP.getOut().flush();
           SocketClient_TCP.getOut().writeObject(msg);
           SocketClient_TCP.getOut().flush();
           
           try{
             	
               msg = (Mensagem) SocketClient_TCP.getIn().readObject(); 
               
               } catch (Exception  e) {                                                    
                   JOptionPane.showMessageDialog(contentPanel,"Erro ao obter lista de jogadores!");
                   VarsGlobais.NovoJogoThreadCreated = false;                     
               }
           
           return msg;
   }
	
	
	public void getListaJogadores(Mensagem listajogadores) //lista de jogadores que NAO estao a jogar..
	{
		nomeJogadores = listajogadores.getNomesClientes();		
		
		
		for(int i=0; i<nomeJogadores.size();i++)
		{
			if(!listajogadores.getNomesClientes().get(i).equals(this.getNomeJogador()))
				VarsGlobais.modelListaJogadores.addElement(nomeJogadores.get(i)); 		
		}
		
		VarsGlobais.listaJogadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		VarsGlobais.listaJogadores.setSelectedIndex(0); //selecionar o 1º
		VarsGlobais.listaJogadores.setLayoutOrientation(JList.VERTICAL); 		
		VarsGlobais.listaJogadores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		VarsGlobais.listaJogadores.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));	
		JScrollPane listaScroll = new JScrollPane(VarsGlobais.listaJogadores);   
		listaScroll.setBounds(327, 51, 181, 135);		
		contentPanel.add(listaScroll);
	}


	public ArrayList<String> getNomeJogos() {
		return nomeJogos;
	}


	public void setNomeJogos(ArrayList<String> nomeJogos) {
		this.nomeJogos = nomeJogos;
	}


	public ArrayList<String> getNomeJogadores() {
		return nomeJogadores;
	}


	public void setNomeJogadores(ArrayList<String> nomeJogadores) {
		this.nomeJogadores = nomeJogadores;
	}


	public DefaultListModel<String> getModelListaJogos() {
		return VarsGlobais.modelListaJogos;
	}


	public void setModelListaJogos(DefaultListModel<String> modelListaJogos) {
		VarsGlobais.modelListaJogos = modelListaJogos;
	}


	public JList<String> getListaJogos() {
		return VarsGlobais.listaJogos;
	}


	public void setListaJogos(JList<String> listaJogos) {
		VarsGlobais.listaJogos = listaJogos;
	}


	public DefaultListModel<String> getModelListaJogadores() {
		return VarsGlobais.modelListaJogadores;
	}


	public void setModelListaJogadores(DefaultListModel<String> modelListaJogadores) {
		VarsGlobais.modelListaJogadores = modelListaJogadores;
	}


	public JList<String> getListaJogadores() {
		return VarsGlobais.listaJogadores;
	}


	public void setListaJogadores(JList<String> listaJogadores) {
		VarsGlobais.listaJogadores = listaJogadores;
	}


	public JPanel getContentPanel() {
		return contentPanel;
	}


	public void setNomeJogador(String nomeJogador) {
		this.nomeJogador = nomeJogador;
	}
	
	
	
}
