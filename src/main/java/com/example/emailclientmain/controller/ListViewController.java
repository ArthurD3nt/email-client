package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class ListViewController {

    @FXML
    public Label noEmails;

    @FXML
    public ListView listviewEmail;

    @FXML
    public BorderPane root;

    public ClientController clientController;

    @FXML
    public Button deleteAll;


    public void loadController(ClientModel clientModel, BorderPane root, Parent showEmailView, ShowEmailController ShowEmailController, ClientController clientController){
       
        this.root = root;
        this.clientController = clientController;

        clientModel.getCurrentEmails().addListener((ListChangeListener<EmailBody>)(value)-> {
            if(value.getList().size() > 0) {
                this.noEmails.setVisible(false);
                this.noEmails.setManaged(false);
                this.listviewEmail.setVisible(true);
                this.listviewEmail.setManaged(true);
                listviewEmail.itemsProperty().setValue(value.getList());
            }
            else{
                this.noEmails.setVisible(true);
                this.noEmails.setManaged(true);
                this.listviewEmail.setVisible(false);
                this.listviewEmail.setManaged(false);
            }

        });

        clientModel.getTextView().addListener((observableValue,oldValue,newValue)-> {
            if(newValue.equals("bin")) {
                this.deleteAll.setVisible(true);
                this.deleteAll.setManaged(true);
            }
            else {
                this.deleteAll.setVisible(false);
                this.deleteAll.setManaged(false);
            }
        });

        listviewEmail.setCellFactory((listView)-> {
            try {
                if(clientModel.getTextView().getValue().equals("bin")) {
                    this.deleteAll.setVisible(true);
                    this.deleteAll.setManaged(true);
                }
                else {
                    this.deleteAll.setVisible(false);
                    this.deleteAll.setManaged(false);
                }
                return new EmailCellController(showEmailView, root, ShowEmailController);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setDeleteAll(Boolean value) {
        this.deleteAll.setVisible(value);
        this.deleteAll.setManaged(value);
    }

    public void deleteAllEmails(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        this.clientController.deleteAllEmails();
    }
}
