import java.io.Serializable; 
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Actuador extends UnicastRemoteObject 
						  implements iActuador, Serializable {
  	private int id;
  	private int invernadero;
    private Registry registry;

  	public Actuador(int id, String host) throws java.rmi.RemoteException {
      super();
      System.setSecurityManager(new RMISecurityManager());
      registry = LocateRegistry.getRegistry(host,1099);
  		this.id = id;
  		this.invernadero = -1;
  	}

  	public void setInvernadero(int invernadero) throws Exception {
  		if (this.invernadero == -1) {
  			this.invernadero = invernadero;
  		} else {
  			throw new Exception("El actuador " + id + " ya pertenece al invernadero " + invernadero);
  		}
  	}

  	public int getInvernadero() {
  		return this.invernadero;
  	}

  	public boolean realizarAccion(String accion) throws java.rmi.RemoteException, IOException, NotBoundException, Exception {
  		boolean realizada = false;
      int valor = -1;
      iInvernadero inv = (iInvernadero) registry.lookup("/invernadero/" + this.invernadero);
  		if (accion.equals("activarAspersores")) {
        valor = 40;
        inv.setHumedad(valor);
  			realizada = true;
  		} else if(accion.equals("activarDeshumidificador")) {
        valor = 75;
        inv.setHumedad(valor);
  			realizada = true;
  		} else if(accion.equals("activarCalefaccion")) {
        valor = 35;
        inv.setTemperatura(valor);
  			realizada = true;
  		} else if(accion.equals("activarAireAcondicionado")) {
        valor = 25;
        inv.setTemperatura(valor);
  			realizada = true;
  		}
  		return realizada;
  	}
}