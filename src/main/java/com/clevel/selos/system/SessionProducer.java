package com.clevel.selos.system;

import com.clevel.selos.util.FacesUtil;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionProducer {
    @Produces
    HttpSession getSession() {
         return FacesUtil.getRequest().getSession(false);
    }
}
