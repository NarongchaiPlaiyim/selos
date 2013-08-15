package com.clevel.selos.system;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

@ApplicationScoped
@ManagedBean(name="app")
public class Application {
    @Inject
    Logger log;

    @Inject
    public Application() {
    }

    @PostConstruct
    public void onCreation() {
    }
}