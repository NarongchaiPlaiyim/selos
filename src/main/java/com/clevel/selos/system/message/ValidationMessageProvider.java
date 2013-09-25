package com.clevel.selos.system.message;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

@ApplicationScoped
@ValidationMessage
@Named("validationMsg")
public class ValidationMessageProvider extends MessageProvider {
    @Inject
    private Logger log;
    private static final String messageFile = "com.clevel.selos.validationMessages";

    @Inject
    public ValidationMessageProvider() {
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation.");
        resource = messageFile;
    }

    @Override
    public String get(String key) {
        String message;
        try {
            message = getBundle().getString("validation."+key);
        } catch (MissingResourceException e) {
            log.error("Missing resource! (key: {})",key,e);
            message = "???";
        }
        return message;
    }
}
