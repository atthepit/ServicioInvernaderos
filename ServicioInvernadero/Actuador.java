import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;

public class Actuador extends UnicastRemoteObject 
						  implements InterfazRemoto, Serializable {
  	private int id;
  	private int invernadero;

  	public Actuador(int id) {
  		this.id = id;
  		this.invernadero = "";
  	}

  	public void setInvernadero(int invernadero) {
  		if (this.invernadero != "") {
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