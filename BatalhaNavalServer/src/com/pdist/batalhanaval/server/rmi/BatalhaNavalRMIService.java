package com.pdist.batalhanaval.server.rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.pdist.batalhanaval.server.main.VarsGlobais;



public class BatalhaNavalRMIService extends UnicastRemoteObject implements BatalhaNavalRMIInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1629769723876297832L;
	//OBSERVERS
	protected static List<ClientObserverInterface> observers = new ArrayList<ClientObserverInterface>(); 
	
	
	//construtor
	public BatalhaNavalRMIService() throws RemoteException{}

		
	
	@Override
	public int getNumeroExplosoes(int jogo, int cliente) throws RemoteException {
		
		//System.out.println("Observer: Pedido getNumeroExplosoes");
		
		//receber o numero do jogo e o numero do cliente (1/2)
		//se cliente > 2 ou < 1.. erro
		//se numero do jogo > numeroJogos ou < 0.. erro
		//percorrer todas as unidades tabuleiro e somar 1 sempre que se detectar um isShooted
		//retornar esse valor
		
		if( (jogo > VarsGlobais.nJogos) || (jogo < 0) )
			return -2;
		if( (cliente > 2) || (cliente < 1) )
			return -3;
		if(VarsGlobais.nJogos == 0)
			return -4;
		
		
		int aux = 0; //numero de quadriculas explosao
		
		//for(int i=0; i<VarsGlobais.jogos.size(); i++){
		if(cliente == 1){
			for(int i=1; i<=10; i++){
				for(int j=1; j<=10; j++){
					if(VarsGlobais.jogos.get(jogo).getC1().getTabuleiro().getUnidade(i, j).isBoat())
						if(VarsGlobais.jogos.get(jogo).getC1().getTabuleiro().getUnidade(i, j).isShooted())
							aux++;	
				}
			}
			return aux;
		}
		if(cliente == 2){
			for(int i=1; i<=10; i++){
				for(int j=1; j<=10; j++){
					if(VarsGlobais.jogos.get(jogo).getC2().getTabuleiro().getUnidade(i, j).isBoat())
						if(VarsGlobais.jogos.get(jogo).getC2().getTabuleiro().getUnidade(i, j).isShooted())
							aux++;	
				}
			}
			return aux;
		}
				
		return -1; //erro
			
	}

	
	@Override
	public ArrayList<String> getJogadores() throws RemoteException {
		
		//System.out.println("Observer: Pedido getJogadores");
		
		ArrayList<String> nomes = new ArrayList<String>();
		
		if(VarsGlobais.ClientesOn.size() > 0){
			for(int i=0; i<VarsGlobais.ClientesOn.size(); i++)
				nomes.add( VarsGlobais.ClientesOn.get(i).getNome() );
			return nomes;
		}
				
		return nomes; //mesmo que nao va nada.. é melhor que que fazer return null
	}

	
	@Override
	public ArrayList<String> getListaJogos() throws RemoteException {
		
		//System.out.println("Observer: Pedido getListaJogos");
		
		ArrayList<String> jogos = new ArrayList<String>();
		String temp;
		
		if(VarsGlobais.jogos.size() > 0){
			for(int i=0; i<VarsGlobais.jogos.size(); i++){
				temp = VarsGlobais.jogos.get(i).getC1().getNome();
				temp = (i+1)+") "+ temp + "  vs  " + VarsGlobais.jogos.get(i).getC2().getNome();
				jogos.add(temp);
			}
			return jogos;
		}
		
		return jogos;
	}
	
	
	@Override
	public void addObserver(ClientObserverInterface obs) throws Exception {
		
		//System.out.println("Observer: Pedido do cliente para fazer Add observer");
		
		observers.add(obs);
	}
	
	//NOTIFICAR OS OBSERVERS
	public static void notifyObservers(){
		
		for(int i=0; i<observers.size(); i++){
			try{
                observers.get(i).actualizarInfo();
            }catch(RemoteException e){
                System.out.println("Faulty observer: "+e);
                observers.remove(i--);
                System.out.println("The faulty observer won't be considered anymore!");
            }
		}
		
	}
	
	
	
	/**
     * Instancia e regista o serviço remoto.
     **/
	//public static void main(String[] args) {
	public static void iniciaServicoRMI(){
        try{
            /**
             * Lanca um registry na maquina local, 
             * no porto por omissao Registry.REGISTRY_PORT.
             **/          
            try{                
                LocateRegistry.createRegistry(Registry.REGISTRY_PORT);                                                
                System.out.println("Registry lancado!");
            }catch(RemoteException e){
                //Ja' foi provavelmente lancado...
            	System.out.println("Remote Exception " + e);
            }
            
            
            /**
             * Cria o servico BatalhaNavalRMIService.
             **/
            BatalhaNavalRMIService batalhaNavalRMIService = new BatalhaNavalRMIService();
            System.out.println("Servico BatalhaNavalRMIService criado e em execucao...");
            
            /**
             * Regista o servico para que os clientes possam encontra'-lo, ou seja,
             * obter a sua referencia remota.
             */ 
            Naming.bind("rmi://localhost/BatalhaNavalRMI", batalhaNavalRMIService);                                
            System.out.println("Servico batalhaNavalRMI registado no registry local...");
            

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(1);
        }  
    }

	
}
