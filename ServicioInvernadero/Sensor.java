import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;

public class Sensor extends UnicastRemoteObject 
						  implements InterfazRemoto, Serializable {
	private String tipo;
	private int invernadero;
	private int id;

	public Sensor(int id) {
		this.id = id;
		this.invernadero = "";
		this.tipo = "";
	}

	public void setTipo(String tipo) {
		if (!this.tipo.equals("")) {
			this.tipo = tipo;
		} else {
			throw new Exception("El sensor " + id + " es de tipo " + this.tipo);
		}
	}

	public void setInvernadero(int invernadero) {
		if(this.invernadero != -1) {
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

	public int getValor() {
		int valor = -1;
		if (tipo.equals("temperatura")) {
			valor = 25;
		} else {
			valor = 47;
		}
		return valor;
	}
}