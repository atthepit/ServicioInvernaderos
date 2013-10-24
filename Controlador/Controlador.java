import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
* Controlador
*
* Controlador encargado de gestionar las peticiones recibidas desde
* el servidor miniHTTP. El controlador es capaz de gestionar las peticiones
* concurrentemente.
*
* @author Pedro Paredes Andreu
* @version 24.10.2013
* @see HiloControlador
* @see ServidorMiniHTTP
* @see java.net.Socket
* @see java.rmi
**/
public class Controlador {

    /**
    * Main, encargado de aceptar las peticiones del servidor y distribuirlas en hilos.
    * @param args Argumentos: host del servidor de objetos.
    * @throws IOException si hay algún problema en la comunicación de los sockets.
    **/
	public static void main(String[] args) throws IOException {
		int puerto = 9876;
        String hostServidorObjetos = args[0];
        int puertoServidorObjetos = 1099;
		ServerSocket skControlador = new ServerSocket(puerto);
		System.out.println("Servidor activo.");
        System.out.println("Escuchando puerto " + puerto);
		try{
            for(;;){
                Socket skServidor = skControlador.accept();
                System.out.println("Sirviendo peticion del Servidor...");
                Thread hilo = new HiloControlador(skServidor,hostServidorObjetos,puertoServidorObjetos);
                hilo.start();
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
	}
}