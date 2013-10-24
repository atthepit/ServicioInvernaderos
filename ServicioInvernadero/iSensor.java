import java.rmi.Remote;
import java.io.IOException;
import java.rmi.NotBoundException;

public interface iSensor extends Remote {
	public void setTipo(String tipo) throws java.rmi.RemoteException, Exception;
	public void setInvernadero(int invernadero) throws java.rmi.RemoteException, Exception;
	public String getTipo() throws java.rmi.RemoteException;
	public int getInvernadero() throws java.rmi.RemoteException;
	public int getValor() throws java.rmi.RemoteException, IOException, NotBoundException, Exception;
}