package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.SLOSActivityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.SLOSActivity;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

public class SLOSAuditor implements SystemAuditor, Serializable{
    @Inject
    @SELOS
    Logger log;
    @Inject
    SLOSActivityDAO slosActivityDAO;

    @Inject
    public SLOSAuditor(){
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        slosActivityDAO.persist(new SLOSActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, resultDate, linkKey));
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey) {
        slosActivityDAO.persist(new SLOSActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, new Date(), linkKey));
    }

}
