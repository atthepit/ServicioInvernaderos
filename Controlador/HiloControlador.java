import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
* HiloControlador
* 
* Cada uno de los hilos del controlador recibe la petición del servidor
* miniHTTP, y la procesa. Los pasos que sigue son los siguientes:
*	· Obtiene la petición del socket con el servidor
*	· Obtiene el recurso que se solicitará al servidor de Objetos
*	· Comprueba si el recurso existe y lo solicita
*	· El objeto remoto realiza la acción pertinente (sensor/actuador)
*	· Se comprueba que los valores sean adecuados
*	· Se formatea la respuesta en HTML
*	· Se envía la respuesta al servidor miniHTTP 
* 
* @author Pedro Paredes Andreu
* @version 24.10.2013
* @see Controlador
* @see java.rmi
* @see java.net.Socket
**/
public class HiloControlador extends Thread {
	
	/**
	* Socket de conexión con el servidor miniHTTP.
	**/
	Socket skServidor;
	
	/**
	* Sensor solicitado.
	**/
	iSensor sensor;
	
	/**
	* Actuador solicitado.
	**/
	iActuador actuador;
	
	/**
	* Servidor de objetos.
	**/
	Registry registry;
	
	/**
	* Flujo de entra del socket.
	**/
	private BufferedReader entrada;
	
	/**
	* Flujo de salida del socket.
	**/
    private PrintWriter salida;

    /**
    * Constructor.
    * @param socket Socket de conexión con el servidor miniHTTP.
    * @param host Dirección donde se encuentra el servidor de Objetos.
    * @param port Puerto de escucha del servidor de Objetos.
    * @throws java.rmi.RemoteException Se lanza en caso de que haya algún error al obtener el objeto de registro.
    **/
	public HiloControlador(Socket socket, String host, int port) throws java.rmi.RemoteException {
		System.setSecurityManager(new RMISecurityManager());
		registry = LocateRegistry.getRegistry(host,port);
		skServidor = socket;
		sensor = null;
		actuador = null;
	}

	/**
	* Ejecuta el hilo del controlador.
	* Es el método encargado de toda la lógica del controlador.
	* @see Thread
	**/
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
					respuesta = obtenerRespuesta();
					System.out.println(respuesta);
				} else if(peticion[3].equals("actuador")) {
					actuador = (iActuador) registry.lookup(objetoRemoto);
					accion = peticion[4];
					System.out.println("Realizando accion...");
					boolean realizado = actuador.realizarAccion(accion);
					if (realizado) {
						respuesta = obtenerRespuesta(accion);
						System.out.println(respuesta);
					} else {
						System.err.println("ERROR: El actuador del invernadero '" + peticion[2] 
											+ "' no ha podido realizar la accion '" + accion +"'");
						throw new Exception("500");
					}
				} else {
					System.err.println("ERROR: Objeto '" + peticion[3] + "' invalido");
					throw new Exception("400");
				}
			} else {
				System.err.println("ERROR: El recurso '" + objetoRemoto + "' no existe");
				throw new Exception("404");
			}
			respuestaHTML = cabeceraHTML + "<p>" + respuesta + "</p>" + pieHTML;
		} catch (Exception ex) {
			String mensaje = ex.getMessage();
			if (mensaje != null && mensaje.length() == 3) {
				respuestaHTML = mensaje;
			} else {
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
    * @param socket Socket con el que se establecerá la conexión.
    * @throws IOException Si hay algún problema al crear algún flujo.
    **/
    private void establecerFlujos(Socket socket) throws IOException {
        establecerFlujoEntrada(socket);
        establecerFlujoSalida(socket);
    }

    /**
    * Establece el flujos de entrada con un socket.
    * @param socket Socket con el que se establecerá la conexión.
    * @throws IOException Si hay algún problema al establecer el flujo de entrada.
    **/
    private void establecerFlujoEntrada(Socket socket) throws IOException {
        InputStream flujoEntrada = socket.getInputStream();
        entrada = new BufferedReader(new InputStreamReader(flujoEntrada));
    }

    /**
    * Establece el flujos de salida con un socket.
    * @param socket Socket con el que se establecerá la conexión.
    * @throws IOException Si hay algún problema al establecer el flujo de salida.
    **/
    private void establecerFlujoSalida(Socket socket) throws IOException {
        OutputStream flujoSalida = socket.getOutputStream();
        salida = new PrintWriter(new OutputStreamWriter(flujoSalida));
    }

    /**
    * Lee un mensaje del socket.
    * @return mensaje leído del socket.
    * @throws IOException Si hay algún problema al leer del socket.
    **/
    private String leeSocket() throws IOException {
        return entrada.readLine();
    }

    /**
    * Escribre un mensaje en el socket.
    * @param cadena Mensaje que se escribirá en el socket
    * @throws IOException Si hay algún problema al escribir en el socket.
    **/
    private void escribeSocket(String cadena) throws IOException {
       salida.println(cadena);
       salida.flush();
    }

    /**
    * Comprueba si el recurso solicitado existe en el servidor de Objetos.
    * @param objeto Recurso solicitado por el servidor miniHTTP.
    * @return True si el objeto existe.
    * @throws Si hay algún problema con el objeto registry.
    * @throws Exception si el objeto no existe.
    **/
    private boolean existeEnServidor(String objeto) throws java.rmi.RemoteException, Exception {
    	String[] lista = registry.list();
    	for (int i=0; i < lista.length; i++) {
			if(lista[i].equals(objeto)) return true;
		}
		System.err.println("ERROR: Objeto '"+ objeto +"' no encontrado");
		throw new Exception("404");
    }

    /**
    * Despedaza la petición del servidor miniHTTP para que pueda ser procesada.
    * @param cadena Petición recibida del servirdor miniHTTP sin procesar.
    * @return petición procesada.
    * @throws Exception si la cadena es null o vacía.
    **/
    private String[] obtenerPeticion(String cadena) throws Exception {
    	if (cadena != null) {
    		if (cadena != "") {
    			return cadena.split("/");
    		}
    	}
    	System.err.println("ERROR: URL no introducida");
    	throw new Exception("400");
    }

    /**
    * Obtiene la URL del objeto remoto a partir de la petición del servidor miniHTTP.
    * @param peticion Petición procesada del servidor miniHTTP.
    * @return URL del objeto remoto a solicitar.
    * @throws Exception Si la petición está mal formada o el objeto solicitado no es correcto.
    **/
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
				System.err.println("ERROR: Objeto '" + peticion[3] + "' invalido");
				throw new Exception("400");
			}
		} else {
			System.err.println("ERROR: Peticion invalida");
			throw new Exception("400");
		}
		return objetoRemoto;
    }

    /**
    * Procesa el valor del sensor solicitado y crea la respuesta que se enviará al servidor miniHTTP.
    * @return Respuesta para el servidor miniHTTP.
    * @throws java.rmi.RemoteException Si hay algún problema al realizar las acciones del sensor.
    * @throws Exception si los valores del sensor son errones.
    **/
    private String obtenerRespuesta() throws java.rmi.RemoteException, Exception{
    	String respuesta = "";
    	String tipo = sensor.getTipo();
		String valor = sensor.getValor() + "";
		String id_invernadero = sensor.getInvernadero() + "";
		String warning = "</p><p style=\"color:red;\">ADVERTENCIA: La " + tipo + " del invernadero " + id_invernadero + " es";
		boolean mostrarWarning = false;
		if(tipo.equals("temperatura")) {
			int temp = Integer.parseInt(valor);
			valor += "ºC";
			if(temp < iInvernadero.MIN_TEMP_PERM || temp > iInvernadero.MAX_TEMP_PERM) {
				if (temp >= iInvernadero.MIN_TEMP && temp < iInvernadero.MIN_TEMP_PERM){ 
					warning += " demasiado baja. ACTIVE EL SISTEMA DE VENTILACION";
					mostrarWarning = true;
				} else if(temp > iInvernadero.MAX_TEMP_PERM && temp <= iInvernadero.MAX_TEMP) { 
					warning += " demasiado alta. ACTIVE LA APERTURA DE VENTANAS";
					mostrarWarning = true;
				} else {
					System.err.println("ERROR: Temperatura '" + valor + "' fuera del rango permitido");
					throw new Exception("500");
				}
				mostrarWarning = true;
			}
		} else if(tipo.equals("humedad")) {
			int hum = Integer.parseInt(valor);
			valor += "%";
			if(hum < iInvernadero.MIN_HUM_PERM || hum > iInvernadero.MAX_HUM_PERM) {
				if (hum >= iInvernadero.MIN_HUM && hum < iInvernadero.MIN_HUM_PERM){ 
					warning += " demasiado baja. ACTIVE EL GOTEO";
					mostrarWarning = true;
				} else if(hum > iInvernadero.MAX_HUM_PERM && hum <= iInvernadero.MAX_HUM) { 
					warning += " demasiado alta. ACTIVE EL DESHUMIDIFICADOR";
					mostrarWarning = true;
				} else {
					System.err.println("ERROR: Humedad '" + valor + "' fuera del rango permitido");
					throw new Exception("500");
				}
			}
		} else {
			System.err.println("ERROR: Tipo de sensor '" + tipo + "' incorrecto");
			throw new Exception("500");
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String fecha = dateFormat.format(date);
		respuesta = fecha + ": La " + tipo + " del invernadero " + id_invernadero + " es " + valor;
		if (mostrarWarning){
			respuesta += warning;
		}
		return respuesta;
    }

    /**
    * Procesa la petición del actuador solicitado y crea la respuesta que se enviará al servidor miniHTTP.
    * @param accion Acción a llevar a cabo por el actuador.
    * @return Respuesta para el servidor miniHTTP.
    * @throws java.rmi.RemoteException si hay algún problema al consultar el ID del invernadero.
    **/
    private String obtenerRespuesta(String accion) throws java.rmi.RemoteException {
		String respuesta;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String fecha = dateFormat.format(date);
		String id_invernadero = actuador.getInvernadero() + "";
		respuesta = fecha + ": El actuador del invernadero " + id_invernadero 
						+ " ha realizado la accion " + accion + " con exito";
		return respuesta;
    }
}