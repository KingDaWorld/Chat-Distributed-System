package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void registerClient(ClientInterface client, String username) throws RemoteException;
    void broadcastMessage(String username, String message) throws RemoteException;
    void unregisterClient(ClientInterface client) throws RemoteException;
}
