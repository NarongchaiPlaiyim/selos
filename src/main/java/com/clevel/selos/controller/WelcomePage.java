package com.clevel.selos.controller;

import com.clevel.selos.dao.ConfigDAO;
import com.clevel.selos.model.db.Config;
import com.clevel.selos.system.MessageProvider;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name="welcomePage")
public class WelcomePage implements Serializable {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;
    @Inject
    ConfigDAO configDAO;
    List<Config> configList;

    private Date now;

    public WelcomePage() {
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation.");
        now = new Date();
        reloadConfig();
    }

    public void reloadConfig() {
        configList = configDAO.findAll();
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }
}
