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

/**
* Registro
* Se encargará de registrar cada una de los objetos remotos en el 
* servidor de objetos.
* 
* @author Pedro Paredes Andreu
* @version 25.10.2013
* @see Invernadero
* @see Sensor
* @see Actuador
**/
public class Registro {
    public static void main (String args[])     
    {
        String host = args[0];
        int port = 1099;
        String servidor = "rmi://" + host + ":" + port;
        int opcion;
        int id;
        try {
            System.out.println("¿Que elemento deseas crear:");
            System.out.println("1 - Invernadero");
            System.out.println("2 - Sensor");
            System.out.println("3 - Actuador");
            System.out.print("Opcion (1, 2, 3): ");

            try{
                System.setSecurityManager(new RMISecurityManager());
                BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
                opcion = Integer.parseInt(entrada.readLine());
                System.out.print("Introduce el id del elemento: ");
                id = Integer.parseInt(entrada.readLine());
                Registry registry = LocateRegistry.getRegistry(host,port);
                if(opcion == 1) {
                    registrarInvernadero(registry, id);
                } else { 
                    System.out.print("Introduce el id del invernadero al que pertenece: ");
                    int invernadero_id = Integer.parseInt(entrada.readLine());
                    if (opcion == 2) {
                        System.out.print("Introduce el tipo de sensor [temperatura/humedad]: ");
                        String tipo = entrada.readLine();
                        registrarSensor(registry, id, tipo, invernadero_id, host);
                    } else if (opcion == 3) {
                        registrarActuador(registry, id, invernadero_id, host);
                    } else {
                        System.err.println("ERROR: Opcion " + opcion + " no valida");
                    }
                } 
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private static void registrarInvernadero(Registry registry, int id) throws Exception {
        Invernadero invernadero = new Invernadero(id);
        String URLRegistro = "/invernadero/" + id;
        registry.rebind(URLRegistro,invernadero);
        System.out.println("Registrado");
    }

    private static void registrarSensor(Registry registry, int id, String tipo, int invernadero_id, String host) throws Exception {
        Sensor sensor = new Sensor(id,host);
        sensor.setTipo(tipo);
        iInvernadero invernadero = (iInvernadero) registry.lookup("/invernadero/" + invernadero_id);
        if(tipo.equals("temperatura")) {
            invernadero.setSensorTemperatura(id);
        } else if (tipo.equals("humedad")) {
            invernadero.setSensorHumedad(id);
        }
        sensor.setInvernadero(invernadero_id);
        String URLRegistro = "/invernadero/" + invernadero_id + "/sensor/" + id;
        registry.rebind (URLRegistro, sensor);
        System.out.println("Registrado");
    }

    private static void registrarActuador(Registry registry, int id, int invernadero_id, String host) throws Exception {
        Actuador actuador = new Actuador(id,host);
        iInvernadero invernadero = (iInvernadero) registry.lookup("/invernadero/" + invernadero_id);
        invernadero.setActuador(id);
        actuador.setInvernadero(invernadero_id);
        String URLRegistro = "/invernadero/" + invernadero_id + "/actuador";
        registry.rebind (URLRegistro, actuador);
        System.out.println("Registrado");
    }
}
