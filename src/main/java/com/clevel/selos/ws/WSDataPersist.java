package com.clevel.selos.ws;

import com.clevel.selos.dao.ext.crs.CRSDataDAO;
import com.clevel.selos.model.ActionResult;

import com.clevel.selos.model.db.ext.crs.CRSData;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class WSDataPersist {
    @Inject
    Logger log;
    @Inject
    CRSDataDAO crsDataDAO;

    @Inject
    public WSDataPersist() {
    }

    public void addNewCase(CRSData crsData) {
        log.debug("addNewCase. ({})",crsData);
        crsDataDAO.persist(crsData);
    }
}
