package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.CustomerCSI;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerCSIDAO extends GenericDAO<CustomerCSI, Long> {
    @Inject
    private Logger log;

    @Inject
    public CustomerCSIDAO(){

    }
}