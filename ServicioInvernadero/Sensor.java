import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Sensor extends UnicastRemoteObject 
						  implements iSensor, Serializable {
	private String tipo;
	private int invernadero;
	private int id;
	private Registry registry;

	public Sensor(int id, String host) throws java.rmi.RemoteException {
		super();
		System.setSecurityManager(new RMISecurityManager());
		registry = LocateRegistry.getRegistry(host,1099);
		this.id = id;
		this.invernadero = -1;
		this.tipo = "";
	}

	public void setTipo(String tipo) throws Exception {
		if (this.tipo.equals("")) {
			this.tipo = tipo;
		} else {
			throw new Exception("El sensor " + id + " es de tipo " + this.tipo);
		}
	}

	public void setInvernadero(int invernadero) throws Exception {
		if(this.invernadero == -1) {
			this.invernadero = invernadero;
		} else {
			throw new Exception("El sensor " + id + " ya pertenece a un invernadero");
		}
	}

	public String getTipo() {
		return tipo;
	}

	public int getInvernadero() {
		return invernadero;
	}

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