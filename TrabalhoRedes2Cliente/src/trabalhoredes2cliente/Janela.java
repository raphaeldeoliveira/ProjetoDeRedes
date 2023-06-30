package trabalhoredes2cliente;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.DefaultListModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Janela extends javax.swing.JFrame {

    DefaultListModel<String> model = new DefaultListModel<>();
    JSONObject json;
    // vetor para armazenar as mensagens
    String[] cmd;
    
    InetAddress address;
    int srvPort;
    Socket sock;
    DataInputStream in;
    DataOutputStream out;
    Arquivo arq;
        
    public Janela() {
        initComponents();
        fazerConexaoServidor();
        mostrarMenu();
    }
    
    public void fazerConexaoServidor() {
        // faz a conexão com o servidor
        try {
            address = InetAddress.getByName("127.0.0.1");
            srvPort = 50000;
            // cria o socket
            sock = new Socket(address, srvPort);
            // configura o fluxo de entrada
            in = new DataInputStream(sock.getInputStream());
            // configura o fluxo de saida
            out = new DataOutputStream(sock.getOutputStream());
            // instancia objeto arquivo
            arq = new Arquivo(sock, this);
            model.addElement("Conectado ao sevidor!");
            jList1.setModel(model);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void escreverNoCMD(String mensagem) {
        model.addElement(mensagem);
        jList1.setModel(model);
    }
    
    public void mostrarMenu() {
        model.addElement("----------------------------------------------------------");
        model.addElement("------------------------- CMD ----------------------------");
        model.addElement("----------------------------------------------------------");
        model.addElement("- HELP - Exibe esse menu ---------------------------------");
        model.addElement("- PUT <arquivo> - Manda arquivo para o servidor ----------");
        model.addElement("- GET <arquivo> - Faz o download do arquivo do servidor --");
        model.addElement("- LIST - Lista os arquivos do diretorio local ------------");
        model.addElement("- LISTLOCAL - Lista os arquivos do diretorio local -------");
        model.addElement("- CLOSE - Fecha a conexão com o servidor -----------------");
        model.addElement("- CLEAR - Limpa o terminal -------------------------------");
        model.addElement("----------------------------------------------------------");
        model.addElement("");
        jList1.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jList1.setBackground(new java.awt.Color(51, 51, 51));
        jList1.setForeground(new java.awt.Color(102, 255, 102));
        jScrollPane1.setViewportView(jList1);

        jTextField1.setBackground(new java.awt.Color(51, 51, 51));
        jTextField1.setForeground(new java.awt.Color(102, 255, 102));
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String mensagemCMD;
            json = new JSONObject();
            
            mensagemCMD = jTextField1.getText();
            // divide o texto pelo espaço entre comando e argumento do comando e os armazena
            // no vetor 'cmd'
            cmd = mensagemCMD.split(" ");
            
            jTextField1.setText("");
            
            if (!cmd[0].isEmpty()) {
                
                try {
            
                    // se o comando tiver argumento (nome do arquivo) ele coloca no JSON
                    // os dois valores. Se não somente comando
                    if ("PUT".equalsIgnoreCase(cmd[0]) || "GET".equalsIgnoreCase(cmd[0])) {
                        json.put("command", cmd[0]);
                        json.put("file", cmd[1]);
                    } else {
                        json.put("command", cmd[0]);
                    }

                    // se o comando for listlocal
                    if (!json.get("command").equals("LISTLOCAL")) {
                        out.writeUTF(json.toString());
                    }

                    // se o comando for list
                    if ("LIST".equalsIgnoreCase(json.get("command").toString())) {
                        // constroi o JSON
                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(in.readUTF());
                        json = (JSONObject) obj;
                        model.addElement(json.get("list").toString());
                        jList1.setModel(model);
                    } else if ("LISTLOCAL".equalsIgnoreCase(json.get("command").toString())) {
                        arq.listaArquivos();
                    } else if ("PUT".equalsIgnoreCase(json.get("command").toString())) {
                        if (arq.arquivoExiste(json)) {
                            arq.enviaArquivo(json);
                        } else {
                            model.addElement("O arquivo nao existe no cliente!\nUse o comando LISTLOCAL para verificar os arquivos do cliente.");
                            jList1.setModel(model);
                        }
                    } else if ("GET".equalsIgnoreCase(json.get("command").toString())) {
                        String msg = in.readUTF();
                        if ("true".equals(msg)) {
                            arq.recebeArquivo(json);
                        } else {
                            model.addElement("O arquivo nao existe no servidor!\nUse o comando LIST para verificar os arquivos do servidor.");
                            jList1.setModel(model);
                        }
                    } else if ("help".equalsIgnoreCase(json.get("command").toString())) {
                        mostrarMenu();
                    } else if ("clear".equalsIgnoreCase(json.get("command").toString())) {
                        model.removeAllElements();
                        jList1.setModel(model);
                    } else if ("close".equalsIgnoreCase(json.get("command").toString())) {
                        model.addElement("Fechando socket do cliente!");
                        jList1.setModel(model);
                        sock.shutdownInput();
                        sock.shutdownOutput();
                        System.exit(0);
                    } else {
                        model.addElement("Comando desconhecido, por favor tente novamente!");
                        jList1.setModel(model);
                    }
                
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                model.addElement("Comando inválido!");
                jList1.setModel(model);
            }
        }
        
    }//GEN-LAST:event_jTextField1KeyReleased

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Janela().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
