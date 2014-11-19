package com.clevel.selos.controller;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "index")
public class Index implements Serializable {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    UserDAO userDAO;

    private boolean checkInboxVisible;
    private User user;

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
        HttpSession session = FacesUtil.getSession(false);
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int roleId = userDetail.getRoleId();
        String page = Util.getCurrentPage();

        checkInboxVisible = showInbox(roleId);

        user = (User) session.getAttribute("user");
        if (user == null) {
            user = userDAO.findById(userDetail.getUserName());
            session = FacesUtil.getSession(false);
            session.setAttribute("user", user);
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
