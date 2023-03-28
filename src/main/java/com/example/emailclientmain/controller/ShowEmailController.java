package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    @FXML
    public Button replyToAllButton;

    @FXML
    public Button forwardButton;

    @FXML
    public Button replyButton;


    private EmailBody email;

    private ClientModel clientModel;

    private ClientController clientController;

    private Parent writeView;

    private Parent listView;

    private WriteController writeController;

    private BorderPane root;

    private String oldView;


    public void initializeController(ClientModel clientModel, ClientController clientController, Parent writeView, WriteController writeController, BorderPane root, Parent listView) {
        this.clientController = clientController;
        this.clientModel = clientModel;
        this.writeView = writeView;
        this.writeController = writeController;
        this.root = root;
        this.listView = listView;
    }

    public void showEmail(EmailBody email) {
        this.email = email;
        this.sender.setText(email.getSender());
        this.receivers.setText(email.getReceivers().toString().replace("[", "").replace("]",""));
        this.subject.setText(email.getSubject());
        this.message.setText(email.getText());
        this.oldView = this.clientModel.getTextView().getValue();


        if(clientModel.getTextView().getValue().equals("bin")){
            this.forwardButton.setVisible(false);
            this.replyButton.setVisible(false);
            this.replyToAllButton.setVisible(false);
        }
        else {
            this.forwardButton.setVisible(true);
            this.replyButton.setVisible(true);
            this.replyToAllButton.setVisible(true);
        }
    }


    @FXML
    public void moveEmailToBin(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        this.clientController.moveToBin(this.email.getId(), this.clientModel.emailAddressProperty().getValue(), clientModel.getTextView().getValue());
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

    @FXML
    public void forward(ActionEvent actionEvent) {

        this.writeController.subject.setText(this.email.getSubject());
        this.writeController.message.setText("\n\n---------- Forwarded message ---------\nFrom: "  +
                this.email.getSender() + "\n\n" + this.email.getText());
        root.setCenter(this.writeView);

    }

    public void closeShowEmail(ActionEvent actionEvent) {

        this.root.setCenter(this.listView);
        this.clientModel.setTextView(this.oldView);
    }
}
