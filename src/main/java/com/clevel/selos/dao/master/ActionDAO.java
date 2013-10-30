package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Action;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ActionDAO extends GenericDAO<Action, Long> {
    @Inject
    private Logger log;

    @Inject
    public ActionDAO() {
    }
}
