package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.WorkCase;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WokCaseDAO extends GenericDAO<WorkCase,Long> {
    @Inject
    private Logger log;

    @Inject
    public WokCaseDAO() {
    }
}
