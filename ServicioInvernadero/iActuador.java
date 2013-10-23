import java.rmi.Remote;

public interface iActuador extends Remote {
  	public void setInvernadero(int invernadero) throws java.rmi.RemoteException, Exception;
  	public int getInvernadero() throws java.rmi.RemoteException;
  	public boolean realizarAccion(String accion) throws java.rmi.RemoteException;
}