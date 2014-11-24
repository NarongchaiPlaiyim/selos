package com.clevel.selos.security;

import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Inject
    @SELOS
    Logger log;

    @Override
    public void sessionCreated(HttpSessionEvent event){
        log.debug("Session Created.");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Do here the job.
        log.debug("Session Destroyed.");
    }

}
