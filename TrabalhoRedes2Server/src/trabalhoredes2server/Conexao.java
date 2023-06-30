package trabalhoredes2server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Conexao extends Thread {

    private DataInputStream in;
    private DataOutputStream out;
    private Socket sock;
    private Arquivo arq;

    public Conexao(Socket socket) {
        try {
            sock = socket;
            // configura fluxo de entrada
            in = new DataInputStream(sock.getInputStream());
            // configura fluxo de saida
            out = new DataOutputStream(sock.getOutputStream());

        } catch (IOException ex) {
            System.err.println("IOException na classe Conexao: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // instancia um objeto Arquivo
            arq = new Arquivo(sock);
            while (true) {
                JSONParser parser = new JSONParser();
                // Espera a mensagem chegar e a armazena em uma string
                String jsonString = in.readUTF();
                // Constroi o objeto Json com base na string
                Object obj = parser.parse(jsonString);
                JSONObject json = (JSONObject) obj;

                // Se o comando for close, fecha o servidor
                if ("close".equalsIgnoreCase(json.get("command").toString())) {
                    System.out.println("Fechando a conexao com IP:  " + sock.getInetAddress().toString() + ":" + sock.getPort() + ".");
                    // ENCERRA A COMUNICAÇÃO
                    // encerra o fluxo de entrada do socket
                    sock.shutdownInput();
                    // encerra o fluxo de saida do socket
                    sock.shutdownOutput();
                    break;
                }

                // Se o comando for list, ele lista os arquivos do servidor
                if ("LIST".equalsIgnoreCase(json.get("command").toString())) {
                    arq.listaArquivos();
                }

                // Se o comando for get
                if (("GET").equalsIgnoreCase(json.get("command").toString())) {
                    if (arq.arquivoExiste(json)) {
                        // manda true
                        out.writeUTF("true");
                        
                        arq.enviaArquivo(json);
                    } else {
                        // manda false
                        out.writeUTF("false");
                    }
                }

                // se o comando for put
                if (("PUT").equalsIgnoreCase(json.get("command").toString())) {
                    arq.recebeArquivo(json);
                }

            }
            System.out.println("Conexao com o cliente encerrada.\n--------------------------//--------------------------");
            // encerra a conexão com o socket
            sock.close();
            // interrompe a thread
            interrupt();

        } catch (IOException ex) {
            System.err.println("IOException na classe Conexao: " + ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
