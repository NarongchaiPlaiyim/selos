package com.clevel.selos.dao.working;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.CustomerOblInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerOblInfoDAO extends GenericDAO<CustomerOblInfo, Long> {

    @Inject
    @SELOS
    Logger log;
    @Inject
    public CustomerOblInfoDAO() {
    }

    public CustomerOblInfo findByCustomerID(long cusID){
        log.debug("findByCustomerID ::: cusID : {}", cusID);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", cusID));
        CustomerOblInfo customerOblInfo = (CustomerOblInfo) criteria.uniqueResult();
        log.debug("return customer obl info {}::", customerOblInfo);
        return customerOblInfo;
    }
}
