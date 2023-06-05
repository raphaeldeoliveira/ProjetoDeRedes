package redes1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Painel2 extends javax.swing.JPanel {

    // instancia o defaulList para colocar as mensagens enviadas e recebidas
    DefaultListModel<String> chat = new DefaultListModel<>();
    
    String mensagem;
    InetAddress group;
    MulticastSocket socket;
    
    static String backupMensagem;
    
    Thread thread1;
    
    public Painel2() {
        initComponents();
        entrarGrupoMulticast();
        ligatThreadRecebimento();
    }
    
    public void ligatThreadRecebimento() {
        thread1 = new Thread(Janela.recebedor);
        thread1.start();
    }
    
    public void receberMensagem(String mensagem) {
        // verifica se é a mensagem que foi enviada
        
        // verifica se a mensagem que recebeu não é igual a que envio, para
        // não duplicar as mensagens quando envia
        if (mensagem.equals(backupMensagem)) {
            
        }
        else {
            // converte a mensagem em formato do painel
            String mensagemConvertida = converterFormato(mensagem);

            // coloca a mensagem no model do list
            chat.addElement(mensagemConvertida);

            // atualiza a lista com o model
            jList1.setModel(chat);
        }
    }
    
    public String horaExata() {
        // cria um objeto Date com a data e hora atuais
        Date agora = new Date();
        
        // cria um objeto SimpleDateFormat para formatar a data
        SimpleDateFormat formato = new SimpleDateFormat("(dd/MM/yyyy) HH:mm");
        
        // usa o objeto SimpleDateFormat para formatar a data
        String dataFormatada = formato.format(agora);
        
        return dataFormatada;
    }
    
    public String converterFormato(String messagemFormatada) {
        String mensagemConvertida = "vazio";
        //messagemFormatada.indexOf("username")
        String usuario = messagemFormatada.substring((messagemFormatada.indexOf("username") + 11), (messagemFormatada.indexOf("}") - 1));
        String mensagem = messagemFormatada.substring((messagemFormatada.indexOf("message") + 10), (messagemFormatada.indexOf("username") - 3));
        String hora = messagemFormatada.substring((messagemFormatada.indexOf("time") + 7), (messagemFormatada.indexOf("message") - 3));
        
        mensagemConvertida = hora.concat(" ~ ");
        mensagemConvertida = mensagemConvertida.concat(usuario);
        mensagemConvertida = mensagemConvertida.concat(": ");
        mensagemConvertida = mensagemConvertida.concat(mensagem);
        
        return mensagemConvertida;
    }
    
    public void enviarMensagem(String messagemFormatada) throws IOException {
        
        // Converta a string JSON em um array de bytes
        byte[] jsonBytes = messagemFormatada.getBytes(StandardCharsets.UTF_8);
        
        // faz o envio da mensagem no grupo multicast
        DatagramPacket pacoteEnvio = new DatagramPacket(jsonBytes, jsonBytes.length, group, Janela.porta);
        
        // envia o pacote para o group
        this.socket.send(pacoteEnvio);
        
        // tranforma o texto em formato de chat
        System.out.println("mensagem: "+messagemFormatada);
        String mensagemConvertida = converterFormato(messagemFormatada);
        
        chat.addElement(mensagemConvertida);
        
        // atualiza a lista com o model
        jList1.setModel(chat);
    }
    
    public String converterJson(String mensagem) {
        String dataFormatada = horaExata();
        
        // divide a mensagem em data e hora
        String data = dataFormatada.substring(0, 12);
        String hora = dataFormatada.substring(13, 18);
        
        // converte em JSON
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("date", data);
        jsonMessage.put("time", hora);
        jsonMessage.put("username", Janela.user);
        jsonMessage.put("message", mensagem);
        
        String mensagemEmJson = jsonMessage.toJSONString();
        System.out.println("formato do JSON");
        System.out.println(mensagemEmJson);
        
        return mensagemEmJson;
    }
    
    public void entrarGrupoMulticast() {
        try {
            socket = new MulticastSocket(Janela.porta);
            
            group = InetAddress.getByName(Janela.ip);
            this.socket.joinGroup(group);
            System.out.println("Joined multicast group");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 204));
        jLabel1.setText("ZAPCHAT");

        jScrollPane1.setViewportView(jList1);

        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(157, 157, 157))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
        
        mensagem = jTextField1.getText();
        
        String messagemFormatada = converterJson(mensagem);
        backupMensagem = messagemFormatada;
        
        try {
            enviarMensagem(messagemFormatada);
        } catch (IOException ex) {
            Logger.getLogger(Janela.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jTextField1.setText("");
        
    }//GEN-LAST:event_jButton1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
