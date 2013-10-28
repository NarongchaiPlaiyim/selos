package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerAcceptanceDAO extends GenericDAO<CustomerAcceptance, Integer> {
    @Inject
    private Logger log;

    @Inject
    public CustomerAcceptanceDAO() {
    }


    public CustomerAcceptance findCustomerAcceptanceByWorkCase(WorkCase workCase) {
        CustomerAcceptance customerAcceptance;

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        customerAcceptance = (CustomerAcceptance) criteria.uniqueResult();
        log.info("getListByDistrict. (result size: {})", customerAcceptance);

        return customerAcceptance;
    }
}