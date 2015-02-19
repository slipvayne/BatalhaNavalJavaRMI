package com.pdist.batalhanaval.server.controlo;

import java.io.Serializable;

//nota: tem de ser serializable para poder enviar as coordenadas na mensagem

//Letras no Tabuleiro
public class Letra implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1092146332279996721L;
	private char letra;
	private int posY;
	
	
	//construtores
	public Letra(){
		posY = -1;
		letra = ' ';
	}	
	public Letra(char letra){
		posY = -1;
		this.letra = Character.toUpperCase(letra);
	}
	public Letra(char letra, int posY){
		this.posY = posY;
		this.letra = Character.toUpperCase(letra);
	}
	
	//Getters
	public char getLetra() {return letra;}
	public int getPosY() {return posY;}
	
	//Setters
	public void setLetra(char letra) {this.letra = Character.toUpperCase(letra);}
	public void setPosY(int posY) {this.posY = posY;}
	
	
	
	

}
