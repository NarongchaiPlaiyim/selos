package com.clevel.selos.security;

import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;

public class SessionHandler implements Serializable,HttpSessionBindingListener {
    @Inject
    @SELOS
    Logger log;

    public void valueBound(HttpSessionBindingEvent event) {
        log.debug("valueBound:" + event.getName() + " session:" + event.getSession().getId() );

    }

    public void registerSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put( "sessionBindingListener", this  );
        log.debug( "registered sessionBindingListener"  );
    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        log.debug("valueUnBound:" + event.getName() + " session:" + event.getSession().getId() );
        // add you unlock code here:
        //clearLocksForSession( event.getSession().getId() );
    }
}