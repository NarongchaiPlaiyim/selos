package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AppraisalCompany;
import com.clevel.selos.model.db.master.Occupation;
import com.clevel.selos.model.db.working.Appraisal;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AppraisalCompanyDAO extends GenericDAO<AppraisalCompany,Integer> {
    @Inject
    private Logger log;

    @Inject
    public AppraisalCompanyDAO() {
    }
}
