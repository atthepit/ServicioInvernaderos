import java.io.*;
import java.net.Socket;

/**
 *
 * @author mesmerismo
 */
class hiloServidor extends Thread {
    private Socket skCliente;
    private String host = "localhost";
    private int port = 9876;
    private BufferedReader entrada;
    private PrintWriter salida;
    
    public hiloServidor(Socket skCliente) {
        this.skCliente = skCliente;
    }
    
    @Override
    public void run(){
        /*
            1º Se lee la petición HTTP del cliente
                1.1 Previamente se debe establecer el flujo de entrada
            2º Se obtiene la URL de la petición
                2.1 Se puede formatear o enviar tal cual
            3º Se envía la URL al controlador
                3.1 Se debe establecer el flujo de salida 
            4º Se recibe la respuesta del controlador
                4.1 Se deber cambiar el flujo de entrada al controlador
                4.2 Parsear la respuesta (tipoSensor,valor)
                4.3 Una vez recibida la respuesta cerrar conexión con controlador
            5º Se crea la respuesta HTTP que se enviará
                5.1 Basta con introducir el resultado entre etiquetas <p></p>
            6º Se envia la respuesta HTTP al cliente
                6.1 Enviar primero estado luego línea en blanco y html
        */
        String cadena = "";
        try {
            establecerFlujos(skCliente);
            cadena = leeSocket();
            String aux = "";
            do{
                aux = leeSocket();
                System.out.println(aux);
            }while(!aux.equals("")); 
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        String[] peticion = cadena.split(" ");
        String metodo = peticion[0];
        String url = peticion[1];
        String httpVerison = peticion[2];
        String codigoEstado = "200";
        String mensajeEstado = "OK";
        String cabeceraHTML = "<html><head><title>Prueba</title></head><body>";
        String respuesta = "<p>Hola</p>";
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
        
        //escribeSocket(cadena);
        try{
            escribeSocket(estado);
            escribeSocket("");
            escribeSocket(cuerpo);
            skCliente.close();
        }catch (IOException ex){
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
}