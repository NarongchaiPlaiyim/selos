package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.db.master.SpecialProgram;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class RequestTypeDAO extends GenericDAO<RequestType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public RequestTypeDAO() {
    }

    @Override
    public List<RequestType> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active",1));
        List<RequestType> list = criteria.list();
        return list;
    }
}
