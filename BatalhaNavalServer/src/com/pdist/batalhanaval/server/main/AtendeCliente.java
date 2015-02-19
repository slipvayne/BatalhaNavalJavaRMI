package com.pdist.batalhanaval.server.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.pdist.batalhanaval.server.controlo.Cliente;
import com.pdist.batalhanaval.server.controlo.Jogo;
import com.pdist.batalhanaval.server.macros.Macros;
import com.pdist.batalhanaval.server.mensagens.Mensagem;
import com.pdist.batalhanaval.server.rmi.BatalhaNavalRMIService;

public class AtendeCliente extends Thread{
	
	protected Socket socket;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;
	protected boolean logIn;
	protected Cliente cliente;
	protected GameObject game;
	
	
	public AtendeCliente(Socket s){
		game = null;
		socket = s;
		criaCliente();				
		logIn = false;
		
		try {
			in = new ObjectInputStream(socket.getInputStream());    //so pode ser criado uma vez (no cria cliente ja actualizei)
			out = new ObjectOutputStream(socket.getOutputStream());
			
			//meti no criaCliente() tb mas nao tava a dar nada, dps alterar isto ou deixar tar
			cliente.setIn(in);
			cliente.setOut(out);
			
		} catch (IOException e) {
			System.out.println("Conexão falhou");	
			return;
		}
		
	}
	
	public void run(){		
		while(true){			
			//tratar mensagens recebidas e enviar respostas
			try {					
				
				//TODO O jogador quer criar um novo jogo
				//TODO rever a cena do tabuleiro, n vale a pena complicar muito
				
				Mensagem msg = (Mensagem) in.readObject();
				
				//Java RMI
				
				
				
				switch(msg.getType()){
					case Macros.MSG_LOGIN_REQUEST: //utilizador pede para se logar
						getLoginRequest(msg);						
						break;
					case Macros.MSG_LISTA_ONLINE: //o utilizador pede a lista de jogadores
						sendListaOnline(msg);
						BatalhaNavalRMIService.notifyObservers(); //pede lista online.. pode ser um novo jogador ou acabou um jogo
						break;
					case Macros.MSG_LISTA_JOGOS: //o utilizador pede a lista de jogos a decorrer
						sendListaJogos(msg);
						
						break;
					case Macros.MSG_INICIAR_JOGO: //o utilizador faz um pedido de jogo
						sendInvite(msg);
						BatalhaNavalRMIService.notifyObservers();  //notify novo jogo...
						
						break;
					case Macros.MSG_PEDIDO_RESPONSE: //Resposta do 2º utilizador ao pedido de jogo						
						getResponse(msg);
						BatalhaNavalRMIService.notifyObservers();  //notify novo jogo...
						break;		
					case Macros.MSG_ATACAR:
						System.out.println("atacar?"); //so para testes
						if(game!=null)
							if(game.getJogo().isStarted()){
								System.out.println("o jogo esta iniciado! ATACAAAAR!!"); //so para testes
								setAtaque(msg);
							}
						BatalhaNavalRMIService.notifyObservers();  //notify novo ataque..
						break;
					
				}					
			} catch (IOException | NullPointerException e) {
				System.out.println("Cliente desligou");	
				if(game == null){
					VarsGlobais.ClientesOn.remove(cliente);		
					VarsGlobais.nClientes--;
					notifyChangesOnPlayers();
				}
				if(game != null){
					notificaAdversario();
					notifyChangesOnPlayers();
					notifyChangesOnGames();
				}
				break;
			} catch (ClassNotFoundException  e) {
				System.out.println("ERR: Não é mensagem");				
			} 
		}

		
		VarsGlobais.threads.remove(this);
		VarsGlobais.nThreads--;
	}
	
	
	private synchronized boolean validaLogin(Mensagem msg){
		boolean result = true;
		
		//ver se o nome não é repetido
		for(int i = 0;i<VarsGlobais.ClientesOn.size();i++){
			if(VarsGlobais.ClientesOn.get(i).getNome().compareToIgnoreCase(msg.getMsgText())==0){
				result = false;
				break;
			}
		}
		
		if(result)
			cliente.setNome(msg.getMsgText());
		
		return result;
	}
	
	private synchronized void criaCliente(){
		cliente = new Cliente(socket.getInetAddress());
		cliente.setMySocket(socket);
		
		cliente.setIn(in);
		cliente.setOut(out);
		
		cliente.setMyThread(this);
		cliente.setOnGame(false);		
	}
	
	
	private synchronized void getLoginRequest(Mensagem msg) throws IOException{
		
		if(!logIn){
			if(!validaLogin(msg)){
				msg.setType(Macros.MSG_LOGIN_FAIL);
				out.flush();
				out.writeObject(msg);
				out.flush();		
				System.out.println("O Login falhou");
			}else{
				msg.setType(Macros.MSG_LOGIN_VALIDATED);
				out.flush();
				out.writeObject(msg);
				out.flush();
				logIn = true;
				VarsGlobais.ClientesOn.add(cliente);
				VarsGlobais.nClientes++;
				
				notifyChangesOnPlayers();
				
				System.out.println("O cliente "+VarsGlobais.ClientesOn.get(VarsGlobais.nClientes-1).getNome()+" esta logado.");
				System.out.println("Estão logados "+VarsGlobais.nClientes+" clientes");
			}
		}else{
			msg.setType(Macros.MSG_LOGIN_LOGGED);
			out.flush();
			out.writeObject(msg);
			out.flush();
			System.out.println("Ja esta logado");
		}
	}
		
	private void notifyChangesOnPlayers(){
		System.out.println("O Jogador "+cliente.getNome()+" foi notificado");
		for(int i = 0; i<VarsGlobais.ClientesOn.size();i++){
			Mensagem msg = new Mensagem(Macros.MSG_NOTIFY_CHANGES_PLAYERS);
			try {
			
				VarsGlobais.ClientesOn.get(i).getOut().flush();
				VarsGlobais.ClientesOn.get(i).getOut().writeObject(msg);
				VarsGlobais.ClientesOn.get(i).getOut().flush();
				
			} catch (IOException e) {
				//admite-se que o cliente fechou
				try {
					VarsGlobais.ClientesOn.get(i).getMySocket().close();				
					VarsGlobais.ClientesOn.get(i).getMyThread().finalize();
					VarsGlobais.ClientesOn.remove(i);
					VarsGlobais.nClientes--;
					notifyChangesOnPlayers();
				} catch (Throwable e1) {					
					e1.printStackTrace();
				}			
			}			
		}
	}
	
	private void notifyChangesOnGames(){
		System.out.println("O Jogador "+cliente.getNome()+" foi notificado");
		for(int i = 0; i<VarsGlobais.ClientesOn.size();i++){
			Mensagem msg = new Mensagem(Macros.MSG_NOTIFY_CHANGES_GAMES);
			try {
			
				VarsGlobais.ClientesOn.get(i).getOut().flush();
				VarsGlobais.ClientesOn.get(i).getOut().writeObject(msg);
				VarsGlobais.ClientesOn.get(i).getOut().flush();
				
			} catch (IOException e) {
				//admite-se que o cliente fechou
				try {
					VarsGlobais.ClientesOn.get(i).getMySocket().close();				
					VarsGlobais.ClientesOn.get(i).getMyThread().finalize();
					VarsGlobais.ClientesOn.remove(i);
					VarsGlobais.nClientes--;
					notifyChangesOnPlayers();
				} catch (Throwable e1) {					
					e1.printStackTrace();
				}			
			}			
		}
	}

	private void sendListaOnline(Mensagem msg) throws IOException{
		System.out.println("O Jogador "+cliente.getNome()+" recebeu lista jogadores");
		msg.setType(Macros.MSG_ONLINE_RESPONSE);
		
		msg.getNomesClientes().clear();
		for(int i = 0;i<VarsGlobais.nClientes;i++)
			msg.addNomesClientes(VarsGlobais.ClientesOn.get(i).getNome());
		
		out.flush();
		out.writeObject(msg);
		out.flush();
	}
	
	private void sendListaJogos(Mensagem msg) throws IOException{
		System.out.println("O Jogador "+cliente.getNome()+" recebeu lista jogos");
		msg.setType(Macros.MSG_JOGOS_RESPONSE);		
		msg.getNomesJogos().clear();
		for(int i = 0;i<VarsGlobais.nJogos;i++){
			String nomeJogo = "Jogo num "+(i+1)+": "+VarsGlobais.jogos.get(i).getC1().getNome()+" VS "+VarsGlobais.jogos.get(i).getC2().getNome();				
			msg.addNomesJogos(nomeJogo);
		}
		
		out.flush();
		out.writeObject(msg);
		out.flush();
		
	}
	
	private void sendInvite(Mensagem msg) throws IOException{
		msg.setType(Macros.MSG_PEDIDO_JOGO);
		System.out.println("Enviado convite de: "+cliente.getNome()+"\nPara: "+msg.getMsgText());
		
		//no campo MsgText vem o nome do jogador a convidar e depois vai o nome de quem convidou
		for(int i = 0;i<VarsGlobais.nClientes;i++){
			if(VarsGlobais.ClientesOn.get(i).getNome().equalsIgnoreCase(msg.getMsgText())){
				msg.setMsgText(cliente.getNome());
				VarsGlobais.ClientesOn.get(i).getMyThread().out.flush();
				VarsGlobais.ClientesOn.get(i).getMyThread().out.writeObject(msg);
				System.out.println("SENT");
				break;
			}				
		}
		
		
	}

	private synchronized void getResponse(Mensagem msg) throws IOException{
		msg.setType(Macros.MSG_INICIAR_RESPONSE);
		
		for(int i = 0;i<VarsGlobais.nClientes;i++){
			if(VarsGlobais.ClientesOn.get(i).getNome().equalsIgnoreCase(msg.getMsgText())){				
				VarsGlobais.ClientesOn.get(i).getMyThread().out.flush();
				VarsGlobais.ClientesOn.get(i).getMyThread().out.writeObject(msg);	
				VarsGlobais.ClientesOn.get(i).getMyThread().out.flush();
				break;
			}				
		}
				
		if(msg.getResponseText().equals(Macros.ACEITAR_PEDIDO)){
			Cliente c2 = null;
			for(int i = 0; i< VarsGlobais.nClientes;i++)
				if(VarsGlobais.ClientesOn.get(i).getNome().equalsIgnoreCase(msg.getMsgText())){
					c2 = VarsGlobais.ClientesOn.get(i);
					break;
				}
						
			startNewGame(cliente,c2);			
		}
	}
	
	private void startNewGame(Cliente jog1, Cliente jog2){
		Jogo j = new Jogo();
		j.setC1(jog1);
		j.setC2(jog2);
		j.setStarted(true);
				
		System.out.println("==\nNovo Jogo!\n"+jog1.getNome()+" VS "+jog2.getNome());
		

		GameObject jogo = new GameObject(j,	jog1.getOut(),jog2.getOut());
		
		jog2.getMyThread().setGame(jogo);		
		game = jogo;		
		jogo.start(); //inicia a thread aqui...
		
		notifyChangesOnGames();
		notifyChangesOnPlayers();
	}

	private synchronized void setGame(GameObject game){this.game = game;}
	
	private synchronized void setAtaque(Mensagem msg){
		
		int t = 0;
		int imagem = 0; //imagem da quadricula
		
		int posX = msg.getNumero().getPosX();
		int posY = msg.getLetra().getPosY();
		
		if(!game.getOnline())
			notificaAdversario();
		else{
			//ATACA NO TABULEIRO ADVERSARIO
			if(game.getJogo().getC1().getNome().equalsIgnoreCase(cliente.getNome())){ //jogador 1
				if(game.getJogo().getTurn() == 1){
					System.out.println("JOGADOR1 TURNO");
					if(game.getJogo().getC2().getTabuleiro().getUnidade(posY, posX).isShooted() == true){
						msg.setType(Macros.MSG_ATACAR_COORD_REPETIDA);
						System.out.println("tentou atacar coordenada repetida");
					}else{
						if(game.getJogo().getC2().getTabuleiro().getUnidade(posY, posX).isBoat() == true)
							msg.setType(Macros.MSG_ATACAR_SUCCESS); //se for um barco
						else
							msg.setType(Macros.MSG_ATACAR_FAIL); //se nao for um barco (agua)
							
						game.getJogo().getC2().getTabuleiro().getUnidade(posY, posX).setShooted(true);
						game.getJogo().setTurn(2);	
						t = 2;
						
						
						imagem = game.getJogo().getC2().getTabuleiro().getUnidade(posY, posX).getImage();
					}
				}
				
				
			}else{												//jogador 2
				
				if(game.getJogo().getTurn() == 2){
					System.out.println("JOGADOR 2 TURNO"); //teste
					if(game.getJogo().getC1().getTabuleiro().getUnidade(posY, posX).isShooted() == true){
						msg.setType(Macros.MSG_ATACAR_COORD_REPETIDA);
						System.out.println("tentou atacar coordenada repetida");
					}else{
							if(game.getJogo().getC1().getTabuleiro().getUnidade(posY, posX).isBoat() == true)
								msg.setType(Macros.MSG_ATACAR_SUCCESS); //se for um barco
							else
								msg.setType(Macros.MSG_ATACAR_FAIL); //se nao for um barco (agua)
							
							
							game.getJogo().getC1().getTabuleiro().getUnidade(posY, posX).setShooted(true);
							game.getJogo().setTurn(1);	
							t = 1;
							
							imagem = game.getJogo().getC1().getTabuleiro().getUnidade(posY, posX).getImage();
					}
				}
			}
				
			try {
				
				
				out.flush();
				out.writeObject(msg);
				out.flush();
				
				System.out.println(msg.getType()); //para testes
				
				if( (msg.getType() == Macros.MSG_ATACAR_SUCCESS) || (msg.getType() == Macros.MSG_ATACAR_FAIL) )
					game.notifyAtack(t, posY, posX, imagem);
				
				if(game.getJogo().isFim()){
					game = null;
				}
			} catch (IOException e) {
				System.out.println("Cliente desligou");	
				notificaAdversario();			
			}
		}
	}
	
	
	private void notificaAdversario(){
		if(!game.getJogo().getC1().getNome().equals(cliente.getNome())){
			Mensagem msg = new Mensagem(Macros.MSG_EXIT_OPPONENT);
			try {
				game.getJogo().getC1().getOut().flush();
				game.getJogo().getC1().getOut().writeObject(msg);
				game.getJogo().getC1().getOut().flush();
				
				VarsGlobais.ClientesOn.add(game.getJogo().getC1());
				VarsGlobais.nClientes++;
				VarsGlobais.jogos.remove(game.getJogo());
				VarsGlobais.nJogos--;
				game = null;
			} catch (IOException e) {
				VarsGlobais.jogos.remove(game.getJogo());
				VarsGlobais.nJogos--;
				game = null;
			}
		}else{
			Mensagem msg = new Mensagem(Macros.MSG_EXIT_OPPONENT);
			try {
				game.getJogo().getC2().getOut().flush();
				game.getJogo().getC2().getOut().writeObject(msg);
				game.getJogo().getC2().getOut().flush();
				
				VarsGlobais.ClientesOn.add(game.getJogo().getC2());
				VarsGlobais.nClientes++;
				VarsGlobais.jogos.remove(game.getJogo());
				VarsGlobais.nJogos--;
				game = null;
				notifyChangesOnPlayers();
				notifyChangesOnGames();
			} catch (IOException e) {
				VarsGlobais.jogos.remove(game.getJogo());
				VarsGlobais.nJogos--;
				game = null;
				notifyChangesOnPlayers();
				notifyChangesOnGames();
			}
		}
	}
			
	
}



