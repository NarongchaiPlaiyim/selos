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
}