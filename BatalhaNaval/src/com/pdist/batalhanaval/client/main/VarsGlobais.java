package com.pdist.batalhanaval.client.main;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;




public class VarsGlobais {
	//CLIENTE
	public static boolean NovoJogoThreadCreated = false;
	public static boolean MeuTurno = false;
	
	public static ArrayList<Integer> tabJogador1 = new ArrayList<Integer>();
	
	public static DefaultListModel<String> modelListaJogos = new DefaultListModel<String>();  //LISTA JOGOS
	public static JList<String> listaJogos = new JList<String>(modelListaJogos);  //LISTA JOGOS
	
	public static DefaultListModel<String> modelListaJogadores = new DefaultListModel<String>();  //LISTA JOGADORES
	public static JList<String> listaJogadores = new JList<String>(modelListaJogadores); //LISTA JOGADORES
	
	//para impedir que o user spamme o outro com convites enquanto não receber a resposta do ultimo que mandou
	public static boolean sentConvite = false;
	
	public static boolean atendeServidorON = false;
	
	
	private VarsGlobais(){}
}
