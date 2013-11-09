package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MaritalStatus;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MaritalStatusDAO extends GenericDAO<MaritalStatus, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public MaritalStatusDAO() {
    }
}
