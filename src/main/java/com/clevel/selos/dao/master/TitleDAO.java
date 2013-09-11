package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.Title;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class TitleDAO extends GenericDAO<Title,Integer> {
    @Inject
    private Logger log;

    @Inject
    public TitleDAO() {
    }

    public List<Title> getListByCustomerType(CustomerEntity customerEntity) {
        log.info("getListByCustomerType. (businessGroup: {})", customerEntity);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerType", customerEntity));
        criteria.addOrder(Order.asc("id"));
        List<Title> titles = criteria.list();
        log.info("getListByCustomerType. (result size: {})",titles.size());
        return titles;
    }
}
