package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Role;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class RoleDAO extends GenericDAO<Role,Integer> {
    @Inject
    private Logger log;

    @Inject
    public RoleDAO() {
    }
}
