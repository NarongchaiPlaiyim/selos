package com.clevel.selos.system.message;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Language;
import com.clevel.selos.util.FacesUtil;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class MessageProvider implements Message {
    @Inject
    @SELOS
    Logger log;
    protected String resource;
    protected ResourceBundle messageTh;
    protected ResourceBundle messageEn;

    protected ResourceBundle getBundle() {
        HttpSession httpSession = null;
        try {
            httpSession = FacesUtil.getSession(false);
        } catch (Exception e) {
            if (messageEn == null) {
                log.debug("Face session is null. load service resources.");
                messageEn = ResourceBundle.getBundle(resource, new Locale("en", "US"));
            }
            log.debug("Return service message resources. (En)");
            return messageEn;
        }
        if (Language.TH == httpSession.getAttribute("language")) {
            if (messageTh == null) {
                log.debug("Load resource: {} (Th)", resource);
                messageTh = ResourceBundle.getBundle(resource, new Locale("th", "TH"));
            }
            return messageTh;
        }
        if (messageEn == null) {
            log.debug("Load resource: {} (En)", resource);
            messageEn = ResourceBundle.getBundle(resource, new Locale("en", "US"));
        }
        return messageEn;
    }

    public void setLanguageTh() {
        log.debug("setLanguageTh.");
        FacesUtil.getSession(false).setAttribute("language", Language.TH);
    }

    public void setLanguageEn() {
        log.debug("setLanguageEn.");
        FacesUtil.getSession(false).setAttribute("language", Language.EN);
    }

    @SuppressWarnings("unchecked")
    public String get(String key, String... vars) {
        String text = get(key);
        Map valuesMap = new HashMap();
        int index = 0;
        for (String var : vars) {
            valuesMap.put("" + (index++), var);
        }
        return StrSubstitutor.replace(text, valuesMap);
    }
}
