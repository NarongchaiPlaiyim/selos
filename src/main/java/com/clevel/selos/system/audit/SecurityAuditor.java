package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.SecurityActivityDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.SecurityActivity;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SecurityAuditor {
    @Inject
    Logger log;
    @Inject
    SecurityActivityDAO securityActivityDAO;

    @Inject
    public SecurityAuditor() {
    }

    public void add(String userName,String action,String actionDetail,ActionResult actionResult,String resultDetail) {
        securityActivityDAO.persist(new SecurityActivity(userName,action,actionDetail, actionResult,resultDetail, FacesUtil.getRequest().getRemoteAddr()));
    }

    public void addSucceed(String userName,String action,String actionDetail) {
        add(userName,action,actionDetail,ActionResult.SUCCEED,"");
    }

    public void addFailed(String userName,String action,String actionDetail,String resultDetail) {
        add(userName,action,actionDetail,ActionResult.FAILED,resultDetail);
    }

    public void addException(String userName,String action,String actionDetail,String resultDetail) {
        add(userName,action,actionDetail,ActionResult.EXCEPTION,resultDetail);
    }

}
