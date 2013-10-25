import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
* Sensor
* Se encarga de comprobar la temperatura o la humedad de un invernadero.
*
* @author Pedro Paredes Andreu
* @version 25.10.2013
* @see Invernadero
* @see iSensor
* @see UnicastRemoteObject
* @see java.io.Serializable
**/
public class Sensor extends UnicastRemoteObject 
					implements iSensor, Serializable {
	
  	/**
	* Tipo del sensor [temperatura/humedad].
	**/
	private String tipo;
	
	/**
	* ID del invernadero al que pertenece.
	**/
	private int invernadero;

	/**
	* ID del sensor.
	**/
	private int id;

	/**
	* Servidor de objetos.
	**/
	private Registry registry;

	/**
	* Constructor.
	* @param id ID del sensor.
	* @param host Direccion del servidor de objetos.
	* @throws java.rmi.RemoteException si hay algun problema al conectar con el servidor de objetos.
	**/
	public Sensor(int id, String host) throws java.rmi.RemoteException {
		super();
		System.setSecurityManager(new RMISecurityManager());
		registry = LocateRegistry.getRegistry(host,1099);
		this.id = id;
		this.invernadero = -1;
		this.tipo = "";
	}

	/**
	* Establece el tipo del sensor.
	* @param tipo Tipo del sensor.
	* @throws Exception si el sensor ya tiene un tipo.
	**/
	public void setTipo(String tipo) throws Exception {
		if (this.tipo.equals("")) {
			this.tipo = tipo;
		} else {
			throw new Exception("El sensor " + id + " es de tipo " + this.tipo);
		}
	}

	/**
	* Establece el ID del invernadero del sensor.
	* @param invernadero ID del invernadero.
	* @throws Exception si el sensor ya tiene un invernadero.
	**/
	public void setInvernadero(int invernadero) throws Exception {
		if(this.invernadero == -1) {
			this.invernadero = invernadero;
		} else {
			throw new Exception("El sensor " + id + " ya pertenece a un invernadero");
		}
	}

	/**
	* Obtiene el tipo del sensor.
	* @return tipo del sensor.
	**/
	public String getTipo() {
		return tipo;
	}

	/**
	* Obtiene el ID del invernadero al que pertenece el sensor.
	* @return ID del invernadero.
	**/
	public int getInvernadero() {
		return invernadero;
	}

	/**
	* Devuelve el valor de temperatura o humedad del invernadero.
	* @return valor de lectura del sensor.
	* @throws java.rmi.RemoteException si hay algún problema con el servidor de nombres.
	* @throws si hay algún problema en la lectura de valores desde fichero en el invernadero.
	* @throws NotBoundException si el invernadero no existe en el servidor de objetos.
	* @throws Exception si hay algun problema al establecer los nuevos valores en el invernadero.
	**/
	public int getValor() throws java.rmi.RemoteException, IOException, NotBoundException, Exception {
		int valor = -1;
		iInvernadero inv = (iInvernadero) registry.lookup("/invernadero/" + this.invernadero);
		if (tipo.equals("temperatura")) {
			valor = inv.getTemperatura();
		} else {
			valor = inv.getHumedad();
		}
		System.out.println(valor);
		return valor;
	}
}