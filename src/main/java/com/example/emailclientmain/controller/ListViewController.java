package com.example.emailclientmain.controller;

import com.example.emailclientmain.EmailClientMain;
import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ListViewController {
    @FXML
    public ListView listviewEmail;

    @FXML
    public BorderPane root;


    public void loadController(ClientModel clientModel, BorderPane root){

        this.root = root;

        clientModel.getCurrentEmails().addListener((ListChangeListener<EmailBody>)(value)-> {
            listviewEmail.itemsProperty().setValue(value.getList());
        });

        listviewEmail.setOnMouseClicked(mouseEvent -> {
            try {
                showSelectedEmail(mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        listviewEmail.setCellFactory((listView)-> {
            try {
                return new EmailCellController(clientModel, listviewEmail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void showSelectedEmail(MouseEvent mouseEvent) throws IOException {
        FXMLLoader showEmail = new FXMLLoader(EmailClientMain.class.getResource("showEmail.fxml"));
        root.setCenter(showEmail.load());
    }


}
