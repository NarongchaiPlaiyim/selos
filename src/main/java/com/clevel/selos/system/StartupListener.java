package com.clevel.selos.system;

import com.clevel.selos.businesscontrol.master.ApplicationCacheLoader;
import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {
    @Inject
    @SELOS
    Logger log;
    @Inject
    ApplicationCacheLoader applicationCacheLoader;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.debug("contextInitialized.");
        applicationCacheLoader.loadCacheDB();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
