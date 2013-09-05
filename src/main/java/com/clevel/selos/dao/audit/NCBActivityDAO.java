package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.NCBActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class NCBActivityDAO extends GenericDAO<NCBActivity,Long> {
    @Inject
    private Logger log;

    @Inject
    public NCBActivityDAO() {
    }
}
