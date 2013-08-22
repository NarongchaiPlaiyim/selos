package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Degree;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DegreeDAO extends GenericDAO<Degree,Integer> {
    @Inject
    private Logger log;

    @Inject
    public DegreeDAO() {
    }
}
