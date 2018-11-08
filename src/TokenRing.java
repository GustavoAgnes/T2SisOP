import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenRing {

    public static void main(String[] args) throws IOException {
        String ip_port;
        int port;
        int t_token = 0;
        boolean token = false;
        String nickname;
        String myNickName = "Teste";
        MessageQueue queue;


        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            System.out.println("O ip é: "+ip);
        }

        UI interf;
        /* Le arquivo de configuração. */
        try ( BufferedReader inputFile = new BufferedReader(new FileReader("ring.cfg"))) {
            ip_port = inputFile.readLine();
            String aux[] = ip_port.split(":");
            port = Integer.parseInt(aux[1]);
            /* Lê apelido */
            nickname = inputFile.readLine();
                
            queue = new MessageQueue();
            
            interf = new UI(aux[0],aux[1], myNickName, queue, nickname);
            interf.setVisible(true);
            /* Lê tempo de espera com o token. Usado para fins de depuração. Em caso de 
            execução normal use valor 0. */
            t_token = Integer.parseInt(inputFile.readLine());
            
            /* Lê se a estação possui o token inicial. */
            token = Boolean.parseBoolean(inputFile.readLine());
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TokenRing.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
 
        MessageController controller = new MessageController(queue, ip_port, t_token, token, nickname, myNickName, interf);
        Thread thr_controller = new Thread(controller);
        Thread thr_receiver = new Thread(new MessageReceiver(queue, port, controller));
        
        thr_controller.start();
        thr_receiver.start();
        
        /* Neste ponto, a thread principal deve ficar aguarando o usuário entrar com o destinatário
         * e a mensagem a ser enviada. Destinatário e mensagem devem ser adicionados na fila de mensagens pendentes.
         * MessageQueue()
         *
         */
        
    }
    
}
