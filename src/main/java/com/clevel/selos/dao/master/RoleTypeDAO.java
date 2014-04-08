package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.RoleType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RoleTypeDAO extends GenericDAO<RoleType, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public RoleTypeDAO() {
    }
}
