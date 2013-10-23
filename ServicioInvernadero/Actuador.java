import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;

public class Actuador extends UnicastRemoteObject 
						  implements iActuador, Serializable {
  	private int id;
  	private int invernadero;

  	public Actuador(int id) throws java.rmi.RemoteException {
      super();
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

  	public boolean realizarAccion(String accion) {
  		boolean realizada = false;
  		if (accion.equals("activarAspersores")) {
  			realizada = true;
  		} else if(accion.equals("activarDeshumidificador")) {
  			realizada = true;
  		} else if(accion.equals("activarCalefaccion")) {
  			realizada = true;
  		} else if(accion.equals("activarAireAcondicionado")) {
  			realizada = true;
  		} else {
  			realizada = true;
  		}
  		return realizada;
  	}
}