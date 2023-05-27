package parte2.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RodarServidorGA extends Thread {
    
    public static void runServer(int port){
        System.out.println("Running TCPServer at port " + port + "...");
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
    
    public void run() {
        int port = 50000;
        runServer(port);
        
    }
    
}
