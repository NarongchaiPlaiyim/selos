package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.WorkCase;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WokCaseDAO extends GenericDAO<WorkCase,Long> {
    @Inject
    private Logger log;

    @Inject
    public WokCaseDAO() {
    }
}
