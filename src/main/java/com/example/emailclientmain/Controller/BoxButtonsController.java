package com.example.emailclientmain.Controller;

import com.example.emailclientmain.EmailClientMain;
import com.example.emailclientmain.Model.ClientModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BoxButtonsController {
    @FXML
    public Label emailLabel;

    @FXML
    private BorderPane root;

    @FXML
    public Stage stage;
    public void loadController(ClientModel clientModel, BorderPane root) {
        this.root = root;
        emailLabel.textProperty().bind(clientModel.emailAddressProperty());
    }

    @FXML
    public void loadPageController(ActionEvent actionEvent) throws IOException {

       stage = (Stage)root.getScene().getWindow();
       Button button = (Button)actionEvent.getSource();
       stage.setTitle(button.getText());

        FXMLLoader newPage;

       switch(button.getText()){
           case "SCRIVI":
               newPage = new FXMLLoader(EmailClientMain.class.getResource("scrivi.fxml"));
               root.setCenter(newPage.load());
               //Carica controller dell'invia
               break;
           case "IN ARRIVO":
           case "INVIATE":
           case "CESTINO":
               newPage = new FXMLLoader(EmailClientMain.class.getResource("listview.fxml"));
               root.setCenter(newPage.load());
               //Carica controller dell'invia
               break;

       }


    }

    @FXML
    public void loadIncomingEmailController(ActionEvent actionEvent) throws IOException {

        stage = (Stage)root.getScene().getWindow();
        Button button = (Button)actionEvent.getSource();
        stage.setTitle(button.getText());

        FXMLLoader write = new FXMLLoader(EmailClientMain.class.getResource("scrivi.fxml"));
        root.setCenter(write.load());
    }
}



