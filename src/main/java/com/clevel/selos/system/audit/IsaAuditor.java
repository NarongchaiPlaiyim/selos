package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.IsaActivityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.IsaActivity;
import com.clevel.selos.model.db.master.User;
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

    private void add(String userId, String action, String actionDesc, ActionResult actionResult, String resultDesc, User modifyBy, String oldData, String newData) {
        final IsaActivity model = new IsaActivity(userId, action, actionDesc, DateTime.now().toDate(), actionResult, resultDesc, FacesUtil.getRequest().getRemoteAddr(), DateTime.now().toDate(), modifyBy, oldData, newData);
        isaActivityDAO.persist(model);
    }

    public void audit(String userId, String action, String actionDetail, ActionResult actionResult, String resultDetail, User modifyBy, String oldData, String newData){
        if(ActionResult.SUCCESS.equals(actionResult)){
            add(userId, action, actionDetail, ActionResult.SUCCESS, "", modifyBy, oldData, newData);
        } else {
            add(userId, action, actionDetail, actionResult, resultDetail, modifyBy, oldData, newData);
        }
    }
}
