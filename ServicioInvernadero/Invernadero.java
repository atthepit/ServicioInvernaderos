import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;

public class Invernadero extends UnicastRemoteObject 
						  implements iInvernadero, Serializable {
  	
	/**
	* Nombre del fichero donde se almacenan la temperatura:humedad del invernadero.
	**/
	public static final String nombreFichero = "valores.txt";

	/**
	* ID del sensor de temperatura del invernadero.
	**/
	private int sensorTemperatura;

	/**
	* ID del sensor de humedad del invernadero.
	**/
	private int sensorHumedad;

	/**
	* ID del actuador del invernadero.
	**/
	private int actuador;

	/**
	* ID del invernadero.
	**/
	private int id;

	/**
	* Temperatura del invernadero.
	**/
	private int temperatura;

	/**
	* Humedad del invernadero.
	**/
	private int humedad;

	/**
	* Flujo de entrada de lectura del fichero.
	**/
	private BufferedReader entrada;

	/**
	* Flujo de salida de escritura del fichero.
	**/
	private BufferedWriter salida;

	/**
	* Constructor.
	* @param id ID del invernadero.
	* @throws java.rmi.RemoteException si hay algún problema de conexión.
	* @throws FileNotFoundException si no se encuentra el fichero de valores.
	* @throws IOException si hay algún problema con la lectura del fichero.
	**/
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

	/**
	* Establece el ID del sensor de temperatura.
	* @param sensor ID del sensor.
	* @throws Exception si el invernadero ya tiene un sensor de temperatura.
	**/
	public void setSensorTemperatura(int sensor) throws Exception {
		if(this.sensorTemperatura == -1) {
			this.sensorTemperatura = sensor;
		} else {
			String mensaje = "ERROR: El invernadero " + id + " ya contiene un sensor de Temperatura";
			System.err.println(mensaje);
			throw new Exception(mensaje);
		}
	}

	/**
	* Establece el ID del sensor de humedad.
	* @param sensor ID del sensor.
	* @throws Exception si el invernadero ya tiene un sensor de humedad.
	**/
	public void setSensorHumedad(int sensor) throws Exception {
		if(this.sensorHumedad == -1) {
			this.sensorHumedad = sensor;
		} else {
			String mensaje = "ERROR: El invernadero " + id + " ya contiene un sensor de Humedad";
			System.err.println(mensaje);
			throw new Exception(mensaje); 
		}
	}

	/**
	* Establece el ID del actuador.
	* @param actuador ID del actuador.
	* @throws Exception si el invernadero ya tiene un actuador.
	**/
	public void setActuador(int actuador) throws Exception {
		if(this.actuador == -1) {
			this.actuador = actuador;
		} else {
			String mensaje = "ERROR: El invernadero " + id + " ya contiene un Actuador";
			System.err.println(mensaje);
			throw new Exception(mensaje);
		}
	}

	/**
	* Establece la temperatura del invernadero.
	* @param temperatura Nueva temperatura del invernadero.
	* @throws Exception si la temperatura esta fuera de rango.
	**/
	public void setTemperatura(int temperatura) throws Exception {
		if(temperatura != -1) {
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
		} else {
			establecerNuevosValores();
		}
	}

	/**
	* Establece la humedad del invernadero.
	* @param humedad Nueva humedad del invernadero.
	* @throws Exception si la humedad esta fuera de rango.
	**/
	public void setHumedad(int humedad) throws Exception {
		if(humedad != -1) {
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
		} else {
			establecerNuevosValores();
		}
	}

	/**
	* Devuelve el ID del sensor de temperatura.
	* @return ID del sensor.
	**/
	public int getSensorTemperatura() {
		return sensorTemperatura;
	}

	/**
	* Devuelve el ID del sensor de humedad.
	* @return ID del sensor.
	**/
	public int getSensorHumedad() {
		return sensorHumedad;
	}

	/**
	* Devuelve el ID del actuador.
	* @return ID del actuador.
	**/
	public int getActuador() {
		return actuador;
	}

	/**
	* Obtiene la temperatura del invernadero. Establece también un nuevo valor
	* de temperatura después de la lectura.
	* @return Temperatura del invernadero.
	* @throws IOException si hay algún problema en la lectura del fichero.
	* @throws Exception si hay algún problema en los valores de la temperatura.
	**/
	public int getTemperatura() throws IOException, Exception {
		int temperatura = this.temperatura;
		return temperatura;
	}

	/**
	* Obtiene la humedad del invernadero. Establece también un nuevo valor
	* de humedad después de la lectura.
	* @return Humedad del invernadero.
	* @throws IOException si hay algún problema en la lectura del fichero.
	* @throws Exception si hay algún problema en los valores de la humedad.
	**/
	public int getHumedad() throws IOException, Exception {
		int humedad = this.humedad;
		return humedad;
	}

	/**
	* Lee los nuevos valores de temperatura:humedad.
	* @throws Exception si hay problemas en la lectura del fichero.
	**/
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

	/**
	* Escribe nuevas temperatura:humedad en el fichero de valores
	* @throws IOException si hay algun fallo en la lectura del fichero.
	**/
	private void escribeFichero() throws IOException {
		salida.append("\n" + this.temperatura + ":" + this.humedad);
		salida.flush();
	}
}
