package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDocReason;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateDocReasonDAO extends GenericDAO<MandateDocReason, Long>{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public MandateDocReasonDAO(){}

}
