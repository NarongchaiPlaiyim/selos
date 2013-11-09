package com.clevel.selos.dao.history;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.history.CaseHistory;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CaseHistoryDAO extends GenericDAO<CaseHistory, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public CaseHistoryDAO() {
    }
}
