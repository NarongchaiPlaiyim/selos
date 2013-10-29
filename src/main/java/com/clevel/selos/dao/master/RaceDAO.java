package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Race;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RaceDAO extends GenericDAO<Race, Integer> {

    @Inject
    private Logger log;

    @Inject
    public RaceDAO() {
    }
}
