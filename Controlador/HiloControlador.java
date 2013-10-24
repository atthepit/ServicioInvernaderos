import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

class HiloControlador extends Thread {
	Socket skServidor;
	iSensor sensor;
	iActuador actuador;
	Registry registry;
	private BufferedReader entrada;
    private PrintWriter salida;

	public HiloControlador(Socket socket, String host, int port) throws java.rmi.RemoteException {
		System.setSecurityManager(new RMISecurityManager());
		registry = LocateRegistry.getRegistry(host,port);
		skServidor = socket;
		sensor = null;
		actuador = null;
	}

	@Override
	public void run(){
		String cadena = "";
		String[] peticion = null;
		String objetoRemoto = "";
		String servidorRMI = "";
		String accion = "";
		String cabeceraHTML = "<html><head><title>Servicio Invernaderos</title><meta charset=\"UTF-8\" /></head><body>";
		String respuesta = "";
		String pieHTML = "</body></html>";
		String respuestaHTML = "";

		try {
			System.out.println("Estableciendo conexión con el servidor miniHTTP");
			establecerFlujos(skServidor);
			System.out.println("Leyendo peticion...");
			cadena = leeSocket();
			System.out.println(cadena);
			peticion = obtenerPeticion(cadena);
			objetoRemoto = obtenerObjetoRemoto(peticion);
			if(existeEnServidor(objetoRemoto)) {
				if (peticion[3].equals("sensor")) {
					sensor = (iSensor) registry.lookup(objetoRemoto);
					System.out.println("Obteniendo valor del sensor " + peticion[4] + ": ");
					respuesta = obtenerRespuesta(sensor, peticion[2]);
					System.out.println(respuesta);
				} else if(peticion[3].equals("actuador")) {
					actuador = (iActuador) registry.lookup(objetoRemoto);
					accion = peticion[4];
					System.out.println("Realizando accion...");
					boolean realizado = actuador.realizarAccion(accion);
					if (realizado) {
						respuesta = obtenerRespuesta(actuador,peticion[2],accion);
						System.out.println(respuesta);
					} else {
						System.err.println("Error: El actuador del invernadero " + peticion[2] 
											+ " no ha podido realizar la accion " + accion);
						throw new Exception("500");
					}
				} else {
					System.err.println("Objeto " + peticion[3] + " invalido");
					throw new Exception("400");
				}
			} else {
				System.err.println("Error: El recurso " + objetoRemoto + "no existe");
				throw new Exception("404");
			}
			respuestaHTML = cabeceraHTML + "<p>" + respuesta + "</p>" + pieHTML;
		} catch (Exception ex) {
			String mensaje = ex.getMessage();
			if (mensaje != null && mensaje.length() == 3) {
				respuestaHTML = mensaje;
			} else {
				System.err.println(mensaje);
				respuestaHTML = "500";
			}
		} finally {
			try {
				System.out.println("Enviando la respuesta al servidor miniHTTP");
				escribeSocket(respuestaHTML);
				System.out.println("Respuesta enviada");
				skServidor.close();
				System.out.println("Cerrada conexion con servidor miniHTTP");
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
    * Establece los flujos de entrada y salida con un socket.
    * \param socket Socket con el que se establecerá la conexión.
    **/
    private void establecerFlujos(Socket socket) throws IOException {
        establecerFlujoEntrada(socket);
        establecerFlujoSalida(socket);
    }

    /**
    * Establece el flujos de entrada con un socket.
    * \param socket Socket con el que se establecerá la conexión.
    **/
    private void establecerFlujoEntrada(Socket socket) throws IOException {
        InputStream flujoEntrada = socket.getInputStream();
        entrada = new BufferedReader(new InputStreamReader(flujoEntrada));
    }

    /**
    * Establece el flujos de salida con un socket.
    * \param socket Socket con el que se establecerá la conexión.
    **/
    private void establecerFlujoSalida(Socket socket) throws IOException {
        OutputStream flujoSalida = socket.getOutputStream();
        salida = new PrintWriter(new OutputStreamWriter(flujoSalida));
    }

        /**
    * Lee un mensaje del socket.
    * \return mensaje leído del socket.
    **/
    private String leeSocket() throws IOException {
        return entrada.readLine();
    }

    /**
    * Escribre un mensaje en el socket.
    * \param cadena Mensaje que se escribirá en el socket
    **/
    private void escribeSocket(String cadena) throws IOException {
       salida.println(cadena);
       salida.flush();
    }

    private boolean existeEnServidor(String objeto) throws java.rmi.RemoteException, Exception {
    	String[] lista = registry.list();
    	for (int i=0; i < lista.length; i++) {
			if(lista[i].equals(objeto)) return true;
		}
		System.err.println("Objeto no encontrado: " + objeto);
		throw new Exception("404");
    }

    private String[] obtenerPeticion(String cadena) throws Exception {
    	if (cadena != null) {
    		if (cadena != "") {
    			return cadena.split("/");
    		}
    	}
    	System.err.println("URL no introducida");
    	throw new Exception("400");
    }

    private String obtenerObjetoRemoto(String[] peticion) throws Exception {
    	String objetoRemoto = "";
    	if (peticion.length == 5) {
			objetoRemoto = "/" + peticion[1] + "/" + peticion[2] + "/";
			if (peticion[3].equals("sensor")) {
				System.out.println("Solcicitando sensor " + peticion[4] + " al servidor de nombres");
				objetoRemoto += peticion[3] + "/" + peticion[4];
			} else if (peticion[3].equals("actuador")) {
				System.out.println("Solicitando actuador del invernadero" + peticion[2]);
				objetoRemoto += peticion[3];
			} else {
				System.err.println("Objeto " + peticion[3] + " invalido");
				throw new Exception("400");
			}
		} else {
			System.err.println("Peticion invalida");
			throw new Exception("400");
		}
		return objetoRemoto;
    }

    private String obtenerRespuesta(iSensor sensor, String id_invernadero) throws Exception{
    	String respuesta = "";
    	String tipo = sensor.getTipo();
		String valor = sensor.getValor() + "";
		if(tipo.equals("temperatura")) {
			valor += "ºC"; //TODO: Añadir comprobacion de valores
		} else if(tipo.equals("humedad")) {
			valor += "%";
		} else {
			throw new Exception("500");
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String fecha = dateFormat.format(date);
		respuesta = fecha + ": La " + tipo + " del invernadero " + id_invernadero + " es " + valor;
		return respuesta;
    }

    private String obtenerRespuesta(iActuador actuador, String id_invernadero, String accion) {
		String respuesta;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String fecha = dateFormat.format(date);
		respuesta = fecha + ": El actuador del invernadero " + id_invernadero 
						+ " ha realizado la accion " + accion + " con exito";
		return respuesta;
    }
}