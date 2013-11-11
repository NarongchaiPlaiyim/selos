package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.IsaActivity;
import com.clevel.selos.model.db.audit.UserActivity;
import org.slf4j.Logger;

import javax.inject.Inject;

public class IsaActivityDAO extends GenericDAO<IsaActivity, Long> {
    @Inject
    private Logger log;

    @Inject
    public IsaActivityDAO() {
    }

}