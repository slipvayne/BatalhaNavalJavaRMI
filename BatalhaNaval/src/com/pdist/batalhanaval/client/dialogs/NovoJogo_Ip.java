package com.pdist.batalhanaval.client.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.pdist.batalhanaval.client.main.LoginServidor_IP;
import com.pdist.batalhanaval.client.main.VarsGlobais;

import java.io.IOException;

public class NovoJogo_Ip extends JDialog {
	
	private static final long serialVersionUID = 1578538003101261327L;
	private final JPanel contentPanel = new JPanel();
	private JTextField inputIP;
	private JTextField inputNome;
	private JTextField inputPorto;

	public NovoJogo_Ip() {
				
		setResizable(false);  setModal(true);
		setTitle("Batalha Naval - Novo Jogo - IP/Port");
		setBounds(100, 100, 378, 241);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		//Adiciona texto a pedir IP e TextField respectivo
		
		inputIP = new JTextField();
		inputIP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		inputIP.setText("127.0.0.1");
		inputIP.setBounds(125, 42, 105, 23);
		contentPanel.add(inputIP);
		inputIP.setColumns(10);
		
		JLabel lblIp = new JLabel("Servidor IP:");
		lblIp.setFont(new Font("Arial", Font.BOLD, 16));
		lblIp.setBounds(125, 27, 94, 14);
		contentPanel.add(lblIp);
		
		
		//Adiciona texto a pedir o NOME e TextField respectivo
		
		inputNome = new JTextField();
		inputNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
		inputNome.setBounds(125, 89, 172, 23);
		contentPanel.add(inputNome);
		inputNome.setColumns(10);
		
		JLabel lblUsername = new JLabel("Nome:");
		lblUsername.setFont(new Font("Arial", Font.BOLD, 16));
		lblUsername.setBounds(70, 93, 70, 14);
		contentPanel.add(lblUsername);
		
		JLabel lblPorta = new JLabel("Port:");
		lblPorta.setFont(new Font("Arial", Font.BOLD, 16));
		lblPorta.setBounds(240, 27, 70, 14);
		contentPanel.add(lblPorta);
		
		inputPorto = new JTextField();
		inputPorto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		inputPorto.setText("5001");
		inputPorto.setColumns(10);
		inputPorto.setBounds(240, 42, 57, 23);
		contentPanel.add(inputPorto);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
//---Eventos-OK--
				okButton.addActionListener(new ActionListener() {
					   public void actionPerformed(ActionEvent evt) {	
						   
						//   JOptionPane.showMessageDialog(contentPanel,"IP: "+inputIP.getText()+"Port: "+inputPorto.getText()+"\nNome: "+inputNome.getText());
						   
						   try {
							   
							   Thread t = new Thread(new LoginServidor_IP(inputIP.getText(),inputNome.getText(),inputPorto.getText() ) );
							   t.setDaemon(true);
							   t.start();
							   VarsGlobais.NovoJogoThreadCreated = true;
							   dispose();
							
						   } catch (IOException e) {
							// TRATAR
							e.printStackTrace();
						}
						  
					   }
				});
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
//---Eventos-CANCEL--
				cancelButton.addActionListener(new ActionListener() {
					   public void actionPerformed(ActionEvent evt) {						   
						   dispose();
					   }
				});
			}
		}	
		   
		   
	}
}
