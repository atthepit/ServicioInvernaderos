import java.io.Serializable; 
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


/**
* Actuador
* Se encarga de realizar acciones en el invernadero dependiendo
* de los mensajes recibidos de los sensores. Se encarga de:
*   · Activar el sistema de goteo si la humedad es demasiado baja.
*   · Activar el deshumidificador si la humedad es demasiado alta.
*   · Activar el sistema de ventilación si la temperatura es demasiado alta.
*   · Activar el sistema de calefacción si la temperatura es demasiado baja.
*   
* @author Pedro Paredes Andreu
* @version 25.10.2013
* @see iActuador  
* @see Invernadero
* @see java.io.Serializable
* @see UnicastRemoteObject
**/
public class Actuador extends UnicastRemoteObject 
						  implements iActuador, Serializable {

    /**
    * ID del actuador.
    **/
  	private int id;

    /**
    * ID del invernadero al que pertenece.
    **/
  	private int invernadero;
    
    /**
    * Servidor de objetos.
    **/
    private Registry registry;

    /**
    * Constructor.
    * @param id ID del actuador.
    * @param host Dirección del servidor de Objetos.
    * @throws java.rmi.RemoteException si hay algún problema conectando con el servidor de Objetos.
    **/
  	public Actuador(int id, String host) throws java.rmi.RemoteException {
      super();
      System.setSecurityManager(new RMISecurityManager());
      registry = LocateRegistry.getRegistry(host,1099);
  		this.id = id;
  		this.invernadero = -1;
  	}

    /**
    * Establece el invernadero al que pertenece el actuador.
    * @param invernadero ID del invernadero al que pertenece.
    * @throws Exception si el actuador ya pertenece a un invernadero.
    **/
  	public void setInvernadero(int invernadero) throws Exception {
  		if (this.invernadero == -1) {
  			this.invernadero = invernadero;
  		} else {
  			throw new Exception("El actuador " + id + " ya pertenece al invernadero " + invernadero);
  		}
  	}

    /**
    * Obtiene el invernadero al que pertenece el actuador.
    * @return ID del invernadero al que pertenece.
    **/
  	public int getInvernadero() {
  		return this.invernadero;
  	}

    /**
    * Realiza una acción sobre el invernadero.
    * @param accion Acción a realizar por el actuador
    * @return True si se ha realizado con exito.
    * @throws java.rmi.RemoteException Si hay algún problema de conexión al realizar la accion.
    * @throws NotBoundException si no se encuentra el invernadero en el servidor de objetos.
    * @throws Exception si hay algún problema al realizar la acción.
    **/
  	public boolean realizarAccion(String accion) throws java.rmi.RemoteException, NotBoundException, Exception {
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