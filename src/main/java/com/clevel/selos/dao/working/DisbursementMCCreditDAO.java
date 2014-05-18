package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.DisbursementMCCredit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

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
