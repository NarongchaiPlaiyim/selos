package com.clevel.selos.system;

import com.clevel.selos.model.Language;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@ApplicationScoped
@ManagedBean(name = "msg")
public class MessageProvider {
    @Inject
    private Logger log;
    private static final String RESOURCE = "com.clevel.selos.messages";
    private static ResourceBundle bundleTh;
    private static ResourceBundle bundleEn;
    @Inject
    HttpSession httpSession;

    @Inject
    public MessageProvider() {
    }

    public ResourceBundle getBundle() {
        if (Language.TH == httpSession.getAttribute("language")) {
            if (bundleTh == null) {
                bundleTh = ResourceBundle.getBundle(RESOURCE,new Locale("th","TH"));
            }
            return bundleTh;
        } else {
            if (bundleEn == null) {
                bundleEn = ResourceBundle.getBundle(RESOURCE,new Locale("en","US"));
            }
            return bundleEn;
        }
    }

    public String key(String key) {
        String message;

        try {
            message = getBundle().getString(key);
        } catch (MissingResourceException e) {
            log.error("Missing resource! (key: {})",key,e);
            message = "???";
        }

        return message;
    }

    public void setLanguageTh() {
        log.debug("setLanguageTh.");
        httpSession.setAttribute("language", Language.TH);
    }

    public void setLanguageEn() {
        log.debug("setLanguageEn.");
        httpSession.setAttribute("language",Language.EN);
    }
}
