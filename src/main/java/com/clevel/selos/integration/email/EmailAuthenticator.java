package com.clevel.selos.integration.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {
    private String userName;
    private String password;

    EmailAuthenticator() {
        super();
    }

    EmailAuthenticator(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}