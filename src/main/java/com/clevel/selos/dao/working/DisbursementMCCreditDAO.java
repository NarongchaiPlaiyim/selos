package com.clevel.selos.dao.working;

import java.util.List;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Disbursement;
import com.clevel.selos.model.db.working.DisbursementCredit;
import com.clevel.selos.model.db.working.DisbursementMC;
import com.clevel.selos.model.db.working.DisbursementMCCredit;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.TCGInfo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DisbursementMCCreditDAO extends GenericDAO<DisbursementMCCredit, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementMCCreditDAO() {
    	
    }
    
    public List<DisbursementMCCredit> findByDisbursementMCId(long disbursementMCId) {
        log.info("findByDisbursementId : {}", disbursementMCId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("disbursementMC.id", disbursementMCId));
        List<DisbursementMCCredit> disbursementMCCredit = criteria.list();
        return disbursementMCCredit;
    }

}
