package server;

import interfaces.ChatServer;
import interfaces.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<ClientInterface> clients;
    private List<String> usernames;

    protected ChatServerImpl() throws RemoteException {
        clients = new ArrayList<>();
        usernames = new ArrayList<>();
    }

    public synchronized void registerClient(ClientInterface client, String username) throws RemoteException {
        clients.add(client);
        usernames.add(username);
        broadcastMessage("System", username + " has joined the chat.");
    }

    public synchronized void broadcastMessage(String username, String message) throws RemoteException {
        if (message == null || message.trim().isEmpty()) return;
        for (ClientInterface client : clients) {
            client.receiveMessage(username + ": " + message);
        }
    }

    public synchronized void unregisterClient(ClientInterface client) throws RemoteException {
        int index = clients.indexOf(client);
        if (index >= 0) {
            String username = usernames.get(index);
            clients.remove(index);
            usernames.remove(index);
            broadcastMessage("System", username + " has left the chat.");
        }
    }
}
