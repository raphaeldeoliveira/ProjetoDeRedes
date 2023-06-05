package redes3server;

public class Redes3Server {

    public static void main(String[] args) {
        
        RodarServer r1 = new RodarServer();
        Thread t1 = new Thread(r1);
        
        r1.start();
    }
    
}
