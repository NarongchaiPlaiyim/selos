package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.DWHActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DWHActivityDAO extends GenericDAO<DWHActivity,Long> {
    @Inject
    private Logger log;

    @Inject
    public DWHActivityDAO() {
    }
}
