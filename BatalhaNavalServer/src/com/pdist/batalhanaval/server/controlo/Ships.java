package com.pdist.batalhanaval.server.controlo;

import java.io.Serializable;
import java.util.ArrayList;

import com.pdist.batalhanaval.server.macros.Macros;

public class Ships implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<UnidadeTabuleiro> barco;
	private int xStart, xEnd;
	private int yStart, yEnd;
	private boolean destruido;
	private int tamanho;
	private int hits;
	
	
	//construtores
	public Ships(){
		barco = new ArrayList<UnidadeTabuleiro>();
		xStart = xEnd = yStart = yEnd = -1;
		tamanho = hits = 0;
		destruido = false;
	}
	public Ships(ArrayList<UnidadeTabuleiro> barco){
		this.barco = barco;
		
		xStart = barco.get(0).getX();
		yStart = barco.get(0).getY();
		
		xEnd = barco.get(barco.size()-1).getX()+Macros.SIZE_X;
		yEnd = barco.get(barco.size()-1).getY()+Macros.SIZE_Y;
		
		tamanho = barco.size();
		hits = 0;
		for(int i = 0;i<tamanho;i++)
			if(barco.get(i).isShooted())
				hits++;
		
		if(hits == tamanho)
			destruido = true;
		else
			destruido = false;
	}
	
	//Getters
	public ArrayList<UnidadeTabuleiro> getBarco() {return barco;}
	public int getxStart() {return xStart;}
	public int getxEnd() {return xEnd;}
	public int getyStart() {return yStart;}
	public int getyEnd() {return yEnd;}
	public int getTamanho(){return tamanho;}
	public int getHits(){return hits;}
	public boolean isDestruido(){return destruido;}
	
	//Setters
	public void setBarco(ArrayList<UnidadeTabuleiro> barco) {
		this.barco = barco;
		
		xStart = barco.get(0).getX();
		yStart = barco.get(0).getY();
		
		xEnd = barco.get(barco.size()-1).getX()+Macros.SIZE_X;
		yEnd = barco.get(barco.size()-1).getY()+Macros.SIZE_Y;
		
		tamanho = barco.size();
		hits = 0;
		for(int i = 0;i<tamanho;i++)
			if(barco.get(i).isShooted())
				hits++;
		
		if(hits == tamanho)
			destruido = true;
		else
			destruido = false;
	}
	public void setxStart(int xStart) {this.xStart = xStart;}
	public void setxEnd(int xEnd) {this.xEnd = xEnd;}
	public void setyStart(int yStart) {this.yStart = yStart;}
	public void setyEnd(int yEnd) {this.yEnd = yEnd;}
	public void setTamanho(int tamanho){this.tamanho = tamanho;}
	public void setHits(int hits){this.hits = hits;}
	public void setDestruido(boolean destruido){this.destruido = destruido;}
	
	//Metodo actualizar x's e y's (caso se adicionem ou retirem unidade de tabuleiro ao barco)
	public boolean updateSize(){
		if(barco.size()<=0)
			return false;
		
		xStart = barco.get(0).getX();
		yStart = barco.get(0).getY();
		
		xEnd = barco.get(barco.size()-1).getX()+Macros.SIZE_X;
		yEnd = barco.get(barco.size()-1).getY()+Macros.SIZE_Y;
		
		return true;
	}
	//Metodo para saber se o toque foi dento do barco
	public boolean isOn(int pX, int pY){
		if(xStart<0 || yStart<0 || xEnd <= xStart || yEnd <= yStart) 
			return false;
	
		if(xStart <= pX && xEnd >= pX)
			if(yStart <= pY && yEnd >= pY)
				return true;
		return false;
	}
	
	
	public void addUnidade(UnidadeTabuleiro uni){barco.add(uni);
		updateSize();
	}
	
	

}
