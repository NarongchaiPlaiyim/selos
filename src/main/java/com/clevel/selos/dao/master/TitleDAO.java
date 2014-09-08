package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.Title;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class TitleDAO extends GenericDAO<Title, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public TitleDAO() {
    }

    /*@Override
    public List<Title> findAll() {

        Criteria criteria = getSession().createCriteria(getEntityClass());
        criteria.addOrder(new org.hibernate.criterion.Order("code", true) {
            @Override
            public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
                return "cast(code as int)";
            }
        });
        List<Title> list = criteria.list();

        log.info("findALl Override {}", list);
        return list;
    }*/

    public List<Title> getListByCustomerEntity(CustomerEntity customerEntity) {
        log.info("getListByCustomerType. (customerEntity: {})", customerEntity);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity", customerEntity));
        criteria.add(Restrictions.eq("active", 1));
        criteria.addOrder(new org.hibernate.criterion.Order("code", true) {
            @Override
            public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
                return "cast(code as int)";
            }
        });
        List<Title> titles = criteria.list();
        log.info("getListByCustomerType. (result size: {})", titles.size());
        return titles;
    }

    public List<Title> getListByCustomerEntityId(int customerEntityId) {
        log.info("getListByCustomerEntityId. (customerEntityId: {})", customerEntityId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        criteria.add(Restrictions.eq("active", 1));
        criteria.addOrder(new org.hibernate.criterion.Order("code", true) {
            @Override
            public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
                return "cast(code as int)";
            }
        });
        List<Title> titles = criteria.list();
        log.info("getListByCustomerType. (result size: {})", titles.size());
        return titles;
    }
}
