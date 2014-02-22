package com.clevel.selos.dao.working;

import java.util.List;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.PreDisbursement;
import com.clevel.selos.model.db.working.PreDisbursementDetail;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PreDisbursementDetailDAO extends GenericDAO<PreDisbursementDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public PreDisbursementDetailDAO() {
    	
    }
    
    public List<PreDisbursementDetail> findPreDisbursementByPreDisbursementId(Long predisbursement_id) {
        log.info("-- findPreDisbursementByPreDisbursementId ::: {}", predisbursement_id);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("predisbursement_id", predisbursement_id));
        List<PreDisbursementDetail> preDisbursementDetailList = (List<PreDisbursementDetail>) criteria.list();
        log.info("-- preDisbursementDetailList ::: size : {}", preDisbursementDetailList.size());
        return preDisbursementDetailList;
    }

}
