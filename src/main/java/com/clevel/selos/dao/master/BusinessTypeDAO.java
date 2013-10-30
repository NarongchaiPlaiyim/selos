package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BusinessType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BusinessTypeDAO extends GenericDAO<BusinessType, Integer> {
    @Inject
    private Logger log;

    @Inject
    public BusinessTypeDAO() {
    }
}
