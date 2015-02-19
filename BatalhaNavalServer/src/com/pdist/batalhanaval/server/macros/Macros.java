package com.pdist.batalhanaval.server.macros;


public class Macros {

	public static final int SIZE_X = 10;
	public static final int SIZE_Y = 10;
	public static final int TAM_X = 30;
	public static final int TAM_Y = 30;
	public static int MAX_SIZE = 1000;
	public static final String askIP = "GIVE_IP";
	public final static int TIMEOUT = 1500;
	
	//MACROS MENSAGENS
	public static final int MSG_LOGIN_REQUEST = 1; //pedido para o jogador se registar
	public static final int MSG_LOGIN_FAIL = 2; // aviso de falha
	public static final int MSG_LOGIN_VALIDATED = 3; // aviso de validação
	public static final int MSG_LOGIN_CANCEL = 4; //Aviso de que o utilizador já não se quer registar
	public static final int MSG_LOGIN_LOGGED = 5; //Ja estava logado
	
	public static final int MSG_LISTA_ONLINE = 6;
	public static final int MSG_ONLINE_RESPONSE = 7;
	public static final int MSG_LISTA_JOGOS = 8;
	public static final int MSG_JOGOS_RESPONSE = 9;
	
	public static final int MSG_INICIAR_JOGO = 10;//um jogador pede para o jogo começar
	public static final int MSG_PEDIDO_JOGO = 11;//o servidor reencaminha o pedido para o 2º jogador
	public static final int MSG_INICIAR_RESPONSE = 12;//Resposta dada ao 1º utilizador
	public static final int MSG_PEDIDO_RESPONSE = 13;//Resposta dada pelo 2º utilizador ao servidor
	
	public static final int MSG_JOGAR = 14;
	public static final int MSG_JOGAR_RESPONSE = 15;
	public static final int MSG_GET_TABULEIRO = 16;
	public static final int MSG_SET_TABULEIRO = 17;
	
	public static final int MSG_ATACAR = 18;//jogador quer atacar o outro
	public static final int MSG_ATACAR_FAIL = 19;//falhou o tiro
	public static final int MSG_ATACAR_SUCCESS = 20;//acertou o tiro
	public static final int MSG_ACTUALIZAR_YOUR_TAB = 21;
	public static final int MSG_ATACAR_COORD_REPETIDA = 23; //atacar uma coordenada que já foi atacada antes
	
	public static final int MSG_EXIT_OPPONENT = 27;
	
	public static final int MSG_NOTIFY_CHANGES_PLAYERS = 22;
	public static final int MSG_NOTIFY_CHANGES_GAMES = 26;
	
	public static final int MSG_VITORIA = 24; //venceu o jogo
	public static final int MSG_DERROTA = 25; //perdeu o jogo
	
	public static final String ACEITAR_PEDIDO = "aceitar";
	public static final String REJEITAR_PEDIDO = "rejeitar";
	public static final String IGNORAR_PEDIDO = "ignorar"; // pode ser com timeOut depois de receber a proposta
	
	public static final int IMAGEM_AGUA = 1000;
	public static final int IMAGEM_FAIL = 1001;
	public static final int IMAGEM_BARCO_ESQ = 1002;//cabeça do barco
	public static final int IMAGEM_BARCO_MEIO = 1003;//meio do barco
	public static final int IMAGEM_BARCO_DIR = 1004;//resto do barco	
	//com Fogo
	public static final int IMAGEM_BARCO_ESQ_FOGO = 1005;//cabeça do barco
	public static final int IMAGEM_BARCO_MEIO_FOGO = 1006;//cabeça do barco
	public static final int IMAGEM_BARCO_DIR_FOGO = 1007;//resto do barco
	
	//provisorio
	public static final int IMAGEM_SHOTED = 1008;
	
	private Macros(){} //Singleton
	

}
