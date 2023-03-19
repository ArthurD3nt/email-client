package com.example.transmission;

import java.util.ArrayList;

public class ConnectionBody extends BaseBody {
    private ArrayList<ArrayList<EmailBody>> emails;

    public ConnectionBody(String email, ArrayList<ArrayList<EmailBody>> emails) {
        super(email);
        this.emails = emails;
    }

    public ArrayList<ArrayList<EmailBody>> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<ArrayList<EmailBody>> emails) {
        this.emails = emails;
    }
}
