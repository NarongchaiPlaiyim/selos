package com.clevel.selos.controller;

import com.clevel.selos.system.MessageProvider;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

@ViewScoped
@ManagedBean(name = "prescreenInitial")
public class PrescreenInitial {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

    public PrescreenInitial(){

    }

    @PostConstruct
    public void onCreation() {

    }
}
