package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingCreditDetailDAO extends GenericDAO<ExistingCreditDetail, Long> {

    @Inject
    private Logger log;

    @Inject
    public ExistingCreditDetailDAO() {
    }
}
