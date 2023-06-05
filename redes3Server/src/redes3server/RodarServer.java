package redes3server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONObject;

public class RodarServer extends Thread {
    
    // locais
        static boolean luz_guarita = false;
        static boolean ar_guarita = false;
        static boolean luz_estacionamento = false;
        static boolean luz_galpao_externo = false;
        static boolean luz_galpao_interno = false;
        static boolean luz_escritorios = false;
        static boolean ar_escritorios = false;
        static boolean luz_sala_reunioes = false;
        static boolean ar_sala_reunioes = false;
        
        public static boolean get(String argumento) {
            switch (argumento) {
                case "luz_guarita":
                    return luz_guarita;
                case "ar_guarita":
                    return ar_guarita;
                case "luz_estacionamento":
                    return luz_estacionamento;
                case "luz_galpao_externo":
                    return luz_galpao_externo;
                case "luz_galpao_interno":
                    return luz_galpao_interno;
                case "luz_escritorios":
                    return luz_escritorios;
                case "ar_escritorios":
                    return ar_escritorios;
                case "luz_sala_reunioes":
                    return luz_sala_reunioes;
                case "ar_sala_reunioes":
                    return ar_sala_reunioes;
            }
            // padrão, só pra ter, pois é impossivel cair aqui sem mexer no codigo
            return false;
        }
        
        public static void inverterBoolean() {
            
        }
        
        public static void set(String argumento, String status) {
            System.out.println("set status: "+status);
            if (status.equals("on")) {
                switch (argumento) {
                case "luz_guarita":
                    luz_guarita = true;
                case "ar_guarita":
                    ar_guarita = true;
                case "luz_estacionamento":
                    luz_estacionamento = true;
                case "luz_galpao_externo":
                    luz_galpao_externo = true;
                case "luz_galpao_interno":
                    luz_galpao_interno = true;
                case "luz_escritorios":
                    luz_escritorios = true;
                case "ar_escritorios":
                    ar_escritorios = true;
                case "luz_sala_reunioes":
                    luz_sala_reunioes = true;
                case "ar_sala_reunioes":
                    ar_sala_reunioes = true;
                }
            }
            
            if (status.equals("off")) {
                switch (argumento) {
                case "luz_guarita":
                    luz_guarita = false;
                case "ar_guarita":
                    ar_guarita = false;
                case "luz_estacionamento":
                    luz_estacionamento = false;
                case "luz_galpao_externo":
                    luz_galpao_externo = false;
                case "luz_galpao_interno":
                    luz_galpao_interno = false;
                case "luz_escritorios":
                    luz_escritorios = false;
                case "ar_escritorios":
                    ar_escritorios = false;
                case "luz_sala_reunioes":
                    luz_sala_reunioes = false;
                case "ar_sala_reunioes":
                    ar_sala_reunioes = false;
                }
            }
            
        }
        
    public static String formataJson(String argumento, String status) {
        JSONObject resposta = new JSONObject();
        resposta.put("locate", argumento);
        resposta.put("status", status);
        
        String response = resposta.toString();
        System.out.println("responseee: "+response);
        return response;
    }
    
    public void run() {
        
        int port = 50000;
        
        // Informa que o programa está em execução
        System.out.print("Starting UDPServer at port " + port +"...");
        
        try {
            // Criar porta de comunicação UDP
            DatagramSocket serverSock = new DatagramSocket(port);
            System.out.print(" [OK]");
            
            // Cria os buffers de comunicação
            // 65535 - 20 IP - 8 UDP = 65507
            byte[] rxData = new byte[65507];
            byte[] txData = new byte[65507];
            
            // Looping de comunicação
            while (true) {
                // Cria pacote UDP vazio
                DatagramPacket rxPkt = new DatagramPacket(rxData, rxData.length);
                
                // Aguarda o recebimento de uma mensagem
                System.out.println("\nWaiting messages...");
                serverSock.receive(rxPkt);
                
                // Trata a mensagem recebida
                // Obtém o IP e a porta do cliente
                InetAddress srcIPAddr = rxPkt.getAddress();
                int srcPort = rxPkt.getPort();
                
                // Obtém o payload da mensagem
                rxData = rxPkt.getData();
                
                String msg = new String(rxData, StandardCharsets.UTF_8);
                msg = msg.substring(0, rxPkt.getLength());
                
                // trata a mensagem: 
                
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                
                String comando = msg.substring((msg.indexOf("}") - 4), (msg.indexOf("}") - 1));
                String argumento = msg.substring((msg.indexOf("e") + 4), (msg.indexOf(",") - 1));
                String valor;
                
                String txtResposta = "vazio";
                String status = "vazio";
                if (comando.equals("get")) {
                    // ja trata direto, sem o valor
                    boolean resposta = get(argumento);
                        if (resposta == true) {
                            // converte para o formato JSON
                            status = "on";
                            txtResposta = formataJson(argumento, status);
                            //txtResposta = "sim";
                        }
                        else {
                            // converte para o JSON
                            status = "off";
                            txtResposta = formataJson(argumento, status);
                            //txtResposta = "nao";   
                        }
                }
                else {
                    if (comando.equals("set")) {
                        // pega o valor
                        valor = msg.substring((msg.indexOf(",") + 10), (msg.indexOf("}") - 17));
                        System.out.println("valorr: "+valor);
                        
                        // converte para o formato JSON
                        
                            if (valor.equals("off")) {
                                status = "off";
                                
                                // chama o metodo set
                                set(argumento, status);
                                
                                txtResposta = formataJson(argumento, status);
                            }
                            else {
                                if (valor.equals("on")) {
                                    status = "on";
                                    
                                    // chama o metodo set
                                    set(argumento, status);
                                    System.out.println("argumento1: "+argumento);
                                    System.out.println("status1: "+status);
                                    
                                    txtResposta = formataJson(argumento, status);
                                    System.out.println("txtresposta "+txtResposta);
                                }
                            }
                    }
                }
                
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                
                    System.out.println("txtReposta: "+txtResposta);
                
                // Imprime na tela a mensagem recebida
                System.out.println("Mensagem recebida:");
                System.out.println("\tIP origem: " + srcIPAddr);
                System.out.println("\tPorta origem: " + srcPort); 
                System.out.println("\tTamanho mensagem: " +rxPkt.getLength());
                System.out.println("\tMensagem: " + msg);
                
                // Cria mensagem de resposta do servidor
                //String txMsg = "ACK";
                txData = txtResposta.getBytes(StandardCharsets.UTF_8);
                
                // Cria o pacote de resposta do servidor para o cliente
                DatagramPacket txPkt = new DatagramPacket(
                        txData, txData.length, srcIPAddr, srcPort);
                
                // Envia a mensagem de resposta ao cliente
                System.out.println("Enviando a resposta para o cliente!");
                serverSock.send(txPkt);
            }
            
        } catch (SocketException ex) {
            System.err.println("\n\tSocket error: " + ex.getMessage());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("\n\tMessage error: " + ex.getMessage());
            System.exit(1);
        }
        
    }
    
}
