package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.SLOSActivityDAO;
import com.clevel.selos.model.ActionAudit;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.SLOSActivity;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

//@Stateless
public class SLOSAuditor implements Serializable{
    @Inject
    SLOSActivityDAO slosActivityDAO;

    @Inject
    public SLOSAuditor(){
    }

    public void add(int screenId, String userId, ActionAudit actionAudit, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc) {
        slosActivityDAO.persist(new SLOSActivity(screenId, userId, actionAudit, actionDesc, actionDate, actionResult, resultDesc, new Date()));
    }
}
