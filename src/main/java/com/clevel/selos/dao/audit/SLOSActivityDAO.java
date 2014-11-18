package com.clevel.selos.dao.audit;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.audit.SLOSActivity;

import javax.inject.Inject;

public class SLOSActivityDAO extends GenericDAO<SLOSActivity, Long> {
    @Inject
    public SLOSActivityDAO(){
    }
}
