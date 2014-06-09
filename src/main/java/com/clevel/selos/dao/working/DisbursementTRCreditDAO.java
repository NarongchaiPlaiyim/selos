package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.DisbursementTRCredit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DisbursementTRCreditDAO extends GenericDAO<DisbursementTRCredit, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementTRCreditDAO() {
    	
    }
    
    public List<DisbursementTRCredit> findByDisbursementTRId(long disbursementTRId) {
        log.info("findByDisbursementId : {}", disbursementTRId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("disbursementTR.id", disbursementTRId));
        List<DisbursementTRCredit> disbursementTRCredit = criteria.list();
        return disbursementTRCredit;
    }

}
