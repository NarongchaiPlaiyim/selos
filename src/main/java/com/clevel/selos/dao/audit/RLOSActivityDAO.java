package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.ROLSActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RLOSActivityDAO extends GenericDAO<ROLSActivity,Long> {
    @Inject
    private Logger log;

    @Inject
    public RLOSActivityDAO() {
    }
}
