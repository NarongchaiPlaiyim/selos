package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Decision;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DecisionDAO extends GenericDAO<Decision, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public DecisionDAO() {
    }

    public Decision findByWorkCaseId(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        return (Decision) criteria.uniqueResult();
    }

    public Decision findByWorkCase(WorkCase workCase) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        return (Decision) criteria.uniqueResult();
    }
}
