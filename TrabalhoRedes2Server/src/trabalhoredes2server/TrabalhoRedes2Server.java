package trabalhoredes2server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TrabalhoRedes2Server {

    public static void main(String[] args) {
        
        int port = 50000;
        try {
            // Cria o socket de servidor
            ServerSocket listenSock = new ServerSocket(port, 5);
            while (true) {
                System.out.println("\tAguardando conexao do cliente...");
                // cria o socket e aguarda até que uma seja conectado pelo cliente
                Socket clientSock = listenSock.accept();
                System.out.println("Conectado ao Ip: " + clientSock.getInetAddress().toString() + ":" + clientSock.getPort());

                // instancia a classe conexão
                Conexao c = new Conexao(clientSock);
                c.start();
            }
        } catch (IOException e) {
            System.err.println("IOException na classe ServidorTCP: " + e.getMessage());
        }
        
    }
    
}
