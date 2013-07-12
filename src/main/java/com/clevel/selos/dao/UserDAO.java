package com.clevel.selos.dao;

import com.clevel.selos.model.db.User;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserDAO extends GenericDAO<User,Long> {
    @Inject
    private Logger log;

    @Inject
    public UserDAO() {
    }

    public User findByUserName(String userName) {
        log.debug("findByUserName. (userName: {})",userName);
        return findOneByCriteria(Restrictions.eq("userName",userName));
    }
}
