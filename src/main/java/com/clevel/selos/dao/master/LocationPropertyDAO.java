package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.LocationProperty;
import org.slf4j.Logger;

import javax.inject.Inject;

public class LocationPropertyDAO extends GenericDAO<LocationProperty,Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public LocationPropertyDAO() {
    }
}
