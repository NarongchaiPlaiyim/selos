package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RMAuditDAO extends GenericDAO<RMAuditDAO, Long> {
    @Inject
    private Logger log;

    @Inject
    public RMAuditDAO() {
    }
}
