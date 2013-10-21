import java.rmi.Remote;

public interface iSensor extends Remote {
	public Sensor(int id) throws java.rmi.RemoteException;
	public void setTipo(String tipo) throws java.rmi.RemoteException;
	public void setInvernadero(int invernadero) throws java.rmi.RemoteException;
	public String getTipo() throws java.rmi.RemoteException;
	public int getInvernadero() throws java.rmi.RemoteException;
	public int getValor() throws java.rmi.RemoteException;
}