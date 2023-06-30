package trabalhoredes2server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Arquivo {

    private Socket sock;
    private DataOutputStream out;
    private DataInputStream in;

    public Arquivo(Socket socket) throws IOException {
        this.sock = socket;
        // configura fluxo de entrada
        out = new DataOutputStream(sock.getOutputStream());
        // configura fluxo de saida
        in = new DataInputStream(sock.getInputStream());
    }

    public boolean arquivoExiste(JSONObject json) {
        // Cria um objeto File referenciando o diretório
        File file = new File("src/arquivos");
        
        // Cria um vetor com os arquivos contidos no diretorio (do file)
        File[] listaArq = file.listFiles();
        
        // verifica se o arquivo contido no JSON existe no diretorio 'arquivos'
        for (File file1 : listaArq) {
            if(json.get("file").equals(file1.getName())){
                return true;
            }
        }
        return false;
    }

    public void listaArquivos() throws IOException {
        // instancia o JSON
        JSONObject json = new JSONObject();
        String msg = "";
        
        // Cria um objeto File referenciando o diretório
        File file = new File("src/arquivos");

        // Cria um vetor com os arquivos contidos no diretorio (do file)
        File[] listaArq = file.listFiles();
        
        // Insere o nome dos arquivos um ao lado do outro seguido de um espaço
        for (File file1 : listaArq) {
            msg += file1.getName() + "      ";
        }
        
        // coloca no JSON com  a chave list
        json.put("list", "\n" + msg);
        
        // Converte o objeto JSON em uma string e envia a mensagem.
        out.writeUTF(json.toString());
    }

    private String getHash(String nomeArquivo) {
        // instancia o objeto do arquivo
        File file = new File("src/arquivos/" + nomeArquivo);
        // retorna o hash do arquivo
        return "" + file.hashCode() + file.length();
    }
    

    private void enviaHash(String nomeArquivo, String operation) throws IOException {
        // cria o objeto arquivo para referenciar o arquivo no diretorio
        File file = new File("src/arquivos/" + nomeArquivo);
        
        // instancia o json e coloca as chaves-valor do arquivo, operação e o hash
        JSONObject json = new JSONObject();
        json.put("file", nomeArquivo);
        json.put("operation", operation);
        // gera o hash e insere com o put
        json.put("hash", "" + file.hashCode() + file.length());
        // envia o Json
        out.writeUTF(json.toString());
    }

    public void enviaArquivo(JSONObject json) {
        try {
            // envia o hash
            enviaHash(json.get("file").toString(), json.get("command").toString()); 

            // instancia o arquivo
            File file = new File("src/arquivos/" + json.get("file")); 

            // cria um array de byte com o tamanho do arquivo
            byte[] arrayBytes = new byte[(int) file.length()];

            // le o conteudo (bytes) do arquivo
            FileInputStream fis = new FileInputStream(file); 

            // serve para ler mais rapido os dados do fis; Pois
            // faz uso de um buffer interno para armazenar os dados lidos
            BufferedInputStream bis = new BufferedInputStream(fis); 

            // efetivamente le os bytes do array de bytes da posição 0 até o final
            bis.read(arrayBytes, 0, arrayBytes.length); 

            // escreve os dados do array de bytes no fluxo de saida (envia)
            out.write(arrayBytes, 0, arrayBytes.length); 

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException na classe Arquivo: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException na classe Arquivo: " + ex.getMessage());
        }
    }

    public void recebeArquivo(JSONObject json) {
        try {
            System.out.println("\nObtendo hash...");

            // recebe o json como string e controi como json
            JSONParser parser = new JSONParser();
            String jsonString = in.readUTF();
            Object obj = parser.parse(jsonString);
            JSONObject jsonHash = (JSONObject) obj;

            int bytesRead, current = 0;

            // cria um array de bytes com o tamanho de 20 megabytes
            byte[] arrayBytes = new byte[20971520];

            // escreve os bytes em um arquivo
            FileOutputStream fos = new FileOutputStream("src/arquivos/" + json.get("file").toString()); 

            // melhora o desempenho de gravação dos dados no FileOutputStream.
            BufferedOutputStream bos = new BufferedOutputStream(fos); 

            System.out.println("Realizando download...");

            // atribui a variavel o número de bytes lidos
            bytesRead = in.read(arrayBytes, 0, arrayBytes.length); 

            current = bytesRead; 

            // faz a leitura de todos os bytes
            do {
                // serve pra não travar a thread
                Thread.sleep(1);
                // le os bytes que chegam e armazena no bytes lidos
                bytesRead = in.read(arrayBytes, current,in.available());
                // verifica se foram lidos bytes
                if (bytesRead >= 0) {
                    // incrementa o current com a quantidade de bytes lidos
                    current += bytesRead;
                }
                // verifica se ainda existem bytes disponíveis no fluxo de entrada para leitura.
            } while (in.available() != 0);
            // grava os bytes armazenados no array de bytes
            bos.write(arrayBytes, 0, current); 
            // fecha o BufferedOutputStream
            bos.close();

            // verifica se o hash do arquivo recebido é igual ao hash do JSON
            if (getHash(json.get("file").toString()).equals(jsonHash.get("hash"))) {
                // instancia o JSON, coloca as chaves e valores e envia para o cliente
                jsonHash = new JSONObject();
                jsonHash.put("file", json.get("file"));
                jsonHash.put("operation", json.get("command"));
                jsonHash.put("status", "success");
                out.writeUTF(jsonHash.toString());

            } else {
                jsonHash = new JSONObject();
                jsonHash.put("file", json.get("file"));
                jsonHash.put("operation", json.get("command"));
                jsonHash.put("status", "success");
                out.writeUTF(jsonHash.toString());
                
                // O arquivo é excluido do diretorio de arquivos
                File file = new File("src/arquivos/" + json.get("file").toString());
                file.delete();
            }

        } catch (IOException ex) {
            System.out.println("IOExpcetion na classe arquivo: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException na classe arquivo: " + ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
