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
}