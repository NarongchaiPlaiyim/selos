package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.SecurityActivityDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.SecurityActivity;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class SecurityAuditor {
    @Inject
    Logger log;
    @Inject
    SecurityActivityDAO securityActivityDAO;
    @Inject
    UserDAO userDAO;

    @Inject
    public SecurityAuditor() {
    }

    public void add(String userName,String action,String actionDetail,Date actionDate,ActionResult actionResult,String resultDetail) {
        securityActivityDAO.persist(new SecurityActivity(userName,action,actionDetail,actionDate, actionResult,resultDetail, FacesUtil.getRequest().getRemoteAddr()));
    }

    public void addSucceed(String userName,String action,String actionDetail,Date actionDate) {
        User user = userDAO.findById(userName);
        user.setLastLogon(actionDate);
        user.setLastIP(FacesUtil.getRequest().getRemoteAddr());
        userDAO.persist(user);
        add(userName,action,actionDetail,actionDate,ActionResult.SUCCESS,"");
    }

    public void addFailed(String userName,String action,String actionDetail,String resultDetail) {
        add(userName,action,actionDetail,new Date(),ActionResult.FAILED,resultDetail);
    }

    public void addException(String userName,String action,String actionDetail,String resultDetail) {
        add(userName,action,actionDetail,new Date(),ActionResult.EXCEPTION,resultDetail);
    }

}
