package com.pdist.batalhanaval.server.main;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.pdist.batalhanaval.server.controlo.Jogo;
import com.pdist.batalhanaval.server.controlo.Letra;
import com.pdist.batalhanaval.server.controlo.Numero;
import com.pdist.batalhanaval.server.controlo.Ships;
import com.pdist.batalhanaval.server.controlo.Tabuleiro;
import com.pdist.batalhanaval.server.controlo.UnidadeTabuleiro;
import com.pdist.batalhanaval.server.macros.Macros;
import com.pdist.batalhanaval.server.mensagens.Mensagem;

public class GameObject{

	private Jogo jogo;
	private boolean online;
	private ObjectOutputStream out1;
	private ObjectOutputStream out2;
	

	public GameObject(Jogo jogo, ObjectOutputStream out1, ObjectOutputStream out2){
		this.jogo = jogo;
		
		//Alterado
		this.out1 = out1;
		this.out2 = out2;
		
		online = true;
		
		VarsGlobais.jogos.add(jogo);
		VarsGlobais.nJogos++;
		VarsGlobais.nClientes = VarsGlobais.nClientes-2;
		VarsGlobais.ClientesOn.remove(jogo.getC1());
		VarsGlobais.ClientesOn.remove(jogo.getC2());
		
				
	}
	
	public synchronized void start() {
		Mensagem msg = null;
		
		Tabuleiro tab1 = gerarTabuleiro();
		Tabuleiro tab2 = gerarTabuleiro();
		jogo.getC1().setTabuleiro(tab1);
		jogo.getC2().setTabuleiro(tab2);
		
		//valores fixos SE OS BARCOS FOREM GERADOS ALEAT. NAO FUNCIONA!!
		//nota: o numero de quadriculas barco terá de ser igual para os 2, nenhum jogador pode ter mais 
		//  ou menos barcos do que o outro
		jogo.getC1().getTabuleiro().setNumQuadBarcos(11);
		jogo.getC2().getTabuleiro().setNumQuadBarcos(11);
		
		
		if(jogo.isStarted()){			
			
			//a mensagem pode ser a mesma porque o tabuleiro não é random (caso contrario é preciso enviar uma mensagem diferente para cada jogador)
			//msg = new Mensagem(Macros.MSG_GET_TABULEIRO, gerarTabuleiro());
			try {
				
				msg = new Mensagem(Macros.MSG_GET_TABULEIRO, tab1);
				
				msg.setMsgText(jogo.getC1().getNome()); //USADO PARA AVISAR DE QUEM É O TURNO
				out1.flush();
				out1.writeObject(msg);
				out1.flush();
				
				msg = new Mensagem(Macros.MSG_GET_TABULEIRO, tab2);
				msg.setMsgText(jogo.getC1().getNome());
				out2.flush();
				out2.writeObject(msg);
				out2.flush();			
				
			} catch (IOException e) {				
				System.out.println("Ligação caiu");
				VarsGlobais.jogos.remove(jogo);
				VarsGlobais.nJogos--;
				VarsGlobais.nClientes = VarsGlobais.nClientes+2;
				VarsGlobais.ClientesOn.add(jogo.getC1());
				VarsGlobais.ClientesOn.add(jogo.getC2());
				online = false;
				return;
			}
			
		}
		
			
	}

	public Jogo getJogo(){return jogo;}
	
	
	public boolean getOnline(){return online;}
	public void notifyAtack(int jog, int y, int x, int img) throws IOException{	
	
		System.out.println("-notifyAtack-"); //so para testes
		Letra coord_y = new Letra('a', y);
		Numero coord_x = new Numero('1', x);
	
		
		
		Mensagem msg = new Mensagem(Macros.MSG_ACTUALIZAR_YOUR_TAB, coord_y, coord_x);
		msg.setImagem(img);
		
		if(jogo.getTurn() == 1)
			msg.setMsgText(jogo.getC1().getNome());
		else
			msg.setMsgText(jogo.getC2().getNome());
		
		if(jog == 1){	
			
			out1.flush();
			out1.writeObject(msg);
			out1.flush();
			
			//agua
			if(img == Macros.IMAGEM_FAIL)
				jogo.getC1().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_FAIL);					
			//hit esquerdo
			if(img == Macros.IMAGEM_BARCO_ESQ)
				jogo.getC1().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_BARCO_ESQ_FOGO);	
			//hit meio
			if(img == Macros.IMAGEM_BARCO_MEIO)
				jogo.getC1().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_BARCO_MEIO_FOGO);	
			//hit direito
			if(img == Macros.IMAGEM_BARCO_DIR)
				jogo.getC1().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_BARCO_DIR_FOGO);	
			
			boolean allHit = true;
			for(int i = 0;i < 10;i++){
				for(int j = 0;j<10;j++){
					if(jogo.getC1().getTabuleiro().getUnidade(j+1, i+1).getImage()>1001 &&
							jogo.getC1().getTabuleiro().getUnidade(j+1, i+1).getImage()<1005){
						allHit = false;
						break;
					}
				}
			
			}
				
				
			System.out.println("ALLHIT: "+allHit);
			if(allHit)
				notifyOfEnd(jog);		
			
		}else{
			out2.flush();
			out2.writeObject(msg);
			out2.flush();
			//agua
			if(img == Macros.IMAGEM_FAIL)
				jogo.getC2().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_FAIL);					
			//hit esquerdo
			if(img == Macros.IMAGEM_BARCO_ESQ)
				jogo.getC2().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_BARCO_ESQ_FOGO);	
			//hit meio
			if(img == Macros.IMAGEM_BARCO_MEIO)
				jogo.getC2().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_BARCO_MEIO_FOGO);	
			//hit direito
			if(img == Macros.IMAGEM_BARCO_DIR)
				jogo.getC2().getTabuleiro().getUnidade(y,x).setImage(Macros.IMAGEM_BARCO_DIR_FOGO);	
			
			boolean allHit = true;
			for(int i = 0;i < 10;i++){
				for(int j = 0;j<10;j++){
					if(jogo.getC2().getTabuleiro().getUnidade(j+1, i+1).getImage()>1001 &&
							jogo.getC2().getTabuleiro().getUnidade(j+1, i+1).getImage()<1005){
						allHit = false;
						break;
					}
				}
			
			}
				
			System.out.println("ALLHIT: "+allHit);
			if(allHit)
				notifyOfEnd(jog);
		}
				
		
	}
		
	private void notifyOfEnd(int jog)throws IOException{
		if(jog == 1){//SE o 1 perdeu
			Mensagem msg = new Mensagem(Macros.MSG_DERROTA);
			out1.flush();
			out1.writeObject(msg);
			out1.flush();
			
			msg.setType(Macros.MSG_VITORIA);
			out2.flush();
			out2.writeObject(msg);
			out2.flush();
		}else{
			Mensagem msg = new Mensagem(Macros.MSG_DERROTA);
			out2.flush();
			out2.writeObject(msg);
			out2.flush();
			
			msg.setType(Macros.MSG_VITORIA);
			out1.flush();
			out1.writeObject(msg);
			out1.flush();
		}
		
		jogo.setFim(true);
		VarsGlobais.jogos.remove(jogo);
		VarsGlobais.nJogos--;
		
		VarsGlobais.ClientesOn.add(jogo.getC1());
		VarsGlobais.nClientes++;
		VarsGlobais.ClientesOn.add(jogo.getC2());
		VarsGlobais.nClientes++;
	}
	
	//ATENCAO! ISTO NAO ESTA A GERAR ALEATORIAMENTE, OS BARCOS SAO FIXOS
	public Tabuleiro gerarTabuleiro(){
		
		Tabuleiro tab;
		ArrayList<UnidadeTabuleiro> unidades = new ArrayList<UnidadeTabuleiro>();
		
		//criar ArrayList de UnidadesTabuleiro 10x10
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				unidades.add(new UnidadeTabuleiro(j+1, i+1) ); //x-j, y-i
			}
		}
		
		
		//gerar posição dos barcos OU (neste caso) definir posições fixas
		ArrayList<UnidadeTabuleiro> unidades_barco1 = new ArrayList<UnidadeTabuleiro>();
		ArrayList<UnidadeTabuleiro> unidades_barco2 = new ArrayList<UnidadeTabuleiro>();
		ArrayList<UnidadeTabuleiro> unidades_barco3 = new ArrayList<UnidadeTabuleiro>();
		
		  //22,23,24,25 -> posições (y=3 e x=3,4,5,6)
		unidades.get(22).setOcupied(true);
		unidades.get(23).setOcupied(true);
		unidades.get(24).setOcupied(true);
		unidades.get(25).setOcupied(true);
		
		unidades.get(22).setImage(Macros.IMAGEM_BARCO_ESQ);
		unidades.get(23).setImage(Macros.IMAGEM_BARCO_MEIO);
		unidades.get(24).setImage(Macros.IMAGEM_BARCO_MEIO);
		unidades.get(25).setImage(Macros.IMAGEM_BARCO_DIR);
		
		unidades_barco1.add( unidades.get(22) );
		unidades_barco1.add( unidades.get(23) );
		unidades_barco1.add( unidades.get(24) );
		unidades_barco1.add( unidades.get(25) );
		  //criar o barco em si
		Ships barco1 = new Ships(unidades_barco1);
		
		  //74,75,76,77,78 -> posições (y=8 e x=5,6,7,8,9)
		unidades.get(74).setOcupied(true);
		unidades.get(75).setOcupied(true);
		unidades.get(76).setOcupied(true);
		unidades.get(77).setOcupied(true);
		unidades.get(78).setOcupied(true);
		
		unidades.get(74).setImage(Macros.IMAGEM_BARCO_ESQ);
		unidades.get(75).setImage(Macros.IMAGEM_BARCO_MEIO);
		unidades.get(76).setImage(Macros.IMAGEM_BARCO_MEIO);
		unidades.get(77).setImage(Macros.IMAGEM_BARCO_MEIO);
		unidades.get(78).setImage(Macros.IMAGEM_BARCO_DIR);
		
		unidades_barco2.add( unidades.get(74) );
		unidades_barco2.add( unidades.get(75) );
		unidades_barco2.add( unidades.get(76) );
		unidades_barco2.add( unidades.get(77) );
		unidades_barco2.add( unidades.get(78) );
		
		  //criar o barco em si
		Ships barco2 = new Ships(unidades_barco2);
		
		  //5,6 -> posições (y=1 e x=6,7)
		unidades.get(5).setOcupied(true);
		unidades.get(6).setOcupied(true);
		unidades.get(5).setImage(Macros.IMAGEM_BARCO_ESQ);
		unidades.get(6).setImage(Macros.IMAGEM_BARCO_DIR);
		
		unidades_barco3.add( unidades.get(5) );
		unidades_barco3.add( unidades.get(6) );
		
		  //criar o barco em si
		//Ships barco3 = new Ships(unidades_barco3);
		
		  //criar o arraylist dos barcos
		ArrayList<Ships> barcos = new ArrayList<Ships>();
		barcos.add(barco1);
		barcos.add(barco2);
		
		
		//criar o tabuleiro
		tab = new Tabuleiro(unidades, barcos);
		
		//CASO O TABULEIRO SEJA ALEATORIO É PRECISO ALTERAR ISTO
		//jogo.getC1().setTabuleiro(tab);
		//Tabuleiro tab2 = new Tabuleiro(unidades, barcos);
		//jogo.getC2().setTabuleiro(tab2);
		
		return tab;
	}
	
	
	
}
