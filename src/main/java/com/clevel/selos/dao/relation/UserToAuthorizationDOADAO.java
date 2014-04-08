package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.UserToAuthorizationDOA;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserToAuthorizationDOADAO extends GenericDAO<UserToAuthorizationDOA, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    AuthorizationDOA authorizationDOA;
    @Inject
    public UserToAuthorizationDOADAO()
    {
    }

    List<Long> doaIdList = new ArrayList<Long>();

    public List<AuthorizationDOA> findbyusernameasid(String userName)
    {
        Criteria cr = getSession().createCriteria(UserToAuthorizationDOA.class).setProjection(Projections.projectionList()
                      .add(Projections.property("doa_id"), "doa_id"))
                      .add(Restrictions.eq("userId",userName))
                      .setResultTransformer(Transformers.aliasToBean(UserToAuthorizationDOA.class));

        List<UserToAuthorizationDOA> doaList = cr.list();

        Iterator it = doaList.iterator();

        while(it.hasNext()==true)
        {
            UserToAuthorizationDOA userToAuthorizationDOA = new UserToAuthorizationDOA();

            userToAuthorizationDOA=(UserToAuthorizationDOA)it.next();

            long doaId = userToAuthorizationDOA.getDoa_id();

            log.info("doaId:::::::::::::::::::::::::::"+doaId);

            doaIdList.add(doaId)  ;

            userToAuthorizationDOA = null;

        }
        log.info("doaIdList list :::"+doaIdList);

        Criteria criteria2 = getSession(). createCriteria(AuthorizationDOA.class);

        criteria2.setProjection(Projections.projectionList().add(Projections.property("doapriorityorder"), "doapriorityorder")).add(Restrictions.in("id", doaIdList)) .setResultTransformer(Transformers.aliasToBean(AuthorizationDOA.class));
        log.info("criteria2::::::::::::::::value is:" + criteria2);

        log.info("final list valus is :::" + criteria2.list());

        List<AuthorizationDOA>  doaPriorityList = new ArrayList<AuthorizationDOA>();

        doaPriorityList =   criteria2.list();

        return  doaPriorityList;

    }

    public List<User> getUserListFromDOALevel(long doaLevelId){
        List<User> userList = new ArrayList<User>();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("doa_id", doaLevelId));
        criteria.addOrder(Order.asc("id"));
        List<UserToAuthorizationDOA> userToAuthorizationDOAList = criteria.list();
        if(userToAuthorizationDOAList!=null && userToAuthorizationDOAList.size()>0){
            List<String> userNames = new ArrayList<String>();
            for(UserToAuthorizationDOA userToAuthorizationDOA: userToAuthorizationDOAList){
                userNames.add(userToAuthorizationDOA.getUserId());
            }

            if(userNames.size()>0){
                Criteria criteria2 = getSession().createCriteria(User.class);
                criteria2.add(Restrictions.in("id", userNames));
                criteria2.addOrder(Order.asc("id"));
                userList = criteria2.list();
            }
        }

        return userList;
    }


}
