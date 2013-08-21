package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AppealReason;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AppealReasonDAO extends GenericDAO<AppealReason,Integer> {
    @Inject
    private Logger log;

    @Inject
    public AppealReasonDAO() {
    }
}
