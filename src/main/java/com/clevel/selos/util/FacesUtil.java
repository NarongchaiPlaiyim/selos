package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clevel.selos.model.Language;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FacesUtil implements Serializable {
    private static final long serialVersionUID = 553394611368789880L;
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
    public static final String getParameter(String name) {
		Map<String,String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (paramMap == null)
			return null;
		return paramMap.get(name);
	}
    @SuppressWarnings("unchecked")
	public static final Map<String,Object> getParamMapFromFlash(String name) {
    	Object obj = getFlash().get(name);
    	if (obj == null || !(obj instanceof Map))
    		return new HashMap<String, Object>();
    	else
    		return (HashMap<String, Object>) obj;
    }
    public static final Language getLanguage() {
    	Language lang = (Language) FacesUtil.getSession(false).getAttribute("language");
    	if (lang == null)
    		return Language.EN;
    	else
    		return lang;
    }
}
