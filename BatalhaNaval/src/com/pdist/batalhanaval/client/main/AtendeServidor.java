package com.pdist.batalhanaval.client.main;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.pdist.batalhanaval.client.dialogs.ListaJogosEJogadores;
import com.pdist.batalhanaval.client.jogo.Jogo;
import com.pdist.batalhanaval.server.macros.Macros;
import com.pdist.batalhanaval.server.mensagens.Mensagem;

public class AtendeServidor extends Thread{

	protected JFrame jogoFrame;
	protected ListaJogosEJogadores listajogadores;
	protected Jogo jogo;
	
	protected ImageIcon agua = new ImageIcon("Imagens/agua.png");
	protected ImageIcon barcoEsq = new ImageIcon("Imagens/barcos/barco_esq.png");
	protected ImageIcon barcoMeio = new ImageIcon("Imagens/barcos/barco_meio.png");
	protected ImageIcon barcoDir = new ImageIcon("Imagens/barcos/barco_dir.png");
	protected ImageIcon fail = new ImageIcon("Imagens/aguaFail.png"); //agua
	protected ImageIcon hitEsq = new ImageIcon("Imagens/barcos/fogo/barco_esq_f.png"); //explosao esquerda
	protected ImageIcon hitMeio = new ImageIcon("Imagens/barcos/fogo/barco_meio_f.png"); //explosao meio
	protected ImageIcon hitDir = new ImageIcon("Imagens/barcos/fogo/barco_dir_f.png"); //explosao direita
	protected ImageIcon hit = new ImageIcon("Imagens/aguaHit.png");	
	
	
	protected JButton[][] botao = new JButton[10][10]; //botoes do mapa esquerdo
	
	
	//CONSTRUTOR
	public AtendeServidor(JFrame jFrame, ListaJogosEJogadores listajogadores, Jogo jogo){
		jogoFrame = jFrame;
		this.listajogadores = listajogadores;
		
		this.jogo = jogo;
		
		try {
			SocketClient_TCP.getSocket().setSoTimeout(0);
			
						
		} catch (IOException e1) {
			//SERVIDOR DESLIGOU
			JOptionPane.showMessageDialog(jogoFrame, "O SERVIDOR DESLIGOU");
			return;
		}
		
		
	}
	
	
	//RUN
	public void run(){
		//JOptionPane.showMessageDialog(jogoFrame, "RUN (thread)"); //so para mostrar que a thread ta a correr
		Mensagem msg;
		//tratar as mensagens recebidas
		while(true){
			try{				
												
				msg = (Mensagem) SocketClient_TCP.getIn().readObject();
			//	JOptionPane.showMessageDialog(jogoFrame, "msg.type ->" + msg.getType()); //so para testes
				
				switch(msg.getType()){
					case Macros.MSG_PEDIDO_JOGO: //user recebeu um convite
						receberConvites(msg);	
						break;
					case Macros.MSG_INICIAR_RESPONSE: //resposta do convite (aceitou? rejeitou?)
						receberRespostaConvite(msg);
						break;						
					case Macros.MSG_ATACAR_COORD_REPETIDA: //atacou uma coordenada que ja tinha atacado antes
						respostaAtaque(msg);
						break;
					case Macros.MSG_ATACAR_FAIL: //falhou o tiro (agua)
						respostaAtaque(msg);
						break;
					case Macros.MSG_ATACAR_SUCCESS: //acertou num barco
						respostaAtaque(msg);
						break;
					case Macros.MSG_ACTUALIZAR_YOUR_TAB: //fui atacado, actualizar tabuleiro
						actualizarTabuleiro(msg);
						break;
					case Macros.MSG_NOTIFY_CHANGES_PLAYERS:
						actualizaListaJogadores();
						break;
					case Macros.MSG_NOTIFY_CHANGES_GAMES:
						actualizaListaJogos();
						break;
					case Macros.MSG_GET_TABULEIRO: //obter tabuleiro do jogo						
						receberTabuleiro(msg);											
						break;
						
					case Macros.MSG_VITORIA:
						JOptionPane.showMessageDialog(jogoFrame, "VITORIA!");
						initGame();
						//...
						break;
					case Macros.MSG_DERROTA:
						JOptionPane.showMessageDialog(jogoFrame, "Derrota..");
						initGame();
						//...
						break;
					case Macros.MSG_ONLINE_RESPONSE:
						showOnline(msg);
						break;
					case Macros.MSG_JOGOS_RESPONSE:
						showGames(msg);
						break;
					case Macros.MSG_EXIT_OPPONENT:
						exitOponent();
						break;
				}
				
			}catch(SocketTimeoutException e){
				JOptionPane.showMessageDialog(jogoFrame, "Erro Timeout socket na thread \n" + e);
			}catch(NullPointerException e){
				JOptionPane.showMessageDialog(jogoFrame, "Erro NULLPOINTER na thread \n" + e);
				return;
			}catch(IOException e){
				JOptionPane.showMessageDialog(jogoFrame, "Erro de leitura na thread \n" + e);
				return;
			}catch(ClassNotFoundException e){
				JOptionPane.showMessageDialog(jogoFrame, "Erro na thread: classNotFound \n" + e);
			}//fim try catch	
		}//fim while(true)
		
	}
	
	
	
	private void exitOponent(){
		JOptionPane.showMessageDialog(jogoFrame, "O Seu adversário saiu");
		initGame();
	}
	
	private void initGame(){	

      jogo = new Jogo(listajogadores);
      
      listajogadores.setVisible(true);
		
		
	}
	
	private void showOnline(Mensagem msg){
		VarsGlobais.modelListaJogadores.clear();
		for(int i = 0;i<msg.getNomesClientes().size();i++){
			if(!msg.getNomesClientes().get(i).equals(listajogadores.getNomeJogador())){
				VarsGlobais.modelListaJogadores.addElement(msg.getNomesClientes().get(i));
			}
		}
		VarsGlobais.listaJogadores.setModel(VarsGlobais.modelListaJogadores);
		listajogadores.repaint();
	}
	
	private void showGames(Mensagem msg){
		
		VarsGlobais.modelListaJogos.clear();
		for(int i = 0;i<msg.getNomesJogos().size();i++){			
			VarsGlobais.modelListaJogos.addElement(msg.getNomesJogos().get(i));
		}
		VarsGlobais.listaJogos.setModel(VarsGlobais.modelListaJogos);
		listajogadores.repaint();
	}
	
	private void actualizaListaJogadores(){
		try {
			Mensagem msg = new Mensagem(Macros.MSG_LISTA_ONLINE);
			SocketClient_TCP.getOut().flush();
			SocketClient_TCP.getOut().writeObject(msg);
			SocketClient_TCP.getOut().flush();
			
		} catch (IOException e) {
			//ERRO A LIGAR AO SERVIDOR
			JOptionPane.showMessageDialog(jogoFrame,"ERRO NO SERVIDOR.... VAI ENCERRAR");
			System.exit(-1);
		}
	}
	
	private void actualizaListaJogos(){
		try {			
			Mensagem msg = new Mensagem(Macros.MSG_LISTA_JOGOS);
			SocketClient_TCP.getOut().flush();
			SocketClient_TCP.getOut().writeObject(msg);
			SocketClient_TCP.getOut().flush();
		} catch (IOException e) {
			//ERRO A LIGAR AO SERVIDOR
			JOptionPane.showMessageDialog(jogoFrame,"ERRO NO SERVIDOR.... VAI ENCERRAR");
			System.exit(-1);
		}
	}
	
	//receber os convites feitos por outros jogadores
	public void receberConvites(Mensagem msg) throws IOException{		
		msg.setType(Macros.MSG_PEDIDO_RESPONSE);
			
		int opcao; //opcao escolhida na dialog box
		Object[] options = {"Aceitar", "Rejeitar", "Ignorar"};
		String msgConvite = "Recebeu um convite de: " + msg.getMsgText();	
		String title = "Convite para um novo jogo!";
		opcao = JOptionPane.showOptionDialog(jogoFrame, msgConvite, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);	
		
		if(opcao == JOptionPane.YES_OPTION){
			//JOptionPane.showMessageDialog(jogoFrame, "SIM");//remover depois
			msg.setResponseText(Macros.ACEITAR_PEDIDO);
			listajogadores.dispose(); //se aceite...para fexar dialog
			
			 Jogo.setNomeJogador2(msg.getMsgText());
             BatalhaNaval_Client.setEstado("A aguardar que o jogador (X) ataque!"); 
				
			//enviar mensagem ao servidor
			SocketClient_TCP.getOut().flush();
			SocketClient_TCP.getOut().writeObject(msg);
			SocketClient_TCP.getOut().flush();
		        
		}else if(opcao == JOptionPane.NO_OPTION){
			//JOptionPane.showMessageDialog(jogoFrame, "NAO"); //remover depois
			msg.setResponseText(Macros.REJEITAR_PEDIDO);
			
			//enviar resposta ao servidor
			SocketClient_TCP.getOut().flush();
			SocketClient_TCP.getOut().writeObject(msg);
			SocketClient_TCP.getOut().flush();
				
		}else{
			msg.setResponseText(Macros.IGNORAR_PEDIDO);
			
			//enviar resposta ao servidor
			SocketClient_TCP.getOut().flush();
			SocketClient_TCP.getOut().writeObject(msg);
			SocketClient_TCP.getOut().flush();
		}
				
	}
	
	
	//para imprimir o tabuleiro esquerdo (do proprio jogador) conforme os dados do tabuleiro recebido
	public void receberTabuleiro(Mensagem msg){
			
		int x = 70;
		int y = 70;
			
		for(int i=0; i<Macros.SIZE_X; i++){
			   for(int j=0; j<Macros.SIZE_Y; j++){
				   
				   botao[i][j] = new JButton(agua);
				   botao[i][j].setBounds(x+(j*29), y+(i*29), Macros.TAM_X, Macros.TAM_Y);
				  //botao[i][j].addActionListener((ActionListener) this);			
				   botao[i][j].setDisabledIcon(agua); //senao aparecia cinzento quando nao esta em "enabled"
				   botao[i][j].setEnabled(false);
				   //formula marada (i)*10 ) + (j) para obter a quadricula certa
				   if(msg.getTabuleiro().getTabuleiro().get( (i)*10 + (j) ).isBoat()){
					   if(msg.getTabuleiro().getTabuleiro().get( (i)*10 + (j) ).getImage() == Macros.IMAGEM_BARCO_ESQ)
						   botao[i][j].setDisabledIcon(barcoEsq);
					   if(msg.getTabuleiro().getTabuleiro().get( (i)*10 + (j) ).getImage() == Macros.IMAGEM_BARCO_MEIO)
						   botao[i][j].setDisabledIcon(barcoMeio);
					   if(msg.getTabuleiro().getTabuleiro().get( (i)*10 + (j) ).getImage() == Macros.IMAGEM_BARCO_DIR)
						   botao[i][j].setDisabledIcon(barcoDir);
				   }
					   
					
				   jogoFrame.getContentPane().add(botao[i][j]);
			   }
		}
		
		if(msg.getMsgText().equals(listajogadores.getNomeJogador())){
			BatalhaNaval_Client.setEstado("É a sua vez de jogar!");
		}
		else
			BatalhaNaval_Client.setEstado("É a vez do seu adversário jogar!");
		
		jogoFrame.repaint();
		
	}
	
	
	
	//receber a RESPOSTA do servidor (sobre o convite que foi feito anteriormente)
		public void receberRespostaConvite(Mensagem msg) throws IOException{			
			
			VarsGlobais.sentConvite = false;
			
			if(msg.getResponseText().equals(Macros.ACEITAR_PEDIDO)){ //pedido ACEITE
				JOptionPane.showMessageDialog(jogoFrame, "O convite foi aceite pelo jogador! \nA iniciar um novo jogo..");				
				listajogadores.dispose(); //para fechar dialog
				
				
				Jogo.setNomeJogador2(ListaJogosEJogadores.nomeJogadorConvidado); //nome do jogador convidado 
	            
				
			}
			if(msg.getResponseText().equals(Macros.REJEITAR_PEDIDO)){ //pedido RECUSADO
				JOptionPane.showMessageDialog(jogoFrame, "O convite foi rejeitado pelo jogador!");
				
				return;
			}	
		}
		
		
		
	//actualiza o mapa (ESQUERDO) com a coordenada que foi atacada	
	public void actualizarTabuleiro(Mensagem msg) throws IOException{
			
		
		int coordY = msg.getLetra().getPosY() -1; //-1 para acertar a coisa
		int coordX = msg.getNumero().getPosX() -1;
		
		//agua
		if(msg.getImagem() == Macros.IMAGEM_FAIL)
			botao[coordY][coordX].setDisabledIcon(fail);
		
		//hit esquerdo
		if(msg.getImagem() == Macros.IMAGEM_BARCO_ESQ)
			botao[coordY][coordX].setDisabledIcon(hitEsq);
		//hit meio
		if(msg.getImagem() == Macros.IMAGEM_BARCO_MEIO)
			botao[coordY][coordX].setDisabledIcon(hitMeio);
		//hit direito
		if(msg.getImagem() == Macros.IMAGEM_BARCO_DIR)
			botao[coordY][coordX].setDisabledIcon(hitDir);
		
		if(msg.getMsgText().equals(listajogadores.getNomeJogador())){
			BatalhaNaval_Client.setEstado("É a sua vez de jogar!");
		}
		else
			BatalhaNaval_Client.setEstado("É a vez do seu adversário jogar!");
		
		jogoFrame.repaint();
		
	}
		
		
	//receber a resposta se acertou ou não num barco do inimigo
	public void respostaAtaque(Mensagem msg) throws IOException{
			
		int posX = msg.getNumero().getPosX() -1; //-1 para acertar a coisa
		int posY = msg.getLetra().getPosY() -1;
		
		
		//ATACOU COORDENADA REPETIDA
		if(msg.getType() == Macros.MSG_ATACAR_COORD_REPETIDA){
			JOptionPane.showMessageDialog(jogoFrame, "Já tinha atacado esta posição.\n Ataque outra.");					
			return;
		}
		
		//FALHOU
		if(msg.getType() == Macros.MSG_ATACAR_FAIL){
			BatalhaNaval_Client.setEstado("É a vez do seu adversário jogar!");
			//mudar o icone azul no tabuleiro para agua
			jogo.getBotaoAdv(posY, posX).setIcon(fail);
			return;
		}
		
		//ACERTOU
		if(msg.getType() == Macros.MSG_ATACAR_SUCCESS){
			BatalhaNaval_Client.setEstado("É a vez do seu adversário jogar!");
			//mudar o icone azul no tabuleiro para uma explosao
			jogo.getBotaoAdv(posY, posX).setIcon(hit);
			return;
		}
		
			
	}
		
	
	
}
