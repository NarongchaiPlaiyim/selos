package com.clevel.selos.system.audit;

import com.clevel.selos.dao.audit.EmailActivityDAO;
import com.clevel.selos.integration.Email;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.audit.EmailActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
@Email
public class EmailAuditor implements SystemAuditor {
    @Inject
    Logger log;
    @Inject
    EmailActivityDAO emailActivityDAO;

    @Inject
    public EmailAuditor() {
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, Date resultDate, String linkKey) {
        emailActivityDAO.persist(new EmailActivity(userId,action,actionDesc,actionDate,actionResult,resultDesc,resultDate,linkKey));
    }

    @Override
    public void add(String userId, String action, String actionDesc, Date actionDate, ActionResult actionResult, String resultDesc, String linkKey) {
        emailActivityDAO.persist(new EmailActivity(userId,action,actionDesc,actionDate,actionResult,resultDesc,new Date(),linkKey));
    }
}
