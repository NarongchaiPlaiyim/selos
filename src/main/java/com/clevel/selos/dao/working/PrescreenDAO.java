package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Prescreen;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PrescreenDAO extends GenericDAO<Prescreen, Integer> {
    @Inject
    private Logger log;

    @Inject
    public PrescreenDAO(){

    }
}
