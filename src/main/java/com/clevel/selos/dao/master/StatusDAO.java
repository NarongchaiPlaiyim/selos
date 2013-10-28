package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Status;
import org.slf4j.Logger;

import javax.inject.Inject;

public class StatusDAO extends GenericDAO<Status, Long> {
    @Inject
    private Logger log;

    @Inject
    public StatusDAO() {
    }
}
