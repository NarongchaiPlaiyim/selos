package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerAcceptanceDAO extends GenericDAO<CustomerAcceptance, Long> {
    private static final long serialVersionUID = -5249297432741235023L;
	@Inject
    @SELOS
    Logger log;
    
    public CustomerAcceptanceDAO() {
    }


    public CustomerAcceptance findCustomerAcceptanceByWorkCase(WorkCase workCase) {
        log.info("-- findCustomerAcceptanceByWorkCase : {}", workCase);
        CustomerAcceptance customerAcceptance;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        customerAcceptance = (CustomerAcceptance) criteria.uniqueResult();
        return customerAcceptance;
    }
    public CustomerAcceptance findCustomerAcceptanceByWorkCase(long workCaseId) {
        CustomerAcceptance customerAcceptance;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        customerAcceptance = (CustomerAcceptance) criteria.uniqueResult();
        return customerAcceptance;
    }
}