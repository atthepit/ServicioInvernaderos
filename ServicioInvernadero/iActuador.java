import java.rmi.Remote;
import java.io.IOException;
import java.rmi.NotBoundException;

public interface iActuador extends Remote {
  	public void setInvernadero(int invernadero) throws java.rmi.RemoteException, Exception;
  	public int getInvernadero() throws java.rmi.RemoteException;
  	public boolean realizarAccion(String accion) throws java.rmi.RemoteException, IOException, NotBoundException, Exception;
}