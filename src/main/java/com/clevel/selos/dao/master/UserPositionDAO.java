package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.UserPosition;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserPositionDAO extends GenericDAO<UserPosition,Integer> {
    @Inject
    private Logger log;

    @Inject
    public UserPositionDAO() {
    }
}
