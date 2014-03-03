package com.clevel.selos.dao.working;

import java.util.List;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Disbursement;
import com.clevel.selos.model.db.working.DisbursementCredit;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.TCGInfo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DisbursementSummaryDAO extends GenericDAO<DisbursementCredit, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementSummaryDAO() {
    	
    }
    
    public List<DisbursementCredit> findByDisbursemetnId(long disbursementId) {
        log.info("findByNewCreditId : {}", disbursementId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("disbursement.id", disbursementId));
        List<DisbursementCredit> disbursementCreditList = criteria.list();
        return disbursementCreditList;
    }
    
    
    public DisbursementCredit findByNewCreditId(long newCreditId) {
        log.info("findByNewCreditId : {}", newCreditId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("creditDetail.id", newCreditId));
        DisbursementCredit disbursementCredit = (DisbursementCredit) criteria.uniqueResult();

        return disbursementCredit;
    }

}
