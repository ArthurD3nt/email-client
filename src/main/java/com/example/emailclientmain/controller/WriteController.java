package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    public void sendEmail(ActionEvent actionEvent){
         String[] parts = receivers.getText().split(", ");
         ArrayList<String> receiversEmail = new ArrayList<>();

         for(String s : parts){
             if(!s.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")){
                 Alert.AlertType alertType = Alert.AlertType.ERROR;
                 Alert alert = new Alert(alertType, "Email: "+ s +" not valid");
                 alert.showAndWait();
                 return;
             }
             receiversEmail.add(s);
         }

         EmailBody email = new EmailBody(this.clientModel.emailAddressProperty().getValue(),receiversEmail,subject.getText(),message.getText());

         this.clientController.sendEmail(email);

         this.message.setText("");
         this.subject.setText("");
         this.receivers.setText("");

    }

}
