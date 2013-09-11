package com.clevel.selos.dao.export;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.export.NCBIExport;
import org.slf4j.Logger;

import javax.inject.Inject;

public class NCBIExportDAO extends GenericDAO<NCBIExport,Long> {
    @Inject
    private Logger log;

    @Inject
    public NCBIExportDAO() {
    }
}
