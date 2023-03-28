package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import javafx.fxml.FXML;
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
    public Button receivedButton;

    @FXML
    public Button sendButton;
    @FXML
    public Button binButton;
    @FXML
    public Button writeButton;

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
               setButtonCss("SCRIVI");
               root.setCenter(this.writeView);
               break;
           case "IN ARRIVO":
           default:
               setButtonCss("IN ARRIVO");
               root.setCenter(this.listView);
               clientModel.setTextView("received");
               break;
           case "INVIATE":
               setButtonCss("INVIATE");
               root.setCenter(this.listView);
               clientModel.setTextView("sent");
               break;
           case "CESTINO":
               setButtonCss("CESTINO");
               root.setCenter(this.listView);
               clientModel.setTextView("bin");
               break;
       }

    }

    public void loadPage(String button){
        switch(button){
            case "SCRIVI":
                setButtonCss("SCRIVI");
                root.setCenter(this.writeView);
                break;
            case "IN ARRIVO":
                setButtonCss("IN ARRIVO");
                root.setCenter(this.listView);
                clientModel.setTextView("received");
                break;
            case "INVIATE":
                setButtonCss("INVIATE");
                root.setCenter(this.listView);
                clientModel.setTextView("sent");
                break;
            case "CESTINO":
                setButtonCss("CESTINO");
                root.setCenter(this.listView);
                clientModel.setTextView("bin");
                break;
        }
    }

    public void setButtonCss(String button){
        switch(button){
            case "SCRIVI":
                this.writeButton.getStyleClass().add("button-selected");
                this.receivedButton.getStyleClass().removeAll("button-selected");
                this.binButton.getStyleClass().removeAll("button-selected");
                this.sendButton.getStyleClass().removeAll("button-selected");
                break;
            case "IN ARRIVO":
                this.writeButton.getStyleClass().removeAll("button-selected");
                this.receivedButton.getStyleClass().add("button-selected");
                this.binButton.getStyleClass().removeAll("button-selected");
                this.sendButton.getStyleClass().removeAll("button-selected");
                break;
            case "INVIATE":
                this.writeButton.getStyleClass().removeAll("button-selected");
                this.receivedButton.getStyleClass().removeAll("button-selected");
                this.binButton.getStyleClass().removeAll("button-selected");
                this.sendButton.getStyleClass().add("button-selected");
                break;
            case "CESTINO":
                this.writeButton.getStyleClass().removeAll("button-selected");
                this.receivedButton.getStyleClass().removeAll("button-selected");
                this.binButton.getStyleClass().add("button-selected");
                this.sendButton.getStyleClass().removeAll("button-selected");
                break;
        }

    }

}



