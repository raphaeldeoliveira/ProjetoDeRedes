package parte3.cliente;

import java.util.logging.Level;
import java.util.logging.Logger;

public class requesicoes extends Thread {
    
    // a cada X milisegundos faz a requesição na classe pai
    // (fica chamando o metodo que faz o get geral)
    
    int tempo;
    
    public void passarParametro(int tempo) {
        this.tempo = tempo;
    }
    
    public void run() {
        while (true) {
            try {
                JanelaEnergia.p1.getGeral();
                Thread.sleep(tempo);
            } catch (InterruptedException ex) {
                Logger.getLogger(requesicoes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
