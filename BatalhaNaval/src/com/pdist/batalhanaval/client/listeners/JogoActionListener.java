package com.pdist.batalhanaval.client.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import com.pdist.batalhanaval.client.jogo.Jogo;
import com.pdist.batalhanaval.client.main.BatalhaNaval_Client;
import com.pdist.batalhanaval.client.main.SocketClient_TCP;
import com.pdist.batalhanaval.server.controlo.Letra;
import com.pdist.batalhanaval.server.controlo.Numero;
import com.pdist.batalhanaval.server.macros.Macros;
import com.pdist.batalhanaval.server.mensagens.Mensagem;

public class JogoActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		 StringTokenizer tokens = new StringTokenizer(new String(arg0.getSource().toString()),",");
		 tokens.nextToken().trim();
		 int x = Integer.parseInt(tokens.nextToken().trim());
	     int y = Integer.parseInt(tokens.nextToken().trim());	 
		  
	     for(int i=0; i<10; i++){
			   for(int j=0; j<10; j++){	
				   
				   if(Jogo.botaoAdv[i][j].getBounds().x == x && Jogo.botaoAdv[i][j].getBounds().y == y)
				   {				 		
					   
					   Letra coord_y = new Letra('a', i+1);
					   Numero coord_x = new Numero('1', j+1);
					   
					   
					   Mensagem msg = new Mensagem(Macros.MSG_ATACAR, coord_y, coord_x); //atacar, Y(letra), X(numero)
					   try{
						   SocketClient_TCP.getOut().flush();
						   SocketClient_TCP.getOut().writeObject(msg);
						   SocketClient_TCP.getOut().flush();
					   }catch(NullPointerException exp){
						   JOptionPane.showMessageDialog( BatalhaNaval_Client.getBatalhaNavalUI()	, "ERRO a enviar a coordenada a atacar(NullPointer)" + exp);
					   }catch(IOException exp){
						   JOptionPane.showMessageDialog( BatalhaNaval_Client.getBatalhaNavalUI()	, "ERRO a enviar a coordenada a atacar(IOException)" + exp);
					   }
					   
				   
				   }
				   
				   
			   }
	     }
	}
}


