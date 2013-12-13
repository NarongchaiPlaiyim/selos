package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ExistingCreditFacilityDAO extends GenericDAO<ExistingCreditFacility, Long> {

    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingCreditFacilityDAO() {

    }

    public ExistingCreditFacility findByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.addOrder(Order.asc("id"));
        ExistingCreditFacility existingCreditFacility = (ExistingCreditFacility) criteria.uniqueResult();
        return existingCreditFacility;
    }

    public ExistingCreditFacility findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        ExistingCreditFacility existingCreditFacility = (ExistingCreditFacility) criteria.uniqueResult();
        return existingCreditFacility;
    }
}
