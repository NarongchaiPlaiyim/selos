package com.clevel.selos.businesscontrol;

import com.clevel.selos.security.UserDetail;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;

public abstract class BusinessControl {

    @Inject
    Logger log;

    protected String getCurrentUserID(){
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetail != null)
            return userDetail.getUserName();
        return null;
    }

}
