package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RMAuditDAO extends GenericDAO<RMAuditDAO, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public RMAuditDAO() {
    }
}
