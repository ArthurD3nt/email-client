package com.example.emailclientmain.Model;

import com.example.emailclientmain.Email;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Classe Client, conterrà la lista di mail che sarà il model
 */

public class ClientModel {
    private final ListProperty<Email> inbox;
    private final ObservableList<Email> inboxContent;
    private final StringProperty emailAddress;

    public ClientModel(String emailAddress) {
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>();
        this.inbox.set(inboxContent);
        this.emailAddress = new SimpleStringProperty(emailAddress);
    }

    public ListProperty<Email> inboxProperty() {
        return inbox;
    }

    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    public void deleteEmail(Email email) {
        inboxContent.remove(email);
    }
}

