package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Role;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RoleDAO extends GenericDAO<Role, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public RoleDAO() {

    }

    public Role findRoleByName(final String  name) {
        log.info("-- findRoleByName : {}", name);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", name));
        Role role = (Role) criteria.uniqueResult();
        return role;

    }
}
