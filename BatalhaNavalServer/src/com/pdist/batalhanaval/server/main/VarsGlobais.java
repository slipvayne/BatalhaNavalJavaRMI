package com.pdist.batalhanaval.server.main;

import java.util.ArrayList;
import com.pdist.batalhanaval.server.controlo.Cliente;
import com.pdist.batalhanaval.server.controlo.Jogo;

public class VarsGlobais {

	
	public static ArrayList<Thread> threads = new ArrayList<Thread>();
	public static ArrayList<Jogo> jogos = new ArrayList<Jogo>();
	public static ArrayList<Cliente> ClientesOn = new ArrayList<Cliente>();
	
	public static int nThreads = 0;
	public static int nJogos = 0;
	public static int nClientes = 0;
	
	private VarsGlobais(){}

}
