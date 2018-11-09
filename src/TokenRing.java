import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;

import static javafx.application.Application.launch;

public class TokenRing {

    Parent root = FXMLLoader.load(getClass().getResource("interface.fxml"));
    static InterfaceController ic = new InterfaceController();

    public TokenRing() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        String ip_port;
        int port;
        int t_token = 0;
        boolean token = false;
        String nickname;
        String myNickName = "Teste";
        MessageQueue queue;



       // UI interf;
        /* Le arquivo de configuração. */
        try (BufferedReader inputFile = new BufferedReader(new FileReader("ring.cfg"))) {
            ip_port = inputFile.readLine();
            String aux[] = ip_port.split(":");
            //System.out.println("Imprimindo ip_port: "+ip_port.toString());





            ic.launch(InterfaceController.class);
            //ic.testePrint();
            //javafx.application.Application.launch(InterfaceController.class);


            //ic.setNickname("nickname teste");
            //System.out.println("Retorna porta(supostamente): "+ic.getPort());
            port = Integer.parseInt(aux[1]);
            /* Lê apelido */
            nickname = inputFile.readLine();

            queue = new MessageQueue();
          //  interf = new UI(aux[0], aux[1], myNickName, queue, nickname);
          //  interf.setVisible(true);
            t_token = Integer.parseInt(inputFile.readLine());

            /* Lê se a estação possui o token inicial. */
            token = Boolean.parseBoolean(inputFile.readLine());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TokenRing.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

       // MessageController controller = new MessageController(queue, ip_port, t_token, token, nickname, myNickName, interf);
        MessageController controller = new MessageController(queue, ip_port, t_token, token, nickname, myNickName, ic);
        Thread thr_controller = new Thread(controller);
        Thread thr_receiver = new Thread(new MessageReceiver(queue, port, controller));

        thr_controller.start();
        thr_receiver.start();


        //

        /* Neste ponto, a thread principal deve ficar aguarando o usuário entrar com o destinatário
         * e a mensagem a ser enviada. Destinatário e mensagem devem ser adicionados na fila de mensagens pendentes.
         * MessageQueue()
         *
         */

    }

}
