package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.RiskType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class RiskTypeDAO extends GenericDAO<RiskType, Integer> {
    @Inject
    private Logger log;

    @Inject
    public RiskTypeDAO() {
    }

    @Override
    public List<RiskType> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<RiskType> list = criteria.list();
        return list;
    }
}
