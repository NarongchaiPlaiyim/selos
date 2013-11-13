package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.BRMSActivityDAO;
import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.BRMSActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
@BRMS
public class BRMSAuditor implements SystemAuditor {
    @Inject
    @SELOS
    Logger log;
    @Inject
    BRMSActivityDAO brmsActivityDAO;

    @Inject
    public BRMSAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        brmsActivityDAO.persist(new BRMSActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, resultDate, linkKey));
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey) {
        brmsActivityDAO.persist(new BRMSActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, new Date(), linkKey));
    }

}
