package com.clevel.selos.dao.working;

import java.util.List;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Disbursement;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.TCGInfo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DisbursementDAO extends GenericDAO<Disbursement, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementDAO() {
    	
    }

    
    public Disbursement findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        Disbursement disbursement = (Disbursement) criteria.uniqueResult();

        return disbursement;
    }
}
