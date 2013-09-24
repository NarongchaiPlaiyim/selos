package com.clevel.selos.dao.working;

import com.clevel.selos.model.db.working.PrescreenFacility;
import com.clevel.selos.dao.GenericDAO;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PrescreenFacilityDAO extends GenericDAO<PrescreenFacility, Integer> {
    @Inject
    private Logger log;

    @Inject
    public PrescreenFacilityDAO(){

    }
}
