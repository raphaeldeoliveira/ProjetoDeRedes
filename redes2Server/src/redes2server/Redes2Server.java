package redes2server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Redes2Server {

    public static void main(String[] args) {
        
        int port = 50000;
        try {
            ServerSocket listenSocket = new ServerSocket(port, 5);
            
            while(true){
                
                System.out.println("\tWaiting connection...");
                Socket clientSocket = listenSocket.accept();
                
                System.out.println("\t\tConnected to "
                    + clientSocket.getInetAddress().toString()
                    + " at port " + clientSocket.getPort());
                // 
                
                Connection c = new Connection(clientSocket);
                c.start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
    }
    
}
