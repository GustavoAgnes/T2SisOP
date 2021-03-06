import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageController implements Runnable{
    private MessageQueue queue;
    private InetAddress IPAddress;
    private int port;
    private Semaphore WaitForMessage;
    private String nickname;
    private int time_token;
    private Boolean token;
    private UI panel;
    private InterfaceController ic;
    private String myNickName;
    
    public MessageController(   MessageQueue q, 
                                String ip_port, 
                                int t_token,
                                Boolean t,
                                String n,
                                String myNickName,
                                InterfaceController ic)
                               // UI panel)
                                 throws UnknownHostException{
        
        queue = q;
        this.ic = ic;
        this.panel = panel;
        String aux[] = ip_port.split(":");
        
        IPAddress = InetAddress.getByName(aux[0]);
        port = Integer.parseInt(aux[1]);
        this.myNickName = myNickName;
        time_token = t_token;
        token = t;
        nickname = n;
        WaitForMessage = new Semaphore(0);
        
    }
    
    /** ReceiveMessage()
     *  Nesta função, vc deve decidir o que fazer com a mensagem recebida do vizinho da esquerda:
     *      Se for um token, é a sua chance de enviar uma mensagem de sua fila (queue);
     *      Se for uma mensagem de dados e se for para esta estação, apenas a exiba no console, senão, 
     * envie para seu vizinho da direita;
     *       Se for um ACK e se for para você, sua mensagem foi enviada com sucesso, passe o token para o vizinho da direita, senão, 
     * repasse o ACK para o seu vizinho da direita.
     */
    public void ReceivedMessage(String msg) throws InterruptedException{
        System.out.println("Metodo de Received Message");
        if (msg.trim().equals("4060")){
//            panel.setReceivedText("Token Received");
            System.out.println("Deveria ter chamado para adicionar token received");
            ic.setReceivedText("Token Received");
            if (!queue.queue.isEmpty()){
                sendMessage();
            }
            //queue.removeFirstElement();
            WaitForMessage.release();
            return;
        }
        String[] msg_splitted = msg.trim().split(";");
        System.out.println(msg);
        String msg_type = msg_splitted[0];
        String content_msg = msg_splitted[1];
        
        if (msg_type.equals("ACK")){
           // panel.setReceivedText("ACK received");
            try {
                if(content_msg.equals(myNickName))
                    queue.addMessage("4060");
                else
                    queue.addMessage(msg);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            queue.removeFirstElement();
            WaitForMessage.release();
            return;
        }
        String[] to_me = content_msg.split(":");
        String to_who = to_me[0];
       // panel.setReceivedText("Message Received");
        if (to_who.equals(myNickName)){
            queue.addMessage("ACK;"+to_me[1]);
        }else{
            queue.addMessage(msg);
        }
        queue.removeFirstElement();
        WaitForMessage.release();
    }
    
    public void sendMessage(){
        DatagramSocket clientSocket = null;
        byte[] sendData;
        
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        String msg = queue.queue.get(0);
        sendData = msg.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);         

        try {
            clientSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        queue.removeFirstElement();
    }
    
    
    @Override
    public void run() {
        DatagramSocket clientSocket = null;
        byte[] sendData;
        
        /* Cria socket para envio de mensagem */
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        while(true){

            /* Neste exemplo, considera-se que a estação sempre recebe o token 
               e o repassa para a próxima estação. */

            try {
                /* Espera time_token segundos para o envio do token. Isso é apenas para depuração,
                   durante execução real faça time_token = 0,*/    
                Thread.sleep(time_token*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(token == true){

                /* Converte string para array de bytes para envio pelo socket. */
                String msg = "4060"; /* Lembre-se do protocolo, "4060" é o token! */
                sendData = msg.getBytes();
            
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);         
                
                /* Realiza envio da mensagem. */
                try {
                    clientSocket.send(sendPacket);
                } catch (IOException ex) {
                    Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            /* A estação fica aguardando a ação gerada pela função ReceivedMessage(). */
            try {
                WaitForMessage.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
