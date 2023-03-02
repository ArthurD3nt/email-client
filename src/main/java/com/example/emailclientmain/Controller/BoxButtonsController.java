package com.example.emailclientmain.Controller;


import java.io.IOException;

import com.example.emailclientmain.Client;
import com.example.emailclientmain.EmailClientMain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class BoxButtonsController {
    @FXML
    public Label emailLabel;

    @FXML
    public BorderPane root;
    
    @FXML
    public ListView listviewEmail;

    @FXML
    public Stage stage;

    public void loadController(Client client) {
        emailLabel.textProperty().bind(client.emailAddressProperty());
    }

    public void loadWriteController(ActionEvent actionEvent){

    
        /*recupero finestra tramite id e setto un nuovo titolo*/
        stage = (Stage) root.getScene().getWindow();
        stage.setTitle("Scrivi un'email");
      
        FXMLLoader write = new FXMLLoader(EmailClientMain.class.getResource("write.fxml"));
        try {
            root.setCenter(write.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    

    }
}
