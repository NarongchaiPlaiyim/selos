package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PrdGroupToPrdProgramDAO extends GenericDAO<PrdGroupToPrdProgram,Integer> {
    @Inject
    private Logger log;

    @Inject
    public PrdGroupToPrdProgramDAO() {
    }
}
