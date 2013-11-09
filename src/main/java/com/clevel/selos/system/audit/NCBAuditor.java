package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.NCBActivityDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.NCBActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
@NCB
public class NCBAuditor implements SystemAuditor {
    @Inject
    @SELOS
    Logger log;
    @Inject
    NCBActivityDAO ncbActivityDAO;

    @Inject
    public NCBAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        ncbActivityDAO.persist(new NCBActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, resultDate, linkKey));
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey) {
        ncbActivityDAO.persist(new NCBActivity(userId, action, actionDesc, actionDate, actionResult, resultDesc, new Date(), linkKey));
    }
}
