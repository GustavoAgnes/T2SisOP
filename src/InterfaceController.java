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

    @Override
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
        //try {
            //String txt = textArea.getText().trim();
            //queue.AddMessage(txt);
            System.out.println("Adicionado a fila: "+ textArea.getText());
         //   textArea.setText("");
        //} catch (InterruptedException ex) {
        //    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

}