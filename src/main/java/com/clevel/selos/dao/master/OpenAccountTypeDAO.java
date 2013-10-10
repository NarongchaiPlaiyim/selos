package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.OpenAccountType;
import com.clevel.selos.model.db.master.RiskType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccountTypeDAO extends GenericDAO<OpenAccountType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public OpenAccountTypeDAO() {
    }

    @Override
    public List<OpenAccountType> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<OpenAccountType> list = criteria.list();
        return list;
    }
}
