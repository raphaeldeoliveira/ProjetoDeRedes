package redes3cliente;

import java.util.logging.Level;
import java.util.logging.Logger;

public class requesicoes extends Thread {
    
    int tempo;
    
    public void passarParametro(int tempo) {
        this.tempo = tempo;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Janela.p1.getGeral();
                Thread.sleep(tempo);
            } catch (InterruptedException ex) {
                Logger.getLogger(requesicoes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
