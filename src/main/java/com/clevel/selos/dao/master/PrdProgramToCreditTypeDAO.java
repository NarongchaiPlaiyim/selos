package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PrdProgramToCreditTypeDAO extends GenericDAO<PrdProgramToCreditType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public PrdProgramToCreditTypeDAO() {
    }
}
