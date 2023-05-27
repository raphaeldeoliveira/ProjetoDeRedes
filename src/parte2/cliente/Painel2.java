package parte2.cliente;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.json.simple.JSONObject;

public class Painel2 extends javax.swing.JPanel {

    DefaultListModel<String> arquivosServer = new DefaultListModel<>();
    
    // componentes de comunicação
    DataInputStream in;
    DataOutputStream out;
    Socket sock;
    
    String url = "C:\\Users\\conto\\Pictures\\exerciciosrobison\\parte2Redes-Cliente\\";
    
    public Painel2() {
        initComponents();
        jList1.setModel(arquivosServer);
        ligar();
    }
    
    public void ligar() {
        try {
            JanelaArquivos.srvAddr = InetAddress.getByName(JanelaArquivos.ip);
            sock = new Socket(JanelaArquivos.srvAddr, JanelaArquivos.porta);
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String gerarHashMD5(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for(byte b : hash){
            String toAppend = String.format("%2X", b).replace("", "0"); 
            sb.append(toAppend);
        }
        return sb.toString();
    }
    
    public String formatarJson(String comando, String argumento, String hash) {
        JSONObject convertido = new JSONObject();
        convertido.put("command", comando);
        if (argumento.equals("")) {
            
        }
        else {
            convertido.put("file", argumento);
            if (hash.equals("")) {
                
            }
            else {
                //gerarHashMD5();
                convertido.put("hash", hash);
            }
        }
        String txMsg = convertido.toString();
        System.out.println("aoaoaoaoaoao: "+txMsg);
        return txMsg;
    }
    
    public void enviar(String comando, String argumento, String hash)  {
        String mensagemFormatada = formatarJson(comando, argumento, hash);
        String outMsg, inMsg;
        
        try {
            
            Socket sock = new Socket(JanelaArquivos.srvAddr, JanelaArquivos.porta);
            
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            
            // constroi o objeto do arquivo
            File arquivo_file = new File(argumento);

            // pega o tamanho do arquivo
            int arquivo_size = (int)arquivo_file.length(); 

            // cria o array de byte do arquivo (somente define o tamanho para criar)
            byte[] arquivo_bytes = new byte[arquivo_size]; 

            // copia todos os bytes do arquivo (transforma em um array de bytes)
            arquivo_bytes = Files.readAllBytes(arquivo_file.toPath());
            
            // faz o hash e o armazena como array de byte
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash_value = md.digest(arquivo_bytes);
            
            // instancia o objeto do arquivo
            File file = new File("src\\arquivo.md5");
            // instancia a ferramenta que escreve no arquivo especificado
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            
            // escreve o hash no arquivo, concatenando o hash com o nome do arquivo
            writer.write(hash_value.toString() +"  arquivo.txt");
            // salva
            writer.flush();
            // fecha
            writer.close();
            
            /*
            // formata a mensagem em JSON, para poder enviar
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("file_name", arquivo_file);
            jsonObj.put("hash_value", hash_value.toString());
            */
            //Envia a mensagem
            out.writeUTF(mensagemFormatada);

            // Recebe resposta do servidor
            inMsg = in.readUTF();
            System.out.println("[Responde]: " + inMsg);
            
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Painel2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void colocarNoPainel(String comando, String argumento) {
        String mensagem;
        if (argumento.equals("")) {
            mensagem = JanelaArquivos.user.concat(": ");
            mensagem = mensagem.concat(comando);
            arquivosServer.addElement(mensagem);
            jList1.setModel(arquivosServer);
        }
        else {
            mensagem = JanelaArquivos.user.concat(": ");
            mensagem = mensagem.concat(comando);
            mensagem = mensagem.concat(" ");
            mensagem = mensagem.concat(argumento);
            arquivosServer.addElement(mensagem);
            jList1.setModel(arquivosServer);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Servidor de arquivos");

        jScrollPane1.setViewportView(jList1);

        jButton1.setText("LIST");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setText("PUT");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("GET");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jLabel2.setText("Nome do arquivo: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(39, 233, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(0, 8, Short.MAX_VALUE))
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
        // faz a requesição dos arquivos do servidor
        String comando = "list";
        String argumento = "";
        String hash = "";
        
        enviar(comando, argumento, hash);
        
        colocarNoPainel(comando, argumento);
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked

        String comando = "put";
        String argumento = url.concat(jTextField1.getText());
        String hash = "sim";
        
        enviar(comando, argumento, hash);
        
        colocarNoPainel(comando, argumento);
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked

        String comando = "get";
        String argumento = url.concat(jTextField1.getText());;
        String hash = "";
        
        enviar(comando, argumento, hash);
        
        colocarNoPainel(comando, argumento);
        
    }//GEN-LAST:event_jButton3MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
