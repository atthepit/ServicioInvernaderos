import java.rmi.Remote;

public interface iSensor extends Remote {
	public Actuador(int id) throws java.rmi.RemoteException;
  	public void setInvernadero(int invernadero) throws java.rmi.RemoteException;
  	public int getInvernadero() throws java.rmi.RemoteException;
  	public boolean realizarAccion(String accion) throws java.rmi.RemoteException;
}