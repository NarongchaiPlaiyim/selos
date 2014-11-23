package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.SLOSActivityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionAudit;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.SLOSActivity;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;

@Stateless
public class SLOSAuditor implements Serializable{
    @Inject
    @SELOS
    Logger log;
    @Inject
    SLOSActivityDAO slosActivityDAO;

    @Inject
    public SLOSAuditor(){
    }

    public void add(int screenId, String userId, ActionAudit actionAudit, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc) {
        HttpSession session = FacesUtil.getSession(false);
        slosActivityDAO.persist(new SLOSActivity(screenId, userId, Util.parseString(session.getAttribute("appNumber"), ""), actionAudit, actionDesc, actionDate, actionResult, resultDesc, new Date()));
    }
}
