package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.User;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserDAO extends GenericDAO<User,String> {
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
