package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.DWHActivityDAO;
import com.clevel.selos.integration.DWH;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.DWHActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
@DWH
public class DWHAuditor implements SystemAuditor {
    @Inject
    Logger log;
    @Inject
    DWHActivityDAO dwhActivityDAO;

    @Inject
    public DWHAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        dwhActivityDAO.persist(new DWHActivity(userId,action,actionDesc,actionDate,actionResult,resultDesc,resultDate,linkKey));
    }
}
