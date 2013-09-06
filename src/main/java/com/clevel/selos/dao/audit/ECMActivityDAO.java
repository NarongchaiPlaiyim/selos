package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.ECMActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ECMActivityDAO extends GenericDAO<ECMActivity,Long> {
    @Inject
    private Logger log;

    @Inject
    public ECMActivityDAO() {
    }
}
