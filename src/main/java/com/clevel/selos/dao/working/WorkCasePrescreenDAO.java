package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WorkCasePrescreenDAO extends GenericDAO<WorkCasePrescreen,Long> {
    @Inject
    private Logger log;

    @Inject
    public WorkCasePrescreenDAO() {
    }
}
