package com.example.emailclientmain.Controller;

import com.example.emailclientmain.Email;
import com.example.emailclientmain.EmailClientMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class EmailCellController extends ListCell<Email> {

    public EmailCellController(){
        FXMLLoader emailCell = new FXMLLoader(EmailClientMain.class.getResource("cella-email.fxml"));
        emailCell.setController(this);
    }

}
