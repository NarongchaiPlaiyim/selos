package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.BPMActivityDAO;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.BPMActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
@BPM
public class BPMAuditor implements SystemAuditor {
    @Inject
    Logger log;
    @Inject
    BPMActivityDAO bpmActivityDAO;

    @Inject
    public BPMAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        bpmActivityDAO.persist(new BPMActivity(userId,action,actionDesc,actionDate,actionResult,resultDesc,resultDate,linkKey));
    }
}
