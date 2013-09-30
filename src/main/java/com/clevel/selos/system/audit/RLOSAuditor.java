package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.RLOSActivityDAO;
import com.clevel.selos.integration.RLOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.ROLSActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
@RLOS
public class RLOSAuditor implements SystemAuditor {
    @Inject
    Logger log;
    @Inject
    RLOSActivityDAO RLOSActivityDAO;

    @Inject
    public RLOSAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        RLOSActivityDAO.persist(new ROLSActivity(userId,action,actionDesc,actionDate,actionResult,resultDesc,resultDate,linkKey));
    }
}
