package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Occupation;
import org.slf4j.Logger;

import javax.inject.Inject;

public class OccupationDAO extends GenericDAO<Occupation,Integer> {
    @Inject
    private Logger log;

    @Inject
    public OccupationDAO() {
    }
}
