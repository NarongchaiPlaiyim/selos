package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserZone;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserZoneDAO extends GenericDAO<UserZone, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public UserZoneDAO() {
    }
}
