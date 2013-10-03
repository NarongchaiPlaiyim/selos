package com.clevel.selos.system.message;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.MissingResourceException;

@ApplicationScoped
@ExceptionMessage
@Named("exceptionMsg")
public class ExceptionMessageProvider extends MessageProvider implements ExceptionMapping {
    @Inject
    private Logger log;
    private static final String messageFile = "com.clevel.selos.exceptionMessages";

    @Inject
    public ExceptionMessageProvider() {
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
            message = getBundle().getString("exception."+key);
        } catch (MissingResourceException e) {
            log.error("Missing resource! (key: {})",key,e);
            message = "???";
        }
        return message;
    }
}
