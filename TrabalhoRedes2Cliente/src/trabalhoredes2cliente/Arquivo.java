package trabalhoredes2cliente;

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
    private Janela janela;

    public Arquivo(Socket socket, Janela janela) throws IOException {
        this.sock = socket;
        // configura o fluxo de saida
        out = new DataOutputStream(sock.getOutputStream());
        // configura o fluxo de entrada
        in = new DataInputStream(sock.getInputStream());
        this.janela = janela;
    }

    private String getHash(String nomeArquivo) {
        // instancia o objeto arquivo
        File file = new File("src/arquivos/" + nomeArquivo);
        // retorna o hash
        return "" + file.hashCode() + file.length();
    }
	
    public boolean arquivoExiste(JSONObject json) {
        // instancia um file com o diretorio especificado
        File file = new File("src/arquivos");
        // obtem a lista de arquivos presente no diretorio
        File[] listaArq = file.listFiles();
        
        for (File file1 : listaArq) {
            // verifica se o arquivo do JSON existe no diretorio
            if(json.get("file").equals(file1.getName())){
                return true;
            }
        }
        return false;
    }
	
    public void listaArquivos() throws IOException {
        String msg="";
        // referencia a localização do diretorio onde estão os arquivos
        File file = new File("src/arquivos");
        
        // para cada arquivo a string é adicionado o seu nome seguida por um espaço
        File[] listaArq = file.listFiles();
        for (File file1 : listaArq) {
            msg += file1.getName() + "      ";
        }
        // mostra na GUI a lista de arquivos
        janela.escreverNoCMD(msg);
    }
    
    public void recebeArquivo(JSONObject json) {
        try {
            janela.escreverNoCMD("Obtendo hash...");

            // pega a string recebida e constroi o JSON a partir dela
            JSONParser parser = new JSONParser();
            String jsonString = in.readUTF();
            JSONObject jsonHashRecebido = (JSONObject) parser.parse(jsonString);

            int bytesRead, current = 0;

            byte[] arrayBytes = new byte[20971520];
                   
            // define o nome do arquivo a partir do JSON e seu caminho no diretorio
            String fileName = (String) json.get("file");
            String filePath = "src/arquivos/" + fileName;

            FileOutputStream fos = new FileOutputStream(filePath); 

            BufferedOutputStream bos = new BufferedOutputStream(fos); 

            janela.escreverNoCMD("Realizando download...");

            bytesRead = in.read(arrayBytes, 0, arrayBytes.length); 

            current = bytesRead; 

            do {
                Thread.sleep(1);
                bytesRead = in.read(arrayBytes, current, in.available());
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
            } while (in.available() != 0);

            bos.write(arrayBytes, 0, current); 
            bos.close();
			
            // se o hash do arquivo recebido for igual ao hash calculado o download
            // deu certo. Se não deu errado e o arquivo é deletado
            if (getHash(fileName).equals(jsonHashRecebido.get("hash"))) {
                janela.escreverNoCMD("Download concluido com sucesso!");
            } else {
                janela.escreverNoCMD("Falha no download, o arquivo sera removido do computador.");
                File file = new File("src/arquivos/" + fileName);
                file.delete();
            }

        } catch (IOException ex) {
            janela.escreverNoCMD("IOException na classe Archive: " + ex.getMessage());
        } catch (InterruptedException ex) {
            janela.escreverNoCMD("InterruptedException na classe Archive: " + ex.getMessage());
        } catch (ParseException ex) { 
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private void enviaHash(String nomeArquivo, String operation) throws IOException {
        File file = new File("src/arquivos/" + nomeArquivo);
        JSONObject json = new JSONObject();
        json.put("file", nomeArquivo);
        json.put("operation", operation);
        json.put("hash", "" + file.hashCode() + file.length());
        out.writeUTF(json.toString());
    }

    public void enviaArquivo(JSONObject json) {
        try {
            janela.escreverNoCMD("Enviando hash...");
                    
            // define o nome do arquivo e o nome do comando
            String fileName = (String) json.get("file");
            String commandName = (String) json.get("file");

            enviaHash(fileName, commandName); 

            File file = new File("src/arquivos/" + json.get("file")); 

            byte[] arrayBytes = new byte[(int) file.length()]; 

            FileInputStream fis = new FileInputStream(file); 

            BufferedInputStream bis = new BufferedInputStream(fis);

            bis.read(arrayBytes, 0, arrayBytes.length); 
			
            janela.escreverNoCMD("Enviando arquivo...");

            out.write(arrayBytes, 0, arrayBytes.length); 

            janela.escreverNoCMD("Aguardando status...");

            // recebe a mensagem do servidor e constroi o JSON
            JSONParser parser = new JSONParser();
            String jsonString = in.readUTF();
            JSONObject json2 = (JSONObject) parser.parse(jsonString);

            // pega os valores e mostra na GUI
            String fileName2 = (String) json2.get("file");
            String operation = (String) json2.get("operation");
            String status = (String) json2.get("status");

            janela.escreverNoCMD("Arquivo: "+fileName2+"      Operacao: "+operation+"      Status: "+status);
            		
        } catch (FileNotFoundException ex) {
            janela.escreverNoCMD("FileNotFoundException na classe Arquivo: " + ex.getMessage());
        } catch (IOException ex) {
            janela.escreverNoCMD("IOException na classe Arquivo: " + ex.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
