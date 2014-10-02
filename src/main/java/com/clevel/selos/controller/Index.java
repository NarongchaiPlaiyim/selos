package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "index")
public class Index implements Serializable {
    @Inject
    @SELOS
    private Logger log;

    private boolean checkInboxVisible;

    public Index(){

    }

    public void preRender(){
        log.debug("preRender ::: setSession ");

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int roleId = userDetail.getRoleId();
        String page = Util.getCurrentPage();

        if(roleId == RoleValue.VIEWER.id() && !page.equals("generic_search.jsf")){
            FacesUtil.redirect("/site/generic_search.jsf");
        }else{
            FacesUtil.redirect("/site/inbox.jsf");
        }
        return;
    }

    @PostConstruct
    public void onCreation(){
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int roleId = userDetail.getRoleId();
        String page = Util.getCurrentPage();

        checkInboxVisible = showInbox(roleId);
    }

    private boolean showInbox(int roleId){
        boolean showInbox = true;
        if(roleId == RoleValue.VIEWER.id()){
            showInbox = false;
        }

        return showInbox;
    }

    public boolean isCheckInboxVisible() {
        return checkInboxVisible;
    }

    public void setCheckInboxVisible(boolean checkInboxVisible) {
        this.checkInboxVisible = checkInboxVisible;
    }
}
