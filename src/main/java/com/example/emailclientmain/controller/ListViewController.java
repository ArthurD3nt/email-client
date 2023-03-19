package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;

public class ListViewController {
    @FXML
    public ListView listviewEmail;

    public void loadController(ClientModel clientModel){

        clientModel.getCurrentEmails().addListener((ListChangeListener<EmailBody>)(value)-> {
            listviewEmail.itemsProperty().setValue((ObservableList<EmailBody>) value.getList());
        });

        listviewEmail.setCellFactory((listView)-> {
            try {
                return new EmailCellController(clientModel, listviewEmail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
