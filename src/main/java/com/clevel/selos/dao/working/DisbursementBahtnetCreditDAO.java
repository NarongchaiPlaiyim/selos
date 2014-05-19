package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.DisbursementBahtnetCredit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DisbursementBahtnetCreditDAO extends GenericDAO<DisbursementBahtnetCredit, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementBahtnetCreditDAO() {
    	
    }
    
    public List<DisbursementBahtnetCredit> findByDisbursementBahtnetId(long disbursementBahtnetId) {
        log.info("findByDisbursementId : {}", disbursementBahtnetId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("disbursementBahtnet.id", disbursementBahtnetId));
        List<DisbursementBahtnetCredit> disbursementBahtnetCreditList = criteria.list();
        return disbursementBahtnetCreditList;
    }

}
