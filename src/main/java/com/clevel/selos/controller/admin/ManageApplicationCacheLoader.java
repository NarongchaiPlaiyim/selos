package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.master.ApplicationCacheLoader;
import com.clevel.selos.integration.ADMIN;
import org.slf4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name="manageApplicationCacheLoader")
public class ManageApplicationCacheLoader implements Serializable{

    @Inject
    @ADMIN
    private Logger log;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    public ManageApplicationCacheLoader(){}

}
