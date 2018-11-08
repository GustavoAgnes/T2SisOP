import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;


public class InterfaceController extends Application {



    public static Stage stage;
    private MessageQueue queue;
    @FXML
    private TextArea textArea;
    @FXML
    private Button enviar = new Button();

    @FXML
    public void initialize(MessageQueue queue) {
        this.queue = queue;
    }


    public void start(Stage stage) throws Exception {
        this.stage = stage; // initialize value of stage.
        Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //   enviar.setOnAction(new EventHandler<ActionEvent>() {

    public void handle(ActionEvent e) {
       // try {
            String txt = textArea.getText().trim();
        MessageQueue fila = new MessageQueue();
        try {
            fila.addMessage(txt);
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
            System.out.println("Adicionado a fila: "+ textArea.getText());
            textArea.setText("");
    }
}