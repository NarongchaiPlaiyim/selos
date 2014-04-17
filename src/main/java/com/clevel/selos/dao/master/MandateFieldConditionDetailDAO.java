package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateFieldConditionDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateFieldConditionDetailDAO extends GenericDAO<MandateFieldConditionDetail, Long> {

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public MandateFieldConditionDetailDAO(){}

}
