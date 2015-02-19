package com.pdist.batalhanaval.server.controlo;

import java.io.Serializable;

import com.pdist.batalhanaval.server.macros.*;

public class UnidadeTabuleiro implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x,y;
	private int image;
	private boolean isBoat;
	private boolean shooted;

	//Constructores;
	public UnidadeTabuleiro(){
		x = y = -1;
		image = -1;
		isBoat = shooted = false;
		
	}
	public UnidadeTabuleiro(int x, int y){

		this.x = x;
		this.y = y;
		//image = -1;
		image = Macros.IMAGEM_FAIL; //por default fica esta imagem
		isBoat = shooted = false;
	}
	
	//Getters
	public int getX(){return x;}
	public int getY() {return y;}
	public int getImage() {return image;}
	public boolean isBoat() {return isBoat;}
	public boolean isShooted() {return shooted;}
	
	//Setters
	public void setX(int x){this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setImage(int image) {this.image = image;}
	public void setOcupied(boolean isBoat) {this.isBoat = isBoat;}
	public void setShooted(boolean shooted) {
		//if(shooted)
			//image = Macros.IMAGEM_FAIL; isto não é necessario e causa conflitos
		this.shooted = shooted;}

	//Verificar se um ponto está na quadricula
	public boolean isOn(int pX, int pY){
		
		if(x<0 || y<0) //falta pôr os >
			return false;
	
		if(x <= pX && (x+Macros.SIZE_X) >= pX)
			if(y <= pY && (y+Macros.SIZE_Y) >= pY)
				return true;
		return false;
	}

}
