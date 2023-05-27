package parte2.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    
    public Connection(Socket aClientSoclet){
        try {
            clientSocket = aClientSoclet;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (Exception e) {
            System.err.println("Connection: " + e.getMessage());
        }
    }
    
    public void list() {
        // como verificar os arquivos que tem aqui?
        // cria uma mensagem com os arquivos e envia ( la trata com um for pra listar todos no Jlist)
    }
    
    public void get(String nomeArquivo) {
        // procura o arquivo (por meio de um for, com a string que recebe)
        // faz o download deste
    }
    
    public void put() {
        
    }
    
    public void tratarMensagem(String data) {
        String comando = data.substring((data.indexOf("{") + 8), (data.indexOf("command") - 3));
        String nomeArquivo = data.substring((data.indexOf("command") + 10), (data.indexOf("hash") - 3));
        String hash = data.substring((data.indexOf("hash") + 6), (data.indexOf("}") - 1));
        
        // teste de depuração (ainda nao realizado)
        System.out.println("testes de recebimento");
        System.out.println("comandoo: "+comando);
        System.out.println("nomeArquivoo: "+nomeArquivo);
        System.out.println("hashh: "+hash);
        
        // verificação de qual comando. Dentro do metodo, vai enviar a mensagem
        if (comando.equals("list")) {
            // chama o metodo list 
            list();
        }
        else {
            if (comando.equals("get")) {
                // chama o metodo get
                get(nomeArquivo);
            }
            else {
                if (comando.equals("put")) {
                    // chama o metodo put
                    put();
                }
            }
        }
    }
    
    public void run(){
        try {
            System.out.println("foi");
            //Recebe a mensagem do cliente
            String data = in.readUTF();
            
            // trata a mensagem, verifica o comando
            tratarMensagem(data);
                       
            /*JSONObject jsonObj = new JSONObject(data);
            
            File arquivo_file = new File(jsonObj.getString("file_name"));
            
            String hash = jsonObj.getString("hash_value");*/
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(data);

            String fileName = (String) jsonObj.get("file_name");
            File arquivo_file = new File(fileName);

            String hash = (String) jsonObj.get("hash_value");
            
            // ATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃO
            // o que esta acima equivale as 3 linhas apagadas
            // ATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃOATENÇÃO
            
            int arquivo_size = (int)arquivo_file.length(); 
            
            byte[] arquivo_bytes = new byte[arquivo_size]; 
            
            arquivo_bytes = Files.readAllBytes(arquivo_file.toPath());
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            byte[] hash_value = md.digest(arquivo_bytes);
            
            System.out.println("\tHash: "+ (hash_value));
            
            System.out.println("\t[Received = " + clientSocket.getInetAddress().toString()
                + ":" + clientSocket.getPort() + "]: " + data);
                
            String hashCalculado = hash_value.toString();
            if(hash.equals(hashCalculado)){
                System.out.println("ok");
                String rdata = "Received by server";
            out.writeUTF(rdata);
            }
            
           
            
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
    private static String toHexFormat(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for(byte b : hash){
            String toAppend = String.format("%2X", b).replace("", "0"); 
            sb.append(toAppend);
        }
        return sb.toString();
    }
}
