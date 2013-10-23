import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;

public class Invernadero extends UnicastRemoteObject 
						  implements iInvernadero, Serializable {
	private int sensorTemperatura;
	private int sensorHumedad;
	private int actuador;
	private int id;

	public Invernadero(int id) throws java.rmi.RemoteException {
		super();
		this.id = id;
		sensorTemperatura = -1;
		sensorHumedad = -1;
		actuador = -1;
	}

	public void setSensorTemperatura(int sensor) throws Exception {
		if(this.sensorTemperatura == -1) {
			this.sensorTemperatura = sensor;
		} else {
			throw new Exception("El invernadero " + id + " ya contiene un sensor de Temperatura");
		}
	}

	public void setSensorHumedad(int sensor) throws Exception {
		if(this.sensorHumedad == -1) {
			this.sensorHumedad = sensor;
		} else {
			throw new Exception("El invernadero " + id + " ya contiene un sensor de Humedad"); 
		}
	}

	public void setActuador(int actuador) throws Exception {
		if(this.actuador == -1) {
			this.actuador = actuador;
		} else {
			throw new Exception("El invernadero " + id + " ya contiene un Actuador");
		}
	}

	public void setTemperatura(int temperatura) throws Exception {

	}

	public void setHumedad(int humedad) throws Exception {

	}

	public int getSensorTemperatura() {
		return sensorTemperatura;
	}

	public int getSensorHumedad() {
		return sensorHumedad;
	}

	public int getActuador() {
		return actuador;
	}

	public int getTemperatura() {
		return 24;
	}

	public int getHumedad() {
		return 60;
	}
}