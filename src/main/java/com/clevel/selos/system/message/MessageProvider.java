package com.clevel.selos.system.message;

import com.clevel.selos.model.Language;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class MessageProvider implements Message {
    @Inject
    private Logger log;
    @Inject
    HttpSession httpSession;
    protected String resource;
    protected ResourceBundle messageTh;
    protected ResourceBundle messageEn;

    protected ResourceBundle getBundle() {
        if (Language.TH == httpSession.getAttribute("language")) {
            if (messageTh == null) {
                log.debug("Load resource: {} (Th)",resource);
                messageTh = ResourceBundle.getBundle(resource, new Locale("th", "TH"));
            }
            return messageTh;
        }
        if (messageEn == null) {
            log.debug("Load resource: {} (En)",resource);
            messageEn = ResourceBundle.getBundle(resource, new Locale("en", "US"));
        }
        return messageEn;
    }

    public void setLanguageTh() {
        log.debug("setLanguageTh.");
        httpSession.setAttribute("language", Language.TH);
    }

    public void setLanguageEn() {
        log.debug("setLanguageEn.");
        httpSession.setAttribute("language", Language.EN);
    }

}
