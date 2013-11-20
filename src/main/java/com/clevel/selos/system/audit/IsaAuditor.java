package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.IsaActivityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ManageUserAction;
import com.clevel.selos.model.db.audit.IsaActivity;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

@Stateless
public class IsaAuditor implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject
    IsaActivityDAO isaActivityDAO;

    @Inject
    public IsaAuditor() {
    }

    public void add(String userName, ManageUserAction action, String actionDetail, ActionResult actionResult, String resultDetail) {
        isaActivityDAO.persist(new IsaActivity(userName, action.name(), actionDetail, actionResult, resultDetail, FacesUtil.getRequest().getRemoteAddr()));
    }

    public void addSucceed(String userName, ManageUserAction action, String actionDetail) {
        add(userName, action, actionDetail, ActionResult.SUCCESS, "");
    }

    public void addFailed(String userName, ManageUserAction action, String actionDetail, String resultDetail) {
        add(userName, action, actionDetail, ActionResult.FAILED, resultDetail);
    }

    public void addException(String userName, ManageUserAction action, String actionDetail, String resultDetail) {
        add(userName, action, actionDetail, ActionResult.EXCEPTION, resultDetail);
    }
}
