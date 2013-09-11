package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CustomerEntity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerTypeDAO extends GenericDAO<CustomerEntity,Integer> {
    @Inject
    private Logger log;

    @Inject
    public CustomerTypeDAO() {
    }
}
