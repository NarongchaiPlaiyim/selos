package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CreditType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CreditTypeDAO extends GenericDAO<CreditType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public CreditTypeDAO() {
    }
}
