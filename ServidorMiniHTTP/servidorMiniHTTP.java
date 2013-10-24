import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
* ServidorMiniHTTP.java
*
* Servidor encargado de gestionar las peticiones miniHTTP recibidas
* desde un cliente web. El servidor gestiona las peticiones concurrentemente.
*
* @author Pedro Paredes Andreu
* @version 24.10.2013
* @see HiloServidor
* @see Controlador
* @see java.net.Socket
**/
public class ServidorMiniHTTP {

    /**
    * Main, encargado de aceptar las peticiones del cliente y distribuirlas en hilos.
    * @param args Argumentos: no se espera ninguno.
    * @throws IOException si hay algún problema en la comunicación de los sockets.
    **/
    public static void main(String[] args) throws IOException{
        int puerto = 8080;
        
            ServerSocket skServidor = new ServerSocket(puerto);
            System.out.println("Servidor activo.");
            System.out.println("Escuchando puerto " + puerto);
        try{
            for(;;){
                Socket skCliente = skServidor.accept();
                System.out.println("Sirviendo cliente...");
                Thread hilo = new HiloServidor(skCliente);
                hilo.start();
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}