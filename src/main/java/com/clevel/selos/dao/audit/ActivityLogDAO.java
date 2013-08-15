package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.ActivityLog;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ActivityLogDAO extends GenericDAO<ActivityLog,Long> {
    @Inject
    private Logger log;

    @Inject
    public ActivityLogDAO() {
    }
}