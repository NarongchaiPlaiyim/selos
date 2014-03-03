package com.clevel.selos.dao.working;

import java.util.List;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Disbursement;
import com.clevel.selos.model.db.working.DisbursementCredit;
import com.clevel.selos.model.db.working.DisbursementMC;
import com.clevel.selos.model.db.working.DisbursementTR;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.TCGInfo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DisbursementTRDAO extends GenericDAO<DisbursementTR, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementTRDAO() {
    	
    }
    
    public List<DisbursementTR> findByDisbursementId(long disbursementId) {
        log.info("findByDisbursementId : {}", disbursementId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("disbursement.id", disbursementId));
        List<DisbursementTR> disbursementTRList = criteria.list();
        return disbursementTRList;
    }

}
