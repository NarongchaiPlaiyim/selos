package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AppraisalDivision;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AppraisalDivisionDAO extends GenericDAO<AppraisalDivision,Integer> {
    @Inject
    private Logger log;

    @Inject
    public AppraisalDivisionDAO() {
    }
}
