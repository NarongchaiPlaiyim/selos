package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BorrowerType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BorrowerTypeDAO extends GenericDAO<BorrowerType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BorrowerTypeDAO() {
    }
}
