package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;

public class ShowEmailController {

    @FXML
    public TextField receivers;
    @FXML
    public TextField subject;
    @FXML
    public TextArea message;

    @FXML
    public TextField sender;

    private EmailBody email;

    private ClientModel clientModel;

    private ClientController clientController;

    private Parent writeView;

    private WriteController writeController;

    private BorderPane root;


    public void inizializeController(ClientModel clientModel, ClientController clientController, Parent writeView, WriteController writeController, BorderPane root) {
        this.clientController = clientController;
        this.clientModel = clientModel;
        this.writeView = writeView;
        this.writeController = writeController;
        this.root = root;
    }

    public void showEmail(EmailBody email) {
        this.email = email;
        this.sender.setText(email.getSender());
        this.receivers.setText(email.getReceivers().toString().replace("[", "").replace("]",""));
        this.subject.setText(email.getSubject());
        this.message.setText(email.getText());
    }


    @FXML
    public void moveEmailToBin(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        this.clientController.moveToBin(this.email.getId(), this.clientModel.emailAddressProperty().getValue());
    }

    @FXML
    public void reply(ActionEvent actionEvent) {
        this.writeController.receivers.setText(this.email.getSender());
        this.writeController.subject.setText("RE: " + this.email.getSubject());
        this.writeController.message.setText("\n---------------------------\n" + this.email.getText());
        root.setCenter(this.writeView);
    }

    @FXML
    public void replyToAll(ActionEvent actionEvent) {
        String allReceivers;
        ArrayList<String> receiversCopy = (ArrayList<String>) this.email.getReceivers();
        Boolean result = receiversCopy.contains(this.clientModel.emailAddressProperty().getValue());
        if(result){
            receiversCopy.remove(this.clientModel.emailAddressProperty().getValue());
        }
        allReceivers = this.email.getSender();

        for(int i = 0; i < receiversCopy.size(); i++){
             allReceivers += ", ";
             allReceivers += receiversCopy.get(i);
        }
        this.writeController.receivers.setText(allReceivers);
        this.writeController.subject.setText(this.email.getSubject().contains("RE") ? this.email.getSubject() : "RE: " + this.email.getSubject());
        this.writeController.message.setText("\n---------------------------\n" + this.email.getText());
        root.setCenter(this.writeView);

    }
}
