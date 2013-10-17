import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author mesmerismo
 */
class hiloServidor extends Thread {
    private Socket skCliente;
    private Socket skControlador;
    
    public hiloServidor(Socket skCliente) {
        this.skCliente = skCliente;
    }

    public hiloServidor(Socket skCliente, Socket skControlador) {
        this.skCliente = skCliente;
        this.skControlador = skControlador;
    }
    
    @Override
    public void run(){
        String cadena;
        cadena = leeSocket(skCliente);
        String[] peticion = cadena.split(" ");
        String metodo = peticion[0];
        String url = peticion[1];
        String httpVerison = peticion[2];
        String codigoEstado = "200";
        String mensajeEstado = "OK";
        String cabeceraHTML = "<html><head></head><body>";
        String respuesta = "";
        String pieHTML = "</body></html>";
        if (metodo.equals("GET")){
            //escribeSocket(skControlador,url);
        } else {
            codigoEstado = "400";
            mensajeEstado = "Bad Request";
            respuesta = "<p>El metodo HTTP " + metodo + " no es soportado en esta practica.</p>";
        }
        String estado = httpVerison + codigoEstado + mensajeEstado;
        String cuerpo =  cabeceraHTML + respuesta + pieHTML;
        String[] mensajeHTML = new String[3];
        mensajeHTML[0] = estado;
        mensajeHTML[1] = "\n";
        mensajeHTML[2] = cuerpo;
        escribeSocket(skCliente,mensajeHTML[0]);
        //escribeSocket(cadena);
    }

    private String leeSocket(Socket socket) {
        InputStream flujoEntrada = null;
        String cadena = "";
        try {
            flujoEntrada = socket.getInputStream();
            BufferedReader socketInput = new BufferedReader(new InputStreamReader(flujoEntrada));
            cadena = socketInput.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                flujoEntrada.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return cadena;
    }

    private void escribeSocket(Socket socket, String cadena) {
       try {
            OutputStream aux = skCliente.getOutputStream();
            DataOutputStream flujo= new DataOutputStream( aux );
            flujo.writeUTF(cadena);      
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.toString());
        }
    }
}