package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.audit.SafeWatchActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SafeWatchActivityDAO extends GenericDAO<SafeWatchActivity, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public SafeWatchActivityDAO() {
    }
}
