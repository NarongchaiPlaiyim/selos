package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Step;
import org.slf4j.Logger;

import javax.inject.Inject;

public class StepDAO extends GenericDAO<Step,Long> {
    @Inject
    private Logger log;

    @Inject
    public StepDAO() {
    }
}
