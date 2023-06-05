package redes1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class receberMensagem extends Thread {

    public void run() {
        
        try {
            InetAddress group = InetAddress.getByName(Janela.ip);
            
            MulticastSocket socket = new MulticastSocket(Janela.porta);
            socket.joinGroup(group);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                Janela.p2.receberMensagem(message);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
