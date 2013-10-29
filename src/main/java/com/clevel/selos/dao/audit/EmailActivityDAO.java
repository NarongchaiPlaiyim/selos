package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.EmailActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class EmailActivityDAO extends GenericDAO<EmailActivity, Long> {
    @Inject
    private Logger log;

    @Inject
    public EmailActivityDAO() {
    }
}
