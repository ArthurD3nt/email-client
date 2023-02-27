package com.example.emailclientmain.Controller;

import com.example.emailclientmain.Client;
import com.example.emailclientmain.EmailClientMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class BoxButtonsController {
    @FXML
    public Label emailLabel;

    public void loadController(Client client) {

        emailLabel.textProperty().bind(client.emailAddressProperty());


    }
}
