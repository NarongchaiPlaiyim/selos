package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingGuarantorDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingGuarantorDetailDAO extends GenericDAO<ExistingGuarantorDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingGuarantorDetailDAO() {

    }


}
