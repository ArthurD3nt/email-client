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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmailCellController extends ListCell<EmailBody> {

    @FXML
    private Label account;
    @FXML
    public BorderPane root;
    @FXML
    private Label text;

    @FXML
    private Label time;
    private ClientModel clientModel;
    private Parent showEmailView;
    private ShowEmailController showEmailController;

    private EmailBody email;

    public EmailCellController( Parent showEmailView, BorderPane root, ShowEmailController showEmailController) throws IOException {
        this.showEmailView = showEmailView;
        this.root = root;
        this.showEmailController = showEmailController;
        FXMLLoader emailCell = new FXMLLoader(EmailClientMain.class.getResource("cellEmail.fxml"));
        emailCell.setController(this);
        emailCell.setRoot(this);
        emailCell.load();

        itemProperty().addListener((obs, oldValue, newValue) -> {   
            this.email = newValue;

            if (newValue == null) {
                return;
            }
            
            account.setText(newValue.getSender());
            
            String text = (newValue.getSubject() + " - " + newValue.getText()).length() > 45
            ? (newValue.getSubject() + " - " + newValue.getText().replace("\n", ""))
            .substring(0, 45) + "..."
            : (newValue.getSubject() + " - " + newValue.getText().replace("\n", ""));
            this.text.setText(text);

            /* Data di oggi*/
            Date dateToday = new Date();
            Calendar calendarToday = Calendar.getInstance();
            calendarToday.setTime(dateToday);

            Date date = new Date(newValue.getTimestamp().getTime());
            Calendar dateEmail = Calendar.getInstance();
            dateEmail.setTime(date);

            if(dateEmail.get(Calendar.DAY_OF_MONTH) == calendarToday.get(Calendar.DAY_OF_MONTH)) {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm");
                String hourMinute = simpleFormat.format(dateEmail.getTime());
                this.time.setText(hourMinute);
            }
            else {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("MMMM");
                String strMonth = simpleFormat.format(dateEmail.getTime());
                this.time.setText(dateEmail.get(Calendar.DAY_OF_MONTH) + " " + strMonth);
            }

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        });
        
        setOnMouseClicked(mouseEvent -> {
            showSelectedEmail(mouseEvent, email);
        });
    }

    private void showSelectedEmail(MouseEvent mouseEvent, EmailBody email) {
        root.setCenter(showEmailView);
        this.showEmailController.showEmail(email);
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
