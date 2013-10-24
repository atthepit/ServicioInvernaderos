import java.rmi.Remote;

public interface iInvernadero extends Remote {
	public void setSensorTemperatura(int sensor) throws java.rmi.RemoteException, Exception;
	public void setSensorHumedad(int sensor) throws java.rmi.RemoteException, Exception;
	public void setActuador(int actuador) throws java.rmi.RemoteException, Exception;
	public void setTemperatura(int temperatura) throws java.rmi.RemoteException, Exception;
	public void setHumedad(int humedad) throws java.rmi.RemoteException, Exception;
	public int getSensorTemperatura() throws java.rmi.RemoteException, Exception;
	public int getSensorHumedad() throws java.rmi.RemoteException, Exception;
	public int getActuador() throws java.rmi.RemoteException,Exception;
	public int getTemperatura() throws java.rmi.RemoteException, Exception;
	public int getHumedad() throws java.rmi.RemoteException, Exception;

	public static final int MIN_TEMP = -10;
  	public static final int MAX_TEMP = 45;
  	public static final int MIN_TEMP_PERM = 25;
  	public static final int MAX_TEMP_PERM = 35;
  	public static final int MIN_HUM = 0;
  	public static final int MAX_HUM = 100;
  	public static final int MIN_HUM_PERM = 40;
  	public static final int MAX_HUM_PERM = 75;
}