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

    public List<User> findBDMChecker(String userName){
        log.debug("findBDMChecker. BDM Maker : {}", userName);
        User bdmUser = findByUserName(userName);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", userName));
        criteria.add(Restrictions.eq("team", bdmUser.getTeam()));
        criteria.addOrder(Order.asc("id"));
        List<User> userList = criteria.list();
        log.info("findBDMChecker. (result size: {})", userList.size());

        return userList;
    }
}
