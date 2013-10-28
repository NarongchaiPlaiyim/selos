package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.ECMActivityDAO;
import com.clevel.selos.integration.ECM;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.ECMActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
@ECM
public class ECMAuditor implements SystemAuditor {
    @Inject
    Logger log;
    @Inject
    ECMActivityDAO ecmActivityDAO;

    @Inject
    public ECMAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        ecmActivityDAO.persist(new ECMActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, resultDate, linkKey));
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey) {
        ecmActivityDAO.persist(new ECMActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, new Date(), linkKey));
    }
}
