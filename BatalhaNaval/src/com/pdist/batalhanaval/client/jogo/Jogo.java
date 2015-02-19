package com.pdist.batalhanaval.client.jogo;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.pdist.batalhanaval.client.dialogs.ListaJogosEJogadores;
import com.pdist.batalhanaval.client.listeners.JogoActionListener;
import com.pdist.batalhanaval.client.main.AtendeServidor;
import com.pdist.batalhanaval.client.main.BatalhaNaval_Client;
import com.pdist.batalhanaval.client.main.VarsGlobais;
import com.pdist.batalhanaval.server.macros.Macros;


public class Jogo{
	

	private static JogoActionListener listener = new JogoActionListener();
	public static JButton[][] botaoAdv;
	
	private JFrame BatalhaNavalUI;
	
	private static JLabel lblJogador_1 = new JLabel("<nome?>"); //label JOGADOR 1
	private static JLabel lblJogador_2 = new JLabel("<nome?>"); //laber JOGADOR 2
	
	//Imagens..	
	private static ImageIcon agua = new ImageIcon("Imagens/agua.png");
	private static ImageIcon mira = new ImageIcon("Imagens/mira.png");	
	//Bg2
	public static String background = "Imagens/outros/background2.jpg";		
		
	private Thread AtendeServidor; //thread do AtendeServidor
	
	
	
	
	public Jogo(ListaJogosEJogadores listajogadores) {
		
		
		BatalhaNavalUI = BatalhaNaval_Client.getBatalhaNavalUI();			
			
		BatalhaNaval_Client.loadBackground(background);		
		BatalhaNaval_Client.loadMenuBar();	
				
				
		criaMapaAdversario(400,70);
				
				
		criaLabels();
								
				
		BatalhaNavalUI.repaint(); //necessario fazer repaint depois..			
		
		
		//CRIAR A THREAD DE ATENDIMENTO de pedidos do servidor
		if(VarsGlobais.atendeServidorON==false){
		AtendeServidor = new AtendeServidor(BatalhaNavalUI,listajogadores, this);
		AtendeServidor.start();
		VarsGlobais.atendeServidorON=true;
		}
		
				
		
	}
	

	public JButton getBotaoAdv(int y, int x){
		return botaoAdv[y][x];
	}		 		 
		 

		 public void criaMapaAdversario(int x,int y) 
		 {
			 botaoAdv = new JButton[12][12];
			 
			  for(int i=0; i<10; i++){
				   for(int j=0; j<10; j++){			   
					 
				    botaoAdv[i][j] = new JButton(agua);		 
				    botaoAdv[i][j].setBounds(x+(j*29), y+(i*29), Macros.TAM_X, Macros.TAM_Y);
				    botaoAdv[i][j].addActionListener(listener);	
				    botaoAdv[i][j].setRolloverIcon(mira);
				    BatalhaNavalUI.getContentPane().add(botaoAdv[i][j]);				    
				  
				   }
			  }
		 }
	
		 
		 public void criaLabels()
		 {
			    JLabel label = new JLabel("Jogador:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Tahoma", Font.BOLD, 16));
				label.setBounds(89, 32, 83, 37);
				BatalhaNavalUI.getContentPane().add(label);
				
			    //Nome Jogador 1
				lblJogador_1.setForeground(Color.BLACK);
				lblJogador_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
				lblJogador_1.setBounds(169, 32, 163, 37);
				BatalhaNavalUI.getContentPane().add(lblJogador_1);		
				
				JLabel label_1 = new JLabel("Inimigo:");
				label_1.setForeground(Color.BLACK);
				label_1.setFont(new Font("Tahoma", Font.BOLD, 16));
				label_1.setBounds(415, 29, 73, 43);
				BatalhaNavalUI.getContentPane().add(label_1);			
				
				 //Nome Jogador 2
				lblJogador_2.setForeground(Color.BLACK);
				lblJogador_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
				lblJogador_2.setBounds(490, 29, 192, 43);
				BatalhaNavalUI.getContentPane().add(lblJogador_2);
				
		 }
		 
		 public static void setNomeJogador1(String nome)
		 {
			 lblJogador_1.setText(nome);			 
		 }
		 
		 public static void setNomeJogador2(String nome)
		 {
			 lblJogador_2.setText(nome);			 
		 }


		public JButton[][] getBotaoAdv() {
			return botaoAdv;
		}


		public void setBotaoAdv(JButton[][] botaoAdv) {
			Jogo.botaoAdv = botaoAdv;
		}


		public JFrame getBatalhaNavalUI() {
			return BatalhaNavalUI;
		}


		public void setBatalhaNavalUI(JFrame batalhaNavalUI) {
			BatalhaNavalUI = batalhaNavalUI;
		}


		public static JLabel getLblJogador_1() {
			return lblJogador_1;
		}


		public static void setLblJogador_1(JLabel lblJogador_1) {
			Jogo.lblJogador_1 = lblJogador_1;
		}


		public static JLabel getLblJogador_2() {
			return lblJogador_2;
		}


		public static void setLblJogador_2(JLabel lblJogador_2) {
			Jogo.lblJogador_2 = lblJogador_2;
		}


		public ImageIcon getAgua() {
			return agua;
		}


		public void setAgua(ImageIcon agua) {
			Jogo.agua = agua;
		}


		public ImageIcon getMira() {
			return mira;
		}


		public void setMira(ImageIcon mira) {
			Jogo.mira = mira;
		}


		public static String getBackground() {
			return background;
		}


		public static void setBackground(String background) {
			Jogo.background = background;
		}


		public Thread getAtendeServidor() {
			return AtendeServidor;
		}


		public void setAtendeServidor(Thread atendeServidor) {
			AtendeServidor = atendeServidor;
		}
		 
		
	
}
