package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.security.UserDetail;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;

public abstract class BusinessControl {

    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;

    protected String getCurrentUserID(){
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetail != null)
            return userDetail.getUserName();
        return null;
    }

    protected User getCurrentUser(){
        try{
            return userDAO.findByUserName(getCurrentUserID());
        } catch(Exception ex){
            log.error("User Not found", ex);
            return null;
        }
    }

}
