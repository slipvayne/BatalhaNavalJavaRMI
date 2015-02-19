package com.pdist.batalhanaval.server.controlo;


public class Jogo {
	private Cliente c1,c2;
	private boolean fim;
	private int turn; //1->Jogador1, 2->Jogador2
	private boolean started;
	
	
	
	//Construtores
	public Jogo(){
		c1 = c2 = null;
		fim = false;
		started = false;
		turn = 1;
	}	
	public Jogo(Cliente c1, Cliente c2){
		this.c1 = c1;
		this.c2 = c2;
		fim = false;
		started = false;
		turn = 1;
	}
	
	//Getters
	public Cliente getC1() {return c1;}
	public Cliente getC2() {return c2;}
	public boolean isFim() {return fim;}
	public int getTurn() {return turn;}
	public boolean isStarted() {return started;}
	
	//Setters
	public void setC1(Cliente c1) {this.c1 = c1;}
	public void setC2(Cliente c2) {this.c2 = c2;}
	public void setFim(boolean fim) {this.fim = fim;}
	public void setTurn(int turn) {this.turn = turn;}
	public void setStarted(boolean started) {this.started = started;}
	
}
