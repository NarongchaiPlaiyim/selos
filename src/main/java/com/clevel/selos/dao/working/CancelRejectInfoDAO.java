package com.clevel.selos.dao.working;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.CancelRejectInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CancelRejectInfoDAO extends GenericDAO<CancelRejectInfo, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public CancelRejectInfoDAO(){

    }

    public CancelRejectInfo findByWorkCaseId(long workCaseId){
        CancelRejectInfo cancelRejectInfo;
        log.info("findByWorkCaseId ::: workCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));

        cancelRejectInfo = (CancelRejectInfo) criteria.uniqueResult();
        return cancelRejectInfo;
    }

    public CancelRejectInfo findByWorkCasePreScreenId(long workCasePreScreenId){
        CancelRejectInfo cancelRejectInfo;
        log.info("findByWorkCasePreScreenId ::: workCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));

        cancelRejectInfo = (CancelRejectInfo) criteria.uniqueResult();
        return cancelRejectInfo;
    }
}
