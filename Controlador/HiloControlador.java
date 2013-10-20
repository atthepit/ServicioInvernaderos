import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class HiloControlador extends Thread {
	Socket skServidor;
	//iSensor sensor;
	String sensor;
	//iActuador actuador;
	String actuador;
	private BufferedReader entrada;
    private PrintWriter salida;

	public HiloControlador(Socket socket) {
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
		String tipo = "";
		String valor = "";
		DateFormat dateFormat = null;
		Date date = null;
		String fecha = "";
		String cabeceraHTML = "<html><head><title>Servicio Invernaderos</title><meta charset=\"UTF-8\" /></head><body>";
		String respuesta = "";
		String pieHTML = "</body></html>";
		String host = "localhost";
		String port = "8765";

		try {
			System.out.println("Estableciendo conexión con el servidor miniHTTP");
			establecerFlujos(skServidor);
			System.out.println("Leyendo peticion...");
			cadena = leeSocket();
			System.out.println(cadena);
			if (cadena != null) {
				if (cadena != "") {
					peticion = cadena.split("/");
					if (peticion.length == 5) {
						objetoRemoto = peticion[1] + "/" + peticion[2] + "/";
						if (peticion[3].equals("sensor")) {
							System.out.println("Solcicitando sensor " + peticion[4] + "al servidor de nombres");
							objetoRemoto += peticion[3] + "/" + peticion[4];
						} else if (peticion[3].equals("actuador")) {
							System.out.println("Solicitando actuador del " + peticion[2]);
							objetoRemoto += peticion[3];
							accion = peticion[4];
						}
						servidorRMI = "rmi://" + host + ":" + port + "/" + objetoRemoto;
						try {
							//System.setSecurityManager(new RMISecurityManager());
							if (accion.equals("")) {
								//sensor = (iSensor) Naming.lookup(servidorRMI);
								System.out.println("Obteniendo valor del sensor " + peticion[4] + ": ");
								tipo = "temperatura";//sensor.getTipo();
								valor = "24";//sensor.getValor().toString();
								if(tipo.equals("temperatura")) {
									valor += "ºC"; //TODO: Añadir comprobacion de valores
								} else {
									valor += "%";
								}
								dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
								date = new Date();
								fecha = dateFormat.format(date);
								respuesta = fecha + ": La " + tipo + " del invernadero " + peticion[2] + " es " + valor;
								System.out.println(respuesta);
							} else {
								//actuador = (iActuador) Naming.lookup(servidorRMI);
								System.out.println("Realizando accion...");
								boolean realizado = true;//actuador.realizarAccion(accion);
								dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
								date = new Date();
								fecha = dateFormat.format(date);
								if (realizado) {
									respuesta = fecha + ": El actuador del invernadero " + peticion[2] + "ha realizado la accion " 
												+ accion + "con exito";
								} else {
									respuesta = "ERROR";
								}
								System.out.println(respuesta);
							}
						} catch (Exception ex) {
							System.err.println("Error:" + ex.getMessage());
						}
					}
				}
			}
			sensor = null;
			actuador = null;

			System.out.println("Enviando la respuesta al servidor miniHTTP");
			escribeSocket(cabeceraHTML + "<p>" + respuesta + "</p>" + pieHTML);
			System.out.println("Respuesta enviada");
			skServidor.close();
			System.out.println("Cerrada conexion con servidor miniHTTP");

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
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

	/*
		1º Leer mensaje del servidor
		2º Parsear el mensaje
		3º Buscar el objeto RMI correspondiente
			3.1 Si no existe devolver ""
		3º Realizar la operacion correspondiente
		4º Enviar el resultado al Servidor
	*/
}