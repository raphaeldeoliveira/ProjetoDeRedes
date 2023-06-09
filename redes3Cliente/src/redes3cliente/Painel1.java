package redes3cliente;

import java.awt.BorderLayout;
import java.awt.Color;
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
import org.json.simple.parser.JSONParser;

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
        
        return convertido;
    }
    
    public JSONObject converterJSON2(String comando, String argumento, String digital) {
        JSONObject convertido = new JSONObject();
        convertido.put("command", comando);
        convertido.put("locate", argumento);
        convertido.put("value", digital);
        
        return convertido;
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
                
            // Converte a mensagem em um array de byte
            txData = txMsg.getBytes(StandardCharsets.UTF_8);
            
            // Cria o pacote de envio
            DatagramPacket txPkt = new DatagramPacket(txData, txMsg.length(), srvIPAddr, Janela.porta);
            
            // Envia a mensagem
            clientSock.send(txPkt);
            
            // Aguardar a resposta do servidor
            // Cria o pacote UDP de recebimento
            DatagramPacket rxPkt = new DatagramPacket(
                    rxData, rxData.length);
            
            // Aguarda a resposta do servidor
            clientSock.receive(rxPkt);
            
            // Processa a resposta do servidor
            String rxMgs = new String(
                    rxPkt.getData(), StandardCharsets.UTF_8);
            rxMgs = rxMgs.substring(0, rxPkt.getLength());
            
            // Imprime a resposta do servidor
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean verSeTemToogleAtivo() {
        if (jPanel4.isVisible() || jPanel5.isVisible() || jPanel3.isVisible() || jPanel6.isVisible() || jPanel8.isVisible() || jPanel9.isVisible() || jPanel2.isVisible() || jPanel11.isVisible() || jPanel10.isVisible()) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public void atualizarToggle(String msg) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String status = (String) json.get("status");
        String local = (String) json.get("locate");
        
        String texto;
        Color corFundo;
        
        if (status.equals("off")) {
            texto = "OFF";
            corFundo = new Color(255, 0, 51);
        }
        else {
            texto = "ON";
            corFundo = new Color(0, 153, 0);
        }
        
        switch (local) {
            case "luz_guarita":
                jPanel4.setBackground(corFundo);
                jLabel9.setText(texto);
                jPanel4.setVisible(true);
                break;
            case "ar_guarita":
                jPanel5.setBackground(corFundo);
                jLabel11.setText(texto);
                jPanel5.setVisible(true);
                break;
            case "luz_estacionamento":
                jPanel3.setBackground(corFundo);
                jLabel12.setText(texto);
                jPanel3.setVisible(true);
                break;
            case "luz_galpao_externo":
                jPanel6.setBackground(corFundo);
                jLabel15.setText(texto);
                jPanel6.setVisible(true);
                break;
            case "luz_galpao_interno":
                jPanel8.setBackground(corFundo);
                jLabel14.setText(texto);
                jPanel8.setVisible(true);
                break;
            case "luz_escritorios":
                jPanel9.setBackground(corFundo);
                jLabel13.setText(texto);
                jPanel9.setVisible(true);
                break;
            case "ar_escritorios":
                jPanel2.setBackground(corFundo);
                jLabel16.setText(texto);
                jPanel2.setVisible(true);
                break;
            case "luz_sala_reunioes":
                jPanel11.setBackground(corFundo);
                jLabel17.setText(texto);
                jPanel11.setVisible(true);
                break;
            case "ar_sala_reunioes":
                jPanel10.setBackground(corFundo);
                jLabel18.setText(texto);
                jPanel10.setVisible(true);
                break;
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
                
            // Converte a mensagem em um array de byte
            txData = txMsg.getBytes(StandardCharsets.UTF_8);
            
            // Cria o pacote de envio
            DatagramPacket txPkt = new DatagramPacket(txData, txMsg.length(), srvIPAddr, Janela.porta);
            
            // Envia a mensagem
            clientSock.send(txPkt);
            
            // Aguardar a resposta do servidor
            // Cria o pacote UDP de recebimento
            DatagramPacket rxPkt = new DatagramPacket(
                    rxData, rxData.length);
            
            // Aguarda a resposta do servidor
            clientSock.receive(rxPkt);
            
            // Processa a resposta do servidor
            String rxMgs = new String(
                    rxPkt.getData(), StandardCharsets.UTF_8);
            rxMgs = rxMgs.substring(0, rxPkt.getLength());
            
            // atualiza os botoes ON/OFF
            atualizarToggle(rxMgs);
            
            // Imprime a resposta do servidor
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public Painel1() {
        initComponents();
        jPanel4.setVisible(false);
        jPanel5.setVisible(false);
        jPanel3.setVisible(false);
        jPanel6.setVisible(false);
        jPanel8.setVisible(false);
        jPanel9.setVisible(false);
        jPanel2.setVisible(false);
        jPanel11.setVisible(false);
        jPanel10.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel10 = new javax.swing.JLabel();
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
        jButton3 = new javax.swing.JButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("OFF");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        jButton1.setText("GET");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        jButton2.setText("SET");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, -1, -1));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("luz_guarita");
        jPanel1.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("ar_guarita");
        jPanel1.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("luz_estacionamento");
        jPanel1.add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, -1, -1));

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("luz_galpao_externo");
        jPanel1.add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, -1, -1));

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("luz_galpao_interno");
        jPanel1.add(jRadioButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, -1, -1));

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText("luz_escritorios");
        jPanel1.add(jRadioButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, -1, -1));

        buttonGroup1.add(jRadioButton7);
        jRadioButton7.setText("ar_escritorios");
        jPanel1.add(jRadioButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, -1, -1));

        buttonGroup1.add(jRadioButton8);
        jRadioButton8.setText("luz_sala_reunioes");
        jPanel1.add(jRadioButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 120, -1, -1));

        buttonGroup1.add(jRadioButton9);
        jRadioButton9.setText("ar_sala_reunioes");
        jPanel1.add(jRadioButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Escolha o local");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 49, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Monitoramento de energia");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 12, -1, -1));

        jButton3.setText("Requesições Automaticas");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(396, 20, -1, -1));

        buttonGroup2.add(jRadioButton10);
        jRadioButton10.setText("OFF");
        jPanel1.add(jRadioButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 250, -1, -1));

        buttonGroup2.add(jRadioButton11);
        jRadioButton11.setText("ON");
        jPanel1.add(jRadioButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 220, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 0, 51));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("OFF");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, 30, 20));

        jPanel3.setBackground(new java.awt.Color(255, 0, 51));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("OFF");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 30, 20));

        jPanel4.setBackground(new java.awt.Color(255, 0, 51));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("OFF");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 30, 20));

        jPanel5.setBackground(new java.awt.Color(255, 0, 51));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("OFF");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 30, 20));

        jPanel6.setBackground(new java.awt.Color(255, 0, 51));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("OFF");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 30, 20));

        jPanel8.setBackground(new java.awt.Color(255, 0, 51));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("OFF");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 30, 20));

        jPanel9.setBackground(new java.awt.Color(255, 0, 51));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("OFF");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 30, 20));

        jPanel10.setBackground(new java.awt.Color(255, 0, 51));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("OFF");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 30, 20));

        jPanel11.setBackground(new java.awt.Color(255, 0, 51));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("OFF");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 30, 20));

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        jButton4.setText("GET GERAL");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 200, 50));

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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // COMANDO GET
        
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
        // COMANDO SET
        
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
        
        getGeral();
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        
        // chama a janela painel2
        JFrame janela = (JFrame) SwingUtilities.getWindowAncestor(this);
        janela.getContentPane().remove(Janela.p1);
        janela.add(Janela.p2, BorderLayout.CENTER);
        janela.pack();
        
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        getGeral();
    }//GEN-LAST:event_jButton4MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
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
    // End of variables declaration//GEN-END:variables
}
