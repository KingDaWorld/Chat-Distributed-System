
package client;

import interfaces.ChatServer;
import interfaces.ClientInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ChatClient extends UnicastRemoteObject implements ClientInterface {
    private ChatServer chatServer;
    private String username;

    protected ChatClient(ChatServer chatServer, String username) throws RemoteException {
        this.chatServer = chatServer;
        this.username = username;
        chatServer.registerClient(this, username);
    }

    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public String getUsername() throws RemoteException {
        return username;
    }

    public static void main(String[] args) {
        try {
            ChatServer chatServer = (ChatServer) Naming.lookup("rmi://localhost:1100/ChatServer");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            ChatClient client = new ChatClient(chatServer, username);
            System.out.println("Connected to chat server as " + username);

            // Register shutdown hook to unregister the client on termination
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    chatServer.unregisterClient(client);
                    System.out.println("Disconnected from chat server.");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }));

            // Keep reading messages from the console and sending to the server
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }
                chatServer.broadcastMessage(username, message);
            }

            // Unregister the client before exiting
            chatServer.unregisterClient(client);
            System.out.println("Disconnected from chat server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



