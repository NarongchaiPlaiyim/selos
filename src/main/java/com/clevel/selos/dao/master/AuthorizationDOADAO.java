package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AuthorizationDOADAO extends GenericDAO<AuthorizationDOA, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AuthorizationDOADAO() {
    }

    public List<AuthorizationDOA> getListByPriority(int priority){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.le("doapriorityorder", priority));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
