package com.clevel.selos.dao.ext.csi;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.csi.CSIData;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class CSIDataDAO extends GenericDAO<CSIData,Long> {
    @Inject
    private Logger log;

    @Inject
    public CSIDataDAO() {
    }
}
