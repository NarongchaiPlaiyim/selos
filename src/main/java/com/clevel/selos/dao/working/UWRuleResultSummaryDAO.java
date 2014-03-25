package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.UWRuleResultSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UWRuleResultSummaryDAO extends GenericDAO<UWRuleResultSummary, Long>{

    @Inject
    @SELOS
    Logger logger;
    @Inject
    public UWRuleResultSummaryDAO() {
    }

    public UWRuleResultSummary findByWorkcaseId(long workCaseId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        UWRuleResultSummary uwRuleResultSummary = (UWRuleResultSummary) criteria.uniqueResult();
        return uwRuleResultSummary;
    }
}
