package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.DBRDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DBRDetailDAO extends GenericDAO<DBRDetail, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public DBRDetailDAO() {
    }
}
