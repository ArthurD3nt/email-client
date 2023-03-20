package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class WriteController {
    @FXML
    public TextField receivers;
    @FXML
    public TextField subject;
    @FXML
    public TextArea message;

    private ClientModel clientModel;

    private ClientController clientController;


    public void loadWriteController( ClientModel clientModel, ClientController clientController){
        this.clientModel = clientModel;
        this.clientController = clientController;
    }

    public void sendEmail(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String[] parts = receivers.getText().split(", ");
        ArrayList<String> receiversEmail = new ArrayList<>();

        for(String s : parts){
            receiversEmail.add(s);
        }

        EmailBody email = new EmailBody(this.clientModel.emailAddressProperty().getValue(),receiversEmail,subject.getText(),message.getText());

        this.clientController.sendEmail(email);
    }
}
