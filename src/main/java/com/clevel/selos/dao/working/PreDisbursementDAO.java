package com.clevel.selos.dao.working;

import java.util.List;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.TCGInfo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PreDisbursementDAO extends GenericDAO<PreDisbursement, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public PreDisbursementDAO() {
    	
    }
    
    public PreDisbursement findPreDisbursementByWorkcaseId(Long workCaseId) {
        log.info("-- findPreDisbursementByWorkcaseId ::: {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        PreDisbursement preDisbursement = (PreDisbursement) criteria.uniqueResult();
        log.info("-- preDisbursement ::: {}", preDisbursement);
        return preDisbursement;
    }

}
