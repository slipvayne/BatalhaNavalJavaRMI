package com.pdist.batalhanaval.client.main;

import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.pdist.batalhanaval.client.listeners.MenuActionListener;

import java.awt.Toolkit;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;


public class BatalhaNaval_Client {

	private static JFrame BatalhaNavalUI; 
	private static JLabel lblEstado = new JLabel("a aguardar login...");	
	private static MenuActionListener menuListener = new MenuActionListener();
	private String background = "Imagens/outros/background.jpg";
	


	public static void main(String[] args)
	{		
		new BatalhaNaval_Client();
		BatalhaNaval_Client.BatalhaNavalUI.setVisible(true);				
	}


	public BatalhaNaval_Client() {		
		initialize();
	}

	
	private void initialize() {
		
		BatalhaNavalUI = new JFrame();
		BatalhaNavalUI.setIconImage(Toolkit.getDefaultToolkit().getImage("Imagens/outros/appIcon.png"));
		BatalhaNavalUI.setResizable(false);
		BatalhaNavalUI.setTitle("Batalha Naval");
		BatalhaNavalUI.getContentPane().setBackground(SystemColor.controlHighlight);
		BatalhaNavalUI.setBounds(100, 100, 775, 500);
		BatalhaNavalUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BatalhaNavalUI.getContentPane().setLayout(null);
		
		
		loadBackground(background);	
		loadMenuBar();	
	
	}

	 
	 
	public static JFrame getBatalhaNavalUI() {
		return BatalhaNavalUI;
	}


	public static void setBatalhaNavalUI(JFrame batalhaNavalUI) {
		BatalhaNavalUI = batalhaNavalUI;
	}

	public static void setEstado(String estado)
	{
		 lblEstado.setText(estado);			 
	}
	
	
	public static void loadMenuBar()
	{
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Arial", Font.PLAIN, 12));
		menuBar.setBounds(0, 0, 800, 21);
		BatalhaNavalUI.getContentPane().add(menuBar);
		
		
		JMenu mnJogo = new JMenu("Jogo");
		menuBar.add(mnJogo);
		
		
		JMenuItem mntmNovoJogo = new JMenuItem("Novo Jogo (IP/Port)");
		mnJogo.add(mntmNovoJogo);

		JMenuItem mntmNovoJogo2 = new JMenuItem("Novo Jogo (Multicast)");
		mnJogo.add(mntmNovoJogo2);		
		
		JMenuItem mntmSair = new JMenuItem("Sair");
		mnJogo.add(mntmSair);				
		
		
		//ACTION LISTENER
		mntmSair.addActionListener(menuListener);
		mntmNovoJogo.addActionListener(menuListener);
		mntmNovoJogo2.addActionListener(menuListener);		
		
		
		JLabel label3 = new JLabel("Estado:");
		label3.setFont(new Font("Tahoma", Font.BOLD, 14));
		label3.setBounds(300, 428, 66, 21);
		BatalhaNavalUI.getContentPane().add(label3);		
		
		lblEstado.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEstado.setBounds(360, 432, 275, 14);
		BatalhaNavalUI.getContentPane().add(lblEstado);	
		
	
	}
	
	
	public static void loadBackground(String background)
	{
		try {
			BatalhaNavalUI.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File(background)))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static JLabel getLblEstado() {
		return lblEstado;
	}


	public static void setLblEstado(JLabel lblEstado) {
		BatalhaNaval_Client.lblEstado = lblEstado;
	}
	
	
}
