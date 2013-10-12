package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.OpenAccountPurpose;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccountPurposeDAO extends GenericDAO<OpenAccountPurpose,Integer> {
    @Inject
    private Logger log;

    @Inject
    public OpenAccountPurposeDAO() {
    }

    @Override
    public List<OpenAccountPurpose> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<OpenAccountPurpose> list = criteria.list();
        return list;
    }
}
