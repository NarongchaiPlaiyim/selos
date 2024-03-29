package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserPosition;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserPositionDAO extends GenericDAO<UserPosition, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public UserPositionDAO() {
    }
}
