package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ShowEmailController {

    @FXML
    public TextField receivers;
    @FXML
    public TextField subject;
    @FXML
    public TextArea message;

    private EmailBody email;

    private ClientModel clientModel;

    private ClientController clientController;

    public void inizializeController(ClientModel clientModel, ClientController clientController) {
        this.clientController = clientController;
        this.clientModel = clientModel;
    }

    public void showEmail(EmailBody email) {
        this.email = email;
        System.out.println(email.getId());
        this.receivers.setText(email.getReceivers().toString().replace("[", "").replace("]",""));
        this.subject.setText(email.getSubject());
        this.message.setText(email.getText());
    }


    @FXML
    public void moveEmailToBin(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        System.out.println(this.email.getId());
        this.clientController.moveToBin(this.email.getId(), this.clientModel.emailAddressProperty().getValue());
    }
}
