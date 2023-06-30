package redes1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class receberMensagem extends Thread {

    private MulticastSocket socket;
    
    public receberMensagem(MulticastSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        
        try {
            // cria array de bytes para armazenar os dados recebidos do socket
            byte[] buffer = new byte[1024];

            // loop infinito para ficar verificando se chegou mensagens
            while (true) {
                // instancia o DatagramPacket (pacote de dados) e prepara ele para receber combase no array de byte
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                // recebe o pacote contendo a mensagem pelo socket e armazena no packet
                socket.receive(packet);
                // converte a mensagem em string
                String message = new String(packet.getData(), 0, packet.getLength());
                // chama o metodo no painel 2 para tratar a mensagem e exibir na GUI
                Janela.p2.receberMensagem(message);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
