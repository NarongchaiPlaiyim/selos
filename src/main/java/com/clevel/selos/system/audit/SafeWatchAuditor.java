package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.SafeWatchActivityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.SafeWatch;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.SafeWatchActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

@Stateless
@SafeWatch
public class SafeWatchAuditor implements SystemAuditor, Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    SafeWatchActivityDAO safeWatchActivityDAO;

    @Inject
    public SafeWatchAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        safeWatchActivityDAO.persist(new SafeWatchActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, resultDate, linkKey));
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey) {
        safeWatchActivityDAO.persist(new SafeWatchActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, new Date(), linkKey));
    }
}
