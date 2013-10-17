package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.ExistingCredit;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingCreditDAO extends GenericDAO<ExistingCredit,Long> {

    @Inject
    private Logger log;

    @Inject
    public ExistingCreditDAO() {
    }

}
