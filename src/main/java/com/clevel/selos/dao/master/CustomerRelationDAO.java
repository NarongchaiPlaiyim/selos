package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CustomerRelation;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerRelationDAO extends GenericDAO<CustomerRelation,Integer> {
    @Inject
    private Logger log;

    @Inject
    public CustomerRelationDAO(){

    }
}