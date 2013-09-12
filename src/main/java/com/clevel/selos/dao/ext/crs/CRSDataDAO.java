package com.clevel.selos.dao.ext.crs;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.crs.CRSData;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CRSDataDAO extends GenericDAO<CRSData,Long> {
    @Inject
    private Logger log;

    @Inject
    public CRSDataDAO() {
    }

}
