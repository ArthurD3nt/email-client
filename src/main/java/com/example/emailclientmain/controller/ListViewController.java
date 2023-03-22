package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ListViewController {
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

            if(clientModel.getTextView().equals("bin")) {
                this.deleteAll.setVisible(true);
                this.deleteAll.setManaged(true);
            }
            else {
                this.deleteAll.setVisible(false);
                this.deleteAll.setManaged(false);
            }

            listviewEmail.itemsProperty().setValue(value.getList());
        });


        listviewEmail.setCellFactory((listView)-> {
            try {
                this.deleteAll.setVisible(true);
                return new EmailCellController(clientModel, listviewEmail, showEmailView, root, ShowEmailController);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



    public void deleteAllEmails(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        this.clientController.deleteAllEmails();
    }
}
