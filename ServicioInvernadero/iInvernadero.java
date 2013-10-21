import java.rmi.Remote;

public interface iInvernadero extends Remote {
	public Invernadero(int id) throws java.rmi.RemoteException;
	public void setSensorTemperatura(int sensor) throws java.rmi.RemoteException;
	public void setSensorHumedad(int sensor) throws java.rmi.RemoteException;
	public void setActuador(int actuador) throws java.rmi.RemoteException;
	public void setTemperatura() throws java.rmi.RemoteException;
	public void setHumedad() throws java.rmi.RemoteException;
	public int getTemperatura() throws java.rmi.RemoteException;
	public int getHumedad() throws java.rmi.RemoteException;
}