package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.DisbursementMC;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DisbursementMCDAO extends GenericDAO<DisbursementMC, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementMCDAO() {
    	
    }
    
    public List<DisbursementMC> findByDisbursementId(long disbursementId) {
        log.info("findByDisbursementId : {}", disbursementId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("disbursement.id", disbursementId));
        List<DisbursementMC> disbursementMCList = criteria.list();
        return disbursementMCList;
    }

}
