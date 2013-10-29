package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BusinessGroup;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BusinessGroupDAO extends GenericDAO<BusinessGroup, Integer> {
    @Inject
    private Logger log;

    @Inject
    public BusinessGroupDAO() {
    }
}
