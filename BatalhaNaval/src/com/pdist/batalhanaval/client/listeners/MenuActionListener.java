package com.pdist.batalhanaval.client.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import com.pdist.batalhanaval.client.dialogs.NovoJogo_Ip;
import com.pdist.batalhanaval.client.dialogs.NovoJogo_Multicast;
import com.pdist.batalhanaval.client.main.VarsGlobais;

public class MenuActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		switch(command){
			case "Novo Jogo (IP/Port)":
				try {			
					if(!VarsGlobais.NovoJogoThreadCreated){
						NovoJogo_Ip dialog = new NovoJogo_Ip();
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);	
					}
					
				} catch (Exception e1) {
					//TRATAR
					e1.printStackTrace();
				}
				break;
				
			case "Novo Jogo (Multicast)":
				try {			
					if(!VarsGlobais.NovoJogoThreadCreated){
						NovoJogo_Multicast dialog = new NovoJogo_Multicast();
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);	
					}
					
				} catch (Exception e1) {
					//TRATAR
					e1.printStackTrace();
				}
				break;
			case "Sair":
				System.exit(0);
				break;					
		
		}
		
	}

}
