package com.example.emailclientmain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Email {

    private String sender;
    private List<String> receivers;
    private String subject;
    private String text;

    private Date datetime;

    private Email() {}

    public Email(String sender, List<String> receivers, String subject, String text) {
        this.sender = sender;
        this.subject = subject;
        this.text = text;
        this.receivers = new ArrayList<>(receivers);
    }

    public String getSender() {
        return sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }


    @Override
    public String toString() {
        return String.join(" - ", List.of(this.sender,this.subject));
    }
}
