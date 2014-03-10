package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Country;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO extends GenericDAO<Country, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public CountryDAO() {
    }

    @Override
    public List<Country> findAll(){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

}
