import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;

public class Invernadero extends UnicastRemoteObject 
						  implements iInvernadero, Serializable {
  	
	public static final String nombreFichero = "valores.txt";
	private int sensorTemperatura;
	private int sensorHumedad;
	private int actuador;
	private int id;
	private int temperatura;
	private int humedad;
	private BufferedReader entrada;
	private BufferedWriter salida;

	public Invernadero(int id) throws java.rmi.RemoteException, FileNotFoundException, IOException {
		super();
		this.id = id;
		sensorTemperatura = -1;
		sensorHumedad = -1;
		actuador = -1;
		temperatura = 25;
		humedad = 40;
		entrada = new BufferedReader(new BufferedReader(new FileReader(nombreFichero)));
		salida = new BufferedWriter(new FileWriter(nombreFichero, true));
	}

	public void setSensorTemperatura(int sensor) throws Exception {
		if(this.sensorTemperatura == -1) {
			this.sensorTemperatura = sensor;
		} else {
			String mensaje = "ERROR: El invernadero " + id + " ya contiene un sensor de Temperatura";
			System.err.println(mensaje);
			throw new Exception(mensaje);
		}
	}

	public void setSensorHumedad(int sensor) throws Exception {
		if(this.sensorHumedad == -1) {
			this.sensorHumedad = sensor;
		} else {
			String mensaje = "ERROR: El invernadero " + id + " ya contiene un sensor de Humedad";
			System.err.println(mensaje);
			throw new Exception(mensaje); 
		}
	}

	public void setActuador(int actuador) throws Exception {
		if(this.actuador == -1) {
			this.actuador = actuador;
		} else {
			String mensaje = "ERROR: El invernadero " + id + " ya contiene un Actuador";
			System.err.println(mensaje);
			throw new Exception(mensaje);
		}
	}

	public void setTemperatura(int temperatura) throws Exception {
		if (temperatura >= MIN_TEMP_PERM && temperatura <= MAX_TEMP_PERM) {
			this.temperatura = temperatura;
			escribeFichero();
		} else if(temperatura >= MIN_TEMP && temperatura <= MAX_TEMP) {
			this.temperatura = temperatura;
		} else {
			String mensaje = "ERROR: Temperatura fuera de rango: " + temperatura;
			System.err.println(mensaje);
			throw new Exception(mensaje);
		}
	}

	public void setHumedad(int humedad) throws Exception {
		if (humedad >= MIN_HUM_PERM && humedad <= MAX_HUM_PERM) {
			this.humedad = humedad;
			escribeFichero();
		} else if(humedad >= MIN_HUM && humedad <= MAX_HUM) {
			this.humedad = humedad;
		} else {
			String mensaje = "ERROR: Humedad fuera de rango: " + humedad;
			System.err.println(mensaje);
			throw new Exception(mensaje);
		}
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

	public int getTemperatura() throws IOException, Exception {
		int temperatura = this.temperatura;
		establecerNuevosValores();
		return temperatura;
	}

	public int getHumedad() throws IOException, Exception {
		int humedad = this.humedad;
		establecerNuevosValores();
		return humedad;
	}

	private void establecerNuevosValores() throws Exception {
		String cadena =  entrada.readLine();
		if (cadena == null) {
			String mensaje = "ERROR: Nada leído";
			System.err.println(mensaje);
			throw new Exception(mensaje);
		}
		String[] valores = cadena.split(":");
		this.temperatura = Integer.parseInt(valores[0]);
		this.humedad = Integer.parseInt(valores[1]);
	}

	private void escribeFichero() throws IOException {
		salida.append("\n" + this.temperatura + ":" + this.humedad);
		salida.flush();
	}
}