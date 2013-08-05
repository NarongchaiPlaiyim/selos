package com.clevel.selos.integration.email.template;

import com.clevel.selos.integration.email.EmailService;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class SimpleEmail extends EmailService {
    @Inject
    Logger log;

    @Inject
    public SimpleEmail() {
    }

    @PostConstruct
    public void onCreation() {
        // todo: for testing
        emailTemplate = "d:/tmp/emailTemplates/simpleEmail.html";
    }

}
