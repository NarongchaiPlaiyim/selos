package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.UserZone;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserZoneDAO extends GenericDAO<UserZone,Integer> {
    @Inject
    private Logger log;

    @Inject
    public UserZoneDAO() {
    }
}