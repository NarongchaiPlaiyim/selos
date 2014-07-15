package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ProposeLine;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProposeLineDAO extends GenericDAO<ProposeLine, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProposeLineDAO() {
    }

    public ProposeLine findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId :: {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        return (ProposeLine)criteria.uniqueResult();
    }

    public ProposeLine findByWorkCasePreScreenId(long workCasePreScreenId){
        log.info("-- findByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        return (ProposeLine)criteria.uniqueResult();
    }
}
