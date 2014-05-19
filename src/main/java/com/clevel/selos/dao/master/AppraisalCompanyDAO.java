package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AppraisalCompany;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AppraisalCompanyDAO extends GenericDAO<AppraisalCompany,Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AppraisalCompanyDAO() {
    }
}
