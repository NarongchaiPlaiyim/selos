package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CustomerType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerTypeDAO extends GenericDAO<CustomerType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public CustomerTypeDAO() {
    }
}
