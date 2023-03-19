package com.example.emailclientmain.model;

import com.example.transmission.EmailBody;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Classe Client, conterrà la lista di mail che sarà il model
 */

public class ClientModel {

    private final ObservableList<EmailBody> currentEmails;
    private final ObservableList<EmailBody> inboxContent;
    private final ObservableList<EmailBody> sentContent;
    private final ObservableList<EmailBody> binContent;
    private final StringProperty emailAddress;
    private final StringProperty textView;

    public ClientModel(String emailAddress) {
        /*Nome del client che visualizzo*/
        this.emailAddress = new SimpleStringProperty(emailAddress);
        /*inboxContent è dove effettivamente arrivano le email, quando cambia questo cambia anche la grafica */
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.sentContent = FXCollections.observableList(new LinkedList<>());
        this.binContent = FXCollections.observableList(new LinkedList<>());
        /* Inizializzo currentEmails per visualizzare le email in arrivo*/
        this.currentEmails = FXCollections.observableList(new LinkedList<>());

        this.textView = new SimpleStringProperty("received");

        /*Inserisco un listener per cambiare currentEmails*/
      this.textViewProperty().addListener( (observable, oldValue, newValue) -> this.setCurrentEmails());

    }

    public ObservableList<EmailBody> getCurrentEmails() {
        return currentEmails;
    }

    public void setCurrentEmails() {
        this.currentEmails.clear();
        this.currentEmails.setAll( textView.getValue().equals("received") ? inboxContent :
                    textView.getValue().equals("sent") ? sentContent : binContent);

    }

    public void setInboxContent(ArrayList<EmailBody> inbox) {
        this.inboxContent.addAll(inbox);
        if(textView.getValue().equals("received")) {
          this.setCurrentEmails();
        }
    }

    public void setSentContent(ArrayList<EmailBody> sent) {
        this.sentContent.addAll(sent);
        if(textView.getValue().equals("sent")) {
            this.setCurrentEmails();
        }
    }
    public void setBinContent(ArrayList<EmailBody> bin) {
        this.binContent.addAll(bin);
        if(textView.getValue().equals("bin")) {
            this.setCurrentEmails();
        }
    }


    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    public void deleteEmail(EmailBody email) {
        inboxContent.remove(email);
    }

    public String getTextView() {
        return textView.get();
    }

    public StringProperty textViewProperty() {
        return textView;
    }

    public void setTextView(String textView) {
        this.textView.set(textView);
    }
}

