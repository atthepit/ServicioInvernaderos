import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;

public class Registro {
    public static void main (String args[])     
    {
        String host = args[0];
        int port = 1099;
        String servidor = "rmi://" + host + ":" + port;
        String URLRegistro;
        int opcion;
        int id;
        String tipo;
        try {
            System.out.println("¿Que elemento deseas crear:");
            System.out.println("1 - Invernadero");
            System.out.println("2 - Sensor");
            System.out.println("3 - Actuador");
            System.out.print("Opcion (1, 2, 3): ");

            try{
                System.setSecurityManager(new RMISecurityManager());
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                opcion = Integer.parseInt(bufferRead.readLine());
                System.out.print("Introduce el id del elemento: ");
                id = Integer.parseInt(bufferRead.readLine());
                Registry registry = LocateRegistry.getRegistry(host,port);
                if(opcion == 1) {
                    Invernadero invernadero = new Invernadero(id);
                    URLRegistro = "/invernadero/" + id;
                    registry.rebind(URLRegistro,invernadero);
                    System.out.println("Registrado");
                } else if (opcion == 2) {
                    Sensor sensor = new Sensor(id,host);
                    System.out.print("Introduce el tipo de sensor [temperatura/humedad]: ");
                    tipo = bufferRead.readLine();
                    sensor.setTipo(tipo);
                    System.out.print("Introduce el id del invernadero al que pertenece: ");
                    int invernadero_id = Integer.parseInt(bufferRead.readLine());
                    iInvernadero invernadero = (iInvernadero) registry.lookup("/invernadero/" + invernadero_id);
                    if(tipo.equals("temperatura")) {
                        invernadero.setSensorTemperatura(id);
                    } else if (tipo.equals("humedad")) {
                        invernadero.setSensorHumedad(id);
                    }
                    sensor.setInvernadero(invernadero_id);
                    URLRegistro = "/invernadero/" + invernadero_id + "/sensor/" + id;
                    registry.rebind (URLRegistro, sensor);
                    System.out.println("Registrado");
                } else if (opcion == 3) {
                    Actuador actuador = new Actuador(id,host);
                    System.out.print("Introduce el id del invernadero al que pertenece: ");
                    int invernadero_id = Integer.parseInt(bufferRead.readLine());
                    iInvernadero invernadero = (iInvernadero) registry.lookup("/invernadero/" + invernadero_id);
                    invernadero.setActuador(id);
                    actuador.setInvernadero(invernadero_id);
                    URLRegistro = "/invernadero/" + invernadero_id + "/actuador";
                    registry.rebind (URLRegistro, actuador);
                    System.out.println("Registrado");
                } else {
                    System.err.println("ERROR: Opcion " + opcion + " no valida");
                }
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}