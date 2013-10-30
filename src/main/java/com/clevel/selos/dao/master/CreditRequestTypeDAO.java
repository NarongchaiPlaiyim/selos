package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CreditRequestType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CreditRequestTypeDAO extends GenericDAO<CreditRequestType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public CreditRequestTypeDAO() {
    }


}
