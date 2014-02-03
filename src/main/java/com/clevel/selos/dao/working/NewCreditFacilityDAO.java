package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class NewCreditFacilityDAO extends GenericDAO<NewCreditFacility, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCreditFacilityDAO() {
    }

    public NewCreditFacility findByWorkCaseId(long workCaseId) {
        log.info("-- findNewCreditFacilityByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        NewCreditFacility newCreditFacility = (NewCreditFacility) criteria.uniqueResult();

        return newCreditFacility;
    }

    public NewCreditFacility findByWorkCase(WorkCase workCase) {
        log.info("-- findNewCreditFacilityByWorkCase : {}", workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        criteria.addOrder(Order.asc("id"));
        NewCreditFacility newCreditFacility = (NewCreditFacility)criteria.uniqueResult();

        return newCreditFacility;
    }
}
