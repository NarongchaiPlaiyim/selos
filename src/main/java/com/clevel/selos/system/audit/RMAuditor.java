package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.RMActivityDAO;
import com.clevel.selos.integration.RM;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.RMActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

@Stateless
@RM
public class RMAuditor implements SystemAuditor, Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    RMActivityDAO rmActivityDAO;

    @Inject
    public RMAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        rmActivityDAO.persist(new RMActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, resultDate, linkKey));
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey) {
        rmActivityDAO.persist(new RMActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, new Date(), linkKey));
    }
}
