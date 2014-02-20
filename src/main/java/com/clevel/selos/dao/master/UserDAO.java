package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.UserZone;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class UserDAO extends GenericDAO<User,String> {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    public UserDAO() {
    }

    public User findByUserName(String userName) {
        //log.debug("findByUserName. (userName: {})",userName);
        return findOneByCriteria(Restrictions.eq("userName",userName));
    }
    public List<User> findBDMChecker(User user){
        //log.debug("findBDMChecker. BDM Maker : {}", user);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role", user.getRole()));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("id"));
        List<User> userList = criteria.list();
        //log.debug("findBDMChecker. (result size: {})", userList.size());

        return userList;
    }

    public List<User> findABDMList(User user, Role abdmRole){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role", abdmRole));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("id"));

        List<User> userList = criteria.list();

        return userList;

    }

    public List<User> findUserListByRole(User user, Role role){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role", role));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("id"));

        List<User> userList = criteria.list();

        return userList;
    }

    public List<User> findUserZoneList(User user){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role.id", RoleValue.ZM.id()));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("userName"));

        List<User> userList = criteria.list();

        return userList;
    }

    public List<User> findUserRegionList(User user){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role.id", RoleValue.RGM.id()));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("userName"));

        List<User> userList = criteria.list();

        return userList;
    }

    public List<User> findUserHeadList(User user){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.ne("id", user.getId()));
        criteria.add(Restrictions.eq("role.id", RoleValue.GH.id()));
        criteria.add(Restrictions.eq("team", user.getTeam()));
        criteria.addOrder(Order.asc("userName"));

        List<User> userList = criteria.list();

        return userList;
    }

}
