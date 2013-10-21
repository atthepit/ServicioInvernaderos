import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Registro {
    public static void main (String args[])     
    {
        String servidor = "rmi://" + host + ":" + port;
        String URLRegistro;
        int opcion;
        int id;
        String tipo
        try {
            System.out.println("¿Que elemento deseas crear:");
            System.out.println("1 - Invernadero");
            System.out.println("2 - Sensor");
            System.out.println("3 - Actuador");
            System.out.print("Opcion (1, 2, 3: ");

            try{
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                opcion = Integer.parseInt(bufferRead.readLine());
                System.out.print("Introduce el id del elemento: ");
                id = Integer.parseInt(bufferRead.readLine());
                if(opcion == 1) {
                    Invernadero invernadero = new Invernadero(id);
                } else if (opcion == 2) {
                    System.setSecurityManager(new RMISecurityManager());
                    Sensor sensor = new Sensor(id);
                    System.out.print("Introduce el tipo de sensor [temperatura/humedad]: ")
                    tipo = bufferRead.readLine();
                    sensor.setTipo(tipo);
                    System.out.print("Introduce el id del invernadero al que pertenece: ");
                    Int invernadero_id = Integer.parseInt(bufferRead.readLine());
                    iInvernadero invernadero = (iInvernadero) Naming.lookup(servidor + "/invernadero/" + invernadero_id);
                    if(tipo.equals("temperatura")) {
                        invernadero.setSensorTemperatura(id);
                    } else if (tipo.equals("humedad")) {
                        invernadero.setSensorHumedad(id);
                    }
                    sensor.setInvernadero(invernadero_id);
                    URLRegistro = "/invernadero/" + invernadero_id + "/sensor/" + id;
                    Naming.rebind (URLRegistro, objetoRemoto);
                    
                } else if (opcion == 3) {
                    Actuador actuador = new Actuador(id);
                } else {
                    System.err.println("ERROR: Opcion " + opcion + " no valida");
                }
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }



            System.setSecurityManager(new RMISecurityManager());
            ObjetoRemoto objetoRemoto = new ObjetoRemoto();                  
            URLRegistro = "/ObjetoRemoto";
            Naming.rebind (URLRegistro, objetoRemoto);            
        System.out.println("Servidor de objeto preparado.");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}