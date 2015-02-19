package com.pdist.batalhanaval.clientrmi;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;




public class Janela {

	
	public static JFrame frame = new JFrame("");
	public static JPanel contentPanel = new JPanel();
	

	private JList<String> listaJogos = new JList<String>();
	private JList<String> listaJogadores = new JList<String>();
	
	public static JTextArea textArea = new JTextArea();
	public static JScrollPane scroll;
	
	//construtor
	public Janela(){}
	
	
	//GETs e SETs
	public JFrame getFrame() {return frame;}
	public void setFrame(JFrame frame) {Janela.frame = frame;}

	public JPanel getContentPanel() {return contentPanel;}
	public void setContentPanel(JPanel contentPanel) {Janela.contentPanel = contentPanel;}


	public JList<String> getListaJogos() {return listaJogos;}
	public void setListaJogos(JList<String> listaJogos) {this.listaJogos = listaJogos;}

	public JList<String> getListaJogadores() {return listaJogadores;}
	public void setListaJogadores(JList<String> listaJogadores) {this.listaJogadores = listaJogadores;}

	public static JTextArea getTextArea() {return textArea;}
	public void setTextArea(JTextArea textArea) {Janela.textArea = textArea;}


	public void criarJanela(){
		
		frame.setResizable(false);  
		frame.setTitle("Cliente RMI");
		frame.setBounds(100, 100, 550, 450);
		frame.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		frame.setVisible(true);
		
		JLabel lblListaDeJogos = new JLabel("Jogos Activos:");
		lblListaDeJogos.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblListaDeJogos.setBounds(77, 13, 158, 26);
		contentPanel.add(lblListaDeJogos);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(276, 0, 2, 215);
		contentPanel.add(separator);
		
		
		JLabel lblConvidarJogadores = new JLabel("Jogadores Online:");
		lblConvidarJogadores.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblConvidarJogadores.setBounds(338, 13, 158, 26);
		contentPanel.add(lblConvidarJogadores);
		
		
		listaJogos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaJogos.setSelectedIndex(0); //selecionar o 1º
		listaJogos.setLayoutOrientation(JList.VERTICAL); 		
		listaJogos.setFont(new Font("Tahoma", Font.PLAIN, 14));
		listaJogos.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));	
		JScrollPane listaScroll = new JScrollPane(listaJogos);   //para aparecer o scroll na lista
		listaScroll.setBounds(49, 50, 181, 135);		
		contentPanel.add(listaScroll);
		
		
		listaJogadores.setEnabled(false);
		//listaJogadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//listaJogadores.setSelectedIndex(0); //selecionar o 1º
		listaJogadores.setLayoutOrientation(JList.VERTICAL); 		
		listaJogadores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		listaJogadores.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));	
		JScrollPane listaScroll2 = new JScrollPane(listaJogadores);   //para aparecer o scroll na lista
		listaScroll2.setBounds(327, 51, 181, 135);		
		contentPanel.add(listaScroll2);
		

	
		textArea.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textArea.setEditable(false);
		textArea.setFont(new Font("Verdana", Font.PLAIN, 14));

		scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(70,215, 400, 150);
	
		contentPanel.add(scroll);
		
		
		contentPanel.setVisible(true);
		contentPanel.repaint();
	}
	
	
}
