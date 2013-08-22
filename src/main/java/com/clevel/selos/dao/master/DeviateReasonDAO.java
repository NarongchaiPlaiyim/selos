package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.DeviateReason;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DeviateReasonDAO extends GenericDAO<DeviateReason,Integer> {
    @Inject
    private Logger log;

    @Inject
    public DeviateReasonDAO() {
    }
}
