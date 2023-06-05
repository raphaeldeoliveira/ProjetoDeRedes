package redes3cliente;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Painel1 extends javax.swing.JPanel {

    String comando;
    String argumento;
    String digital;
    
    public void getGeral() {
        // faz todos os envio
        enviar("get", "luz_guarita");
        enviar("get", "ar_guarita");
        enviar("get", "luz_estacionamento");
        enviar("get", "luz_galpao_externo");
        enviar("get", "luz_galpao_interno");
        enviar("get", "luz_escritorios");
        enviar("get", "ar_escritorios");
        enviar("get", "luz_sala_reunioes");
        enviar("get", "ar_sala_reunioes");
    }
    
    public JSONObject converterJSON(String comando, String argumento) {
        JSONObject convertido = new JSONObject();
        convertido.put("command", comando);
        convertido.put("locate", argumento);
        System.out.println("JSON1: "+convertido);
        
        return convertido;
    }
    
    public JSONObject converterJSON2(String comando, String argumento, String digital) {
        JSONObject convertido = new JSONObject();
        convertido.put("command", comando);
        convertido.put("locate", argumento);
        convertido.put("value", digital);
        System.out.println("JSON2: "+convertido);
        
        return convertido;
    }
    
    public void verificarStatus(String rxMsg) {
        String local = rxMsg.substring((rxMsg.indexOf("{") + 11), (rxMsg.indexOf(",") - 1));
        jLabel6.setText(local);
        String status = rxMsg.substring((rxMsg.indexOf("}") - 3), (rxMsg.indexOf("}") - 1));
        
        if (status.equals("ff")) {
            jLabel8.setText("OFF");
        }
        if (status.equals("on")) {
            jLabel8.setText("ON");
        }
    }
    
    public void enviar2(String comando, String argumento, String digital) {
        
        InetAddress srvIPAddr = null;
        try {
            srvIPAddr = InetAddress.getByName(Janela.ip);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // Obtém a mensagem do terminal
        BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            // Cria socket do cliente
            DatagramSocket clientSock = new DatagramSocket();            
            
            // Cria os buffers de comunicação
            // 65535 - 20 IP - 8 UDP = 65507
            byte[] rxData = new byte[65507];
            byte[] txData = new byte[65507];
            
            // Obtém a mensagem
                // converte o argumento e comando em json
                JSONObject mensagemJson =  converterJSON2(comando, argumento, digital);
            
                // passa para string
                String txMsg = mensagemJson.toString();
                
                // coloca a mensagem de envio no painel
                jTextField2.setText(txMsg);
            
            // Converte a mensagem em um array de byte
            txData = txMsg.getBytes(StandardCharsets.UTF_8);
            
            // Cria o pacote de envio
            DatagramPacket txPkt = new DatagramPacket(txData, txMsg.length(), srvIPAddr, Janela.porta);
            
            // Envia a mensagem
            System.out.println("Enviando a mensagem para o servidor!");
            clientSock.send(txPkt);
            
            // Aguardar a resposta do servidor
            // Cria o pacote UDP de recebimento
            DatagramPacket rxPkt = new DatagramPacket(
                    rxData, rxData.length);
            
            // Aguarda a resposta do servidor
            System.out.println("Aguardando a resposta do servidor!");
            clientSock.receive(rxPkt);
            
            // parei aqui
            
            // Processa a resposta do servidor
            String rxMgs = new String(
                    rxPkt.getData(), StandardCharsets.UTF_8);
            rxMgs = rxMgs.substring(0, rxPkt.getLength());
            
            // Imprime a resposta do servidor
            System.out.println("[Response]: " + rxMgs);
            
            // coloca a mensagem de recebimento no painel
            jTextField3.setText(rxMgs);
            
            verificarStatus(rxMgs);
            
        } catch (IOException e) {
            System.err.println("\n\tMessage error: " + e.getMessage());
            System.exit(1);
        }
        
    }
    
    public void enviar(String comando, String argumento) {
        
        InetAddress srvIPAddr = null;
        try {
            srvIPAddr = InetAddress.getByName(Janela.ip);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // Obtém a mensagem do terminal
        BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            // Cria socket do cliente
            DatagramSocket clientSock = new DatagramSocket();            
            
            // Cria os buffers de comunicação
            // 65535 - 20 IP - 8 UDP = 65507
            byte[] rxData = new byte[65507];
            byte[] txData = new byte[65507];
            
            // Obtém a mensagem
                // converte o argumento e comando em json
                JSONObject mensagemJson =  converterJSON(comando, argumento);
            
                // passa para string
                String txMsg = mensagemJson.toString();
                
                // coloca a mensagem de envio no painel
                jTextField2.setText(txMsg);
            
            // Converte a mensagem em um array de byte
            txData = txMsg.getBytes(StandardCharsets.UTF_8);
            
            // Cria o pacote de envio
            DatagramPacket txPkt = new DatagramPacket(txData, txMsg.length(), srvIPAddr, Janela.porta);
            
            // Envia a mensagem
            System.out.println("Enviando a mensagem para o servidor!");
            clientSock.send(txPkt);
            
            // Aguardar a resposta do servidor
            // Cria o pacote UDP de recebimento
            DatagramPacket rxPkt = new DatagramPacket(
                    rxData, rxData.length);
            
            // Aguarda a resposta do servidor
            System.out.println("Aguardando a resposta do servidor!");
            clientSock.receive(rxPkt);
            
            // parei aqui
            
            // Processa a resposta do servidor
            String rxMgs = new String(
                    rxPkt.getData(), StandardCharsets.UTF_8);
            rxMgs = rxMgs.substring(0, rxPkt.getLength());
            
            // Imprime a resposta do servidor
            System.out.println("[Response]: " + rxMgs);
            
            // coloca a mensagem de recebimento no painel
            jTextField3.setText(rxMgs);
            
            verificarStatus(rxMgs);
            
        } catch (IOException e) {
            System.err.println("\n\tMessage error: " + e.getMessage());
            System.exit(1);
        }
        
    }
    
    public Painel1() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        jButton1.setText("GET");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        jButton2.setText("SET");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("luz_guarita");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("ar_guarita");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("luz_estacionamento");

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("luz_galpao_externo");

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("luz_galpao_interno");

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText("luz_escritorios");

        buttonGroup1.add(jRadioButton7);
        jRadioButton7.setText("ar_escritorios");

        buttonGroup1.add(jRadioButton8);
        jRadioButton8.setText("luz_sala_reunioes");

        buttonGroup1.add(jRadioButton9);
        jRadioButton9.setText("ar_sala_reunioes");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Escolha o local");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Monitoramento de energia");

        jLabel3.setText("Comando Enviado:");

        jLabel4.setText("Comando Recebido: ");

        jLabel5.setText("Local");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

        jLabel7.setText("Status");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 28)); // NOI18N

        jButton3.setText("Requesições Automaticas");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        buttonGroup2.add(jRadioButton10);
        jRadioButton10.setText("OFF");

        buttonGroup2.add(jRadioButton11);
        jRadioButton11.setText("ON");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2)
                            .addComponent(jTextField3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(288, 288, 288)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton11)
                    .addComponent(jRadioButton10))
                .addGap(89, 89, 89))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jRadioButton4)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jRadioButton6)
                                .addComponent(jRadioButton5)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton7)
                            .addComponent(jRadioButton8)
                            .addComponent(jRadioButton9)))
                    .addComponent(jLabel1))
                .addGap(63, 63, 63))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton9))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButton10))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        
        if (buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Deve ser escolhido um local!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        else {
            comando = "get";
            if (jRadioButton1.isSelected()) {
                argumento = "luz_guarita";
                
                // envia o comando
                enviar(comando, argumento);
            }
            else {
                if (jRadioButton2.isSelected()) {
                    argumento = "ar_guarita";
                    
                    // envia o comando
                    enviar(comando, argumento);
                }
                else {
                    if (jRadioButton3.isSelected()) {
                        argumento = "luz_estacionamento";
                        
                        // envia o comando
                        enviar(comando, argumento);
                    }
                    else {
                        if (jRadioButton4.isSelected()) {
                            argumento = "luz_galpao_externo";
                            
                            // envia o comando
                            enviar(comando, argumento);
                        }
                        else {
                            if (jRadioButton5.isSelected()) {
                                argumento = "luz_galpao_interno";
                                
                                // envia o comando
                                enviar(comando, argumento);
                            }
                            else {
                                if (jRadioButton6.isSelected()) {
                                    argumento = "luz_escritorios";
                                    
                                    // envia o comando
                                    enviar(comando, argumento);
                                }
                                else {
                                    if (jRadioButton7.isSelected()) {
                                        argumento = "ar_escritorios";
                                        
                                        // envia o comando
                                        enviar(comando, argumento);
                                    }
                                    else {
                                        if (jRadioButton8.isSelected()) {
                                            argumento = "luz_sala_reunioes";
                                            
                                            // envia o comando
                                            enviar(comando, argumento);
                                        }
                                        else {
                                            if (jRadioButton9.isSelected()) {
                                                argumento = "ar_sala_reunioes";
                                                
                                                // envia o comando
                                                enviar(comando, argumento);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked

        // verifica se algum dos botoes (on ou off) foi clicado
        if (buttonGroup2.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Deve ser selecionado SET ou GET", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            
            if (buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Deve ser escolhido um local!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            else {
                comando = "set";
                // verifica qual comando foi seelcionado (get ou set)
                if (jRadioButton10.isSelected()) {
                    digital = "off";
                }
                else {
                    if (jRadioButton11.isSelected()) {
                        digital = "on";
                    }
                }
                
                if (jRadioButton1.isSelected()) {
                    argumento = "luz_guarita";

                    
                    // envia o comando
                    enviar2(comando, argumento, digital);
                }
                else {
                    if (jRadioButton2.isSelected()) {
                        argumento = "ar_guarita";

                        // envia o comando
                        enviar2(comando, argumento, digital);
                    }
                    else {
                        if (jRadioButton3.isSelected()) {
                            argumento = "luz_estacionamento";

                            // envia o comando
                            enviar2(comando, argumento, digital);
                        }
                        else {
                            if (jRadioButton4.isSelected()) {
                                argumento = "luz_galpao_externo";

                                // envia o comando
                                enviar2(comando, argumento, digital);
                            }
                            else {
                                if (jRadioButton5.isSelected()) {
                                    argumento = "luz_galpao_interno";

                                    // envia o comando
                                    enviar2(comando, argumento, digital);
                                }
                                else {
                                    if (jRadioButton6.isSelected()) {
                                        argumento = "luz_escritorios";

                                        // envia o comando
                                        enviar2(comando, argumento, digital);
                                    }
                                    else {
                                        if (jRadioButton7.isSelected()) {
                                            argumento = "ar_escritorios";

                                            // envia o comando
                                            enviar2(comando, argumento, digital);
                                        }
                                        else {
                                            if (jRadioButton8.isSelected()) {
                                                argumento = "luz_sala_reunioes";

                                                // envia o comando
                                                enviar2(comando, argumento, digital);
                                            }
                                            else {
                                                if (jRadioButton9.isSelected()) {
                                                    argumento = "ar_sala_reunioes";

                                                    // envia o comando
                                                    enviar2(comando, argumento, digital);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
        
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        
        // chama a janela painel2
        JFrame janela = (JFrame) SwingUtilities.getWindowAncestor(this);
        janela.getContentPane().remove(Janela.p1);
        janela.add(Janela.p2, BorderLayout.CENTER);
        janela.pack();
        
    }//GEN-LAST:event_jButton3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
