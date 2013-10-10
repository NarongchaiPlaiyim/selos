package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.master.OpenAccountType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccountProductDAO extends GenericDAO<OpenAccountProduct,Integer> {
    @Inject
    private Logger log;

    @Inject
    public OpenAccountProductDAO() {
    }

    @Override
    public List<OpenAccountProduct> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<OpenAccountProduct> list = criteria.list();
        return list;
    }
}
