package com.example.emailclientmain.controller;

import com.example.emailclientmain.EmailClientMain;
import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class EmailCellController extends ListCell<EmailBody> {

    @FXML
    private Label account;
    @FXML
    public BorderPane root;
    @FXML
    private Label text;
    private ClientModel clientModel;
    private Parent showEmailView;
    private ShowEmailController showEmailController;

    public EmailCellController(ClientModel clientModel, ListView listviewEmail, Parent showEmailView, BorderPane root, ShowEmailController showEmailController) throws IOException {
        this.showEmailView = showEmailView;
        this.root = root;
        this.showEmailController = showEmailController;
        FXMLLoader emailCell = new FXMLLoader(EmailClientMain.class.getResource("cella-email.fxml"));
        emailCell.setController(this);
        emailCell.setRoot(this);
        emailCell.load();

        itemProperty().addListener((obs, oldValue, newValue) -> {   
            // Empty cell
            if (newValue == null) {
                return;
            }
            
            account.setText(newValue.getSender());
            
            String text = (newValue.getSubject() + " - " + newValue.getText()).length() > 45
            ? (newValue.getSubject() + " - " + newValue.getText().replace("\n", ""))
            .substring(0, 45) + "..."
            : (newValue.getSubject() + " - " + newValue.getText().replace("\n", ""));
            this.text.setText(text);
            
            /*
             * if(newValue.getBin()){
             * if(!getStyleClass().contains("toRead")) getStyleClass().add("toRead");
             * }else {
             * getStyleClass().remove("toRead");
             * }
             */
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            
            listviewEmail.setOnMouseClicked(mouseEvent -> {
                showSelectedEmail(mouseEvent, newValue);
            });
        });
        

    }

    private void showSelectedEmail(MouseEvent mouseEvent, EmailBody email) {
        System.out.println("Cliccato");
        System.out.println(mouseEvent);
        System.out.println(email.toString());
        root.setCenter(showEmailView);
        showEmailController.showEmail(email);
    }
    

    @Override
    protected void updateItem(EmailBody item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
            setDisable(true);
            return;
        }
        setDisable(false);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

}
