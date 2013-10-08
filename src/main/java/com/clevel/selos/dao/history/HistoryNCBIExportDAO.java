package com.clevel.selos.dao.history;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.history.HistoryNCBIExport;
import org.slf4j.Logger;

import javax.inject.Inject;

public class HistoryNCBIExportDAO extends GenericDAO<HistoryNCBIExport,Long> {
    @Inject
    private Logger log;

    @Inject
    public HistoryNCBIExportDAO() {
    }
}
