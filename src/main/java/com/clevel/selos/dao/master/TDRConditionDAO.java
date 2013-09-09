package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.TDRCondition;
import org.slf4j.Logger;

import javax.inject.Inject;

public class TDRConditionDAO extends GenericDAO<TDRCondition,Integer> {
    @Inject
    private Logger log;

    @Inject
    public TDRConditionDAO() {
    }
}
