import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;

public class Invernadero extends UnicastRemoteObject 
						  implements iInvernadero, Serializable {
  	
  	public static final int MIN_TEMP = -10;
  	public static final int MAX_TEMP = 45;
  	public static final int MIN_TEMP_PERM = 25;
  	public static final int MAX_TEMP_PERM = 35;
  	public static final int MIN_HUM = 0;
  	public static final int MAX_HUM = 100;
  	public static final int MIN_HUM_PERM = 40;
  	public static final int MAX_HUM_PERM = 75;
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
		if (temperatura >= MIN_TEMP_PERM && temperatura <= MAX_TEMP_PERM) {
			this.temperatura = temperatura;
			escribeFichero();
		} else if(temperatura >= MIN_TEMP && temperatura <= MAX_TEMP) {
			this.temperatura = temperatura;
		} else {
			throw new Exception("Temperatura fuera de rango: " + temperatura);
		}
	}

	public void setHumedad(int humedad) throws Exception {
		if (humedad >= MIN_HUM_PERM && humedad <= MAX_HUM_PERM) {
			this.humedad = humedad;
			escribeFichero();
		} else if(humedad >= MIN_HUM && humedad <= MAX_HUM) {
			this.humedad = humedad;
		} else {
			throw new Exception("Humedad fuera de rango: " + humedad);
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

	public int getTemperatura() throws IOException {
		int temperatura = this.temperatura;
		establecerNuevosValores();
		return temperatura;
	}

	public int getHumedad() throws IOException {
		int humedad = this.humedad;
		establecerNuevosValores();
		return humedad;
	}

	private void establecerNuevosValores() throws IOException {
		String cadena =  entrada.readLine();
		if (cadena == null) throw new IOException("Cadena vacia");
		String[] valores = cadena.split(":");
		this.temperatura = Integer.parseInt(valores[0]);
		this.humedad = Integer.parseInt(valores[1]);
	}

	private void escribeFichero() throws IOException {
		salida.append("\n" + this.temperatura + ":" + this.humedad);
		salida.flush();
	}
}