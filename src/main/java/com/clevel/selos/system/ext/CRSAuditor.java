package com.clevel.selos.system.ext;

import com.clevel.selos.dao.ext.crs.CRSDataDAO;
import com.clevel.selos.model.ActionResult;

import com.clevel.selos.model.db.ext.crs.CRSData;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;


@Stateless
public class CRSAuditor {

    @Inject
    Logger log;
    @Inject
    CRSDataDAO crsDataDAO;

    @Inject
    public CRSAuditor() {
    }


    public void add(CRSData crsData) {
        crsDataDAO.persist(crsData);
    }
}
