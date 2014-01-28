package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class FacesUtil implements Serializable {
    private static Logger log = LoggerFactory.getLogger(FacesUtil.class);

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public static HttpSession getSession(boolean createNewSession) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(createNewSession);
    }

    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public static Flash getFlash() {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash();
    }

    // Getters -----------------------------------------------------------------------------------
    public static Object getSessionMapValue(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }

    // Setters -----------------------------------------------------------------------------------
    public static void setSessionMapValue(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
    }

    public static void redirect(String uriPath) {
        String contextPath = "";
        try {
            ExternalContext ec = getExternalContext();
            contextPath = ec.getRequestContextPath();
            String url = contextPath.concat(uriPath);
            log.debug("redirect to url: {}", url);
            ec.redirect(url);
        } catch (Exception e) {
            log.error("Exception while redirection! (contextPath: {}, uriPath: {})", contextPath, uriPath);
            log.error("", e);
        }
    }
}
