import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;

public class Sensor extends UnicastRemoteObject 
						  implements InterfazRemoto, Serializable {
	private String tipo;
	private int invernadero;
	private int id;

	public void setTipo(String tipo) {
		
	}
}