package com.clevel.selos.dao.ext.csi;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.test.csi.CSIData;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CSIDataDAO extends GenericDAO<CSIData, Long> {
    @Inject
    private Logger log;

    @Inject
    public CSIDataDAO() {
    }
}
