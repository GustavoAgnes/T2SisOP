import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InterfaceController extends Application {


    public static Stage stage;
    //  private MessageQueue queue;
    private MessageQueue fila = new MessageQueue();
    @FXML
    private TextArea textArea = new TextArea();
    @FXML
    private TextArea textArea1 = new TextArea();
    //@FXML
    //  private Button enviar = new Button();
    @FXML
    private Text ip = new Text();
    @FXML
    private Text ipDestino = new Text();
    @FXML
    private Text porta = new Text();
    @FXML
    private Text myNickName = new Text();
    @FXML
    private TextArea hiddenText;

    /*
    public InterfaceController(String ip, String door, String myNickName, MessageQueue queue, String nickNameO){
        this.queue = queue;
        this.ip.setText("Ip: "+ip);
        this.door.setText("Port: "+door);
        this.nickName.setText("My Nickname: "+myNickName);
        this.nickNameO = nickNameO;
    }
    */
    @FXML
    public void initialize() {
        //   ip.setText(queue.getIp());
        try {
            ip.setText(fila.getIp());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            porta.setText(getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ipDestino.setText(getIpDestino());
        } catch (IOException e) {
            e.printStackTrace();
        }

        hiddenText.textProperty().addListener((observableValue, s, s2) -> {
        System.out.println("teste");
        });
        System.out.println("testeInitialize");
    }


    public void start(Stage stage) throws Exception {
        this.stage = stage; // initialize value of stage.
        Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        System.out.println("teste Start");
        while(true){
            textArea1.setText(textArea.getText().toString()+"texto");
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent ke){
        System.out.println("Key Pressed: " + ke.getCode());
    }

    //   enviar.setOnAction(new EventHandler<ActionEvent>() {

    public void handle(ActionEvent e) throws InterruptedException {
        // try {
        String txt = textArea.getText().trim();
        // MessageQueue fila = new MessageQueue();
        try {
            fila.addMessage(txt);
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
        System.out.println("Adicionado a fila: " + textArea.getText());
        fila.addMessage(txt);
        textArea.setText("");
    }


    public String getPort() throws IOException {
        BufferedReader inputFile = new BufferedReader(new FileReader("ring.cfg"));
        String ip_port = inputFile.readLine();
        String aux[] = ip_port.split(":");
        String port = aux[1];
        /* Lê apelido */
        return port;
    }

    public String getIpDestino() throws IOException {
        BufferedReader inputFile = new BufferedReader(new FileReader("ring.cfg"));
        String ip_port = inputFile.readLine();
        String aux[] = ip_port.split(":");
        String port = aux[0];
        /* Lê apelido */
        return port;
    }

    public void setReceivedText(String txt) {
        System.out.println("Chamou método de texto recebido");
//        textArea1.setText(this.textArea1.getText() + "\n" + txt);
//        textArea1.setText(this.hiddenText.getText() + "\n" +txt);
    }


    public void setMyNickName(String n){
        myNickName.setText(n);
        System.out.println("Chamou metodo");
    }

    public void testePrint(){
        System.out.println("Teste print");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}