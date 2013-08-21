package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CollateralType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CollateralTypeDAO extends GenericDAO<CollateralType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public CollateralTypeDAO() {
    }
}
