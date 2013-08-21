package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.ReasonType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ReasonTypeDAO extends GenericDAO<ReasonType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public ReasonTypeDAO() {
    }
}
