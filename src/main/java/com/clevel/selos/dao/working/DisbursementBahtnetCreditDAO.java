package com.clevel.selos.dao.working;

import java.util.List;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Disbursement;
import com.clevel.selos.model.db.working.DisbursementBahtnetCredit;
import com.clevel.selos.model.db.working.DisbursementCredit;
import com.clevel.selos.model.db.working.DisbursementMC;
import com.clevel.selos.model.db.working.DisbursementMCCredit;
import com.clevel.selos.model.db.working.DisbursementTRCredit;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.TCGInfo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

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
