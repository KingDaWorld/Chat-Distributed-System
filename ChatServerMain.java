package server;

import interfaces.ChatServer;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ChatServerMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1100);
            ChatServerImpl chatServer = new ChatServerImpl();
            Naming.rebind("rmi://localhost:1100/ChatServer", chatServer);
            System.out.println("Chat server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
