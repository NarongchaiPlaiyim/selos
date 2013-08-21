package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Customer;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerDAO extends GenericDAO<Customer,Long> {
    @Inject
    private Logger log;

    @Inject
    public CustomerDAO() {
    }
}
