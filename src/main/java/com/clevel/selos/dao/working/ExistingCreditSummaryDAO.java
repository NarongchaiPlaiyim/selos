package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.ExistingCreditSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingCreditSummaryDAO extends GenericDAO<ExistingCreditSummary, Long> {

    @Inject
    private Logger log;

    @Inject
    public ExistingCreditSummaryDAO(){

    }

    public ExistingCreditSummary findByWorkCasePreScreenId(long workCasePreScreenId){
        log.info("findByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.addOrder(Order.asc("id"));
        ExistingCreditSummary existingCreditSummary = (ExistingCreditSummary)criteria.uniqueResult();
        return existingCreditSummary;
    }
}
