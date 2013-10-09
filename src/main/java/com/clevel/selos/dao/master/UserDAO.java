package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

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
    public List<User> findBDMChecker(User user){
        log.debug("findBDMChecker. BDM Maker : {}", user);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("id"));
        List<User> userList = criteria.list();
        log.debug("findBDMChecker. (result size: {})", userList.size());

        return userList;
    }
}
