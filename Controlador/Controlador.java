import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Controlador {

	public static void main(String[] args) throws IOException {
		int puerto = 9876;
		ServerSocket skControlador = new ServerSocket(puerto);
		System.out.println("Servidor activo.");
        System.out.println("Escuchando puerto " + puerto);
		try{
            for(;;){
                Socket skServidor = skControlador.accept();
                System.out.println("Sirviendo peticion del Servidor...");
                Thread hilo = new HiloControlador(skServidor);
                hilo.start();
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
	}
}