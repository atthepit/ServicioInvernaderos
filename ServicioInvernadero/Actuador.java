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
  		if (accion.equals("activarGoteo")) {
        valor = iInvernadero.MIN_HUM_PERM;
        inv.setHumedad(valor);
  			realizada = true;
  		} else if(accion.equals("activarDeshumidificador")) {
        valor = iInvernadero.MAX_HUM_PERM;
        inv.setHumedad(valor);
  			realizada = true;
  		} else if(accion.equals("activarVentilacion")) {
        valor = iInvernadero.MIN_TEMP_PERM;
        inv.setTemperatura(valor);
  			realizada = true;
  		} else if(accion.equals("activarAperturaVentanas")) {
        valor = iInvernadero.MAX_TEMP_PERM;
        inv.setTemperatura(valor);
  			realizada = true;
  		}
  		return realizada;
  	}
}