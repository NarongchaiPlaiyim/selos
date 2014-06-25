package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.IsaActivityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.IsaActivity;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

@Stateless
public class IsaAuditor implements Serializable {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private IsaActivityDAO isaActivityDAO;
    @Inject
    public IsaAuditor() {

    }



    private void add(String userName, String action, String actionDetail, ActionResult actionResult, String resultDetail) {
        isaActivityDAO.persist(new IsaActivity(userName, action, actionDetail, DateTime.now().toDate(), actionResult, resultDetail, FacesUtil.getRequest().getRemoteAddr()));
    }

    public void addSucceed(String userName, String action, String actionDetail) {
        add(userName, action, actionDetail, ActionResult.SUCCESS, "");
    }

    public void addFailed(String userName, String action, String actionDetail, String resultDetail) {
        add(userName, action, actionDetail, ActionResult.FAILED, resultDetail);
    }

    public void addException(String userName, String action, String actionDetail, String resultDetail) {
        add(userName, action, actionDetail, ActionResult.EXCEPTION, resultDetail);
    }
}
