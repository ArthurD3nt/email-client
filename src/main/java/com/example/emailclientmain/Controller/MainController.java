package com.example.emailclientmain.Controller;

import com.example.emailclientmain.Client;
import com.example.emailclientmain.EmailClientMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    public TextField loginTextField;
    @FXML
    public BorderPane root;
    
    @FXML
    public ListView listviewEmail;

    private Stage stage;

    private Parent listview;

    private Client client;

    private BoxButtonsController buttonsController;
    @FXML
    public void loadController(ActionEvent actionEvent) {

        try{
            /*recupero finestra tramite id e setto un nuovo titolo*/
            stage = (Stage) root.getScene().getWindow();
            stage.setTitle("client mail");

            client = new Client(loginTextField.getText());

            /* Carico l'xml dei bottoni a sinistra*/
            FXMLLoader buttons = new FXMLLoader(EmailClientMain.class.getResource("box-buttons.fxml"));
            buttonsController = buttons.getController();
            root.setLeft(buttons.load());
            buttonsController.loadController(client);

            /* Carico l'xml delle email che starà al centro*/
            FXMLLoader listview = new FXMLLoader(EmailClientMain.class.getResource("listview.fxml"));
            this.listview = listview.load();
            root.setCenter(this.listview);


        }
        catch (IOException e){
            System.out.println(e);
        }



    }
}
