package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ShowEmailController {

    @FXML
    public TextField receivers;
    @FXML
    public TextField subject;
    @FXML
    public TextArea message;

    public void inizializeController(ClientModel clientModel) {
    }

    public void showEmail(EmailBody newValue) {
        this.receivers.setText(newValue.getReceivers().toString());
        this.subject.setText(newValue.getSubject());
        this.message.setText(newValue.getText());
    }
    
}
