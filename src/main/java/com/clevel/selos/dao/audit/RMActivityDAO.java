package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.audit.RMActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RMActivityDAO extends GenericDAO<RMActivity, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public RMActivityDAO() {

    }

}
