package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.audit.BPMActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BPMActivityDAO extends GenericDAO<BPMActivity, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BPMActivityDAO() {
    }
}
