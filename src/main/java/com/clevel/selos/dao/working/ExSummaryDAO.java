package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExSummaryDAO extends GenericDAO<ExSummary, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExSummaryDAO() {
    }

    public ExSummary findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        ExSummary exSummary = (ExSummary) criteria.uniqueResult();

        return exSummary;
    }
}
