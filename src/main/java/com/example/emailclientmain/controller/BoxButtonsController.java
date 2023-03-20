package com.example.emailclientmain.controller;

import com.example.emailclientmain.EmailClientMain;
import com.example.emailclientmain.model.ClientModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BoxButtonsController {
    @FXML
    public Label emailLabel;

    @FXML
    private BorderPane root;

    @FXML
    public Stage stage;

    private ClientModel clientModel;

    private Parent listView;
    private Parent writeView;
    public void loadController(ClientModel clientModel, BorderPane root, Parent listView,Parent writeView) {
        this.root = root;
        emailLabel.textProperty().bind(clientModel.emailAddressProperty());
        this.clientModel = clientModel;
        this.listView = listView;
        this.writeView = writeView;
    }

    @FXML
    public void loadPageController(ActionEvent actionEvent) throws IOException {

       stage = (Stage)root.getScene().getWindow();
       Button button = (Button)actionEvent.getSource();
       stage.setTitle(button.getText());

       switch(button.getText()){
           case "SCRIVI":
               root.setCenter(this.writeView);
               break;
           case "IN ARRIVO":
               root.setCenter(this.listView);
               clientModel.setTextView("received");
               break;
           case "INVIATE":
               root.setCenter(this.listView);
               clientModel.setTextView("sent");
               break;
           case "CESTINO":
               root.setCenter(this.listView);
               clientModel.setTextView("bin");
               break;
       }

    }

}



